package edu.chalmers.dat255.audiobookplayer.ctrl;

import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;

/**
 * BookController class - used to edit and create all the Books stored in the users bookshelf.
 * Wraps a Bookshelf instance.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.4
 */

public class BookshelfController {
	private Bookshelf shelf;

	/**
	 * Bookshelf constructor.
	 * 
	 * @param Bookshelf a reference to the Bookshelf to control.
	 */
	public BookshelfController(Bookshelf shelf) {
		this.shelf = shelf;
	}
	/**
	 * Selects a given book in the bookshelf.
	 * @param index
	 */
	public void setSelectedBook(int index) {
		shelf.setSelectedBook(index);
	}
	
//	/**
//	 * Returns the index of the currently selected book.
//	 * 
//	 * @return index
//	 */
//	public int getSelectedBookIndex() {
//		return shelf.getSelectedBookIndex();
//	}

	// sort, swap, move, edit book

}