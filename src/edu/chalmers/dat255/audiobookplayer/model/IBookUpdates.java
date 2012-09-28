package edu.chalmers.dat255.audiobookplayer.model;

public interface IBookUpdates {
	/**
	 * Removes a track from the collection on the specified index.
	 * 
	 * @param index
	 */
	public void removeTrack(int index);

	/**
	 * Adds a track to the collection to the specified index.
	 * 
	 * @param index
	 *            Where to add the track.
	 * @param t
	 *            The Track instance to add.
	 */
	public void addTrack(int index, Track t);

	/**
	 * Swap location of two tracks.
	 * 
	 * @param firstIndex
	 * @param secondIndex
	 */
	public void swap(int firstIndex, int secondIndex);

	/**
	 * Move the track at the specified index to a new index.
	 * 
	 * @param oldIndex
	 *            index of the track
	 * @param newIndex
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
//	public void setBookmark(int trackIndex, int time);
	
//	public void setTag();

	/**
	 * Sets the track index of the book. Rolls over if the index is out of
	 * bounds.
	 * 
	 * @param index
	 */
	public void setCurrentTrackIndex(int index);
	
	/**
	 * Changes the title of the book.
	 * 
	 * @param newTitle
	 */
	public void setBookTitle(String newTitle);

}
