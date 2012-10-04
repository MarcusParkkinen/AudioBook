package edu.chalmers.dat255.audiobookplayer.interfaces;

/**
 * Used to assert that implementing classes can handle updates on Track
 * instances.
 * 
 * @author Aki Käkelä
 * @version 0.6
 * 
 */
public interface ITrackUpdates {
	/**
	 * Sets the elapsed time of the track.
	 * 
	 * @param elapsedTime
	 */
	public void setSelectedTrackElapsedTime(int elapsedTime);

	/*
	 * Tags
	 */
	/**
	 * Adds a tag with the given time.
	 * 
	 * @param time
	 */
	public void addTag(int time);

	/**
	 * Removes the last track in the list. Does nothing if the list is empty.
	 */
	public void removeTag();

	/**
	 * @param tagIndex
	 */
	public void removeTagAt(int tagIndex);

}
