/**
*  This work is licensed under the Creative Commons Attribution-NonCommercial-
*  NoDerivs 3.0 Unported License. To view a copy of this license, visit
*  http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to 
*  Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 
*  94041, USA.
* 
*  Use of this work is permitted only in accordance with license rights granted.
*  Materials provided "AS IS"; no representations or warranties provided.
* 
*  Copyright © 2012 Marcus Parkkinen, Aki Käkelä, Fredrik Åhs.
**/

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
import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;
import edu.chalmers.dat255.audiobookplayer.ctrl.BookshelfController;
import edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController;
import edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents;
import edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.util.BookCreator;

/**
 * The main activity of the application. TODO: insert license
 * 
 * @author Aki K�kel�, Marcus Parkkinen
 * @version 0.6
 * 
 */
public class MainActivity extends FragmentActivity implements IPlayerEvents,
		IBookshelfEvents, PropertyChangeListener {
	private static final String TAG = "MainActivity.class";
	private static final String USERNAME = "Default";
	private static final int PLAYER = 0;
	private static final int BOOKSHELF = 1;

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
		 * TODO NOTE: varje gang vi tabbar upp programmet efter multitasking sa
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

		// Provide a reference to the bookshelf as an argument in the bundle
		Bundle bsReference = new Bundle();
		bsReference.putSerializable(Constants.reference.BOOKSHELF, bs);
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

	public void addButtonPressed() {
		// bc.createTestBook();
		Intent intent = new Intent(this, BrowserActivity.class);
		startActivity(intent);
	}

	public void preferencesButtonPressed() {
		Intent intent = new Intent(this, PreferencesActivity.class);
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
				int lastBookIndex = bs.getNumberOfBooks() - 1;
				// assumes this event is never fired unless atleast one book is
				// added.
				Book b = bs.getBookAt(lastBookIndex);
				// Bookshelf
				bookshelf.bookAdded(b);

				// Player
				// Do nothing
			} else if (eventName.equals(Constants.event.BOOK_SELECTED)) {
				Book b = bs.getSelectedBook();
				// Bookshelf
				// indicate selected book
				// bookshelf.selectedBookChanged(b);
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
			} else if (eventName.equals(Constants.event.BOOK_FINISHED)) {
				// TODO
				// pc.stop();
			} else if (eventName.equals(Constants.event.ELAPSED_TIME_CHANGED)) {
				Book b = bs.getSelectedBook();
				// Bookshelf

				// Player
				// recalculate the track seekbar
				updateTrackSeekbar(b);
				// recalculate the book seekbar
				updateBookSeekbar(b);
				// update time labels
				updateElapsedTimeLabels(b);
			} else if (eventName.equals(Constants.event.TRACK_REMOVED)) {
				Book b = bs.getSelectedBook();
				// Bookshelf
				// remove the track from the list (the child on the given index
				// in the given parent)

				// Player
				// update book duration label
				updateBookDurationLabel(b);
			} else if (eventName.equals(Constants.event.TRACK_ADDED)) {
				Book b = bs.getSelectedBook();
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
				Book b = bs.getSelectedBook();
				// Bookshelf
				// move the "selected track" indicator to the new index

				// Player
				// update track title label
				updateTrackTitleLabel(b);
//				// update track book duration label
//				updateTrackDurationLabel(b);
				// set times to zero
				// TODO
			} else if (eventName.equals(Constants.event.BOOK_TITLE_CHANGED)) {
				Book b = bs.getSelectedBook();
				// Bookshelf
				// update the name of the book (parent) of the given index

				// Player
				// update the book title label
				updateBookTitleLabel(b);
			} else if (eventName.equals(Constants.event.BOOK_DURATION_CHANGED)) {
				Book b = bs.getSelectedBook();
				// Bookshelf

				// Player
				// Update book duration label
				updateBookDurationLabel(b);
			}
		}

	}

	/*
	 * Titles
	 */
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
				// TODO: check if bookshelf selectedBookIndex != -1
				player.updateBookTitleLabel(b.getBookTitle());
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
				if (b.getSelectedTrackIndex() != -1)
					player.updateTrackTitleLabel(b.getTrackTitle());
			}
		});
	}

	/*
	 * Duration times
	 */
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
				// TODO: check if bookshelf selectedBookIndex != -1
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
				if (b.getSelectedTrackIndex() != -1)
					player.updateTrackDurationLabel(b
							.getSelectedTrackDuration());
			}
		});
	}

	/*
	 * Elapsed times
	 */
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
					// TODO: check if bookshelf selectedBookIndex != -1
					if (b.getSelectedTrackIndex() != -1) {
						player.updateTrackElapsedTimeLabel(b
								.getSelectedTrackElapsedTime());
						player.updateBookElapsedTimeLabel(b
								.getBookElapsedTime());
					}
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
		if (b.getSelectedTrackIndex() != -1) {
			// elapsed time
			int trackElapsedTime = b.getSelectedTrackElapsedTime();

			// total duration
			int trackDuration = b.getSelectedTrackDuration();

			double progress = getProgress(trackElapsedTime, trackDuration);

			player.updateTrackSeekBar(progress);
		}
	}

	/**
	 * UI mutator method that updates the book seekbar in the player fragment.
	 * 
	 * @param b
	 *            Book that specifies the change
	 */
	private void updateBookSeekbar(Book b) {
		if (b.getSelectedTrackIndex() != -1) {
			// elapsed time
			int bookElapsedTime = b.getBookElapsedTime();

			// total duration
			int bookDuration = b.getDuration();

			double progress = getProgress(bookElapsedTime, bookDuration);

			player.updateBookSeekBar(progress);
		}
	}

	/**
	 * Private utility method that calculates the progress of a book or track.
	 * 
	 * @param int Elapsed time of the book or track in milliseconds.
	 * @param int Duration of the book or track in milliseconds.
	 * @return The ratio between the elapsed time and the duration.
	 */
	private double getProgress(int elapsedTime, int duration) {
		return ((double) elapsedTime) / duration;
	}

	public void childSelected(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		if (childPosition == -1 || groupPosition == -1) {
			return;
		}
		int selectedBookPosition = bsc.getSelectedBookPosition();
		// if the book is not currently selected, select it
		if (selectedBookPosition != groupPosition) {
			bookSelected(groupPosition);
		}
		// as the book is selected, track can be selected.
		bsc.getSelectedBook().setSelectedTrackIndex(childPosition);
	}

	public void deleteBook(int groupPosition) {
		// TODO Auto-generated method stub

	}

	public void editBook(int groupPosition, String updatedTitle) {
		// TODO Auto-generated method stub

	}
}
