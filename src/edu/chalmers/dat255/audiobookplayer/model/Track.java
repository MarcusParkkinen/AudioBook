package edu.chalmers.dat255.audiobookplayer.model;

import java.security.InvalidParameterException;

import android.util.Log;

/**
 * Represents a single audio track. Includes its duration and path.
 * 
 * @author Marcus Parkkinen, Aki K�kel�
 * @version 0.5
 * 
 */
public final class Track implements ITrackUpdates {
	private static final String TAG = "Bookshelf.java";
	
	private final String path;
	private final int duration;
	private int elapsedTime;

	/**
	 * Constructor for a track. Path to the data source as well as the length of
	 * the track must be provided. The path may not be an empty string ("").
	 * 
	 * @param path
	 *            Path to the track
	 * @param duration
	 *            Playing time of the track in ms.
	 */
	public Track(String path, int duration) throws InvalidParameterException {
		if (path != null && path.length() > 0 && duration > 0) {
			this.path = path;
			this.duration = duration;
		} else {
			throw new InvalidParameterException(
					"Attempting to create track with either null path or negative duration.");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + duration;
		result = prime * result + elapsedTime;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Track other = (Track) obj;
		if (duration != other.duration)
			return false;
		if (elapsedTime != other.elapsedTime)
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 */
	public Track(Track original) {
		this(original.path, original.duration);
		this.elapsedTime = original.elapsedTime;
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
	
	/**
	 * @return The elapsed time of the track.
	 */
	public int getElapsedTime() {
		return elapsedTime;
	}
	
	
	/**
	 * Increment the elapsed time of the track by a specified
	 * amount.
	 * 
	 * @param time
	 */
	public void addToElapsedTime(int time) throws InvalidParameterException {
		if(time > 0) {
			if(elapsedTime + time <= duration) {
				setElapsedTime(elapsedTime + time);
			} else{
				elapsedTime = 0;
			}
		} else{
			throw new InvalidParameterException(
				"Attempting to change track time with illegal value: " + time);
		}
		
	}
	
	/**
	 * Set the elapsed time of the track to a specified amount.
	 * 
	 * @param new time
	 */
	public void setElapsedTime(int elapsedTime) throws InvalidParameterException {
		if( elapsedTime >= 0 && elapsedTime <= duration) {
			this.elapsedTime = elapsedTime;
		} else {
			throw new InvalidParameterException("Attempting to set elapsed time to illegal value.");
		}
	}
}
