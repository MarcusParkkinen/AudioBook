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

package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.io.IOException;

import android.content.Context;

import edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.util.FileParser;
import edu.chalmers.dat255.audiobookplayer.util.JsonParser;

/**
 * Manages setting the current book and bookshelf, as well as saving it when the
 * application terminates.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.7
 */

public class BookshelfController implements IBookshelfEvents {
	private Bookshelf bookshelf;

	/**
	 * Bookshelf constructor.
	 * @param bs 
	 */
	public BookshelfController(Bookshelf bs) {
		bookshelf = bs;
	}

	/**
	 * Selects a given book in the bookshelf.
	 * 
	 * @param index
	 */
	public void setSelectedBook(int index) {
		if (bookshelf != null) {
			bookshelf.setSelectedBookIndex(index);
		}
	}
	public int getSelectedBookPosition() {
		return bookshelf.getSelectedBookIndex();
	}
	public Book getSelectedBook() {
		return bookshelf.getSelectedBook();
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

	public void selectBook(int bookIndex) {
		bookshelf.setSelectedBookIndex(bookIndex);
	}

	public void selectTrack(int bookIndex, int trackIndex) {
		bookshelf.setSelectedBookIndex(bookIndex);
		bookshelf.setSelectedTrackIndex(trackIndex);
	}

	public void removeBook(int groupPosition) {
		bookshelf.removeBook(groupPosition);
	}

	public void setBookTitleAt(int groupPosition, String newTitle) {
		bookshelf.setBookTitle(newTitle);
	}

	public void removeTrack(int trackIndex) {
		bookshelf.removeTrack(trackIndex);
	}
}