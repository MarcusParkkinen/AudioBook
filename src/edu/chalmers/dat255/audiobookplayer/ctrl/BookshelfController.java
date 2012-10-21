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
	private static final String TAG = "BookshelfController";
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
	 * @return
	 */
	public int getSelectedBookIndex() {
		return bookshelf.getSelectedBookIndex();
	}

	/**
	 * (Currently only used for testing.)
	 * 
	 * @return
	 */
	public int getSelectedTrackIndex() {
		return bookshelf.getSelectedTrackIndex();
	}

	// sort, swap, move, edit book

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
		bookshelf.setSelectedTrackIndex(bookIndex, trackIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#removeBook
	 * (int)
	 */
	public void removeBook(int bookIndex) {
		bookshelf.removeBookAt(bookIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#
	 * setBookTitleAt(int, java.lang.String)
	 */
	public void setBookTitleAt(int bookIndex, String newTitle) {
		bookshelf.setSelectedBookTitle(newTitle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#removeTrack
	 * (int)
	 */
	public void removeTrack(int trackIndex) {
		bookshelf.removeTrack(trackIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#removeTrack
	 * (int, int)
	 */
	public void removeTrack(int bookIndex, int trackIndex) {
		bookshelf.removeTrack(bookIndex, trackIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfEvents#moveTrack
	 * (int, int, int)
	 */
	public void moveTrack(int bookIndex, int trackIndex, int offset) {
		bookshelf.moveTrack(bookIndex, trackIndex, offset);
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
		Log.d(TAG, "Adding listener to bookshelf.");
		bookshelf.addPropertyChangeListener(pcl);
	}

	/**
	 * Removes all listeners from the model.
	 */
	public void removeListeners() {
		Log.d(TAG, "Removing ALL listeners of bookshelf.");
		bookshelf.removeListeners();
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
