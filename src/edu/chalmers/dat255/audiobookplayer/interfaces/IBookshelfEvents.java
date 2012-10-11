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

import android.view.View;

/**
 * @author Aki K�kel�, Fredrik �hs
 * @version 0.7
 * 
 */
public interface IBookshelfEvents {
	/**
	 * Informs the listener that the currently selected book should change.
	 * @param groupPosition The index of the book that now should be selected.
	 */
	public void bookSelected(int groupPosition);

	/**
	 * Informs the listener that a book has been long pressed.
	 * @param index The index of the book pressed.
	 */
	public void bookLongPress(int index);

	/**
	 * Informs the listener that the add button has been pressed.
	 */
	public void addButtonPressed();
	
	/**
	 * Informs the listener that the preferences button has been pressed.
	 */
	public void preferencesButtonPressed();

	/**
	 * Informs the listener that the currently selected child should change.
	 * @param groupPosition Position of the book.
	 * @param childPosition Position of the track.
	 */
	public void childSelected(int groupPosition, int childPosition);

	/**
	 * Informs the listener that the book at the given position.
	 * @param groupPosition Position of the book.
	 */
	public void deleteBook(int groupPosition);

	/**
	 * Informs the listener that the book at given position should change name to the given value.
	 * @param groupPosition Position of the book.
	 * @param newTitle The new title of the book.
	 */
	public void editBook(int groupPosition, String newTitle);

	// public void refillBookshelf(Bookshelf );
}
