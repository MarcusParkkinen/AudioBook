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
import edu.chalmers.dat255.audiobookplayer.util.JSONParser;
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
	private static final String USERNAME = "Default";

	// Fragment classes
	private final BookshelfFragment bookshelfFragment = new BookshelfFragment();
	private final PlayerFragment playerFragment = new PlayerFragment();

	// Controller classes
	private BookshelfController bsc;
	private PlayerController pc;
	private BookCreator bc;
	
	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_audiobook);

		// Set up the controller for the bookshelf
		bsc = new BookshelfController(this, USERNAME);
		// Request a reference to the bookshelf
		Bookshelf bookshelf = bsc.getBookshelf();

		// Instantiate Controller classes
		pc = new PlayerController(bookshelf);
		bc = BookCreator.getInstance();
		bc.setBookshelf(bookshelf);

		// Add this class as a property change listener to the model
		bookshelf.addPropertyChangeListener(this);

		// Make sure that audiobook_layout is being used as layout for this
		// activity and that we are not resuming from a previous state as
		//this could result in overlapping fragments
		if (findViewById(R.id.audiobook_layout) != null
				&& savedInstance == null) {

			// In case arguments have been sent through an Intent, forward these
			// to the fragments as well
			bookshelfFragment.setArguments(getIntent().getExtras());

			// Add the bookshelf fragment to this layout
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(R.id.audiobook_layout, bookshelfFragment);
			ft.commit();
		}

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		bsc.saveBookshelf(this, USERNAME);
	}
	
	public void bookLongPress(int index) {
		// TODO
	}

	/* Player*.class functions */
	public void addButtonPressed(View v) {
		
		// Start the browser activity
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
		Log.d(TAG, "Received update: " + eventName);

		if (event.getNewValue() instanceof Bookshelf) {
			// Get the model from the update
			Bookshelf bs = (Bookshelf) event.getNewValue();

			// Check which event was fired, and do relevant updates in the
			// fragments
			if (eventName.equals(Constants.event.BOOK_ADDED)) {
				// Bookshelf
				Book b = bs.getCurrentBook();
				bookshelfFragment.bookAdded(b);

				// Player
				// Do nothing
			} else if (eventName.equals(Constants.event.BOOK_SELECTED)) {
				// Bookshelf
				// indicate selected book

				// show the player
				Log.d(TAG, "Started switching fragments");
				switchToPlayerFragment();
				// start the player
				pc.start();
//				this.playerFragment.updateBookTitleLabel();
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
				// Log.d(TAG, "Updating seek bars...");
				// Log.d(TAG, "playerFragment is " +
				// (playerFragment.isAdded()?"":"not ") + "added");
				if (playerFragment != null && playerFragment.isAdded()) {
					// recalculate the book seekbar
					updateBookSeekbar(b);
					// recalculate the track seekbar
					updateTrackSeekbar(b);
					// Log.d(TAG, "...done updating seek bars.");

					// update time labels
//					this.playerFragment.updateTrackElapsedTimeLabel(""
//							+ b.getElapsedTime());
//					this.playerFragment.updateBookElapsedTimeLabel(""
//							+ b.getBookElapsedTime());
				}
			} else if (eventName.equals(Constants.event.TRACK_REMOVED)) {
				// Bookshelf
				// remove the track from the list (the child on the given index
				// in the given parent)

				// Player
				// Do nothing
			} else if (eventName.equals(Constants.event.TRACK_ADDED)) {
				// Bookshelf
				// add ... -"-

				// Player
				// Do nothing
			} else if (eventName.equals(Constants.event.TRACK_ORDER_CHANGED)) {
				// Bookshelf
				// redraw list

				// Player
				// Do nothing
			} else if (eventName.equals(Constants.event.TRACK_INDEX_CHANGED)) {
				Book b = bs.getCurrentBook();
				// Bookshelf
				// move the "selected track" indicator to the new index

				// Player
				// update the track name label
				// recalculate the track seekbar
//				this.playerFragment.updateTrackDurationLabel(""
//						+ b.getTrackDuration());
//				this.playerFragment.updateTrackTitleLabel(b.getTrackTitle());
			} else if (eventName.equals(Constants.event.BOOK_TITLE_CHANGED)) {
				// Bookshelf
				// update the name of the book (parent) of the given index

				// Player
				// update the book title label
				Book b = bs.getCurrentBook();
//				this.playerFragment.updateBookTitleLabel(b.getTitle());
			} else if (eventName.equals(Constants.event.BOOK_DURATION_CHANGED)) {
				// Bookshelf

				// Player
				// update the book duration label
				Book b = bs.getCurrentBook();
//				this.playerFragment.updateBookDurationLabel(""
//						+ b.getElapsedTime());
			}
		}

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

	private void switchToPlayerFragment() {
		if (findViewById(R.id.audiobook_layout) != null) {
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
		Log.d(TAG, "Finished switching fragments");
	}
	
	/**
	 * Utility method that empties and fills the bookshelf fragment list.
	 */
	public void refillBookshelf() {
		Bookshelf bs = bsc.getBookshelf();
		
		for(int i = 0; i < bs.getNumberOfBooks(); i++) {
			bookshelfFragment.bookAdded(bs.getBookAt(i));
		}
	}
	
	// public int getTrackDuration() {
	// return pc.getTrackDuration();
	// }
	//
	// public int getBookDuration() {
	// return pc.getBookDuration();
	// }

}
