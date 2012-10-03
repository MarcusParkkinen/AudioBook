package edu.chalmers.dat255.audiobookplayer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

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

public class Bookshelf implements IBookUpdates, ITrackUpdates {
	private static final String TAG = "Bookshelf.java";
	private static final int NO_BOOK_SELECTED = -1;

	private LinkedList<Book> books;
	private int selectedBookIndex;
	private PropertyChangeSupport pcs;

	/**
	 * Creates an empty bookshelf.
	 */
	public Bookshelf() {
		books = new LinkedList<Book>();
		selectedBookIndex = NO_BOOK_SELECTED;
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
	 * @param int index
	 */
	public void setSelectedBookIndex(int index) {
		if (isLegalIndex(index)) {
			selectedBookIndex = index;

			// notify the view module that we have selected a book
			pcs.firePropertyChange(Constants.event.BOOK_SELECTED, null,
					new Bookshelf(this));
		} else {
			Log.e(TAG,
					" attempting to select a book with illegal index. Skipping operation.");
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
		if (selectedBookIndex == NO_BOOK_SELECTED)
			selectedBookIndex = 0;
		// Log.d(TAG, "list size (original): " + books.size());
		pcs.firePropertyChange(Constants.event.BOOK_ADDED, null, new Bookshelf(
				this));
	}

	/**
	 * Remove a book from the bookshelf.
	 * 
	 * @param int index
	 */
	public void removeBook(int index) {
		if (isLegalIndex(index)) {
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

			// notify the listeners about this change
			pcs.firePropertyChange(Constants.event.BOOK_REMOVED, null,
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
		if (isLegalIndex(from) && isLegalIndex(to)) {
			if (selectedBookIndex == from) {
				selectedBookIndex = to;
			}
			if (books.size() < from && books.size() < to) {
				Book temp = books.remove(to);
				books.add(from, temp);
				pcs.firePropertyChange(Constants.event.BOOK_MOVED, null,
						new Bookshelf(this));
			}
		} else {
			Log.e(TAG,
					" attempting to move a track from/to illegal index. Skipping operation.");
		}
	}

	/* End Bookshelf methods */

	/* Book methods */

	public void removeTrack(int index) {
		if (!isLegalIndex(index)) {
			throw new IllegalArgumentException(
					"Tried to remove track at index when index is illegal.");
		}
		this.books.get(selectedBookIndex).removeTrack(index);
		pcs.firePropertyChange(Constants.event.TRACK_REMOVED, null,
				new Bookshelf(this));

		/*
		 * since we removed a track we need to recalculate the duration of the
		 * book
		 */
		updateBookDuration();
	}

	/**
	 * Adds a track to the end of the collection.
	 * 
	 * @param t
	 */
	public void addTrack(Track t) {
		this.books.get(selectedBookIndex).addTrack(t);
		pcs.firePropertyChange(Constants.event.TRACK_ADDED, null,
				new Bookshelf(this));
		/*
		 * since we removed a track we need to recalculate the duration of the
		 * book
		 */
		updateBookDuration();
	}

	public void swap(int firstIndex, int secondIndex) {
		this.books.get(selectedBookIndex).swap(firstIndex, secondIndex);
		pcs.firePropertyChange(Constants.event.TRACK_ORDER_CHANGED, null,
				new Bookshelf(this));
	}

	public void moveTrack(int from, int to) {
		this.books.get(selectedBookIndex).moveTrack(from, to);
		pcs.firePropertyChange(Constants.event.TRACK_ORDER_CHANGED, null,
				new Bookshelf(this));
	}

	// public void setBookmark(int trackIndex, int time) { }
	// public void setTag(int trackIndex, int time) { }

	public void setSelectedTrackIndex(int index) {
		this.books.get(selectedBookIndex).setSelectedTrackIndex(index);

		/*
		 * only send an update if the new index is legal (i.e. something should
		 * happen in the GUI).
		 */
		boolean legal = this.books.get(selectedBookIndex).isLegalIndex(index);

		Log.d(TAG, "(Bookshelf.setSelectedTrackindex) new track index: "
				+ index + "(" + (legal ? "legal" : "illegal") + ")");

		if (legal) {
			Log.d(TAG, "Firing update event (Bookshelf.setSelectedTrackindex)");
			pcs.firePropertyChange(Constants.event.TRACK_INDEX_CHANGED, null,
					new Bookshelf(this));
		}
	}

	public void setBookTitle(String newTitle) {
		this.books.get(selectedBookIndex).setBookTitle(newTitle);
		pcs.firePropertyChange(Constants.event.BOOK_TITLE_CHANGED, null,
				new Bookshelf(this));
	}

	public void updateBookDuration() {
		this.books.get(selectedBookIndex).updateBookDuration();
		pcs.firePropertyChange(Constants.event.BOOK_DURATION_CHANGED, null,
				new Bookshelf(this));
	}

	// Extra convenience methods

	public int getSelectedBookDuration() {
		return books.get(selectedBookIndex).getDuration();
	}

	public int getSelectedTrackDuration() {
		return books.get(selectedBookIndex).getTrackDuration();
	}

	public String getSelectedTrackPath() {
			return books.get(selectedBookIndex).getSelectedTrackPath();
	}

	public int getSelectedTrackIndex() {
		return books.get(selectedBookIndex).getSelectedTrackIndex();
	}

	public int getSelectedBookIndex() {
		return this.selectedBookIndex;
	}

	/**
	 * Private method that checks whether a provided index is within the legal
	 * bounds of the list of books.
	 * 
	 * @param index
	 *            index
	 * @return boolean
	 */
	private boolean isLegalIndex(int index) {
		return index >= 0 && index < books.size();
	}

	// End convenience methods

	/* End Book methods */

	/* Track methods */
	public void setElapsedTime(int elapsedTime) {
		// set elapsed time in the currently playing book
		books.get(selectedBookIndex).setElapsedTime(elapsedTime);

		pcs.firePropertyChange(Constants.event.ELAPSED_TIME_CHANGED, null,
				new Bookshelf(this));
	}

	/* End Track methods */

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs = new PropertyChangeSupport(this);
		pcs.addPropertyChangeListener(listener);
		pcs.firePropertyChange(Constants.event.BOOKSHELF_UPDATED, null,
				new Bookshelf(this));
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	public Book getSelectedBook() {
		return this.books.get(selectedBookIndex);
	}

	public Book getBookAt(int index) {
		return this.books.get(index);
	}

	public int getNumberOfBooks() {
		return this.books.size();
	}

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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bookshelf other = (Bookshelf) obj;
		if (books == null) {
			if (other.books != null)
				return false;
		} else if (!books.equals(other.books))
			return false;
		if (selectedBookIndex != other.selectedBookIndex)
			return false;
		return true;
	}

	/**
	 * @param track
	 * @return
	 */
	public int getTrackDurationAt(int track) {
		return books.get(selectedBookIndex).getTrackDurationAt(track);
	}

	/**
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
}
