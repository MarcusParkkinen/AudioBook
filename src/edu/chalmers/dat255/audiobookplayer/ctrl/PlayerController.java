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

package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;
import edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;

/**
 * Manages playing audio files. When its functions are called it will mutate the
 * bookshelf.
 * <p>
 * Handles timed updates of the model.
 * <p>
 * Wraps the android.media.MediaPlayer class.
 * 
 * @author Aki Käkelä
 * @version 0.6
 */
public class PlayerController implements IPlayerEvents {
	private static final String TAG = "PlayerController.class";

	// The audio player
	private MediaPlayer mp;

	// The model to mutate
	private Bookshelf bs;

	// An update thread which writes the elapsed time to the model
	private Thread trackTimeUpdateThread;

	// If the audio is playing, this is true
	private boolean isStarted = false;

	// The frequency of the updates
	private static final int UPDATE_FREQUENCY = Constants.Value.UPDATE_FREQUENCY;

	private static final double ONE_TENTH = 0.1; // 10%
	private static final double MAX_SEEK_PERCENTAGE = 1.0; // 100%

	/**
	 * Creates a PlayerController instance and initializes the Media Player and
	 * Bookshelf.
	 */
	public PlayerController(Bookshelf bs) {
		this.mp = new MediaPlayer();
		this.bs = bs;
	}

	/**
	 * Stops the update timer (audio may still keep playing).
	 */
	public void stopTimer() {
		if (trackTimeUpdateThread != null && trackTimeUpdateThread.isAlive()
				&& !trackTimeUpdateThread.isInterrupted()) {
			this.trackTimeUpdateThread.interrupt();
		}
	}

	/**
	 * Starts a new model update thread.
	 */
	public void startTimer() {
		// stop the old timer
		stopTimer();

		// start a new timer
		this.trackTimeUpdateThread = new Thread(new TrackElapsedTimeUpdater());
		this.trackTimeUpdateThread.start();
	}

	/**
	 * Stops the audio player. Stops updating the model.
	 * <p>
	 * Call when activities are paused or stopped to free resources.
	 */
	public void stop() {
		if (isStarted) {
			// revert what setup() and start() do.
			isStarted = false;
			stopTimer();
			mp.stop();
			mp.reset();

			// ensure that nothing went wrong
			if (isStarted) {
				throw new IllegalStateException(
						"Player is started after stopping.");
			}
		}
	}

	/**
	 * Starts the audio player from the beginning. Starts updating the model.
	 * <p>
	 * Can be called in any state (stopped, paused, resumed, uninitialized).
	 * <p>
	 * To start from the a given time, see {@link PlayerController#startAt(int)}.
	 * 
	 * @precondition The Bookshelf must have been initialized and the path at
	 *               the selected track index can not be null. Otherwise nothing
	 *               is done.
	 */
	public void start() {
		if (setup()) {
			mp.start();
		}
	}

	/**
	 * Starts the audio player from the given time in milliseconds. Starts
	 * updating the model.
	 * <p>
	 * Can be called in any state (stopped, paused, resumed, uninitialized).
	 * <p>
	 * To start from the beginning or resume, see
	 * {@link PlayerController#start()}.
	 * 
	 * @precondition The Bookshelf must have been initialized and the path at
	 *               the selected track index can not be null. Otherwise nothing
	 *               is done.
	 * 
	 * @param ms
	 *            Time in milliseconds to seek to before starting.
	 */
	public void startAt(int ms) {
		if (setup()) {
			seekTo(ms);
			mp.start();
		}
	}

	/**
	 * Starts the audio player from the given position in relation (percentage)
	 * to the duration of the audio file. Starts updating the model.
	 * <p>
	 * Can be called in any state (stopped, paused, resumed, uninitialized).
	 * 
	 * @precondition The Bookshelf must have been initialized and the path at
	 *               the selected track index can not be null. Otherwise nothing
	 *               is done.
	 * 
	 * @param percentage
	 *            0 <= x <= 100
	 */
	private void startAtPercentage(double percentage) {
		if (percentage >= 0 && percentage <= MAX_SEEK_PERCENTAGE && setup()) {
			seekTo((int) (mp.getDuration() * percentage));
			mp.start();
		}
	}

	/**
	 * Can only be called if the path is valid.
	 * 
	 * @return True if setup was run without problems.
	 */
	private boolean setup() {
		Log.d(TAG, "PlayerController setting up.");
		if (bs.getSelectedTrackIndex() == Constants.Value.NO_TRACK_SELECTED) {
			Log.d(TAG, "Index is -1. Should not continue playing. Stopping.");
			stop();

			// no setup was done since no track was selected
			return false;
		}
		/*
		 * we have started playing a file, so start the thread that updates the
		 * time on the Track instance
		 */

		// get the path
		String path = null;
		try {
			path = bs.getSelectedTrackPath();
		} catch (IllegalArgumentException e) {
			// the track index was '-1', so do nothing
		}
		if (path != null) {
			/*
			 * Now we are ready to set the source and start the audio. The audio
			 * is not started yet
			 */
			isStarted = false;

			// stop any currently running timer
			stopTimer();

			/*
			 * prepare the media player after resetting it and providing a file
			 * path
			 */
			mp.reset();
			try {
				mp.setDataSource(path);
				mp.prepare();
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "Illegal argument");
			} catch (SecurityException e) {
				Log.e(TAG, "Security exception");
			} catch (IllegalStateException e) {
				Log.e(TAG, "Illegal state");
			} catch (IOException e) {
				Log.e(TAG, "IO Exception");
			}
			/*
			 * listen to track completion and change to next track if a track is
			 * completed
			 */
			mp.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					Log.i(TAG,
							"onComplete: Track finished. Starting next track.");
					nextTrack();
				}
			});

			// set the status to isStarted
			isStarted = true;

			// start a new timer
			startTimer();

			// mark that the setup went without problems
			return true;
		}

		// path was null, so no setup was done
		return false;

	}

	/**
	 * Convenience method.
	 * 
	 * @return
	 */
	private int getTrackDuration() {
		// check that there is a selected track
		if (bs.getSelectedTrackIndex() != Constants.Value.NO_TRACK_SELECTED) {
			return bs.getSelectedTrackDuration();
		}

		// no track is selected, so the duration is 0ms.
		return 0;
	}

	/**
	 * Convenience method.
	 */
	private void updateTrackTime() {
		// check that there is a selected track
		if (bs.getSelectedTrackIndex() != Constants.Value.NO_TRACK_SELECTED) {
			this.bs.setSelectedTrackElapsedTime(mp.getCurrentPosition());
		}
	}

	/* IPlayerEvents */

	/* (non-Javadoc)
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#pause()
	 */
	public void pause() {
		if (isStarted && mp.isPlaying()) {
			stopTimer();
			mp.pause();
		}
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#resume()
	 */
	public void resume() {
		if (isStarted) {
			startTimer();
			mp.start();
		}
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#previousTrack()
	 */
	public void previousTrack() {
		if (isAllowedBookIndex()) {
			int trackIndex = bs.getSelectedTrackIndex();

			/*
			 * previousTrack will always modify the selected track index of the
			 * model. If no track is selected, the first track will be selected.
			 * If the last track is selected, no track will be selected (it will
			 * be stopped).
			 */

			if (trackIndex == Constants.Value.NO_TRACK_SELECTED) {
				// no track is selected, so start the last one
				// verify that it is legal (there are tracks)
				int lastIndex = bs.getNumberOfTracks() - 1;
				if (bs.isLegalTrackIndex(lastIndex)) {
					bs.setSelectedTrackIndex(lastIndex);
				}
			} else if (trackIndex == 0) {
				// first track is selected, so start the first one
				// verify that there are tracks first
				if (bs.getNumberOfTracks() > 0) {
					bs.setSelectedTrackIndex(0);
				}
			} else {
				/*
				 * a track between the second first (index 1) and the last is
				 * selected, so just select the previous track since it will be
				 * legal.
				 */

				bs.setSelectedTrackIndex(trackIndex - 1);
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#nextTrack()
	 */
	public void nextTrack() {
		if (isAllowedBookIndex()) {
			int trackIndex = bs.getSelectedTrackIndex();

			/*
			 * nextTrack will always modify the selected track index of the
			 * model. If no track is selected, the first track will be selected.
			 * If the last track is selected, no track will be selected (it will
			 * be stopped).
			 */

			if (trackIndex == Constants.Value.NO_TRACK_SELECTED) {
				// no track is selected, so start the first one
				if (bs.isLegalTrackIndex(0)) {
					bs.setSelectedTrackIndex(0);
				}
			} else if (trackIndex == bs.getNumberOfTracks() - 1) {
				// last track is selected
				bs.setSelectedTrackIndex(Constants.Value.NO_TRACK_SELECTED);
			} else {
				/*
				 * a track between the first and the second last is selected, so
				 * just select the next track since it will be legal.
				 */
				bs.setSelectedTrackIndex(trackIndex + 1);
			}

		}

	}

	/* (non-Javadoc)
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#seekRight(boolean)
	 */
	public void seekRight(boolean seek) {
		/*
		 * Note: in the future, 'seek' would have been used to determine a
		 * stopped/started state to end/start seeking.
		 */
		if (isAllowedTrackIndex()) {
			seekTo((int) (ONE_TENTH * getTrackDuration() + mp
					.getCurrentPosition()));
		}
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#seekLeft(boolean)
	 */
	public void seekLeft(boolean seek) {
		/*
		 * Note: in the future, 'seek' would have been used to determine a
		 * stopped/started state to end/start seeking.
		 */
		if (isAllowedTrackIndex()) {
			seekTo((int) (mp.getCurrentPosition() - ONE_TENTH * getTrackDuration()));
		}
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#seekToPercentageInTrack(double)
	 */
	public void seekToPercentageInTrack(double percentage) {
		if (!isLegalPercentage(percentage)) {
			Log.d(TAG,
					"Seeked to an illegal track state (negative or above 100%). "
							+ "Skipping track..");
			// simply play the next track if this happens.
			nextTrack();
		} else if (isAllowedBookIndex()) {
			if (bs.getSelectedTrackIndex() == Constants.Value.NO_TRACK_SELECTED) {
				// set the selected track index to the last one
				bs.setSelectedTrackIndex(bs.getNumberOfTracks() - 1);
			}

			if (isStarted) {
				// no need to restart the player
				seekTo((int) (mp.getDuration() * percentage));
			} else {
				/*
				 * note that startAt() can not be used as MediaPlayer is not
				 * prepared, thus it can not get the duration of the file.
				 */
				startAtPercentage(percentage);
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#seekToPercentageInBook(double)
	 */
	public void seekToPercentageInBook(double percentage) {
		if (!isLegalPercentage(percentage)) {
			Log.d(TAG,
					"Seeked to an illegal book state (negative or above 100%). "
							+ "Player stopping.");
			// stop if this happens.
			stop();
			// TODO(?): deselect the books in the model.
		} else if (isAllowedBookIndex()) {
			// get the duration of the book
			int bookDuration = bs.getSelectedBookDuration();

			// calculate the seekTime (ms)
			int seekTime = (int) (bookDuration * percentage);

			// seek through the tracks
			int track = 0, trackDuration;
			while (seekTime > (trackDuration = bs.getTrackDurationAt(track))) {
				seekTime -= trackDuration;
				track++;
			}

			if (isStarted && bs.getSelectedTrackIndex() == track) {
				// no need to restart the player since it is the same track
				seekTo(seekTime);
			} else {
				// set the correct track
				bs.setSelectedTrackIndex(track);

				/*
				 * start the track we seeked to, and finishing seeking within
				 * that track
				 */
				startAt(seekTime);
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#isStarted()
	 */
	public boolean isStarted() {
		return isStarted;
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#isPlaying()
	 */
	public boolean isPlaying() {
		return mp.isPlaying();
	}

	/* End IPlayerEvents */

	/**
	 * Checks whether the given percentage is legal (0-100).
	 * 
	 * @param percentage
	 * @return True if legal.
	 */
	private boolean isLegalPercentage(double percentage) {
		return percentage >= 0 && percentage <= MAX_SEEK_PERCENTAGE;
	}

	/**
	 * Seeks to the given time.
	 * 
	 * @param time
	 *            ms
	 */
	public void seekTo(int time) {
		if (time > mp.getDuration() || time < 0) {
			Log.e(TAG, "Attempted to seek to an invalid position: " + time);
			if (time > 0) {
				// just play the next track directly
				nextTrack();
			} else {
				// reset the current track
				mp.seekTo(0);
			}
		} else {
			// seek to the given, valid time
			mp.seekTo(time);
		}
	}

	/**
	 * Convenience method.
	 * 
	 * @return
	 */
	private boolean isAllowedTrackIndex() {
		// not allowed unless changed below
		try {
			// try to get the track index
			bs.getSelectedTrackIndex();
		} catch (IllegalArgumentException e) {
			// no book was selected
			return false;
		}
		// if 'index' was changed and there is a selected track, return true
		return true;
	}

	/**
	 * Convenience method.
	 * 
	 * @return
	 */
	private boolean isAllowedBookIndex() {
		return bs.getSelectedBookIndex() != Constants.Value.NO_BOOK_SELECTED;
	}

	/**
	 * Thread that updates the elapsed time in a track.
	 * 
	 * @author Aki Käkelä
	 * 
	 */
	private class TrackElapsedTimeUpdater implements Runnable {

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			while (isStarted && mp.isPlaying()) {
				updateTrackTime();
				try {
					Thread.sleep(UPDATE_FREQUENCY);
				} catch (InterruptedException e) {
					// the thread was interrupted, so simply end the run method.
					return;
				}
			}
		}

	}

	/*
	 * *** FOR TESTING PURPOSES ONLY ***
	 */
	/**
	 * *** FOR TESTING PURPOSES ONLY ***
	 * 
	 * Returns the media player instance for testing.
	 * 
	 */
	public MediaPlayer getMp() {
		return mp;
	}

	/**
	 * *** FOR TESTING PURPOSES ONLY ***
	 * 
	 * Returns the bookshelf for testing.
	 * 
	 */
	public Bookshelf getBs() {
		return bs;
	}

	/**
	 * *** FOR TESTING PURPOSES ONLY ***
	 * 
	 * Returns the thread for testing.
	 * 
	 */
	public Thread getTrackTimeUpdateThread() {
		return trackTimeUpdateThread;
	}

	/*
	 * *** END TESTING PURPOSES ONLY ***
	 */

}
