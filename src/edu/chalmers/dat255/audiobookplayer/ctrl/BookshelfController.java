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

import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.util.FileParser;
import edu.chalmers.dat255.audiobookplayer.util.JsonParser;

/**
 * Manages setting the current book and bookshelf, as well as saving it when the
 * application terminates.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.6
 */

public class BookshelfController {
	private Bookshelf shelf;

	/**
	 * Bookshelf constructor.
	 */
	public BookshelfController() {
		/* ... */
	}

	/**
	 * Selects a given book in the bookshelf.
	 * 
	 * @param index
	 */
	public void setSelectedBook(int index) {
		if (shelf != null) {
			shelf.setSelectedBookIndex(index);
		}
	}
	public int getSelectedBookPosition() {
		return shelf.getSelectedBookIndex();
	}
	public Book getSelectedBook() {
		return shelf.getSelectedBook();
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

	/**
	 * Save a JSON representation of the model object tree to file.
	 * 
	 * @param Context
	 *            context
	 * @param String
	 *            username
	 */
	public boolean saveBookshelf(Context c, String username) {
		try {
			FileParser.writeToInternalStorage(username + ".bookmark", c,
					JsonParser.toJSON(shelf));
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Attempt to load the model object tree from a file. This method
	 * instantiates the member variable with either a new Bookshelf or a
	 * existing one from a file.
	 * 
	 * A reference to the bookshelf is also returned to the calling method.
	 * 
	 * @param Context
	 *            context
	 * @param String
	 *            username
	 */
	public Bookshelf loadBookshelf(Context c, String username) {
		try {
			Object obj = JsonParser.fromJSON(FileParser
					.readFromInternalStorage(username + ".bookmark", c),
					Bookshelf.class);
			if (obj instanceof Bookshelf) {
				return (shelf = (Bookshelf) obj);
			}
		} catch (Exception e) {
			// If anything goes wrong, just continue..
		}
		return (shelf = new Bookshelf());
	}
	// + sort, swap, move, edit book
}