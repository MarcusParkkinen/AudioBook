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

public class Bookshelf {
	private static final String TAG = "Bookshelf.java";

	private LinkedList<Book> books;
	private int selectedBookIndex = 0;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	/**
	 * Creates an empty bookshelf.
	 */
	public Bookshelf() {
		books = new LinkedList<Book>();
	}

	public void addBook(Book b) {
		books.add(b);
		pcs.firePropertyChange(Constants.event.BOOK_ADDED, null, b);
	}

	public void removeBook(int index) {
		books.remove(index);
		if (selectedBookIndex+1 > books.size())
			selectedBookIndex--;
		pcs.firePropertyChange(Constants.event.BOOK_REMOVED, null, null);
	}

	public void moveBook(int from, int to) {
		if (selectedBookIndex == from)
			selectedBookIndex = to;
		if (books.size() < from && books.size() < to) {
			Book temp = books.remove(to);
			books.add(from, temp);
			pcs.firePropertyChange(Constants.event.BOOK_MOVED, null, null);
		} else {
			Log.e(TAG,
					" attempting to move a track from/to illegal index. Skipping operation.");
		}
	}
	
	/*    Book methods    */
	/**
	 * Moves the specified track to the specified index.
	 * 
	 * @param from Start index.
	 * @param to Where to put the track.
	 */
	public void moveTrack(int from, int to) {
		books.get(selectedBookIndex).moveTrack(from, to);
	}
	
	public void incrementTrackIndex() {
		books.get(selectedBookIndex).incrementTrackIndex();
	}

	public void decrementTrackIndex() {
		books.get(selectedBookIndex).decrementTrackIndex();
	}

	/**
	 * The track duration in milliseconds.
	 * @return
	 */
	public int getTrackDuration() {
		return books.get(selectedBookIndex).getTrackDuration();
	}

	/**
	 * The book that the player will play is set here.
	 * 
	 * @param index
	 */
	public void setSelectedBook(int index) {
		this.selectedBookIndex = index;
		Log.i(TAG, "Selected a book");
		pcs.firePropertyChange(Constants.event.BOOK_SELECTED, index, null);
	}

//	/**
//	 * 
//	 * 
//	 * @return The index of the book that is either playing or is to be played.
//	 */
//	public int getSelectedBookIndex() {
//		return selectedBookIndex;
//	}
	
	/*    Track methods    */
	public String getCurrentTrackPath() {
		return books.get(selectedBookIndex).getCurrentTrack().getTrackPath();
	}

	public void addToElapsedTime(int time) {
		setElapsedTime(getElapsedTime() + time);
//		pcs.firePropertyChange(Constants.event.TRACK_TIME_CHANGED, this.books.get(selectedBookIndex).getElapsedTime(), time);
	}
	
	/**
	 * @param time
	 *            ms
	 */
	public void setElapsedTime(int elapsedTime) {
//		Log.d(TAG, "Track time changing from " + elapsedTime + " to " + time);
		this.books.get(selectedBookIndex).setElapsedTime(elapsedTime);
//		pcs.firePropertyChange(Constants.event.TRACK_TIME_CHANGED, getElapsedTime(), elapsedTime);
	}
	
	public int getElapsedTime() {
		return this.books.get(selectedBookIndex).getElapsedTime();
	}

	public int getCurrentTrackIndex() {
		return this.books.get(selectedBookIndex).getCurrentTrackIndex();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
	
	public Book getCurrentBook() {
		return this.books.get(selectedBookIndex);
	}

}
