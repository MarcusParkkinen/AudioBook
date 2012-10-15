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

import java.beans.PropertyChangeListener;

import android.content.Context;

import edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.util.BookshelfHandler;

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
	 * 
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

	// sort, swap, move, edit book

	public void setSelectedTrack(int bookIndex, int trackIndex) {
		bookshelf.setSelectedBookIndex(bookIndex);
		bookshelf.setSelectedTrackIndex(trackIndex);
	}

	public void removeBook(int bookIndex) {
		bookshelf.removeBookAt(bookIndex);
	}

	public void setBookTitleAt(int bookIndex, String newTitle) {
		bookshelf.setSelectedBookTitle(newTitle);
	}

	public void removeTrack(int trackIndex) {
		bookshelf.removeTrack(trackIndex);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		bookshelf.addPropertyChangeListener(pcl);
	}
	
	public void removeListeners() {
		bookshelf.removeListeners();
	}
	
	public void saveBookshelf(Context c, String username) {
		// Remove listeners before saving the bookshelf
		bookshelf.removeListeners();
		
		BookshelfHandler.saveBookshelf(c, username, bookshelf);
	}
}
