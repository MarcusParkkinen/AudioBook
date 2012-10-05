package edu.chalmers.dat255.audiobookplayer.interfaces;

import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Used to assert that implementing classes can handle updates on Book
 * instances.
 * 
 * @author Aki K�kel�
 * @version 0.6
 * 
 */
public interface IBookUpdates {

	/**
	 * Add a track to the list.
	 * 
	 * @param t
	 *            Track to add.
	 */
	public void addTrack(Track t);

	/**
	 * Removes a track from the collection on the specified index.
	 * 
	 * @param index
	 */
	public void removeTrack(int index);

	/**
	 * Swaps the position of two tracks in the list.
	 * 
	 * @param firstIndex
	 * @param secondIndex
	 */
	public void swapTracks(int firstIndex, int secondIndex);

	/**
	 * Move a track from a given index to a given index. Indices inbetween will
	 * be adjusted.
	 * 
	 * @param from
	 * @param to
	 */
	public void moveTrack(int from, int to);

	/**
	 * Set the bookmark to point at a track and a specific time in that track.
	 * 
	 * @param trackIndex
	 *            Index of the track in the book
	 * @param time
	 *            The time at which to add the bookmark (in ms).
	 */

	// public void setBookmark(int trackIndex, int time);

	/**
	 * Sets the track index of the book. Rolls over if the index is out of
	 * bounds.
	 * 
	 * @param index
	 */
	public void setSelectedTrackIndex(int index);

	/**
	 * Set the title of the book.
	 * 
	 * @param newTitle
	 */
	public void setBookTitle(String newTitle);

	/**
	 * Updates the duration of the book to the sum of the duration of its
	 * tracks.
	 */
	public void updateBookDuration();

}