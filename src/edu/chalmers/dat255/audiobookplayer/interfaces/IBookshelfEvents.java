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
 *  Copyright � 2012 Marcus Parkkinen, Aki K�kel�, Fredrik �hs.
 **/

package edu.chalmers.dat255.audiobookplayer.interfaces;

/**
 * @author Aki K�kel�, Fredrik �hs
 * @version 0.7
 * 
 */
public interface IBookshelfEvents {
	/**
	 * Selects the given book.
	 * 
	 * @param bookIndex
	 *            The index of the book that now should be selected.
	 */
	void setSelectedBook(int bookIndex);

	/**
	 * Selects the given track in the given book.
	 * 
	 * @param bookIndex
	 *            Position of the book.
	 * @param trackIndex
	 *            Position of the track.
	 */
	void setSelectedTrack(int bookIndex, int trackIndex);

	/**
	 * Removes the book at the given index.
	 * 
	 * @param bookIndex
	 *            Position of the book.
	 */
	void removeBook(int bookIndex);

	/**
	 * Removes the track at the given index.
	 * 
	 * @param trackIndex
	 */
	void removeTrack(int trackIndex);

	/**
	 * Moves the track at given position the given offset from its current
	 * position
	 * 
	 * @param bookIndex
	 *            The index of the book.
	 * @param trackIndex
	 *            The index of the track.
	 * @param offset
	 *            The offset to move the track (negative values moves the track
	 *            up the list and positive down the list).
	 */
	void moveTrack(int bookIndex, int trackIndex, int offset);

	/**
	 * Removes the track at the given index.
	 * 
	 * @param bookIndex
	 *            The index of the book.
	 * @param trackIndex
	 *            The index of the track.
	 */
	void removeTrack(int bookIndex, int trackIndex);

	/**
	 * Informs the listener that the book at given position should change name
	 * to the given value.
	 * 
	 * @param bookIndex
	 *            Position of the book.
	 * @param newTitle
	 *            The new title of the book.
	 */
	void setBookTitleAt(int bookIndex, String newTitle);

}
