package edu.chalmers.dat255.audiobookplayer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.security.InvalidParameterException;

import edu.chalmers.dat255.audiobookplayer.constants.StringConstants;

import android.util.Log;

/**
 * Represents a single audio track. Includes its duration and path.
 * 
 * @author Aki Käkelä, Marcus Parkkinen
 * @version 0.3
 * 
 */
public class Track {
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
	protected int getTrackDuration() {
		return duration;
	}

	/**
	 * @param time
	 */
	public void setTime(int time) {
		this.elapsedTime = time;
		pcs.firePropertyChange(StringConstants.event.TRACK_TIME_CHANGED, null, null);
	}
	
	public int getTime() {
		return this.elapsedTime;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
}
