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

package edu.chalmers.dat255.audiobookplayer.interfaces;

import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Used to assert that implementing classes can handle updates on Book
 * instances.
 * 
 * @author Aki Käkelä
 * @version 0.6
 * 
 */
public interface IBookUpdates extends ITrackUpdates {

	/**
	 * Add a track to the list.
	 * 
	 * @param t
	 *            Track to add.
	 */
	void addTrack(Track t);

	/**
	 * Removes a track from the collection on the specified index.
	 * 
	 * @param index
	 */
	void removeTrack(int index);

	/**
	 * Swaps the position of two tracks in the list.
	 * 
	 * @param firstIndex
	 *            First index to swap.
	 * @param secondIndex
	 *            Second index to swap.
	 */
	void swapTracks(int firstIndex, int secondIndex);

	/**
	 * Move a track from a given index to a given index. Indices inbetween will
	 * be adjusted.
	 * 
	 * @param from
	 *            Index to move from.
	 * @param to
	 *            Index to move to.
	 */
	void moveTrack(int from, int to);

	/**
	 * Sets the track index of the book. Can be set to "-1", which means
	 * 'deselected.'
	 * <p>
	 * Must be set to integers greater than or equal to -1.
	 * 
	 * @param index Index to select.
	 */
	void setSelectedTrackIndex(int index);

	/**
	 * Sets the title of the book.
	 * 
	 * @param newTitle The new title to set.
	 */
	void setSelectedBookTitle(String newTitle);

	/**
	 * Gets the title of the book.
	 * 
	 * @return The title of the book.
	 */
	String getSelectedBookTitle();

	/**
	 * Returns the author of the selected book.
	 * 
	 * @return
	 */
	String getSelectedBookAuthor();

	/**
	 * Updates the duration of the book to the sum of the duration of its
	 * tracks.
	 */
	void updateBookDuration();

}
