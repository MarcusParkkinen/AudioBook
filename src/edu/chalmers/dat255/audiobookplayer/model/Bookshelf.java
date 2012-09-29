package edu.chalmers.dat255.audiobookplayer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;

/**
 * The bookshelf class contains a collection of books.
 * 
 * @author Marcus Parkkinen, Aki K�kel�
 * @version 0.5
 * 
 */

public class Bookshelf implements IBookUpdates, ITrackUpdates {
	private static final String TAG = "Bookshelf.java";
	private static final int NO_BOOK_SELECTED = -1;

	private LinkedList<Book> books;
	private int selectedBookIndex;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

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
		this.selectedBookIndex = original.selectedBookIndex;
		for (Book b : books) {
			this.books.add(new Book(b));
		}
	}
	
	/* Bookshelf methods */
	/**
	 * The book that the player will use (read from) is set here.
	 * 
	 * @param int index
	 */
	public void setSelectedBook(int index) {
		if(isLegalIndex(index)) {
			selectedBookIndex = index;
			Log.i(TAG, "Selected a book");
			
			// notify the view module that we have selected a book
			pcs.firePropertyChange(Constants.event.BOOK_SELECTED, null,
					new Book(this.books.get(selectedBookIndex)));
		} else{
			Log.e(TAG,
					" attempting to select a book with illegal index. Skipping operation.");
		}
		
		
	}
	
	/**
	 * Add a new book to the bookshelf.
	 * 
	 * @param Book the new book to add
	 */
	public void addBook(Book b) {
		books.addLast(b);
		
		// notify the view module that a new book has been added, provide the book as well as
		// a bookshelf copy as reference to the change 
		pcs.firePropertyChange(Constants.event.BOOK_ADDED, null, new Bookshelf(this));
	}
	
	/**
	 * Remove a book from the bookshelf.
	 * 
	 * @param int index
	 */
	public void removeBook(int index) {
		books.remove(index);
		
		// adjust the selected book index
		if (index < selectedBookIndex) {
			selectedBookIndex--;
		}
		
		// notify the view module of the change, provide a bookshelf copy as reference
		pcs.firePropertyChange(Constants.event.BOOK_REMOVED, null, new Bookshelf(this));
	}

	public void moveBook(int from, int to) {
		if (selectedBookIndex == from) {
			selectedBookIndex = to;
		}	
		if (books.size() < from && books.size() < to) {
			Book temp = books.remove(to);
			books.add(from, temp);
			pcs.firePropertyChange(Constants.event.BOOK_MOVED, null, new Bookshelf(this));
		} else {
			Log.e(TAG,
					" attempting to move a track from/to illegal index. Skipping operation.");
		}
	}

	/* End Bookshelf methods */

	/* Book methods */

	public void removeTrack(int index) {
		this.books.get(selectedBookIndex).removeTrack(index);
		pcs.firePropertyChange(Constants.event.TRACK_REMOVED, null, new Book(this.books.get(selectedBookIndex)));

		/* since we removed a track we need to recalculate the duration of the book*/
		updateBookDuration();
	}

	/**
	 * Adds a track to the end of the collection.
	 * 
	 * @param t
	 */
	public void addTrack(Track t) {
		int index = this.books.get(selectedBookIndex).getNumberOfTracks();
		addTrackTo(index, t);
	}

	public void addTrackTo(int index, Track t) {
		this.books.get(selectedBookIndex).addTrackTo(index, t);
		pcs.firePropertyChange(Constants.event.TRACK_ADDED, null,
				new Book(this.books.get(selectedBookIndex)));
		
		/* since we removed a track we need to recalculate the duration of the book*/
		//updateBookDuration();
	}

	public void swap(int firstIndex, int secondIndex) {
		this.books.get(selectedBookIndex).swap(firstIndex, secondIndex);
		pcs.firePropertyChange(Constants.event.TRACK_ORDER_CHANGED, null, new Book(this.books.get(selectedBookIndex)));
	}

	public void moveTrack(int from, int to) {
		this.books.get(selectedBookIndex).moveTrack(from, to);
		pcs.firePropertyChange(Constants.event.TRACK_ORDER_CHANGED, null, new Book(this.books.get(selectedBookIndex)));
	}

	// public void setBookmark(int trackIndex, int time) { }
	// public void setTag(int trackIndex, int time) { }

	public void setCurrentTrackIndex(int index) {
		this.books.get(selectedBookIndex).setCurrentTrackIndex(index);
		pcs.firePropertyChange(Constants.event.TRACK_INDEX_CHANGED, null, new Book(this.books.get(selectedBookIndex)));
	}

	public void setBookTitle(String newTitle) {
		this.books.get(selectedBookIndex).setBookTitle(newTitle);
		pcs.firePropertyChange(Constants.event.BOOK_TITLE_CHANGED, null,
				new Book(this.books.get(selectedBookIndex)));
	}

	public void updateBookDuration() {
		this.books.get(selectedBookIndex).updateBookDuration();
		pcs.firePropertyChange(Constants.event.BOOK_DURATION_CHANGED, null,
				new Book(this.books.get(selectedBookIndex)));
	}

	// Extra convenience methods

	public int getBookDuration() {
		return books.get(selectedBookIndex).getDuration();
	}

	public int getTrackDuration() {
		return books.get(selectedBookIndex).getTrackDuration();
	}

	public String getCurrentTrackPath() {
		Book b = books.get(selectedBookIndex);
		return b.getCurrentTrackPath();
	}

	public int getCurrentTrackIndex() {
		return books.get(selectedBookIndex).getCurrentTrackIndex();
	}
	
	/**
	 * Private method that checks whether a provided index is within the legal bounds of
	 * the list of books.
	 * 
	 * @param index index
	 * @return boolean
	 */
	private boolean isLegalIndex(int index) {
		return (index >= 0 && index < books.size())? true : false;
	}
	
	// End convenience methods

	/* End Book methods */

	/* Track methods */
	public void setElapsedTime(int elapsedTime) {
		// set elapsed time in the currently playing book
		// NOTE: this operation sets the time for the currently playing track as well
		books.get(selectedBookIndex).setBookElapsedTime(elapsedTime);
		
		// notify the view module to update its representation by providing the following:
		// - a copy of the currently playing book
		pcs.firePropertyChange(Constants.event.ELAPSED_TIME_CHANGED,
				new Book(this.books.get(selectedBookIndex)),
				null);
	}

	/* End Track methods */

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
	
	public Book getNewestBook() {
		return books.getLast();
	}
}
