package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.io.IOException;
import java.util.List;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.core.PlayerQueueControls;
import edu.chalmers.dat255.audiobookplayer.model.PlayerQueue;

/**
 * Wraps the android.media.MediaPlayer class.
 * 
 * @author Aki Käkelä
 * @version 0.2
 */
public class PlayerController implements PlayerQueueControls {
	private MediaPlayer mp;
//	private PlayerQueue pq;
	private Bookshelf bs;

	/**
	 * Creates a PlayerController instance and initializes the Media Player and
	 * Queue.
	 */
	public PlayerController(Bookshelf bs) {
		mp = new MediaPlayer();
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
	 * Sets the volume to the given values.
	 * @param leftVolume left channel balance
	 * @param rightVolume right channel balance
	 */
	public void setVolume(float leftVolume, float rightVolume) {
		mp.setVolume(leftVolume, rightVolume);
	}
	
	/**
	 * Sets the volum to the given value.
	 * @param volume
	 */
	public void setVolume(float volume) {
		setVolume(volume, volume);
	}

	/**
	 * Starts the audio player. The PlayerQueue must have been initialized and
	 * can not be empty.
	 */
	public void start() {
		// we have started playing a file, so start thread that updates time on
		// Track instance once every second, and
		if (bs.getCurrentTrackPath() != null) {
			try {
				mp.setDataSource(bs.getCurrentTrackPath());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				mp.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mp.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {
					Log.i("onComplete", "Track finished");
					mp.stop();
					mp.reset();
					pq.incrementTrackIndex();
					start();
					// does setDataSource, prepare, adds listener and starts MP
				}
			});
			mp.start();
			System.out.println("-- PLAYING: " + pq.getCurrentTrackIndex()
					+ ". " + pq.getCurrentTrackPath() + " @" + mp.getDuration()
					+ "ms");
		} else {
			Log.i("start", "tried to start without choosing data source.");
		}
	}

	/**
	 * Seeks to the right
	 * 
	 * @param time
	 *            ms
	 */
	public void seekRight(int time) {
		seekTo(mp.getCurrentPosition() + time);
	}

	/**
	 * Seeks to the left.
	 * 
	 * @param time
	 *            ms
	 */
	public void seekLeft(int time) {
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

	public void addTrack(String path) {
		pq.addTrack(path);
	}

	public void removeTrack(int index) {
		pq.removeTrack(index);
	}

	public void clearTracks() {
		pq.clearTracks();
	}
	
	public void setQueue(List<String> trackList) {
		pq.setQueue(trackList);
	}

	public void moveTrack(int from, int to) {
		bs.moveTrack(from, to);
	}

	public void moveTracks(int[] tracks, int to) {
		bs.moveTracks(tracks, to);
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

}
