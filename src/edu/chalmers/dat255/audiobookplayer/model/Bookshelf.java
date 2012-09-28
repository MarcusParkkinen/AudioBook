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
	private int selectedBookIndex = 0;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	/**
	 * Creates an empty bookshelf.
	 */
	public Bookshelf() {
		books = new LinkedList<Book>();
	}

	/* Bookshelf methods */
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
	
	/* End Bookshelf methods */
	
	/*    Book methods    */
	// Extra convenience methods
	public void incrementTrackIndex() {
		books.get(selectedBookIndex).incrementTrackIndex();
	}
	
	public void decrementTrackIndex() {
		books.get(selectedBookIndex).decrementTrackIndex();
	}
	// End convenience methods
	
	public void removeTrack(int index) {
		// TODO Auto-generated method stub
		
	}

	public void addTrack(int index, Track t) {
		// TODO Auto-generated method stub
		
	}

	public void swap(int firstIndex, int secondIndex) {
		// TODO Auto-generated method stub
		
	}

	public void moveTrack(int from, int to) {
		// TODO Auto-generated method stub
		
	}

//	public void setBookmark(int trackIndex, int time) {	}
//	public void setTag(int trackIndex, int time) { }

	public void setCurrentTrackIndex(int index) {
		// TODO Auto-generated method stub
		
	}
	
	public void setBookTitle(String newTitle) {
		// TODO Auto-generated method stub
		
	}
	
	/* End Book methods */

	/*    Track methods    */
	public void setElapsedTime(int elapsedTime) {
		this.books.get(selectedBookIndex).setElapsedTime(elapsedTime);
		pcs.firePropertyChange(Constants.event.TRACK_TIME_CHANGED, null, elapsedTime);
	}
	/*    End Track methods    */
	
	public int getTrackDuration() {
		return books.get(selectedBookIndex).getTrackDuration();
	}
	
	public String getCurrentTrackPath() {
		return books.get(selectedBookIndex).getCurrentTrack().getTrackPath();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	// convenience method
	public void addToElapsedTime(int time) {
		this.setElapsedTime(this.books.get(selectedBookIndex).getElapsedTime() + time);
	}

}
