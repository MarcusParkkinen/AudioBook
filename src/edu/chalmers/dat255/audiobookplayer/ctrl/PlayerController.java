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
				while (isStarted) {
					int frequency = 1000;
					while (isStarted && mp.isPlaying()) {
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
		if (bs.getCurrentTrackPath() != null) {
			isStarted = false;
			Log.i(TAG, "Resetting MediaPlayer");
			mp.reset();
			try {
				mp.setDataSource(bs.getCurrentTrackPath());
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
					+ bs.getCurrentTrackPath() + " @" + mp.getDuration() + "ms");
		} else {
			Log.e(TAG, "Start: tried to start without choosing data source.");
		}
	}

	private int getTrackDuration() {
		return bs.getCurrentTrackDuration();
	}

	private void updateTrackTime() {
		this.bs.setElapsedTime(mp.getCurrentPosition());
	}

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
			bs.setCurrentTrackIndex(bs.getNumberOfTracks() - 1);
		} else {
			// decrement track index
			bs.setCurrentTrackIndex(bs.getSelectedTrackIndex() - 1);
		}
		start(); // restart the player
	}

	public void nextTrack() {
		if (bs.getSelectedTrackIndex() != -1) {
			// increment track index unless we are done
			bs.setCurrentTrackIndex(bs.getSelectedTrackIndex() + 1);
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
		bs.setCurrentTrackIndex(track);
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
