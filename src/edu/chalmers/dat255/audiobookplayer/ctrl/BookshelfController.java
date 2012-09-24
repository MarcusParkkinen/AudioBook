package edu.chalmers.dat255.audiobookplayer.ctrl;

import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;

/**
 * @author Aki Käkelä
 * @version 0.3
 * 
 */
public class BookshelfController {
	private Bookshelf shelf;

	/**
	 * Selects a given book in the bookshelf.
	 * @param index
	 */
	public void setSelectedBook(int index) {
		shelf.setSelectedBook(index);
	}

	public int getSelectedBookIndex() {
		return shelf.getSelectedBookIndex();
	}
	
	// sort, swap, move, edit book
	
}
