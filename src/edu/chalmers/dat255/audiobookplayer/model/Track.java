package edu.chalmers.dat255.audiobookplayer.model;

import java.security.InvalidParameterException;

import android.util.Log;

/**
 * Represents a single audio track. Includes its duration and path.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.4
 * 
 */
public class Track implements ITrackUpdates, Cloneable {
	private String path;
	private int elapsedTime;

	private int duration;

	/**
	 * Constructor for a track. Path to the data source as well as the length of
	 * the track must be provided.
	 * 
	 * @param path
	 *            Path to the track
	 * @param duration
	 *            Playing time of the track in ms.
	 */
	public Track(String path, int duration) {
		if (path != null) {
			this.path = path;
			this.duration = duration;
		} else {
			Log.e("Track", "Path to track is null in constructor");
			throw new InvalidParameterException();
		}
	}
	
	public Track clone() {
		Track copy = new Track(this.path, this.duration);
		copy.setElapsedTime(this.elapsedTime);
		return copy;
	}
	
	/**
	 * @return The path to the track.
	 */
	protected String getTrackPath() {
		return path;
	}

	/**
	 * @return The playing time of the track.
	 */
	protected int getDuration() {
		return duration;
	}
	
	public int getElapsedTime() {
		return elapsedTime;
	}
	
	public void addToElapsedTime(int time) {
		setElapsedTime(elapsedTime + time);
	}

	public void setElapsedTime(int elapsedTime) {
//		Log.d(TAG, "Track time changing from " + elapsedTime + " to " + time);
		this.elapsedTime = elapsedTime;
	}
	
}
