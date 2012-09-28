package edu.chalmers.dat255.audiobookplayer.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;
import edu.chalmers.dat255.audiobookplayer.ctrl.BookshelfController;
import edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.model.Track;
import edu.chalmers.dat255.audiobookplayer.util.BookCreator;
import edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment.BookshelfUIEventListener;
import edu.chalmers.dat255.audiobookplayer.view.PlayerFragment.PlayerUIEventListener;

/**
 * Main activity of the application. This class should handle all fragment
 * functionality.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.5
 */
public class AudioBookActivity extends FragmentActivity implements
		BookshelfUIEventListener, PlayerUIEventListener, PropertyChangeListener {

	private static final String TAG = "AudioBookActivity.class";

	// Fragment classes
	private final BookshelfFragment bookshelfFragment = new BookshelfFragment();
	private final PlayerFragment playerFragment = new PlayerFragment();

	// Controller classes
	private BookshelfController bsc;
	private PlayerController pc;
	private BookCreator bc;

	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_audiobook);

		// Temporary solution: always create a new bookshelf
		Bookshelf bookshelf = new Bookshelf();

		// Instantiate Controller classes
		bsc = new BookshelfController(bookshelf);
		pc = new PlayerController(bookshelf);
		bc = BookCreator.getInstance();

		bc.setBookshelf(bookshelf);

		bookshelf.addPropertyChangeListener(this);
		
		//Make sure that audiobook_layout is being used as layout for this activity and
		//that we are not resuming from a previous state as this could result in overlapping
		//fragments
		if(findViewById(R.id.audiobook_layout) != null &&
				savedInstance == null) {

			//In case arguments have been sent through an Intent, forward these to the
			//fragments as well
			bookshelfFragment.setArguments(getIntent().getExtras());

			//Add the STARTING_FRAGMENT to this layout
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add(R.id.audiobook_layout, bookshelfFragment);
			ft.commit();
		}
		
	}

	public void bookLongPress(int index) {
		// TODO
	}

	/* Concerning Player*.class */
	public void addButtonPressed() {
		bc.createTestBook();
	}

	public void previousTrack() {
		pc.previousTrack();
	}

	public void playPause() {
		pc.playPause();
	}

	public void nextTrack() {
		pc.nextTrack();
	}

	public void seekLeft() {
		pc.seekLeft();
	}

	public void seekRight() {
		pc.seekRight();
	}

	public void seekToPercentage(double percentage) {
		pc.seekToPercentage(percentage);
	}

	public void bookSelected() {
		pc.start();
	}

	public void trackTimeChanged() {
		;
	}

	/* End player */

	public void bookDurationChanged() {
		// TODO Auto-generated method stub

	}

	public void bookmarkSet() {
		// TODO Auto-generated method stub

	}

	public void tagSet() {
		// TODO Auto-generated method stub

	}

	public int getTrackTime() {
		return pc.getTrackDuration();
	}
	
	/*        UPDATE EVENTS       */
	public void propertyChange(PropertyChangeEvent event) {
		String eventName = event.getPropertyName();
		Log.d(TAG, "Received update: " + eventName);
		
		if (event.getNewValue() instanceof Bookshelf) {
			// it's a Bookshelf
		} else if (event.getNewValue() instanceof Book) {
			// it's a Book
		} else if (event.getNewValue() instanceof Track) {
			// it's a Track
		} else {
			Log.e(TAG, "Type not recognized: " + event.getNewValue());
		}

		// Bookshelf.class
		if (eventName.equals(Constants.event.BOOK_ADDED)) {
			// Bookshelf
			if (event.getNewValue() instanceof Book) {
				Book b = (Book) event.getNewValue();
				this.bookshelfFragment.bookAdded(b);
			}
		} else if (eventName.equals(Constants.event.BOOK_MOVED)) {
			// Bookshelf
		} else if (eventName.equals(Constants.event.BOOK_REMOVED)) {
			// Bookshelf
		} else if (eventName.equals(Constants.event.BOOK_SELECTED)) {
			// Bookshelf

			// Player
			// no need for index
			if (findViewById(R.id.audiobook_layout) != null) {
				playerFragment.setArguments(getIntent().getExtras());

				switchFragment(bookshelfFragment, playerFragment);

				Log.i(TAG, "bookSelected");

				bookSelected();
			}
		}

		// Book.class
		else if (eventName.equals(Constants.event.TRACK_REMOVED)) {
			// Bookshelf
		} else if (eventName.equals(Constants.event.TRACK_ADDED)) {
			// Bookshelf
		} else if (eventName.equals(Constants.event.TRACK_ORDER_CHANGED)) {
			// Bookshelf
		} else if (eventName.equals(Constants.event.TRACK_INDEX_CHANGED)) {
			// Bookshelf

			// Player
			pc.start();
		} else if (eventName.equals(Constants.event.BOOK_TITLE_CHANGED)) {
			// Bookshelf
			
			// Player
		}

		// Track.class
		else if (eventName.equals(Constants.event.TRACK_TIME_CHANGED)) {
			// Bookshelf
			
			// Player
			if (event.getNewValue() instanceof Integer) {
				int newTime = (Integer) event.getNewValue();
				this.playerFragment.updateTrackDuration(newTime);
			}
		} else {
			Log.e(TAG, "Error: eventName not recognized");
		}
	}
	
	public void bookSelected(int index) {
		if(findViewById(R.id.audiobook_layout) != null) {
			Log.d(TAG, "bookSelected(index)");
			
			playerFragment.setArguments(getIntent().getExtras());

			switchFragment(bookshelfFragment, playerFragment);
		}		
	}
	
	private void switchFragment(Fragment oldFragment, Fragment newFragment) {
		FragmentManager fm = getSupportFragmentManager();
		fm.saveFragmentInstanceState(oldFragment);

		FragmentTransaction ft = fm.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.replace(R.id.audiobook_layout, playerFragment);
		ft.addToBackStack(null);
		ft.commit();
	}

}
