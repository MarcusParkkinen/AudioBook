package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.io.IOException;
import java.util.List;

import edu.chalmers.dat255.audiobookplayer.model.PlayerQueue;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

/**
 * Wraps the android.media.MediaPlayer class.
 * 
 * @author Aki Käkelä
 * @version 0.1
 */
public class PlayerController {
	private MediaPlayer mp;
	private PlayerQueue pq;
	
	private OnCompletionListener ocl = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mp) {
			Log.i("onComplete", "Track finished");
			pq.incrementTrackIndex();
			try {
				mp.setDataSource(pq.getCurrentTrackPath());
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				mp.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("-- NEXT: " + mp.getDuration() + "ms");
		}
	};

	/**
	 * Creates a PlayerController instance and initializes the Media Player and
	 * Queue.
	 */
	public PlayerController() {
		mp = new MediaPlayer();
		pq = new PlayerQueue();
	}

	/**
	 * Pauses the audio if the player is playing.
	 */
	public void pause() {
		if (mp.isPlaying())
			mp.pause();
	}

	/**
	 * Resumes the player if (1) it was paused, (2) the queue is initialized and
	 * (3) the queue is not empty.
	 */
	public void play() {
		if (pq != null && pq.getCurrentTrackPath() != null) {
			mp.start();
		}
	}

	/**
	 * Starts the audio player if the queue is initialized and not empty.
	 */
	public void start() {
		if (pq != null && pq.getCurrentTrackPath() != null) {
			try {
				mp.setDataSource(pq.getCurrentTrackPath());
			} catch (IllegalArgumentException e) {
				Log.e("start IllegalArgumentException", "");
				e.printStackTrace();
			} catch (SecurityException e) {
				Log.e("start SecurityException", "");
				e.printStackTrace();
			} catch (IllegalStateException e) {
				Log.e("start IllegalStateException", "");
				e.printStackTrace();
			} catch (IOException e) {
				Log.e("start IOException", "");
				e.printStackTrace();
			}
			mp.start();
		} else {
			Log.i("start", "tried to start without choosing data source.");
		}
	}

	/**
	 * @param path
	 */
	public void addTrack(String path) {
		pq.addTrack(path);
	}

	/**
	 * Moves the selected track (from) to the position (to) and adjusts indices.
	 * 
	 * @param from
	 * @param to
	 */
	public void moveTrack(int from, int to) {

	}

	/**
	 * @param time
	 *            ms
	 */
	public void seekForward(int time) {
		seekTo(mp.getCurrentPosition() + time);
	}

	/**
	 * @param time
	 *            ms
	 */
	public void seekBackward(int time) {
		seekTo(mp.getCurrentPosition() - time);
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
	 * Sets the player queue to the given list of paths of audio files.
	 * @param trackList a list of paths of audio files.
	 */
	public void setTrackList(List<String> trackList) {
		pq.setQueue(trackList);
	}

}
