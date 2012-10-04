package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.io.IOException;
import java.text.DecimalFormat;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;

/**
 * Wraps the android.media.MediaPlayer class.
 * 
 * @author Aki Käkelä
 * @version 0.6
 */
public class PlayerController implements IPlayerEvents {
	public static final String TAG = "PlayerController.class";
	private MediaPlayer mp;
	private Bookshelf bs;
	private Thread trackTimeUpdateThread;

	private boolean isStarted = false;

	/**
	 * Creates a PlayerController instance and initializes the Media Player and
	 * Queue.
	 */
	public PlayerController(Bookshelf bs) {
		this.mp = new MediaPlayer();
		this.bs = bs;
	}

	private void startTimer() {
		trackTimeUpdateThread = new Thread(new Runnable() {
			public void run() {
				while (isStarted && mp.isPlaying()) {
					int frequency = 1000;
					// Log.d(TAG, "Updating track time @"
					// + (1000/frequency) + "x/s");
					updateTrackTime();
					try {
						Thread.sleep(frequency);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		});
		trackTimeUpdateThread.start();
	}

	/**
	 * Starts the audio player. The Bookshelf must have been initialized and the
	 * path at the selected track index can not be null.
	 */
	public void start() {
		// we have started playing a file, so start the thread that updates the
		// time on the Track instance
		if (bs.getSelectedTrackIndex() != -1 && bs.getSelectedTrackPath() != null) {
			isStarted = false;
			Log.i(TAG, "Resetting MediaPlayer");
			mp.reset();
			try {
				mp.setDataSource(bs.getSelectedTrackPath());
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
					Log.i(TAG,
							"onComplete: Track finished. Starting next track.");
					nextTrack();
				}
			});
			if (trackTimeUpdateThread == null) {
				startTimer();
			}
			mp.start();
			isStarted = true;
			Log.i(TAG, "Playing: " + (bs.getSelectedTrackIndex() + 1) + ". "
					+ bs.getSelectedTrackPath() + " @" + mp.getDuration() + "ms");
		} else {
			if (bs.getSelectedTrackIndex() == -1) 
				Log.d(TAG, "Index is -1. Should not be playing.");
			else
				Log.e(TAG, "Start: tried to start with track path == null");
		}
	}

	/**
	 * Convenience method.
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
			if (mp.isPlaying())
				mp.pause();
			else
				mp.start();
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
		seekToPercentageInTrack(mp.getCurrentPosition() + getTrackDuration()
				/ 10);
	}

	public void seekLeft() {
		seekToPercentageInTrack(mp.getCurrentPosition() - getTrackDuration()
				/ 10);
	}

	public void seekToPercentageInTrack(double percentage) {
		seekTo((int) (mp.getDuration() * percentage));
	}

	public void seekToPercentageInBook(double percentage) {
		DecimalFormat df = new DecimalFormat("#.##");
		Log.d(TAG, "percentage: " + df.format(percentage));
		int bookDuration = bs.getSelectedBookDuration();
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
		bs.setSelectedTrackIndex(track);
		start(); // start the track we seeked to
		mp.seekTo(seekTime); // seek to the time within that track
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

}
