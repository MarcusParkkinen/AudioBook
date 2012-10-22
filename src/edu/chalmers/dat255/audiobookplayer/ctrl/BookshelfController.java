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
import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;
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

	/**
	 * Only for testing purposes.
	 * 
	 * @return
	 */
	public int getSelectedBookIndex() {
		if (bookshelf != null) {
			return bookshelf.getSelectedBookIndex();
		}
		return Constants.Value.NO_BOOK_SELECTED;
	}

	/**
	 * Only for testing purposes.
	 * 
	 * @return
	 */
	public int getSelectedTrackIndex() {
		if (bookshelf != null) {
			return bookshelf.getSelectedTrackIndex();
		}
		return Constants.Value.NO_TRACK_SELECTED;
	}

	/*
	 * IBookshelfEvents
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#
	 * setSelectedTrack(int, int)
	 */
	public void setSelectedTrack(int bookIndex, int trackIndex) {
		if (bookshelf != null) {
			if (bookshelf.getSelectedBookIndex() != bookIndex) {
				bookshelf.setSelectedBookIndex(bookIndex);
			}
			bookshelf.setSelectedTrackIndex(bookIndex, trackIndex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#removeBook
	 * (int)
	 */
	public void removeBook(int bookIndex) {
		if (bookshelf != null) {
			try {
				bookshelf.removeBookAt(bookIndex);
			} catch (IndexOutOfBoundsException e) {
				// Expected when trying to remove last book.
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#
	 * setBookTitleAt(int, java.lang.String)
	 */
	public void setBookTitleAt(int bookIndex, String newTitle) {
		if (bookshelf != null) {
			bookshelf.setSelectedBookTitle(newTitle);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#removeTrack
	 * (int)
	 */
	public void removeTrack(int trackIndex) {
		if (bookshelf != null) {
			try {
				bookshelf.removeTrack(trackIndex);
			} catch (IndexOutOfBoundsException e) {
				// Expected when trying to remove last track in last book.
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#removeTrack
	 * (int, int)
	 */
	public void removeTrack(int bookIndex, int trackIndex) {
		if (bookshelf != null) {
			bookshelf.removeTrack(bookIndex, trackIndex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#moveTrack
	 * (int, int, int)
	 */
	public void moveTrack(int bookIndex, int trackIndex, int offset) {
		if (bookshelf != null) {
			try {
				bookshelf.moveTrack(bookIndex, trackIndex, offset);
			} catch (IndexOutOfBoundsException e) {
				// Expected when trying to move a track to an illegal position.
			}
		}
	}

	/*
	 * End IBookshelfEvents
	 */

	/**
	 * Adds a listener to the bookshelf (model).
	 * 
	 * @param pcl
	 *            The listener to add.
	 */
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		if (bookshelf != null) {
			bookshelf.addPropertyChangeListener(pcl);
		}
	}

	/**
	 * Removes all listeners from the model.
	 */
	public void removeListeners() {
		if (bookshelf != null) {
			bookshelf.removeListeners();
		}
	}

	/**
	 * Saves the model (a "Bookmark").
	 * 
	 * @param c
	 *            Context
	 * @param username
	 *            Name of file.
	 * @return True if saved successfully.
	 */
	public boolean saveBookshelf(Context c, String username) {
		return BookshelfHandler.saveBookshelf(c, username, bookshelf);
	}

}
