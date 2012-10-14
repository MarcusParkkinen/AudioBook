/**
 *  This work is licensed under the Creative Commons Attribution-NonCommercial-
 *  NoDerivs 3.0 Unported License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to 
 *  Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 
 *  94041, USA.
 * 
 *  Use of this work is permitted only in accordance with license rights granted.
 *  Materials provided "AS IS"; no representations or warranties provided.
 * 
 *  Copyright © 2012 Marcus Parkkinen, Aki Käkelä, Fredrik Åhs.
 **/

package edu.chalmers.dat255.audiobookplayer.model;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.List;

import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.interfaces.ITrackUpdates;

/**
 * Represents a single audio track. Includes its duration, current elapsed time
 * and file path.
 * <p>
 * Duration and track path are immutable.
 * 
 * @author Marcus Parkkinen Aki K�kel�
 * @version 0.6
 * 
 */
public final class Track implements ITrackUpdates, Serializable {
	private static final String TAG = "Bookshelf.java";
	private static final String TAG_INDEX_ILLEGAL = " Tag index is illegal";
	private static final long serialVersionUID = 3;

	private final String path;
	private final int duration;
	private int elapsedTime;
	private List<Tag> tags;

	private String title;

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

	/**
	 * Constructor for a track. Path to the data source as well as the title of
	 * the book and the length of the track must be provided. The path may not
	 * be an empty string ("").
	 * 
	 * @param path
	 * @param title
	 * @param duration
	 * @throws InvalidParameterException
	 */
	public Track(String path, String title, int duration)
			throws InvalidParameterException {
		this(path, duration);
		this.setTitle(title);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 */
	public Track(Track original) {
		this(original.path, original.title, original.duration);
		this.elapsedTime = original.elapsedTime;
		this.title = original.title;
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

	public void setSelectedTrackElapsedTime(int elapsedTime)
			throws InvalidParameterException {
		if (elapsedTime >= duration) {
			Log.e(TAG, "elapsedTime (" + elapsedTime + ") set to duration ("
					+ duration + ")");
			this.elapsedTime = duration;
		} else if (elapsedTime >= 0) {
			this.elapsedTime = elapsedTime;
		} else {
			throw new InvalidParameterException(
					"Attempting to set elapsed time to a negative value.");
		}
	}

	/**
	 * Returns the title of the track.
	 * 
	 * @return The title of the track (the path without the parent folders).
	 */
	public String getTrackTitle() {
		return this.title; // TODO: just get the track name, not path
	}

	/**
	 * Sets the title of the track.
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Track other = (Track) obj;
		if (duration != other.duration) {
			return false;
		}
		if (elapsedTime != other.elapsedTime) {
			return false;
		}
		if (path == null) {
			if (other.path != null) {
				return false;
			}
		} else if (!path.equals(other.path)) {
			return false;
		}
		return true;
	}
	
	public void addTag(int time) throws IllegalArgumentException {
		this.tags.add(new Tag(time));
	}

	public void removeTagAt(int tagIndex) throws IllegalArgumentException {
		if (!isLegalTagIndex(tagIndex)) {
			throw new IllegalArgumentException(TAG + " removeTagAt"
					+ TAG_INDEX_ILLEGAL);
		}

		this.tags.remove(tagIndex);
	}

	private boolean isLegalTagIndex(int tagIndex) {
		return tagIndex >= 0 && tagIndex < tags.size();
	}

}
