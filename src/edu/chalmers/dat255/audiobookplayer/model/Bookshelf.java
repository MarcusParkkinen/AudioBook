package edu.chalmers.dat255.audiobookplayer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import edu.chalmers.dat255.audiobookplayer.util.StringConstants;
import android.util.Log;

/**
 * The bookshelf class contains a collection of books.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.4
 * 
 */

public class Bookshelf {
	private static final String TAG = "Bookshelf.java";

	private LinkedList<Book> books;
	private int selectedBook;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	/**
	 * Creates an empty bookshelf.
	 */
	public Bookshelf() {
		books = new LinkedList<Book>();
	}

	public void addBook(Book b) {
		books.add(b);
		pcs.firePropertyChange(StringConstants.event.BOOK_ADDED, null, null);
	}

	public void removeBook(int index) {
		books.remove(index);
		pcs.firePropertyChange(StringConstants.event.BOOK_REMOVED, null, null);
	}

	public void moveBook(int from, int to) {
		if (books.size() < from && books.size() < to) {
			Book temp = books.remove(to);
			books.add(from, temp);
			pcs.firePropertyChange(StringConstants.event.BOOK_MOVED, null, null);
		} else {
			Log.e(TAG,
					" attempting to move a track from/to illegal index. Skipping operation.");
		}
	}

	public String getCurrentTrackPath() {
		return books.get(selectedBook).getCurrentTrack().getTrackPath();
	}

	/**
	 * Moves the specified track to the specified index.
	 * 
	 * @param from Start index.
	 * @param to Where to put the track.
	 */
	public void moveTrack(int from, int to) {
		books.get(selectedBook).moveTrack(from, to);
	}

	public void incrementTrackIndex() {
		books.get(selectedBook).incrementTrackIndex();
	}

	public void decrementTrackIndex() {
		books.get(selectedBook).decrementTrackIndex();
	}

	public int getTrackDuration() {
		return books.get(selectedBook).getTrackDuration();
	}

	/**
	 * The book that the player will play is set here.
	 * 
	 * @param index
	 */
	public void setSelectedBook(int index) {
		this.selectedBook = index;
	}

	/**
	 * 
	 * 
	 * @return The index of the book that is either playing or is to be played.
	 */
	public int getSelectedBookIndex() {
		return selectedBook;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

}
