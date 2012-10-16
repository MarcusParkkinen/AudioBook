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

/**
 * The bookshelf class contains a collection of books.
 * 
 * @author Marcus Parkkinen, Aki K�kel�
 * @version 0.6
 * 
 */

public class Bookshelf implements IBookUpdates, Serializable {
	private static final String TAG = "Bookshelf";
	private static final String BOOK_INDEX_ILLEGAL = " Book index is illegal";

	private static final int NO_BOOK_SELECTED = Constants.Value.NO_BOOK_SELECTED;
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
		if (!isLegalBookIndex(index)) {
			throw new IllegalArgumentException(TAG + " setSelectedBookIndex"
					+ BOOK_INDEX_ILLEGAL);
		}

		selectedBookIndex = index;

		if (hasListeners()) {
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

		if (hasListeners()) {
			// Log.d(TAG, "list size (original): " + books.size());
//			pcs.firePropertyChange(Constants.Event.BOOK_ADDED, null,
			pcs.firePropertyChange(Constants.Event.BOOKS_CHANGED, null,
					new Bookshelf(this));
		}
	}

	/**
	 * Remove a book from the bookshelf at the given index.
	 * 
	 * @param int index
	 */
	public void removeBookAt(int index) {
		if (!isLegalBookIndex(index)) {
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
		if (hasListeners()) {
			// notify the listeners about this change
//			pcs.firePropertyChange(Constants.Event.BOOK_REMOVED, null,
			pcs.firePropertyChange(Constants.Event.BOOKS_CHANGED, null,
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
		if (!isLegalBookIndex(from) || !isLegalBookIndex(to)) {
			throw new IllegalArgumentException(TAG + " moveBook"
					+ BOOK_INDEX_ILLEGAL);
		}
		Book temp = books.remove(to);
		books.add(from, temp);

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.BOOK_MOVED, null,
					new Bookshelf(this));
		}
	}

	/* End Bookshelf methods */

	/* IBookUpdates */

	public void removeTrack(int index) throws IllegalArgumentException {
		if (!isLegalBookIndex(selectedBookIndex)) {
			throw new IllegalArgumentException();
		}

		this.books.get(selectedBookIndex).removeTrack(index);

		/*
		 * since we removed a track we need to recalculate the duration of the
		 * book
		 */
		updateBookDuration();

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.TRACK_REMOVED, null,
					new Bookshelf(this));
		}
	}

	public void addTrack(Track t) throws IllegalArgumentException {
		if (!isLegalBookIndex(selectedBookIndex)) {
			throw new IllegalArgumentException();
		}

		this.books.get(selectedBookIndex).addTrack(t);

		/*
		 * since we removed a track we need to recalculate the duration of the
		 * book
		 */
		updateBookDuration();

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.TRACK_ADDED, null,
					new Bookshelf(this));
		}
	}

	public void swapTracks(int firstIndex, int secondIndex)
			throws IllegalArgumentException {
		if (!isLegalBookIndex(selectedBookIndex)) {
			throw new IllegalArgumentException();
		}

		this.books.get(selectedBookIndex).swapTracks(firstIndex, secondIndex);

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.TRACK_ORDER_CHANGED, null,
					new Bookshelf(this));
		}
	}

	public void moveTrack(int from, int to) throws IllegalArgumentException {
		if (!isLegalBookIndex(selectedBookIndex)) {
			throw new IllegalArgumentException();
		}

		this.books.get(selectedBookIndex).moveTrack(from, to);

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.TRACK_ORDER_CHANGED, null,
					new Bookshelf(this));
		}
	}

	public void setSelectedTrackIndex(int index)
			throws IllegalArgumentException {
		if (!isLegalBookIndex(selectedBookIndex)) {
			throw new IllegalArgumentException();
		}

		this.books.get(selectedBookIndex).setSelectedTrackIndex(index);

		pcs.firePropertyChange(Constants.Event.TRACK_INDEX_CHANGED, null,
				new Bookshelf(this));

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

		if (hasListeners()) {
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

		if (hasListeners()) {
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

	public void setSelectedTrackElapsedTime(int elapsedTime)
			throws IllegalArgumentException {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IllegalArgumentException(TAG
					+ " setSelectedTrackElapsedTime " + NO_BOOK_SELECTED);
		}

		// set elapsed time in the currently playing book
		books.get(selectedBookIndex).setSelectedTrackElapsedTime(elapsedTime);

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.ELAPSED_TIME_CHANGED, null,
					new Bookshelf(this));
		}
	}

	public void addTag(int time) throws IllegalArgumentException {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IllegalArgumentException(TAG + " addTag "
					+ NO_BOOK_SELECTED);
		}

		this.books.get(selectedBookIndex).addTag(time);
		pcs.firePropertyChange(Constants.Event.TAG_ADDED, null, new Bookshelf(
				this));
	}

	public void removeTagAt(int tagIndex) throws IllegalArgumentException {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IllegalArgumentException(TAG + " removeTagAt "
					+ NO_BOOK_SELECTED);
		}

		this.books.get(selectedBookIndex).removeTagAt(tagIndex);
		pcs.firePropertyChange(Constants.Event.TAG_REMOVED, null,
				new Bookshelf(this));
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
	public Book getSelectedBook() throws IllegalArgumentException {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IllegalArgumentException(TAG + " . " + NO_BOOK_SELECTED);
		}

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
	public int getSelectedBookDuration() throws IllegalArgumentException {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IllegalArgumentException(TAG + " . " + NO_BOOK_SELECTED);
		}

		return books.get(selectedBookIndex).getDuration();
	}

	/**
	 * @return
	 */
	public int getSelectedTrackIndex() throws IllegalArgumentException {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IllegalArgumentException(TAG + " . " + NO_BOOK_SELECTED);
		}

		return books.get(selectedBookIndex).getSelectedTrackIndex();
	}

	/**
	 * ** currently unused **
	 * 
	 * @return
	 */
	public int getBookElapsedTime() throws IllegalArgumentException {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IllegalArgumentException(TAG + " . " + NO_BOOK_SELECTED);
		}

		return books.get(selectedBookIndex).getBookElapsedTime();
	}

	/**
	 * The number of tracks in the selected book.
	 * 
	 * @return
	 */
	public int getNumberOfTracks() throws IllegalArgumentException {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IllegalArgumentException(TAG + " . " + NO_BOOK_SELECTED);
		}

		return books.get(selectedBookIndex).getNumberOfTracks();
	}

	/*
	 * Accessors to Track.
	 */

	/**
	 * @return
	 */
	public int getSelectedTrackDuration() throws IllegalArgumentException {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IllegalArgumentException(TAG + " . " + NO_BOOK_SELECTED);
		}

		return books.get(selectedBookIndex).getSelectedTrackDuration();
	}

	/**
	 * @return
	 */
	public String getSelectedTrackPath() throws IllegalArgumentException {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IllegalArgumentException(TAG + " . " + NO_BOOK_SELECTED);
		}

		return books.get(selectedBookIndex).getSelectedTrackPath();
	}

	/**
	 * @param track
	 * @return
	 */
	public int getTrackDurationAt(int track) throws IllegalArgumentException {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IllegalArgumentException(TAG + " . " + NO_BOOK_SELECTED);
		}

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
	private boolean isLegalBookIndex(int index) {
		return index >= 0 && index < books.size();
	}

	private boolean hasListeners() {
		return pcs.getPropertyChangeListeners().length > 0;
	}

	public boolean isLegalTrackIndex(int index) throws IllegalArgumentException {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IllegalArgumentException(TAG + " . " + NO_BOOK_SELECTED);
		}

		return this.books.get(selectedBookIndex).isLegalTrackIndex(index);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		Log.d(TAG,
				"addPropertyChangeListener(listener), # of listeners before adding: "
						+ pcs.getPropertyChangeListeners().length);

		if (listener != null) {
			pcs.addPropertyChangeListener(listener);

			Log.d(TAG,
					"addPropertyChangeListener(listener), # of listeners after adding: "
							+ pcs.getPropertyChangeListeners().length);

			// Synchronize the new listener with the current state of the
			// bookshelf
			pcs.firePropertyChange(Constants.Event.BOOKSHELF_UPDATED, null,
					new Bookshelf(this));

		} else {
			Log.e(TAG,
					" trying to add null as property change listener. Skipping operation.");
		}
	}

	/**
	 * Removes all listeners from the pcs member.
	 */
	public void removeListeners() {
		Log.d(TAG,
				"removeListeners(), # of listeners before remove: "
						+ pcs.getPropertyChangeListeners().length);
		for (PropertyChangeListener pcl : pcs.getPropertyChangeListeners()) {
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

	public String getTrackTitleAt(int bookIndex, int trackIndex) {
		if(isLegalBookIndex(bookIndex)) {
			return getBookAt(bookIndex).getTrackTitleAt(trackIndex);
		}
		return null;
	}

	public int getNumberOfTracksAt(int bookIndex) {
		if(isLegalBookIndex(bookIndex)) {
			return getBookAt(bookIndex).getNumberOfTracks();
		}
		return 0;
	}

	public int getBookDurationAt(int bookIndex) {
		if(isLegalBookIndex(bookIndex)) {
			return getBookAt(bookIndex).getDuration();
		}
		return 0;
	}

	public int getBookElapsedTimeAt(int bookIndex) {
		if(isLegalBookIndex(bookIndex)) {
			return getBookAt(bookIndex).getBookElapsedTime();
		}
		return 0;
	}

	public String getBookAuthorAt(int bookIndex) {
		if(isLegalBookIndex(bookIndex)) {
			return getBookAt(bookIndex).getSelectedBookAuthor();
		}
		return null;
	}

	public int getTrackDurationAt(int bookIndex, int trackIndex) {
		if(isLegalBookIndex(bookIndex)) {
			return getBookAt(bookIndex).getTrackDurationAt(trackIndex);
		}
		return 0;
	}

}
