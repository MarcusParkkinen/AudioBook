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
