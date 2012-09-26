package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;

/**
 * Wraps the android.media.MediaPlayer class.
 * 
 * @author Aki Käkelä
 * @version 0.3
 */
public class PlayerController {
	public static final String TAG = "PlayerController.class";
	private MediaPlayer mp;
	private Bookshelf bs;

	/**
	 * Creates a PlayerController instance and initializes the Media Player and
	 * Queue.
	 */
	public PlayerController(Bookshelf bs) {
		this.mp = new MediaPlayer();
		this.bs = bs;
	}

	/**
	 * Pauses the audio if the player is playing. Resumes or starts otherwise.
	 */
	public void playPause() {
		if (mp.isPlaying())
			mp.pause();
		else
			mp.start();
	}

	/**
	 * Starts the audio player. The Bookshelf must have been initialized and the
	 * path at the selected track index can not be null.
	 */
	public void start() {
		// we have started playing a file, so start thread that updates time on
		// Track instance once every second, and
		if (bs.getCurrentTrackPath() != null) {
			mp.stop();
			mp.reset();
			// Initialize again?
			try {
				mp.setDataSource(bs.getCurrentTrackPath());
			} catch (IllegalArgumentException e) {
				Log.e(TAG, "Illegal argument");
			} catch (SecurityException e) {
				Log.e(TAG, "Security exception");
			} catch (IllegalStateException e) {
				Log.e(TAG, "Illegal state");
			} catch (IOException e) {
				Log.e(TAG, "IO Exception");
			}
			try {
				mp.prepare();
			} catch (IllegalStateException e) {
				Log.e(TAG, "Illegal state, should not be playing.");
			} catch (IOException e) {
				Log.e(TAG, "IO Exception");
			}
			mp.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					Log.i(TAG, "onComplete: Track finished");
					bs.incrementTrackIndex();
					start(); // does setDataSource, prepare, adds listener and
								// starts MP
				}
			});
			mp.start();
			Log.i(TAG,
					"Playing: " + bs.getSelectedBookIndex() + 1 + ". "
							+ bs.getCurrentTrackPath() + " @"
							+ mp.getDuration() + "ms");
		} else {
			Log.i(TAG, "Start: tried to start without choosing data source.");
		}
	}

	/**
	 * Seeks to the right (10% of the track duration).
	 * 
	 * @param time
	 *            ms
	 */
	public void seekRight() {
		seekToPercentage(mp.getCurrentPosition() + getTrackDuration() / 10);
	}

	/**
	 * Seeks to the left (10% of the track duration).
	 * 
	 * @param time
	 *            ms
	 */
	public void seekLeft() {
		seekToPercentage(mp.getCurrentPosition() - getTrackDuration() / 10);
	}

	/**
	 * Seeks to the given progress percentage of the track.
	 * 
	 * @param percentage
	 *            e.g. input value "50" will seek halfway (50%) through the
	 *            track.
	 */
	public void seekToPercentage(double percentage) {
		seekTo((int) (getTrackDuration() * percentage));
	}

	/**
	 * Seeks to the given time.
	 * 
	 * @param time
	 *            ms
	 */
	public void seekTo(int time) {
		mp.seekTo(time);
	}

	/**
	 * Moves the specified track to the specified index.
	 * 
	 * @param from
	 *            Start index.
	 * @param to
	 *            Where to put the track.
	 */
	public void moveTrack(int from, int to) {
		bs.moveTrack(from, to);
	}

	/**
	 * Moves the specified tracks to the specified index.
	 * 
	 * @param from
	 *            Start index.
	 * @param to
	 *            Where to put the tracks.
	 */
	public void moveTracks(int[] tracks, int to) {
		for (int i = 0; i < tracks.length; i++) {
			moveTrack(tracks[i], to);
		}
	}

	public void previousTrack() {
		bs.decrementTrackIndex();
		mp.start();
	}

	public void nextTrack() {
		bs.incrementTrackIndex();
		mp.start();
	}

	public int getTrackDuration() {
		return bs.getTrackDuration();
	}

}
