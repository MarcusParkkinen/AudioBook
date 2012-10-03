package edu.chalmers.dat255.audiobookplayer.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;
import edu.chalmers.dat255.audiobookplayer.ctrl.BookshelfController;
import edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController;
import edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents;
import edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.util.BookCreator;
import edu.chalmers.dat255.audiobookplayer.util.JSONParser;

/**
 * The main activity of the application. TODO: insert license
 * 
 * @author Aki Käkelä, Marcus Parkkinen
 * @version 0.6
 * 
 */
public class MainActivity extends FragmentActivity implements IPlayerEvents,
		IBookshelfEvents, PropertyChangeListener {
	private final static int PLAYER = 0;
	private final static int BOOKSHELF = 1;
	private static final String TAG = "MainActivity.class";
	private static final String USERNAME = "Default";

	// ViewPager
	private ViewPagerAdapter adapter;
	private ViewPager pager;

	// Fragments
	PlayerFragment player = new PlayerFragment();
	BookshelfFragment bookshelf = new BookshelfFragment();

	// Controllers
	private BookshelfController bsc;
	private PlayerController pc;
	private BookCreator bc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initPager();

		/*
		 * NOTE: varje gang vi tabbar upp programmet efter multitasking sa
		 * kommer allt i denna metod att koeras. Skapar konstiga problem, boer
		 * fixas. Detta gaeller aeven alla andra viewkomponenter ocksa.
		 */

		// Set up the controller for the bookshelf
		bsc = new BookshelfController();

		// Load a bookshelf specified by the username
		Bookshelf bs = bsc.loadBookshelf(this, USERNAME);

		// Create controllers with the bookshelf reference
		pc = new PlayerController(bs);
		bc = BookCreator.getInstance();

		bc.setBookshelf(bs);

		// Subscribe as a listener to the model
		bs.addPropertyChangeListener(this);

		// provide a reference to the bookshelf as a temporary resource
		Bundle bsReference = new Bundle();
		bsReference.putString(Constants.reference.BOOKSHELF,
				JSONParser.toJSON(bs));
		bookshelf.setArguments(bsReference);
	}

	private void initPager() {
		// create a list of our fragments
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(player);
		fragments.add(bookshelf);

		// create the adapter
		this.adapter = new ViewPagerAdapter(super.getSupportFragmentManager(),
				fragments);

		// create the view pager and set the adapter
		pager = (ViewPager) super.findViewById(R.id.twopanelviewpager);
		pager.setAdapter(this.adapter);

		// default selected screen
		pager.setCurrentItem(BOOKSHELF);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Whenever we're quitting the application, a bookmark
		// should be saved.
		bsc.saveBookshelf(this, USERNAME);
	}

	/* PlayerUIEventListener */

	/*
	 * The methods below relay user-initiated events from the fragments to
	 * relevant controller methods.
	 */

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

	public void seekToPercentageInBook(double percentage) {
		pc.seekToPercentageInBook(percentage);
	}

	public void seekToPercentageInTrack(double percentage) {
		pc.seekToPercentageInTrack(percentage);
	}

	/* End PlayerUIEventListener */

	/* BookshelfUIListener */
	public void bookSelected(int index) {
		// set the selected book to the new index
		bsc.setSelectedBook(index);
	}

	public void bookLongPress(int index) {
		// TODO Auto-generated method stub

	}

	public void addButtonPressed(View v) {
		// bc.createTestBook();
		Intent intent = new Intent(this, BrowserActivity.class);
		startActivity(intent);
	}

	/* End BookshelfUIListener */

	public void bookmarkSet() {
		Log.d(TAG, "Bookmark set");
	}

	public void tagSet() {
		Log.d(TAG, "Tag set");
	}

	/**
	 * Whenever the model component changes, an event is received here. The new
	 * value property of the event contains a reference to a copy of the model.
	 * 
	 * @param event
	 *            Event object that contains information about the change.
	 */
	public void propertyChange(PropertyChangeEvent event) {
		String eventName = event.getPropertyName();
		if (eventName != Constants.event.ELAPSED_TIME_CHANGED)
			Log.d(TAG, "Received update: " + eventName);

		if (event.getNewValue() instanceof Bookshelf) {
			// Get the model from the update
			Bookshelf bs = (Bookshelf) event.getNewValue();

			// Check which event was fired, and do relevant updates in the
			// fragments
			if (eventName.equals(Constants.event.BOOK_ADDED)) {
				Book b = bs.getCurrentBook();
				// Bookshelf
				bookshelf.bookAdded(b);

				// Player
				// Do nothing
			} else if (eventName.equals(Constants.event.BOOK_SELECTED)) {
				Book b = bs.getCurrentBook();
				// Bookshelf
				// indicate selected book

				// show the player UI
				pager.setCurrentItem(PLAYER);
				// start the player
				pc.start();
				// show the title of the book
				updateBookTitleLabel(b);
				// ... and display its duration
				updateBookDurationLabel(b);
				// show the duration of the track
				updateTrackDurationLabel(b);
			} else if (eventName.equals(Constants.event.BOOK_REMOVED)) {
				// Bookshelf
				// Do nothing for now

				// Player
				// Do nothing
			} else if (eventName.equals(Constants.event.BOOK_MOVED)) {
				// Bookshelf
				// Do nothing for now

				// Player
				// Do nothing
			} else if (eventName.equals(Constants.event.ELAPSED_TIME_CHANGED)) {
				System.out.println("Updating time..3");
				Book b = bs.getCurrentBook();
				// Bookshelf

				// Player
				// recalculate the track seekbar
				updateTrackSeekbar(b);
				// recalculate the book seekbar
				updateBookSeekbar(b);
				// update time labels
				updateElapsedTimeLabels(b);
			} else if (eventName.equals(Constants.event.TRACK_REMOVED)) {
				Book b = bs.getCurrentBook();
				// Bookshelf
				// remove the track from the list (the child on the given index
				// in the given parent)

				// Player
				// update book duration label
				updateBookDurationLabel(b);
			} else if (eventName.equals(Constants.event.TRACK_ADDED)) {
				Book b = bs.getCurrentBook();
				// Bookshelf
				// add ... -"-

				// Player
				// update book duration label
				updateBookDurationLabel(b);
			} else if (eventName.equals(Constants.event.TRACK_ORDER_CHANGED)) {
				// Bookshelf
				// redraw list

				// Player
				// Do nothing
			} else if (eventName.equals(Constants.event.TRACK_INDEX_CHANGED)) {
				// A new track is playing
				Book b = bs.getCurrentBook();
				// Bookshelf
				// move the "selected track" indicator to the new index

				// Player
				// update track title and duration labels
				updateTrackTitleLabel(b);
				updateTrackDurationLabel(b);
			} else if (eventName.equals(Constants.event.BOOK_TITLE_CHANGED)) {
				Book b = bs.getCurrentBook();
				// Bookshelf
				// update the name of the book (parent) of the given index

				// Player
				// update the book title label
				updateBookTitleLabel(b);
			} else if (eventName.equals(Constants.event.BOOK_DURATION_CHANGED)) {
				Book b = bs.getCurrentBook();
				// Bookshelf

				// Player
				// Update book duration label
				updateBookDurationLabel(b);
			}
		}

	}

	// Titles

	/**
	 * UI mutator method that updates the book title label in the player
	 * fragment.
	 * 
	 * @param b
	 *            Book that specifies the change
	 */
	private void updateBookTitleLabel(final Book b) {
		player.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				player.updateBookTitleLabel(b.getTitle());
			}
		});
	}

	/**
	 * UI mutator method that updates the track title in the player fragment.
	 * 
	 * @param b
	 *            Book that specifies the change
	 */
	private void updateTrackTitleLabel(final Book b) {
		player.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				player.updateTrackTitleLabel(b.getTrackTitle());
			}
		});
	}

	// Duration times

	/**
	 * UI mutator method that updates the book duration label in the player
	 * fragment.
	 * 
	 * @param b
	 *            Book that specifies the change
	 */
	private void updateBookDurationLabel(final Book b) {
		player.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				player.updateBookDurationLabel(b.getDuration());
			}
		});
	}

	/**
	 * UI mutator method that updates the track duration label in the player
	 * fragment.
	 * 
	 * @param b
	 *            Book that specifies the change
	 */
	private void updateTrackDurationLabel(final Book b) {
		player.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				player.updateTrackDurationLabel(b.getTrackDuration());
			}
		});
	}

	// Elapsed times

	/**
	 * UI mutator method that updates the track duration label in the player
	 * fragment.
	 * 
	 * @param b
	 *            Book that specifies the change
	 */
	private void updateElapsedTimeLabels(final Book b) {
		if (player.getActivity() != null) {
			player.getActivity().runOnUiThread(new Runnable() {
				public void run() {
					player.updateTrackElapsedTimeLabel(b.getElapsedTime());
					player.updateBookElapsedTimeLabel(b.getBookElapsedTime());
				}
			});
		}
	}

	/**
	 * UI mutator method that updates the track seekbar in the player fragment.
	 * 
	 * @param b
	 *            Book that specifies the change
	 */
	private void updateTrackSeekbar(Book b) {
		// elapsed time
		int trackElapsedTime = b.getCurrentTrack().getElapsedTime();

		// total duration
		int trackDuration = b.getTrackDuration();

		double progress = getProgress(trackElapsedTime, trackDuration);

		player.updateTrackSeekBar(progress);
	}

	/**
	 * UI mutator method that updates the book seekbar in the player fragment.
	 * 
	 * @param b
	 *            Book that specifies the change
	 */
	private void updateBookSeekbar(Book b) {
		// elapsed time
		int bookElapsedTime = b.getBookElapsedTime();

		// total duration
		int bookDuration = b.getDuration();

		double progress = getProgress(bookElapsedTime, bookDuration);

		player.updateBookSeekBar(progress);
	}

	/**
	 * Private utility method that calculates the progress of a book or track.
	 * 
	 * @param int elapsed time of the book or track
	 * @param int duration of the book or track
	 */
	private double getProgress(int elapsedTime, int duration) {
		return ((double) elapsedTime) / ((double) duration);
	}

}
