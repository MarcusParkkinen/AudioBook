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
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.util.BookCreator;
import edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment.BookshelfUIEventListener;
import edu.chalmers.dat255.audiobookplayer.view.PlayerFragment.PlayerUIEventListener;

/**
 * @author Aki Käkelä
 * @version 0.6
 * 
 */
public class MainActivity extends FragmentActivity implements
		PlayerUIEventListener, BookshelfUIEventListener, PropertyChangeListener {
	private final static int PLAYER = 0;
	private final static int BOOKSHELF = 1;
	private static final String TAG = "MainActivity.class";

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

		// TODO: move elsewhere
		Bookshelf bookshelf = new Bookshelf();

		// Create controllers with the bookshelf reference
		bsc = new BookshelfController(bookshelf);
		pc = new PlayerController(bookshelf);
		bc = BookCreator.getInstance();

		bc.setBookshelf(bookshelf);

		// Listen to the model
		bookshelf.addPropertyChangeListener(this);
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

	/* PlayerUIEventListener */

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

	public void addButtonPressed() {
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
				int lastBookIndex = bs.getNumberOfBooks() - 1;
				//assumes this event is never fired unless atleast one book is added.
				Book b = bs.getBookAt(lastBookIndex);
				// Bookshelf
				bookshelf.bookAdded(b);

				// Player
				// Do nothing
			} else if (eventName.equals(Constants.event.BOOK_SELECTED)) {
				Book b = bs.getCurrentBook();
				// Bookshelf
				// indicate selected book
//				bookshelf.selectedBookChanged(b);
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
	private void updateBookTitleLabel(final Book b) {
		player.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				player.updateBookTitleLabel(b.getTitle());
			}
		});
	}

	private void updateTrackTitleLabel(final Book b) {
		player.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				player.updateTrackTitleLabel(b.getTrackTitle());
			}
		});
	}

	// Duration times
	private void updateBookDurationLabel(final Book b) {
		player.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				player.updateBookDurationLabel(b.getDuration());
			}
		});
	}

	private void updateTrackDurationLabel(final Book b) {
		player.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				player.updateTrackDurationLabel(b.getTrackDuration());
			}
		});
	}

	// Elapsed times
	private void updateElapsedTimeLabels(final Book b) {
		player.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				player.updateTrackElapsedTimeLabel(b.getElapsedTime());
				player.updateBookElapsedTimeLabel(b.getBookElapsedTime());
			}
		});
	}

	private void updateTrackSeekbar(Book b) {
		// elapsed time
		int trackElapsedTime = b.getCurrentTrack().getElapsedTime();

		// total duration
		int trackDuration = b.getTrackDuration();

		double progress = getProgress(trackElapsedTime, trackDuration);

		player.updateTrackSeekBar(progress);
	}

	private void updateBookSeekbar(Book b) {
		// elapsed time
		int bookElapsedTime = b.getBookElapsedTime();

		// total duration
		int bookDuration = b.getDuration();

		double progress = getProgress(bookElapsedTime, bookDuration);

		player.updateBookSeekBar(progress);
	}

	private double getProgress(int elapsedTime, int duration) {
		return ((double) elapsedTime) / ((double) duration);
	}

	public void childSelected(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		if(childPosition == -1 || groupPosition == -1) {
			return;
		}
		int selectedBookPosition = bsc.getSelectedBookPosition();
		if(selectedBookPosition != groupPosition ) {
			bookSelected(groupPosition);
		}
		bsc.getSelectedBook().setCurrentTrackIndex(childPosition);
	}

	public void deleteBook(int groupPosition) {
		// TODO Auto-generated method stub
		
	}

	public void editBook(int groupPosition, String updatedTitle) {
		// TODO Auto-generated method stub
		
	}
}
