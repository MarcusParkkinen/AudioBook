package edu.chalmers.dat255.audiobookplayer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;

/**
 * The bookshelf class contains a collection of books.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.5
 * 
 */

public class Bookshelf implements IBookUpdates, ITrackUpdates {
	private static final String TAG = "Bookshelf.java";

	private LinkedList<Book> books;
	private int selectedBookIndex;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	/**
	 * Creates an empty bookshelf.
	 */
	public Bookshelf() {
		books = new LinkedList<Book>();
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param other
	 */
	public Bookshelf(Bookshelf other) {
		this();
		this.selectedBookIndex = other.selectedBookIndex;
		for (Book b: books) {
			this.books.add(new Book(b));
		}
	}
	
	public Object clone() throws CloneNotSupportedException {
		Bookshelf clone = (Bookshelf) super.clone();
		// Immutable values
		clone.selectedBookIndex = this.selectedBookIndex;
		
		// Shallow copy of the object of type Track
		clone.books = (LinkedList<Book>) clone.books;
		clone.books.clear();
		for (Book b : books) {
			Book copyBook = (Book)b.clone(); // clone in Book.class
			clone.addBook(copyBook);
		}
	    return clone;
	  }
	
	/**
	 * The book that the player will use (read from) is set here.
	 * 
	 * @param index
	 */
	public void setSelectedBook(int index) {
		this.selectedBookIndex = index;
		Log.i(TAG, "Selected a book");
		pcs.firePropertyChange(Constants.event.BOOK_SELECTED, null, new Book(this.books.get(selectedBookIndex)));
//																	new Bookshelf(this)
	}

	/* Bookshelf methods */
	public void addBook(Book b) {
		books.add(b);
		pcs.firePropertyChange(Constants.event.BOOK_ADDED, null, new Bookshelf(this));
	}

	public void removeBook(int index) {
		books.remove(index);
		if (selectedBookIndex + 1 > books.size())
			selectedBookIndex--;
		pcs.firePropertyChange(Constants.event.BOOK_REMOVED, null, new Bookshelf(this));
	}

	public void moveBook(int from, int to) {
		if (selectedBookIndex == from)
			selectedBookIndex = to;
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
		updateBookDuration();
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

	// End convenience methods

	/* End Book methods */

	/* Track methods */
	public void setElapsedTime(int elapsedTime) {
		this.books.get(selectedBookIndex).setElapsedTime(elapsedTime);
		pcs.firePropertyChange(Constants.event.TRACK_ELAPSED_TIME_CHANGED,
				null, new Book(this.books.get(selectedBookIndex)).getCurrentTrack());
	}

	/* End Track methods */

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	/* convenience method */
	
	public int getBookDuration() {
		return books.get(selectedBookIndex).getDuration();
	}

	public int getTrackDuration() {
		return books.get(selectedBookIndex).getTrackDuration();
	}

	public String getCurrentTrackPath() {
		return books.get(selectedBookIndex).getCurrentTrackPath();
	}

	public int getCurrentTrackIndex() {
		return books.get(selectedBookIndex).getCurrentTrackIndex();
	}

	/* End convenience method */

}
