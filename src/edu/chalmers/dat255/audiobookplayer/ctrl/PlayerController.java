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
 * Wraps the android.media.MediaPlayer class.
 * 
 * @author Aki K�kel�
 * @version 0.6
 */
public class PlayerController implements IPlayerEvents {

	private static final String TAG = "PlayerController.class";
	private MediaPlayer mp;
	private Bookshelf bs;
	private Thread trackTimeUpdateThread;

	private boolean isStarted = false;
	private static final int UPDATE_FREQUENCY = Constants.Value.UPDATE_FREQUENCY;

	/**
	 * Creates a PlayerController instance and initializes the Media Player and
	 * Queue.
	 */
	public PlayerController(Bookshelf bs) {
		this.mp = new MediaPlayer();
		this.bs = bs;
	}

	/**
	 * Stops the update timer but keeps playing audio.
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
			isStarted = false;
			stopTimer();
			mp.stop();
			mp.reset();
		}
	}

	/**
	 * Starts the audio player from the beginning. Starts updating the model.
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
			mp.seekTo(ms);
			mp.start();
		}
	}

	/**
	 * Can only be called if the path is valid
	 * 
	 * @return True if setup was run without problems.
	 */
	private boolean setup() {
		if (bs.getSelectedTrackIndex() == -1) {
			Log.d(TAG, "Index is -1. Should not continue playing.");
			stop();
		} else {
			// we have started playing a file, so start the thread that updates
			// the time on the Track instance

			// get the path
			String path = null;
			try {
				path = bs.getSelectedTrackPath();
			} catch (IllegalArgumentException e) {
				// the track index was '-1'
				Log.e(TAG,
						"Attempted to get track path when track index was -1."
								+ "Path set to null; skipping start in Player.");
			}
			if (path != null) {
				/*
				 * now we are ready to set the source and start the audio, but
				 * it is not started yet
				 */
				isStarted = false;
				stopTimer();

				// Log.i(TAG, "Resetting MediaPlayer");
				// prepare the media player after resetting it and providing a
				// file path
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
				 * listen to track completion and change to next track if a
				 * track is completed
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

				// start the timer
				startTimer();

				// mark that the setup went without problems
				return true;
			} else {
				Log.e(TAG, "Start: tried to start with track path == null");
			}
		}
		return false;

	}

	/**
	 * Convenience method.
	 * 
	 * @return
	 */
	private int getTrackDuration() {
		return bs.getSelectedTrackDuration();
	}

	/**
	 * Convenience method.
	 */
	private void updateTrackTime() {
		this.bs.setSelectedTrackElapsedTime(mp.getCurrentPosition());
	}

	/* IPlayerEvents */

	public void playPause() {
		if (isStarted) {
			if (mp.isPlaying()) {
				mp.pause();
			} else {
				mp.start();
			}
		}
	}

	public void pause() {
		if (isStarted) {
			if (mp.isPlaying()) {
				mp.pause();
			}
		}
	}

	public void play() {
		if (isStarted) {
			if (!mp.isPlaying()) {
				mp.start();
			}
		}
	}

	public void previousTrack() {
		if (bs.getSelectedTrackIndex() == -1) {
			// go to the final track (replay it)
			bs.setSelectedTrackIndex(bs.getNumberOfTracks() - 1);
		} else {
			// decrement track index
			bs.setSelectedTrackIndex(bs.getSelectedTrackIndex() - 1);
		}
		start(); // restart the player
	}

	public void nextTrack() {
		if (bs.getSelectedTrackIndex() != -1) {
			// increment track index unless we are done
			bs.setSelectedTrackIndex(bs.getSelectedTrackIndex() + 1);
			start(); // restart the player
		}
	}

	public void seekRight() {
		if (isAllowed()) {
			seekToPercentageInTrack(mp.getCurrentPosition()
					+ getTrackDuration() / 10);
		}
	}

	public void seekLeft() {
		if (isAllowed()) {
			seekToPercentageInTrack(mp.getCurrentPosition()
					- getTrackDuration() / 10);
		}
	}

	public void seekToPercentageInTrack(double percentage) {
		if (isAllowed()) {
			seekTo((int) (mp.getDuration() * percentage));
		}
	}

	private boolean isAllowed() {
		return bs.getSelectedTrackIndex() != -1;
	}

	public void seekToPercentageInBook(double percentage) {
		// get the duration of the book
		int bookDuration = bs.getSelectedBookDuration();

		// calculate the seekTime (ms)
		int seekTime = (int) (bookDuration * percentage);

		Log.d(TAG, "seekTime: " + seekTime + ". Book duration: " + bookDuration);

		// seek through the tracks
		int track = 0, trackDuration;
		while (seekTime > (trackDuration = bs.getTrackDurationAt(track))) {
			seekTime -= trackDuration;
			track++;
			Log.d(TAG, "Skipped a track (" + trackDuration
					+ "ms) . New seekTime: " + seekTime + ". Track#: " + track);
		}

		// set the correct track
		bs.setSelectedTrackIndex(track);

		// start the track we seeked to
		start();

		// and finishing seeking within that track
		seekTo(seekTime);
	}

	/* End IPlayerEvents */

	/**
	 * Seeks to the given time.
	 * 
	 * @param time
	 *            ms
	 */
	public void seekTo(int time) {
		mp.seekTo(time);
	}

	private class TrackElapsedTimeUpdater implements Runnable {

		public void run() {
			while (isStarted && mp.isPlaying()) {
				// Log.d(TAG, "Updating Track Elapsed Time");
				// Log.d(TAG, "Updating track time @" + (1000 /
				// UPDATE_FREQUENCY)
				// + "x/s");
				updateTrackTime();
				try {
					Thread.sleep(UPDATE_FREQUENCY);
				} catch (InterruptedException e) {
					// the thread was interrupted, so simply end run.
					Log.d(TAG, "Track elapsed time updater stopped.");
					return;
				}
			}
		}

	}

	/**
	 * Checks if the player is currently playing audio (not paused, stopped or
	 * uninitialized).
	 * 
	 * @return
	 */
	public boolean isStarted() {
		return isStarted;
	}

	/*
	 * *** FOR TESTING PURPOSES ONLY ***
	 */
	public MediaPlayer getMp() {
		return mp;
	}

	public Bookshelf getBs() {
		return bs;
	}

	public Thread getTrackTimeUpdateThread() {
		return trackTimeUpdateThread;
	}

	/*
	 * *** ---
	 */
}
