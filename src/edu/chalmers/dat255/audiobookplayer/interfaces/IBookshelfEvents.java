package edu.chalmers.dat255.audiobookplayer.interfaces;

import android.view.View;

/**
 * @author Aki Käkelä
 * 
 */
public interface IBookshelfEvents {
	/**
	 * Called when a book has been selected in the Bookshelf UI.
	 * 
	 * @param groupPosition
	 *            The index of the book selected.
	 */
	public void bookSelected(int groupPosition);

	/**
	 * Called when a book has been long pressed in the Bookshelf UI.
	 * 
	 * @param index
	 *            The index of the book pressed.
	 */
	public void bookLongPress(int index);

	/**
	 * Called when a book has been added in the Bookshelf UI.
	 * 
	 * @param v
	 */
	public void addButtonPressed(View v);

	// public void refillBookshelf(Bookshelf );
}
