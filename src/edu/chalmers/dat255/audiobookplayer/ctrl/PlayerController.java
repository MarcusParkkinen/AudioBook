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

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
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
public class PlayerController implements IPlayerEvents, OnPreparedListener,
		OnCompletionListener, OnErrorListener {
	private static final String TAG = "PlayerController";

	/**
	 * The audio player
	 */
	private MediaPlayer mp;

	/**
	 * The model to mutate
	 */
	private Bookshelf bs;

	/**
	 * An update thread which writes the elapsed time to the model
	 */
	private Thread trackTimeUpdateThread;

	/**
	 * If audio is playing, this is true
	 */
	private boolean isStarted = false;

	/**
	 * When audio starts, it will seek to this value. Needed both for safety and
	 * to prevent errors with MediaPlayer.
	 */
	private transient int seekRemainder = 0;

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
		if (trackTimeUpdateThread != null && trackTimeUpdateThread.isAlive()) {
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
		setup();
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
		seekRemainder = ms;
		setup();
	}

	/**
	 * Can only be called if the path is valid.
	 * 
	 * @return True if setup was run without problems. False if nothing was done
	 *         (no selected track or path is null).
	 */
	private boolean setup() {
		Log.d(TAG, "PlayerController setting up.");
		isStarted = false;

		// do nothing if no track is selected
		if (bs.getSelectedTrackIndex() == Constants.Value.NO_TRACK_SELECTED) {
			Log.d(TAG, "Stopping since track index is not selected.");
			stop();

			// no setup was done since no track was selected
			return false;
		}

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
			 * is not started yet.
			 */

			// stop any currently running timer
			stopTimer();

			/*
			 * Reset the media player and then prepare it, providing a file
			 * path.
			 */
			Log.d(TAG, "Resetting mp.---");
			mp.reset();
			Log.d(TAG, "---Resetting mp.");

			// start listening for when MediaPlayer is prepared
			mp.setOnPreparedListener(this);

			// listen to track completion
			mp.setOnCompletionListener(this);

			// listen to errors
			mp.setOnErrorListener(this);

			// set the stream type before preparing
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

			// set the data source and start preparing
			try {
				mp.setDataSource(path);
				mp.prepareAsync();
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "Illegal argument");
			} catch (SecurityException e) {
				Log.e(TAG, "Security exception");
			} catch (IllegalStateException e) {
				Log.e(TAG, "Illegal state");
			} catch (IOException e) {
				Log.e(TAG, "IO Exception");
			}

			// mark that the setup went without problems
			Log.d(TAG, "setup ok.");
			return true;
		}

		// path was null, so no setup was done
		return false;

	}

	/**
	 * Stops the audio player. Stops updating the model.
	 * <p>
	 * Call when activities are paused or stopped to free resources.
	 */
	public void stop() {
		tearDown();
	}

	/**
	 * Reverts what setup does.
	 */
	private void tearDown() {
		Log.d(TAG, "Resetting player values.");
		isStarted = false;
		stopTimer();
		mp.stop();
		mp.reset();

		// ensure that nothing went wrong
		if (isStarted) {
			throw new IllegalStateException(
					"Player is still started after stopping.");
		}
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

	/* IPlayerEvents */

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#pause()
	 */
	public void pause() {
		if (isStarted && mp.isPlaying()) {
			stopTimer();
			mp.pause();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#resume()
	 */
	public void resume() {
		if (isStarted) {
			startTimer();
			mp.start();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#previousTrack
	 * ()
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#nextTrack()
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#seekRight
	 * (boolean)
	 */
	public void seekRight(boolean seek) {
		/*
		 * Note: in the future, 'seek' would have been used to determine a
		 * stopped/started state to end/start seeking.
		 */
		if (isAllowedTrackIndex() && getTrackDuration() != 0) {
			seekTo((int) (ONE_TENTH * getTrackDuration() + mp
					.getCurrentPosition()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#seekLeft
	 * (boolean)
	 */
	public void seekLeft(boolean seek) {
		/*
		 * Note: in the future, 'seek' would have been used to determine a
		 * stopped/started state to end/start seeking.
		 */
		if (isAllowedTrackIndex() && getTrackDuration() != 0) {
			seekTo((int) (mp.getCurrentPosition() - ONE_TENTH
					* getTrackDuration()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#
	 * seekToPercentageInTrack(double)
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
				Log.d(TAG, "No track selected when seeking with track bar. "
						+ "Setting the first track as selected.");
				// set the selected track index to the first one
				bs.setSelectedTrackIndex(0);
			} else {
				// seek to the new time
				seekTo((int) (mp.getDuration() * percentage));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#
	 * seekToPercentageInBook(double)
	 */
	public void seekToPercentageInBook(double percentage) {
		if (!isLegalPercentage(percentage)) {
			Log.e(TAG,
					"Seeked to an illegal book state (negative or above 100%). "
							+ "Player stopping.");
			// stop if this happens.
			stop();
		} else if (isAllowedBookIndex()) {
			// get the duration of the book
			int bookDuration = bs.getSelectedBookDuration();

			// calculate the seek time (ms)
			seekRemainder = (int) (bookDuration * percentage);

			// go through the tracks and calculate the remainder and track index
			int track = 0;
			int trackDuration = 0;
			int selectedBook = bs.getSelectedBookIndex();
			while (seekRemainder > (trackDuration = bs.getTrackDurationAt(
					selectedBook, track))) {
				seekRemainder -= trackDuration;
				track++;
			}

			// change to the correct track
			bs.setSelectedTrackIndex(track);

			// setting the track index already starts the player
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#isStarted()
	 */
	public boolean isStarted() {
		return isStarted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents#isPlaying()
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
			Log.e(TAG, "Attempted to seek to an invalid position: " + time
					+ ". Fixing by seeking to the end or beginning.");
			if (time > 0) {
				// just play the next track directly
				nextTrack();
			} else {
				// reset the current track
				Log.d(TAG, "seeking --");
				mp.seekTo(0);
				Log.d(TAG, "-- seeking");
			}
		} else {
			// seek to the given, valid time
			Log.d(TAG, "seeking to " + time + " --");
			mp.seekTo(time);
			Log.d(TAG, "-- seeking to " + time);
		}
	}

	/**
	 * Convenience method.
	 * 
	 * @return
	 */
	private boolean isAllowedTrackIndex() {
		return isAllowedBookIndex()
				&& bs.isLegalTrackIndex(bs.getSelectedTrackIndex());
	}

	/**
	 * Convenience method.
	 * 
	 * @return
	 */
	private boolean isAllowedBookIndex() {
		return bs.isLegalBookIndex(bs.getSelectedBookIndex());
	}

	/**
	 * Thread that updates the elapsed time in a track.
	 * 
	 * @author Aki Käkelä
	 * @version 0.2
	 * 
	 */
	private class TrackElapsedTimeUpdater implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
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

	/**
	 * Convenience method.
	 */
	private void updateTrackTime() {
		// check that there is a selected track
		if (isAllowedTrackIndex()) {
			this.bs.setSelectedTrackElapsedTime(mp.getCurrentPosition());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.media.MediaPlayer.OnPreparedListener#onPrepared(android.media
	 * .MediaPlayer)
	 */
	public void onPrepared(MediaPlayer mp) {
		// seek to starting position
		if (seekRemainder != 0) {
			seekTo(seekRemainder);
		}

		// start the media player
		mp.start();

		isStarted = true;

		// start a new timer
		startTimer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.media.MediaPlayer.OnCompletionListener#onCompletion(android.media
	 * .MediaPlayer)
	 */
	public void onCompletion(MediaPlayer mp) {
		Log.i(TAG, "onComplete: Track finished. Starting next track.");
		nextTrack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.media.MediaPlayer.OnErrorListener#onError(android.media.MediaPlayer
	 * , int, int)
	 */
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		Log.e(TAG, "Error: " + arg1 + ", " + arg2 + ", at " + arg0.toString());
		return false;
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
