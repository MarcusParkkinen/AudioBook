package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.util.List;
import android.media.MediaPlayer;

public class PlayerController {
	private MediaPlayer mp;
	
	public PlayerController() {
		mp = new MediaPlayer();
	}
	
	/**
	 * Moves the selected track (from) to the position (to) and adjusts indices.
	 * @param from
	 * @param to
	 */
	public void moveTrack(int from, int to) {
	}
	
	public void pause() {
		
	}
	
	public void play() {
		
	}
	
	public void seekForward(int time) {
		seek(mp.getCurrentPosition()+time);
	}
	
	public void seekBackward(int time) {
		seek(mp.getCurrentPosition()-time);
	}
	
	public void seek(int time) {
		mp.seekTo(time);
	}
	
	public void setTrackList(List<String> trackList) {
		
	}
	
}
