package edu.chalmers.dat255.audiobookplayer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.security.InvalidParameterException;

import edu.chalmers.dat255.audiobookplayer.constants.Constants;

import android.util.Log;

/**
 * Represents a single audio track. Includes its duration and path.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.4
 * 
 */

public class Track {
	private static final String TAG = "Track.class";
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
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

	/**
	 * @param time
	 *            ms
	 */
	public void setElapsedTime(int elapsedTime) {
//		Log.d(TAG, "Track time changing from " + elapsedTime + " to " + time);
		this.elapsedTime = elapsedTime;
		pcs.firePropertyChange(Constants.event.TRACK_TIME_CHANGED, elapsedTime, this.elapsedTime);
	}
	
	public void addToElapsedTime(int time) {
		setElapsedTime(elapsedTime + time);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

}
