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
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;
import edu.chalmers.dat255.audiobookplayer.constants.PlaybackStatus;
import edu.chalmers.dat255.audiobookplayer.ctrl.BookshelfController;
import edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController;
import edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents;
import edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfGUIEvents;
import edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.util.BookCreator;
import edu.chalmers.dat255.audiobookplayer.util.BookshelfHandler;

/**
 * The main activity of the application.
 * 
 * @author Aki Käkelä, Marcus Parkkinen
 * @version 0.6
 * 
 */
public class MainActivity extends FragmentActivity implements IPlayerEvents,
		IBookshelfEvents, IBookshelfGUIEvents, PropertyChangeListener {
	private static final String TAG = "MainActivity";
	private static final String USERNAME = "Default";
	private static final int PLAYER = 0;
	private static final int BOOKSHELF = 1;

	// ViewPager
	private ViewPager pager;

	// Fragments
	private PlayerFragment playerFragment = new PlayerFragment();
	private BookshelfFragment bookshelfFragment = new BookshelfFragment();

	// Controllers
	private BookshelfController bookshelfController;
	private PlayerController playerController;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		initPager();

		// Load a bookshelf specified by the username
		Bookshelf bs = BookshelfHandler.loadBookshelf(this, USERNAME);

		// Create controllers with the bookshelf reference
		playerController = new PlayerController(bs);
		bookshelfController = new BookshelfController(bs);

		BookCreator.getInstance().setBookshelf(bs);

		// Provide a reference to the bookshelf as an argument in the bundle
		Bundle bsReference = new Bundle();
		bsReference.putSerializable(Constants.Reference.BOOKSHELF, bs);
		bookshelfFragment.setArguments(bsReference);

	}

	/**
	 * Initializes the ViewPager and its adapter.
	 */
	private void initPager() {
		// create a list of our fragments
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(playerFragment);
		fragments.add(bookshelfFragment);

		// create the adapter
		// NOTE: it is a local variable since it will not be used.
		ViewPagerAdapter adapter = new ViewPagerAdapter(
				super.getSupportFragmentManager(), fragments);

		// create the view pager and set the adapter
		pager = (ViewPager) super.findViewById(R.id.twopanelviewpager);
		pager.setAdapter(adapter);

		// default selected screen
		pager.setCurrentItem(BOOKSHELF);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStop()
	 */
	@Override
	protected void onStop() {
		Log.d(TAG, "onStop()");
		super.onStop();

		// Disable updates
		bookshelfController.removeListeners();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		Log.d(TAG, "onResume()");
		super.onResume();
		/*
		 * if the application is stopped, it will go through onStart() and then
		 * onResume(), so just start the updates again here.
		 */
		bookshelfController.addPropertyChangeListener(this);

		/*
		 * This method is run every time the application is created, starts or
		 * resumes. The updates should only start when returning from a paused
		 * or stopped state.
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	protected void onStart() {
		Log.d(TAG, "onStart()");
		super.onStart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");

		// Stop the audio player
		playerController.stop();

		/*
		 * Whenever the application is about to quit, save a bookmark.
		 */
		save();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	/**
	 * Saves a bookmark (the bookshelf).
	 */
	private boolean save() {
		return bookshelfController.saveBookshelf(this, USERNAME);
	}

	/*
	 * Handle the menu item selection. Every item has a unique id.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		switch (id) {
		case R.id.menu_save:
			// save a bookmark and show whether it was successful
			if (save()) {
				Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Saving failed", Toast.LENGTH_SHORT);
			}
			return true;

		default:
			Toast.makeText(this, "Unknown", Toast.LENGTH_SHORT).show();
			return super.onOptionsItemSelected(item);
		}
	}

	/* IPlayerEvents */

	/*
	 * The methods below relay user-initiated events from the fragments to
	 * relevant controller methods.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#previousTrack
	 * ()
	 */
	public void previousTrack() {
		playerController.previousTrack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#resume()
	 */
	public void resume() {
		playerController.resume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#pause()
	 */
	public void pause() {
		playerController.pause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#nextTrack()
	 */
	public void nextTrack() {
		playerController.nextTrack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#seekLeft
	 * (boolean)
	 */
	public void seekLeft(boolean seek) {
		playerController.seekLeft(seek);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#seekRight
	 * (boolean)
	 */
	public void seekRight(boolean seek) {
		playerController.seekRight(seek);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#
	 * seekToPercentageInBook(double)
	 */
	public void seekToPercentageInBook(double percentage) {
		playerController.seekToPercentageInBook(percentage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#
	 * seekToPercentageInTrack(double)
	 */
	public void seekToPercentageInTrack(double percentage) {
		playerController.seekToPercentageInTrack(percentage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#isPlaying()
	 */
	public boolean isPlaying() {
		return playerController.isPlaying();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#isStarted()
	 */
	public boolean isStarted() {
		return playerController.isStarted();
	}

	/* End IPlayerEvents */

	/* BookshelfUIListener */
	public void setSelectedBook(int index) {
		// set the selected book to the new index
		bookshelfController.setSelectedBook(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfGUIEvents#
	 * addBookButtonPressed()
	 */
	public void addBookButtonPressed() {
		Intent intent = new Intent(MainActivity.this, BrowserActivity.class);
		startActivity(intent);
	}

	/* End BookshelfUIListener */

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Log.i(TAG, "Back pressed in Main Activity");
		Intent intent = new Intent(Intent.ACTION_MAIN);

		// move to the home screen when pressing back
		intent.addCategory(Intent.CATEGORY_HOME);

		// start a new task
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	/**
	 * Whenever the model component changes, an event is received here. The new
	 * value property of the event contains a reference to a copy of the model.
	 * 
	 * @param event
	 *            Event object that contains information about the change.
	 */
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getNewValue() instanceof Bookshelf) {
			// Get the model copy from the update
			Bookshelf bs = (Bookshelf) event.getNewValue();

			// Update the fragments
			updateFragments(event.getPropertyName(), bs);
		}
	}

	/**
	 * Handles the updates of the fragments given a model to represent
	 * graphically.
	 * 
	 * @param eventName
	 *            The property name of the fired event.
	 * @param bs
	 *            Model copy.
	 * @precondition The bookshelf instance must not be null.
	 */
	private void updateFragments(String eventName, Bookshelf bs) {
		/*
		 * Check which event was fired, and do relevant updates in the
		 * fragments.
		 */
		if (eventName.equals(Constants.Event.BOOK_LIST_CHANGED)) {
			// update the bookshelf GUI
			bookshelfFragment.bookshelfUpdated(bs);
		} else if (eventName.equals(Constants.Event.BOOK_SELECTED)) {
			Book b = bs.getSelectedBook();

			// reset the player controls to standard values
			playerFragment.resetComponents();

			// update the player GUI components
			updatePlayerGUI(b);

			// make sure the player GUI knows it is playing
			playerFragment.setPlaybackStatus(PlaybackStatus.PLAYING);

			// show the player UI
			pager.setCurrentItem(PLAYER);

			// debug
			if (bs.getSelectedBookIndex() == Constants.Value.NO_BOOK_SELECTED) {
				throw new IndexOutOfBoundsException("Selected illegal book index!");
			}
			
			// start at the saved time
			playerController.setStartPosition(b.getSelectedTrackElapsedTime());

			// select the stored track to start playing in
			bookshelfController.setSelectedTrack(bs.getSelectedBookIndex(),
					bs.getSelectedTrackIndex());

			// TODO: the player should only seek to the saved time if a book was
			// selected (in the bookshelf UI).
		} else if (eventName.equals(Constants.Event.ELAPSED_TIME_CHANGED)) {
			Book b = bs.getSelectedBook();

			// Bookshelf
			updateSelectedBookElapsedTime(b);

			// Player
			// recalculate the track seekbar
			updateTrackSeekbar(b);
			// recalculate the book seekbar
			updateBookSeekbar(b);
			// update time labels
			updateElapsedTimeLabels(b);

		} else if (eventName.equals(Constants.Event.TRACK_LIST_CHANGED)) {
			Book b = bs.getSelectedBook();

			// Player
			// update book duration label
			updateBookDurationLabel(b);

			// show the correct number of tracks
			updateTrackCounterLabel(b);

			// Update book duration label
			updateBookDurationLabel(b);
		} else if (eventName.equals(Constants.Event.TRACK_INDEX_CHANGED)) {
			pager.setCurrentItem(PLAYER);
			if (bs.getSelectedTrackIndex() == Constants.Value.NO_TRACK_SELECTED) {
				/*
				 * Do not play audio.
				 */
				// reset the controls to 'stopped'
				playerFragment.resetComponents();

				// stop the audio player
				playerController.stop();
			} else {
				/*
				 * Play audio.
				 */

				// restart the player
				playerController.start();

				// set the status
				playerFragment.setPlaybackStatus(PlaybackStatus.PLAYING);
			}
			Book b = bs.getSelectedBook();

			// Player
			// update track title label
			updateTrackTitleLabel(b);

			// update track book duration label
			updateTrackDurationLabel(b);

			// show the correct number of tracks, illegal track index or not
			updateTrackCounterLabel(b);
		} else if (eventName.equals(Constants.Event.BOOK_TITLE_CHANGED)) {
			Book b = bs.getSelectedBook();
			// Bookshelf

			// Player
			// update the book title label
			updateBookTitleLabel(b);
		} else if (eventName.equals(Constants.Event.BOOKSHELF_UPDATED)) {
			bookshelfFragment.bookshelfUpdated(bs);
		}
		/*
		 * Eventually tag updates would have been handled here as well, calling
		 * the private method updateTags. Tags are, however, not implemented in
		 * the GUI so they will not be written here, either.
		 */
	}

	/**
	 * Updates the book title/duration, track title/duration and track counter
	 * (i.e. no changes to seek bars).
	 * <p>
	 * Does nothing if the given book is null.
	 * 
	 * @param b
	 *            Book given with the data to be represented by the Player UI.
	 */
	private void updatePlayerGUI(Book b) {
		if (b != null) {
			// show the title of the book
			updateBookTitleLabel(b);

			// display its duration
			updateBookDurationLabel(b);

			// show the title of the track
			updateTrackTitleLabel(b);

			// and display its duration
			updateTrackDurationLabel(b);

			// update the track counter
			updateTrackCounterLabel(b);
		}
	}

	/**
	 * Updates the tags in the player UI (unimplemented in GUI).
	 * 
	 * @param selectedBook
	 */
	@SuppressWarnings("unused")
	private void updateTags(final Book selectedBook) {
		playerFragment.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				playerFragment.updateTagTimes(selectedBook.getTagTimes());
			}
		});
	}

	/**
	 * Updates the count of the tracks in the player UI.
	 * 
	 * @param b
	 */
	private void updateTrackCounterLabel(final Book b) {
		playerFragment.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				int currentTrack = b.getSelectedTrackIndex();
				int numberOfTracks = b.getNumberOfTracks();
				playerFragment.updateTrackCounterLabel(currentTrack,
						numberOfTracks);
			}
		});
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
		playerFragment.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				playerFragment.updateBookTitleLabel(b.getSelectedBookTitle());
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
		playerFragment.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				if (b.getSelectedTrackIndex() != -1) {
					playerFragment.updateTrackTitleLabel(b.getTrackTitle());
				}
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
		playerFragment.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				playerFragment.updateBookDurationLabel(b.getDuration());
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
		playerFragment.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				if (b.getSelectedTrackIndex() != -1) {
					int trackDur = 0;
					try {
						trackDur = b.getSelectedTrackDuration();
					} catch (IllegalArgumentException e) {
						Log.e(TAG, "No track selected when trying to update "
								+ "track duration label. "
								+ "Setting duration to 0.");
					}
					playerFragment.updateTrackDurationLabel(trackDur);
				}
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
		if (playerFragment.getActivity() != null) {
			playerFragment.getActivity().runOnUiThread(new Runnable() {
				public void run() {
					if (b.getSelectedTrackIndex() != -1) {
						playerFragment.updateTrackElapsedTimeLabel(b
								.getSelectedTrackElapsedTime());
						playerFragment.updateBookElapsedTimeLabel(b
								.getBookElapsedTime());
					}
				}
			});
		}
	}

	/**
	 * UI mutator method that updates the book position of the selected book in
	 * bookshelf fragment
	 * 
	 * @param b
	 *            The selected book.
	 */
	private void updateSelectedBookElapsedTime(final Book b) {
		if (b.getSelectedTrackIndex() != -1
				&& bookshelfFragment.getActivity() != null) {
			bookshelfFragment.getActivity().runOnUiThread(new Runnable() {
				public void run() {
					// update
					bookshelfFragment.selectedBookElapsedTimeUpdated(b
							.getBookElapsedTime());
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

			playerFragment.updateTrackSeekBar(progress);
		}
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

		playerFragment.updateBookSeekBar(progress);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#
	 * setSelectedTrack(int, int)
	 */
	public void setSelectedTrack(int bookIndex, int trackIndex) {

		bookshelfController.setSelectedTrack(bookIndex, trackIndex);

		// restart the player
		playerController.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#removeBook
	 * (int)
	 */
	public void removeBook(int bookIndex) {
		bookshelfController.removeBook(bookIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#removeTrack
	 * (int, int)
	 */
	public void removeTrack(int bookIndex, int trackIndex) {
		bookshelfController.removeTrack(bookIndex, trackIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#
	 * setBookTitleAt(int, java.lang.String)
	 */
	public void setBookTitleAt(int bookIndex, String newTitle) {
		bookshelfController.setBookTitleAt(bookIndex, newTitle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#removeTrack
	 * (int)
	 */
	public void removeTrack(int trackIndex) {
		bookshelfController.removeTrack(trackIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#moveTrack
	 * (int, int, int)
	 */
	public void moveTrack(int bookIndex, int trackIndex, int offset) {
		bookshelfController.moveTrack(bookIndex, trackIndex, offset);
	}

}
