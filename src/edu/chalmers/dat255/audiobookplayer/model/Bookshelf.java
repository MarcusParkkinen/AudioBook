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

package edu.chalmers.dat255.audiobookplayer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;
import edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates;
import edu.chalmers.dat255.audiobookplayer.interfaces.ITrackUpdates;

/**
 * The bookshelf class contains a collection of books.
 * 
 * @author Marcus Parkkinen, Aki K�kel�
 * @version 0.6
 * 
 */

public class Bookshelf implements IBookUpdates, ITrackUpdates, Serializable {
	private static final String TAG = "Bookshelf.java";
	private static final String BOOK_INDEX_ILLEGAL = " Book index is illegal";
	
	private static final int NO_BOOK_SELECTED = -1;
	private static final long serialVersionUID = 1;

	private List<Book> books;
	private int selectedBookIndex;
	private transient PropertyChangeSupport pcs;
	
	/**
	 * Creates an empty bookshelf.
	 */
	public Bookshelf() {
		books = new LinkedList<Book>();
		selectedBookIndex = NO_BOOK_SELECTED;
		
		pcs = new PropertyChangeSupport(this);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param original
	 */
	public Bookshelf(Bookshelf original) {
		this();

		// copy instance variables
		this.selectedBookIndex = original.selectedBookIndex;

		// make deep copies of Book in the list

		for (Book b : original.books) {
			this.books.add(new Book(b));
		}
	}

	/* Bookshelf methods */
	/**
	 * The book that the player will use (read from) is set here.
	 * 
	 * @param index
	 * @throws IllegalArgumentException
	 */
	public void setSelectedBookIndex(int index) throws IllegalArgumentException {
		if (!isLegalIndex(index)) {
			throw new IllegalArgumentException(TAG + " setSelectedBookIndex"
					+ BOOK_INDEX_ILLEGAL);
		}

		selectedBookIndex = index;
		
		if(hasListeners()) {
			// notify the view module that we have selected a book
			pcs.firePropertyChange(Constants.Event.BOOK_SELECTED, null,
					new Bookshelf(this));
		}
	}

	/**
	 * Add a new book to the bookshelf.
	 * 
	 * @param Book
	 *            the new book to add
	 */
	public void addBook(Book b) {
		books.add(b);
		// select it if it is the first, otherwise move the selection ahead so
		// that it is pointing at the correct book
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			selectedBookIndex = 0;
		}
		
		if(hasListeners()) {
			// Log.d(TAG, "list size (original): " + books.size());
			pcs.firePropertyChange(Constants.Event.BOOK_ADDED, null, new Bookshelf(
					this));
		}
	}

	/**
	 * Remove a book from the bookshelf at the given index.
	 * 
	 * @param int index
	 */
	public void removeBookAt(int index) {
		if (!isLegalIndex(index)) {
			throw new IllegalArgumentException(TAG + " removeBook"
					+ BOOK_INDEX_ILLEGAL);
		}

		books.remove(index);

		// check whether this was the last book
		if (books.size() == 0) {
			selectedBookIndex = NO_BOOK_SELECTED;
		} else {
			if (index < selectedBookIndex) {
				// adjust the index if we removed one earlier in the list
				selectedBookIndex--;
			} else if (index == selectedBookIndex) {
				// if we removed the selected one then mark the first
				selectedBookIndex = 0;
			}
		}
		if(hasListeners()) {
			// notify the listeners about this change
			pcs.firePropertyChange(Constants.Event.BOOK_REMOVED, null,
					new Bookshelf(this));
		}
	}

	/**
	 * Move a book from a given index to a given index. Indices inbetween will
	 * be adjusted.
	 * 
	 * @param from
	 * @param to
	 */
	public void moveBook(int from, int to) throws IllegalArgumentException {
		if (!isLegalIndex(from) || !isLegalIndex(to)) {
			throw new IllegalArgumentException(TAG + " moveBook"
					+ BOOK_INDEX_ILLEGAL);
		}
		Book temp = books.remove(to);
		books.add(from, temp);
		
		if(hasListeners()) {
			pcs.firePropertyChange(Constants.Event.BOOK_MOVED, null, new Bookshelf(
					this));
		}
	}

	/* End Bookshelf methods */

	/* IBookUpdates */

	public void removeTrack(int index) throws IllegalArgumentException {
		if (!isLegalIndex(index)) {
			throw new IllegalArgumentException(TAG + " removeTrack"
					+ BOOK_INDEX_ILLEGAL);
		}

		this.books.get(selectedBookIndex).removeTrack(index);

		/*
		 * since we removed a track we need to recalculate the duration of the
		 * book
		 */
		updateBookDuration();
		
		if(hasListeners()) {
			pcs.firePropertyChange(Constants.Event.TRACK_REMOVED, null,
					new Bookshelf(this));
		}
	}

	public void addTrack(Track t) {
		this.books.get(selectedBookIndex).addTrack(t);

		/*
		 * since we removed a track we need to recalculate the duration of the
		 * book
		 */
		updateBookDuration();
		
		if(hasListeners()) {
			pcs.firePropertyChange(Constants.Event.TRACK_ADDED, null,
					new Bookshelf(this));
		}
	}

	public void swapTracks(int firstIndex, int secondIndex)
			throws IllegalArgumentException {
		this.books.get(selectedBookIndex).swapTracks(firstIndex, secondIndex);
		
		if(hasListeners()) {
			pcs.firePropertyChange(Constants.Event.TRACK_ORDER_CHANGED, null,
					new Bookshelf(this));
		}
	}

	public void moveTrack(int from, int to) throws IllegalArgumentException {
		this.books.get(selectedBookIndex).moveTrack(from, to);
		
		if(hasListeners()) {
			pcs.firePropertyChange(Constants.Event.TRACK_ORDER_CHANGED, null,
					new Bookshelf(this));
		}
	}

	public void setSelectedTrackIndex(int index)
			throws IllegalArgumentException {
		setSelectedTrackIndex(index, true);
	}

	/**
	 * Sets the selected track index to the given index. Won't fire a property
	 * change event if update is false.
	 * 
	 * @param index
	 * @param update
	 *            Fires an update event if true.
	 * @throws IllegalArgumentException
	 */
	public void setSelectedTrackIndex(int index, boolean update)
			throws IllegalArgumentException {
		this.books.get(selectedBookIndex).setSelectedTrackIndex(index);

		/*
		 * only send an update if the new index is legal (i.e. something changed
		 * in the model so therefore the GUI should update).
		 */
		boolean legal = this.books.get(selectedBookIndex).isLegalTrackIndex(
				index);

		if (legal && update) {
			pcs.firePropertyChange(Constants.Event.TRACK_INDEX_CHANGED, null,
					new Bookshelf(this));
		} else if (update) {
			pcs.firePropertyChange(Constants.Event.BOOK_FINISHED, null,
					new Bookshelf(this));
			// No update sent since book index is illegal
		}

		Log.d(TAG, "Book index : " + selectedBookIndex + ", Track index: "
				+ this.books.get(selectedBookIndex).getSelectedTrackIndex());
	}

	public void setSelectedBookTitle(String newTitle)
			throws IllegalArgumentException {
		setBookTitleAt(selectedBookIndex, newTitle);
	}

	public String getSelectedBookTitle() throws IllegalArgumentException {
		return getBookTitleAt(this.selectedBookIndex);
	}

	public String getSelectedBookAuthor() {
		return this.books.get(selectedBookIndex).getSelectedBookAuthor();
	}

	public void updateBookDuration() {
		this.books.get(selectedBookIndex).updateBookDuration();
		
		if(hasListeners()) {
			pcs.firePropertyChange(Constants.Event.BOOK_DURATION_CHANGED, null,
					new Bookshelf(this));
		}
	}

	/* End IBookUpdates */

	/**
	 * Set the title of the book at the given index.
	 * 
	 * @param bookIndex
	 * @param newTitle
	 * @throws IllegalArgumentException
	 */
	public void setBookTitleAt(int bookIndex, String newTitle)
			throws IllegalArgumentException {
		if (bookIndex == NO_BOOK_SELECTED) {
			throw new IllegalArgumentException(TAG + " setBookTitleAt"
					+ BOOK_INDEX_ILLEGAL);
		}

		this.books.get(bookIndex).setSelectedBookTitle(newTitle);
		
		if(hasListeners()) {
			pcs.firePropertyChange(Constants.Event.BOOK_TITLE_CHANGED, null,
					new Bookshelf(this));
		}
	}

	public String getBookTitleAt(int bookIndex) throws IllegalArgumentException {
		if (bookIndex == NO_BOOK_SELECTED) {
			throw new IllegalArgumentException(TAG + " getBookTitleAt"
					+ BOOK_INDEX_ILLEGAL);
		}

		return this.books.get(bookIndex).getSelectedBookTitle();
	}

	/* ITrackUpdates */

	public void setSelectedTrackElapsedTime(int elapsedTime) {
		// set elapsed time in the currently playing book
		books.get(selectedBookIndex).setSelectedTrackElapsedTime(elapsedTime);
		
		if(hasListeners()) {
			pcs.firePropertyChange(Constants.Event.ELAPSED_TIME_CHANGED, null,
					new Bookshelf(this));
		}
	}

	public void addTag(int time) throws IllegalArgumentException {
		this.books.get(selectedBookIndex).addTag(time);
	}

	public void removeTagAt(int tagIndex) throws IllegalArgumentException {
		this.books.get(selectedBookIndex).removeTagAt(tagIndex);
	}

	/* End ITrackUpdates */

	/*
	 * Accessors to Bookshelf.
	 */
	/**
	 * @return
	 */
	public int getNumberOfBooks() {
		return this.books.size();
	}

	/**
	 * 
	 * @return
	 */
	public int getSelectedBookIndex() {
		return this.selectedBookIndex;
	}

	/**
	 * @return
	 */
	public Book getSelectedBook() {
		return this.books.get(selectedBookIndex);
	}

	/**
	 * @param index
	 * @return
	 */
	public Book getBookAt(int index) {
		return this.books.get(index);
	}

	/*
	 * Accessors to Book.
	 */

	/**
	 * @return
	 */
	public int getSelectedBookDuration() {
		return books.get(selectedBookIndex).getDuration();
	}

	/**
	 * @return
	 */
	public int getSelectedTrackIndex() {
		return books.get(selectedBookIndex).getSelectedTrackIndex();
	}

	/**
	 * ** currently unused **
	 * 
	 * @return
	 */
	public int getBookElapsedTime() {
		return books.get(selectedBookIndex).getBookElapsedTime();
	}

	/**
	 * The number of tracks in the selected book.
	 * 
	 * @return
	 */
	public int getNumberOfTracks() {
		return books.get(selectedBookIndex).getNumberOfTracks();
	}

	/*
	 * Accessors to Track.
	 */

	/**
	 * @return
	 */
	public int getSelectedTrackDuration() {
		return books.get(selectedBookIndex).getSelectedTrackDuration();
	}

	/**
	 * @return
	 */
	public String getSelectedTrackPath() throws IllegalArgumentException {
		return books.get(selectedBookIndex).getSelectedTrackPath();
	}

	/**
	 * @param track
	 * @return
	 */
	public int getTrackDurationAt(int track) {
		return books.get(selectedBookIndex).getTrackDurationAt(track);
	}

	/**
	 * Checks whether a given index is within the legal bounds of the list of
	 * books.
	 * 
	 * @param index
	 *            Index to check.
	 * @return True if within bounds.
	 */
	private boolean isLegalIndex(int index) {
		return index >= 0 && index < books.size();
	}
	
	private boolean hasListeners() {
		return pcs.getPropertyChangeListeners().length > 0;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if(listener != null) {
			pcs.addPropertyChangeListener(listener);
			
			// Synchronize the new listener with the current state of the
			// bookshelf
			pcs.firePropertyChange(Constants.Event.BOOKSHELF_UPDATED, null,
					new Bookshelf(this));
			
		} else{
			Log.e(TAG, " trying to add null as property change listener. Skipping operation.");
		}
	}
	
	/**
	 * Removes all listeners from the pcs member.
	 */
	public void removeListeners() {
		for(PropertyChangeListener pcl : pcs.getPropertyChangeListeners()) {
			pcs.removePropertyChangeListener(pcl);
		}
	}

	// NOTE: Autogenerated method
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((books == null) ? 0 : books.hashCode());
		result = prime * result + selectedBookIndex;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Bookshelf other = (Bookshelf) obj;
		if (books == null) {
			if (other.books != null) {
				return false;
			}
		} else if (!books.equals(other.books)) {
			return false;
		}
		if (selectedBookIndex != other.selectedBookIndex) {
			return false;
		}
		return true;
	}

}
