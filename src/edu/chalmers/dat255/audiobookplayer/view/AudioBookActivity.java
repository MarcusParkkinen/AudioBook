package edu.chalmers.dat255.audiobookplayer.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
 * Main activity of the application. This class should handle all fragment
 * functionality.
 * 
 * @author Marcus Parkkinen, Aki K�kel�
 * @version 0.6
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

		// Make sure that audiobook_layout is being used as layout for this
		// activity and
		// that we are not resuming from a previous state as this could result
		// in overlapping
		// fragments
		if (findViewById(R.id.audiobook_layout) != null
				&& savedInstance == null) {

			// In case arguments have been sent through an Intent, forward these
			// to the
			// fragments as well
			bookshelfFragment.setArguments(getIntent().getExtras());

			// Add the STARTING_FRAGMENT to this layout
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(R.id.audiobook_layout, bookshelfFragment);
			ft.commit();
		}
		
		// Player fragment
		if (findViewById(R.id.audiobook_layout) != null) {
			playerFragment.setArguments(getIntent().getExtras());
		}

	}

	public void bookLongPress(int index) {
		// TODO
		// Show book stats or enable drag&drop to sort
	}

	/* Player*.class functions */
	public void addButtonPressed(View v) {
		// bc.createTestBook();
		Intent intent = new Intent(this, BrowserActivity.class);
		startActivity(intent);
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

	public void seekToPercentageInBook(double percentage) {
		pc.seekToPercentageInBook(percentage);
	}

	public void seekToPercentageInTrack(double percentage) {
		pc.seekToPercentageInTrack(percentage);
	}

	/* End player */

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
		// Log.d(TAG, "Received update: " + eventName);

		if (event.getNewValue() instanceof Bookshelf) {
			// Get the model from the update
			Bookshelf bs = (Bookshelf) event.getNewValue();

			// Debug playerFragment
			if (!playerFragment.isAdded()) {
				Log.e(TAG, "playerFragment has not yet been added.");
			}
			if (playerFragment == null) {
				Log.e(TAG, "playerFragment is null when taking updates.");
			}

			// Check which event was fired, and do relevant updates in the
			// fragments
			if (eventName.equals(Constants.event.BOOK_ADDED)) {
				Book b = bs.getCurrentBook();
				// Bookshelf
				bookshelfFragment.bookAdded(b);

				// Player
				// Do nothing
			} else if (eventName.equals(Constants.event.BOOK_SELECTED)) {
				Book b = bs.getCurrentBook();
				// Bookshelf
				// indicate selected book

				// show the player UI
				switchFragment(bookshelfFragment, playerFragment);
				// start the player
				pc.start();
				// show the title of the book
				updateBookTitleLabel(b);
				// ... and display its duration
				updateBookDurationLabel(b);
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
				// Bookshelf

				// Player
				Book b = bs.getCurrentBook();
				if (playerFragment != null && playerFragment.isAdded()) {
					// recalculate the track seekbar
					updateTrackSeekbar(b);
					// recalculate the book seekbar
					updateBookSeekbar(b);
					// Log.d(TAG, "...done updating seek bars.");

					// update time labels
					updateElapsedTimeLabels(b);
				}
			} else if (eventName.equals(Constants.event.TRACK_REMOVED)) {
				Book b = bs.getCurrentBook();
				// Bookshelf
				// remove the track from the list (the child on the given index
				// in the given parent)

				// Player
				// update the book duration label
				updateBookDurationLabel(b);
			} else if (eventName.equals(Constants.event.TRACK_ADDED)) {
				Book b = bs.getCurrentBook();
				// Bookshelf
				// add ... -"-

				// Player
				// update the book duration label
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
				// update track title label
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
		playerFragment.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				playerFragment.updateBookTitleLabel(b.getTitle());
			}
		});
	}

	private void updateTrackTitleLabel(final Book b) {
		playerFragment.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				playerFragment.updateTrackTitleLabel(b.getTrackTitle());
			}
		});
	}

	// Duration times
	private void updateBookDurationLabel(final Book b) {
		playerFragment.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				playerFragment.updateBookDurationLabel(b.getDuration());
			}
		});
	}

	private void updateTrackDurationLabel(final Book b) {
		playerFragment.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				playerFragment.updateTrackDurationLabel(b.getTrackDuration());
			}
		});
	}

	// Elapsed times
	private void updateElapsedTimeLabels(final Book b) {
		playerFragment.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				playerFragment.updateTrackElapsedTimeLabel(b.getElapsedTime());
				playerFragment.updateBookElapsedTimeLabel(b
						.getBookElapsedTime());
			}
		});
	}

	private void updateTrackSeekbar(Book b) {
		// elapsed time
		int trackElapsedTime = b.getCurrentTrack().getElapsedTime();

		// total duration
		int trackDuration = b.getTrackDuration();

		double progress = getProgress(trackElapsedTime, trackDuration);

		playerFragment.updateTrackSeekBar(progress);
	}

	private void updateBookSeekbar(Book b) {
		// elapsed time
		int bookElapsedTime = b.getBookElapsedTime();

		// total duration
		int bookDuration = b.getDuration();

		double progress = getProgress(bookElapsedTime, bookDuration);

		playerFragment.updateBookSeekBar(progress);
	}

	private double getProgress(int elapsedTime, int duration) {
		return ((double) elapsedTime) / ((double) duration);
	}

	public void bookSelected(int index) {
		// set the selected book to the new index
		bsc.setSelectedBook(index);
	}

	private void switchFragment(Fragment oldFragment, Fragment newFragment) {
		FragmentManager fm = getSupportFragmentManager();
		fm.saveFragmentInstanceState(oldFragment);

		FragmentTransaction ft = fm.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.replace(R.id.audiobook_layout, playerFragment);
		ft.addToBackStack(null);
		ft.commit();
		Log.d(TAG, "Finished switching fragments");
	}

	// public int getTrackDuration() {
	// return pc.getTrackDuration();
	// }
	//
	// public int getBookDuration() {
	// return pc.getBookDuration();
	// }

}
