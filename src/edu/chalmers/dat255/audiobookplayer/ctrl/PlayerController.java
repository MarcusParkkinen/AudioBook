package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
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
	private Thread modelUpdaterThread;

	private boolean isStarted = false;

	/**
	 * Creates a PlayerController instance and initializes the Media Player and
	 * Queue.
	 */
	public PlayerController(Bookshelf bs) {
		this.mp = new MediaPlayer();
		this.bs = bs;
	}

	/* * * * * * * * * * * * * * * * * * * * *
	 * PLAYBACK * * * * * * * * * * * * * * * * * * *
	 */
	/**
	 * Pauses the audio if the player is playing. Resumes or starts otherwise.
	 */
	public void playPause() {
		if (isStarted) {
			if (mp.isPlaying())
				mp.pause();
			else
				mp.start();
		}
	}

	private void startTimer() {
		modelUpdaterThread = new Thread(new Runnable() {
			public void run() {
				while (isStarted) {
					int frequency = 1000; // TODO: this should maybe not be
											// hard-coded
					while (isStarted && mp.isPlaying()) {
						// Log.d(TAG, "Updating track time @"
						// + (1000/frequency) + "x/s");
						updateTrackTime(frequency);
						try {
							Thread.sleep(frequency);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}

		});
		modelUpdaterThread.start();
	}

	/**
	 * Starts the audio player. The Bookshelf must have been initialized and the
	 * path at the selected track index can not be null.
	 */
	public void start() {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		// we have started playing a file, so start thread that updates time on
		// Track instance once every second, and
		if (bs.getCurrentTrackPath() != null) {
			isStarted = false;
			Log.i(TAG, "Resetting MediaPlayer");
			mp.reset();
			try {
				// path = path + "/game.mp3";
				path = path + bs.getCurrentTrackPath();
				Log.i(TAG, "Trying to set <" + path + "> as source.");
				mp.setDataSource(path);
				Log.i(TAG, "Preparing MediaPlayer");
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
			mp.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					Log.i(TAG, "onComplete: Track finished");
					nextTrack();
				}
			});
			if (modelUpdaterThread == null) {
				startTimer();
			}
			mp.start();
			isStarted = true;
			Log.i(TAG, "Playing: " + (bs.getCurrentTrackIndex() + 1) + ". "
					+ bs.getCurrentTrackPath() + " @" + mp.getDuration() + "ms");
		} else {
			Log.e(TAG, "Start: tried to start without choosing data source.");
		}
	}

	/* * * * * * * * * * * * * * * * * * * * *
	 * TRACK * * * * * * * * * * * * * * * * * * *
	 */
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
		// decrement track index
		bs.setCurrentTrackIndex(bs.getCurrentTrackIndex() - 1);
		start(); // restart with the next index
	}

	public void nextTrack() {
		// increment track index
		bs.setCurrentTrackIndex(bs.getCurrentTrackIndex() + 1);
		if (bs.getCurrentTrackIndex() != -1) // check whether we are done
			start(); // restart with the next index
	}

	/**
	 * The track duration in milliseconds.
	 * 
	 * @return
	 */
	public int getTrackDuration() {
		return bs.getTrackDuration();
	}

	/**
	 * The book duration in milliseconds.
	 * 
	 * @return
	 */
	public int getBookDuration() {
		return bs.getBookDuration();
	}

	/**
	 * Saves the elapsed time of the track at a set frequency.
	 * 
	 * @param time
	 *            ms
	 */
	public void updateTrackTime(int time) {
		this.bs.setElapsedTime(mp.getCurrentPosition());
	}

	/* * * * * * * * * * * * * * * * * * * * *
	 * SEEK * * * * * * * * * * * * * * * * * * *
	 */
	/**
	 * Seeks to the right (10% of the track duration).
	 * 
	 * @param time
	 *            ms
	 */
	public void seekRight() {
		seekToMultiplierInTrack(mp.getCurrentPosition() + getTrackDuration()
				/ 10);
	}

	/**
	 * Seeks to the left (10% of the track duration).
	 * 
	 * @param time
	 *            ms
	 */
	public void seekLeft() {
		seekToMultiplierInTrack(mp.getCurrentPosition() - getTrackDuration()
				/ 10);
	}

	/**
	 * Seeks to the given progress percentage of the track.
	 * 
	 * @param multiplier
	 *            e.g. input value "50" will seek halfway (50%) through the
	 *            track.
	 */
	public void seekToMultiplierInTrack(double multiplier) {
		double temp = mp.getDuration() * multiplier;
		Log.d(TAG, "seekTo: " + mp.getDuration() + " * " + multiplier + " = "
				+ temp + " = " + (int) temp);
		seekTo((int) temp);
	}

	/**
	 * Seeks <i>time</i> ms through the tracks of the book.
	 * 
	 * @param multiplier
	 *            e.g. input value "50" will seek halfway (50%) through the
	 *            book.
	 */
	public void seekToMultiplierInBook(double multiplier) {
		int seekTime = (int) (bs.getBookDuration() * multiplier);
		while (seekTime > bs.getTrackDuration()) {
			seekTime -= bs.getTrackDuration();
			bs.setCurrentTrackIndex(bs.getCurrentTrackIndex() - 1);
			/*
			 * note that we are not changing the player state just by changing
			 * the index.
			 */
		}
		start(); // start the track we seeked to
		mp.seekTo(seekTime); // seek to the time within that track
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

}
