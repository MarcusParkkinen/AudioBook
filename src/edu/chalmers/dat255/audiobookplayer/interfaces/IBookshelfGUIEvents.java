package edu.chalmers.dat255.audiobookplayer.interfaces;

/**
 * GUI methods required for Bookshelf.
 * 
 * @author Aki Käkelä
 * @version 0.1
 *
 */
public interface IBookshelfGUIEvents extends IBookshelfEvents {

	/**
	 * Informs the listener that a book has been long pressed.
	 * 
	 * @param index
	 *            The index of the book pressed.
	 */
	public void bookLongPress(int index);

	/**
	 * Informs the listener that the add button has been pressed.
	 */
	public void addBookButtonPressed();
}
