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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;
import edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates;

/**
 * The bookshelf class contains a collection of books.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
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
	 * The book that the player will use (read from) is set here. Can be
	 * deselecting by calling with Constants.Value.NO_BOOK_SELECTED.
	 * 
	 * @param index
	 *            The index to select. -1 <= x <= last list index.
	 */
	public void setSelectedBookIndex(int index) {
		if (isValidBookIndex(index)) {
			selectedBookIndex = index;

			if (hasListeners()) {
				// notify the view module that we have selected a book
				pcs.firePropertyChange(Constants.Event.BOOK_SELECTED, null,
						new Bookshelf(this));
			}
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
			setSelectedBookIndex(0);
		}

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.BOOK_LIST_CHANGED, null,
					new Bookshelf(this));
		}
	}

	/**
	 * Remove a book from the bookshelf at the given index.
	 * 
	 * @param int index
	 */
	public void removeBookAt(int index) {
		checkBookIndexLegal(index);

		books.remove(index);

		// check whether this was the last book
		if (books.size() == 0) {
			// deselect
			setSelectedBookIndex(NO_BOOK_SELECTED);
		} else {
			if (index < selectedBookIndex) {
				// adjust the index if we removed one earlier in the list
				setSelectedBookIndex(selectedBookIndex - 1);
			} else if (index == selectedBookIndex) {
				// if we removed the selected one then select the first
				setSelectedBookIndex(0);
			}
		}

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.BOOK_LIST_CHANGED, null,
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
	public void moveBook(int from, int to) {
		checkBookIndexLegal(from);
		checkBookIndexLegal(to);

		Collections.rotate(books.subList(from, to + 1), -1);

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.BOOK_LIST_CHANGED, null,
					new Bookshelf(this));
		}
	}

	/**
	 * Sets both the track index and the book index.
	 * <p>
	 * Both integers must be valid, and only the track index may be deselected
	 * (set to "-1").
	 * 
	 * @param bookIndex
	 *            The index of the book to select. Can not be -1.
	 * @param trackIndex
	 *            The index of the track to select. Can be -1.
	 */
	public void setSelectedTrackIndex(int bookIndex, int trackIndex) {
		// make no changes if book index is illegal or track index is invalid

		if (isLegalBookIndex(bookIndex)) {
			// the book index must be legal
			if (isValidTrackIndex(bookIndex, trackIndex)) {
				// the track index can be unselected or legal only

				// the track index is either valid or deselects
				// we now know that both indices are valid, so make the changes.
				setSelectedBookIndex(bookIndex);
				setSelectedTrackIndex(trackIndex);
			}
		}

	}

	/* End Bookshelf methods */

	/* IBookUpdates */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#removeTrack
	 * (int)
	 */
	public void removeTrack(int index) {
		checkBookIndexLegal(selectedBookIndex);

		this.books.get(selectedBookIndex).removeTrack(index);

		/*
		 * since we removed a track we need to recalculate the duration of the
		 * book
		 */
		updateBookDuration();

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.TRACK_LIST_CHANGED, null,
					new Bookshelf(this));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#addTrack(
	 * edu.chalmers.dat255.audiobookplayer.model.Track)
	 */
	public void addTrack(Track t) {
		checkBookIndexLegal(selectedBookIndex);

		this.books.get(selectedBookIndex).addTrack(t);

		/*
		 * since we removed a track we need to recalculate the duration of the
		 * book
		 */
		updateBookDuration();

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.TRACK_LIST_CHANGED, null,
					new Bookshelf(this));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#swapTracks
	 * (int, int)
	 */
	public void swapTracks(int firstIndex, int secondIndex) {
		checkBookIndexLegal(selectedBookIndex);

		this.books.get(selectedBookIndex).swapTracks(firstIndex, secondIndex);

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.TRACK_LIST_CHANGED, null,
					new Bookshelf(this));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#moveTrack
	 * (int, int)
	 */
	public void moveTrack(int from, int to) {
		checkBookIndexLegal(selectedBookIndex);

		this.books.get(selectedBookIndex).moveTrack(from, to);

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.TRACK_LIST_CHANGED, null,
					new Bookshelf(this));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#
	 * setSelectedTrackIndex(int)
	 */
	public void setSelectedTrackIndex(int index) {
		checkBookIndexLegal(selectedBookIndex);

		this.books.get(selectedBookIndex).setSelectedTrackIndex(index);

		pcs.firePropertyChange(Constants.Event.TRACK_INDEX_CHANGED, null,
				new Bookshelf(this));

		Log.d(TAG, "Book index : " + selectedBookIndex + ", Track index: "
				+ this.books.get(selectedBookIndex).getSelectedTrackIndex());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#
	 * setSelectedBookTitle(java.lang.String)
	 */
	public void setSelectedBookTitle(String newTitle) {
		setBookTitleAt(selectedBookIndex, newTitle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#
	 * getSelectedBookTitle()
	 */
	public String getSelectedBookTitle() {
		return getBookTitleAt(this.selectedBookIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#
	 * getSelectedBookAuthor()
	 */
	public String getSelectedBookAuthor() {
		return this.books.get(selectedBookIndex).getSelectedBookAuthor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#
	 * updateBookDuration()
	 */
	public void updateBookDuration() {
		this.books.get(selectedBookIndex).updateBookDuration();
	}

	/* End IBookUpdates */

	/**
	 * Set the title of the book at the given index.
	 * 
	 * @param bookIndex
	 * @param newTitle
	 */
	public void setBookTitleAt(int bookIndex, String newTitle) {
		if (bookIndex == NO_BOOK_SELECTED) {
			throw new IndexOutOfBoundsException(TAG + " setBookTitleAt"
					+ BOOK_INDEX_ILLEGAL);
		}

		// TODO(?): Weird method name 'selected'.
		this.books.get(bookIndex).setSelectedBookTitle(newTitle);

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.BOOK_TITLE_CHANGED, null,
					new Bookshelf(this));
		}
	}

	/**
	 * Gets the title of a book at a given index.
	 * 
	 * @param bookIndex
	 *            Index to get the book title from.
	 * @return Book title at a given index.
	 */
	public String getBookTitleAt(int bookIndex) {
		if (bookIndex == NO_BOOK_SELECTED) {
			throw new IndexOutOfBoundsException(TAG + " getBookTitleAt"
					+ BOOK_INDEX_ILLEGAL);
		}

		return this.books.get(bookIndex).getSelectedBookTitle();
	}

	/* ITrackUpdates */

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.ITrackUpdates#
	 * setSelectedTrackElapsedTime(int)
	 */
	public void setSelectedTrackElapsedTime(int elapsedTime) {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IndexOutOfBoundsException(TAG
					+ " setSelectedTrackElapsedTime " + NO_BOOK_SELECTED);
		}

		// set elapsed time in the currently playing book
		books.get(selectedBookIndex).setSelectedTrackElapsedTime(elapsedTime);

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.ELAPSED_TIME_CHANGED, null,
					new Bookshelf(this));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.ITrackUpdates#addTag(int)
	 */
	public void addTag(int time) {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IndexOutOfBoundsException(TAG + " addTag "
					+ NO_BOOK_SELECTED);
		}

		this.books.get(selectedBookIndex).addTag(time);
		pcs.firePropertyChange(Constants.Event.TAG_ADDED, null, new Bookshelf(
				this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.ITrackUpdates#removeTagAt
	 * (int)
	 */
	public void removeTagAt(int tagIndex) {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IndexOutOfBoundsException(TAG + " removeTagAt "
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
	public Book getSelectedBook() {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IndexOutOfBoundsException(TAG + " getSelectedBook "
					+ NO_BOOK_SELECTED);
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
	public int getSelectedBookDuration() {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IndexOutOfBoundsException(TAG
					+ " getSelectedBookDuration " + NO_BOOK_SELECTED);
		}

		return books.get(selectedBookIndex).getDuration();
	}

	/**
	 * @return
	 */
	public int getSelectedTrackIndex() {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IndexOutOfBoundsException(TAG + " getSelectedTrackIndex "
					+ NO_BOOK_SELECTED);
		}

		return books.get(selectedBookIndex).getSelectedTrackIndex();
	}

	/**
	 * ** currently unused **
	 * 
	 * @return
	 */
	public int getBookElapsedTime() {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IndexOutOfBoundsException(TAG + " getBookElapsedTime "
					+ NO_BOOK_SELECTED);
		}

		return books.get(selectedBookIndex).getBookElapsedTime();
	}

	/**
	 * The number of tracks in the selected book.
	 * 
	 * @return
	 */
	public int getNumberOfTracks() {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IndexOutOfBoundsException(TAG + " getNumberOfTracks "
					+ NO_BOOK_SELECTED);
		}

		return books.get(selectedBookIndex).getNumberOfTracks();
	}

	/*
	 * Accessors to Track.
	 */

	/**
	 * @return
	 */
	public int getSelectedTrackDuration() {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IndexOutOfBoundsException(TAG
					+ " getSelectedTrackDuration " + NO_BOOK_SELECTED);
		}

		return books.get(selectedBookIndex).getSelectedTrackDuration();
	}

	/**
	 * @return
	 */
	public String getSelectedTrackPath() {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IndexOutOfBoundsException(TAG + " getSelectedTrackPath "
					+ NO_BOOK_SELECTED);
		}

		return books.get(selectedBookIndex).getSelectedTrackPath();
	}

	/**
	 * @param track
	 * @return
	 */
	public int getTrackDurationAt(int track) {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IndexOutOfBoundsException(TAG + " getTrackDurationAt "
					+ NO_BOOK_SELECTED);
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

	/**
	 * Checks whether a given index is within the valid bounds of the list of
	 * books. Includes the 'deselected' index.
	 * 
	 * @param index
	 *            Index to check.
	 * @return True if within bounds.
	 */
	private boolean isValidBookIndex(int index) {
		return isLegalBookIndex(index)
				|| index == Constants.Value.NO_BOOK_SELECTED;
	}

	/**
	 * Checks whether a given index is within the valid bounds of the list of
	 * tracks. Includes the 'deselected' index.
	 * 
	 * @param bookIndex
	 *            The book at which to check for validity.
	 * @param trackIndex
	 *            Index to check.
	 * @return True if within bounds.
	 */
	private boolean isValidTrackIndex(int bookIndex, int trackIndex) {
		return isLegalTrackIndexAt(bookIndex, trackIndex)
				|| trackIndex == Constants.Value.NO_TRACK_SELECTED;
	}

	/**
	 * @return True if this object has elements in its property change listener
	 *         object.
	 */
	private boolean hasListeners() {
		return pcs.getPropertyChangeListeners().length > 0;
	}

	/**
	 * @param index
	 * @return
	 */
	public boolean isLegalTrackIndex(int index) {
		if (selectedBookIndex == NO_BOOK_SELECTED) {
			throw new IndexOutOfBoundsException(TAG + " isLegalTrackIndex "
					+ NO_BOOK_SELECTED);
		}

		return this.books.get(selectedBookIndex).isLegalTrackIndex(index);
	}

	/**
	 * @param bookIndex
	 * @param trackIndex
	 * @return
	 */
	private boolean isLegalTrackIndexAt(int bookIndex, int trackIndex) {
		checkBookIndexLegal(bookIndex);

		return books.get(bookIndex).isLegalTrackIndex(trackIndex);
	}

	/**
	 * Adds a listener to this object's property change listener object.
	 * 
	 * @param listener
	 *            Listener to add.
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		if (listener != null) {
			pcs.addPropertyChangeListener(listener);

			/*
			 * Synchronize the new listener with the current state of the
			 * bookshelf.
			 */
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
		for (PropertyChangeListener pcl : pcs.getPropertyChangeListeners()) {
			pcs.removePropertyChangeListener(pcl);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(books).append(selectedBookIndex)
				.toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Bookshelf) {
			final Bookshelf other = (Bookshelf) obj;
			return new EqualsBuilder().append(books, other.books)
					.append(selectedBookIndex, other.selectedBookIndex)
					.isEquals();
		} else {
			return false;
		}
	}

	/**
	 * Gets the track title at given position
	 * 
	 * @param bookIndex
	 *            Position of the book
	 * @param trackIndex
	 *            Position of the track
	 * @return Track title of given track
	 */
	public String getTrackTitleAt(int bookIndex, int trackIndex) {
		checkBookIndexLegal(bookIndex);
		checkTrackIndexLegalAt(bookIndex, trackIndex);

		return this.books.get(bookIndex).getTrackTitleAt(trackIndex);
	}

	/**
	 * Gets the number of tracks the book at given position has
	 * 
	 * @param bookIndex
	 *            Position of the book
	 * @return Number of tracks of given book
	 */
	public int getNumberOfTracksAt(int bookIndex) {
		checkBookIndexLegal(bookIndex);

		return this.books.get(bookIndex).getNumberOfTracks();
	}

	/**
	 * Gets the duration of the given book
	 * 
	 * @param bookIndex
	 *            Position of the book
	 * @return
	 */
	public int getBookDurationAt(int bookIndex) {
		checkBookIndexLegal(bookIndex);

		// TODO
		return 0;
	}

	/**
	 * Gets the given books elapsed time
	 * 
	 * @param bookIndex
	 *            Position of the book
	 * @return The given books elapsed time
	 */
	public int getBookElapsedTimeAt(int bookIndex) {
		checkBookIndexLegal(bookIndex);

		// TODO
		return 0;
	}

	/**
	 * Gets the author at given position
	 * 
	 * @param bookIndex
	 *            Position of the book
	 * @return The given books author
	 */
	public String getBookAuthorAt(int bookIndex) {
		checkBookIndexLegal(bookIndex);

		// TODO
		return null;
	}

	/**
	 * Gets duration of a track at given position
	 * 
	 * @param bookIndex
	 *            Position of the book
	 * @param trackIndex
	 *            Position of the track
	 * @return
	 */
	public int getTrackDurationAt(int bookIndex, int trackIndex) {
		checkBookIndexLegal(bookIndex);
		checkTrackIndexLegalAt(bookIndex, trackIndex);

		return this.books.get(bookIndex).getTrackDurationAt(trackIndex);
	}

	/**
	 * If two or more tracks exist, remove the given track, otherwise remove the
	 * book
	 * 
	 * @param bookIndex
	 *            Index of the book
	 * @param trackIndex
	 *            Index of the track
	 */
	public void removeTrack(int bookIndex, int trackIndex) {
		checkBookIndexLegal(bookIndex);
		checkTrackIndexLegalAt(bookIndex, trackIndex);

		// the event to fire
		String event;

		if (this.books.get(bookIndex).getNumberOfTracks() <= 1) {
			// remove the entire book (since there was just 1 track)

			removeBookAt(bookIndex);

			event = Constants.Event.BOOK_LIST_CHANGED;
		} else {
			// if more than 1 track, remove it
			this.books.get(bookIndex).removeTrack(trackIndex);

			// re-calculate the book duration
			updateBookDurationAt(bookIndex);

			event = Constants.Event.TRACK_LIST_CHANGED;
		}

		if (hasListeners()) {
			pcs.firePropertyChange(event, null, new Bookshelf(this));
		}

	}

	/**
	 * Private method to update the duration of the book at given index.
	 * bookIndex shoulve have already been checked to be legal.
	 * 
	 * @param bookIndex
	 *            Index of the book
	 */
	private void updateBookDurationAt(int bookIndex) {
		checkBookIndexLegal(bookIndex);

		this.books.get(bookIndex).updateBookDuration();
	}

	/**
	 * Moves a track given amount of steps.
	 * 
	 * @param bookIndex
	 *            Index of the book
	 * @param trackIndex
	 *            Index of the track
	 * @param offset
	 *            The amount of steps to move the track, negative value moves
	 *            upward visually
	 */
	public void moveTrack(int bookIndex, int trackIndex, int offset) {
		checkBookIndexLegal(bookIndex);
		checkTrackIndexLegalAt(bookIndex, trackIndex);
		checkTrackIndexLegalAt(bookIndex, trackIndex + offset);

		this.books.get(bookIndex).moveTrack(trackIndex, trackIndex + offset);

		if (hasListeners()) {
			pcs.firePropertyChange(Constants.Event.BOOK_LIST_CHANGED, null,
					new Bookshelf(this));
		}
	}

	/**
	 * Convenience method. Checks if the index is valid for a book.
	 * 
	 * @param index
	 */
	private void checkBookIndexLegal(int index) {
		if (!isLegalBookIndex(index)) {
			throw new IndexOutOfBoundsException("Book index not legal: "
					+ index);
		}
	}

	/**
	 * Convenience method. Checks if the index is valid for a track in a book.
	 * 
	 * @param bookIndex
	 *            Book to check in.
	 * @param trackIndex
	 *            Track to check.
	 */
	private void checkTrackIndexLegalAt(int bookIndex, int trackIndex) {
		if (!isLegalTrackIndexAt(bookIndex, trackIndex)) {
			throw new IndexOutOfBoundsException(
					"Track or book index not legal (book, track): " + bookIndex
							+ ", " + trackIndex);
		}
	}

}
