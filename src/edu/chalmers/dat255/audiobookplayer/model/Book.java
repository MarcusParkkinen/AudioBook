package edu.chalmers.dat255.audiobookplayer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.chalmers.dat255.audiobookplayer.util.StringConstants;

import android.util.Log;

/**
 * Represents a collection of Track objects that 
 * collectively form a book.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.4
 */

public class Book {
	private static final String TAG = "Book.java";

	private LinkedList<Track> tracks;
	private int trackIndex;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private String name;
	@SuppressWarnings("unused")
	private int duration;

	// private Bookmark bookmark;
	// private Tag[] tags;
	// private Stats stats;

	/**
	 * Create an empty book.
	 */
	public Book() {
		tracks = new LinkedList<Track>();
	}

	/**
	 * Create a book from the referenced collection of Tracks.
	 * 
	 * @param c
	 *            A collection containing Track instances.
	 */
	public Book(Collection<Track> c, String name) {
		this();
		this.name = name;
		
		for (Track t : c) {
			if (t != null) {
				tracks.add(t);
			}
		}
	}

	public Track getSelectedTrack() {
		return tracks.get(trackIndex);
	}

	/**
	 * Removes a track from the collection on the specified index.
	 * 
	 * @param index
	 */
	public void removeTrack(int index) {
		if (tracks.size() < index) {
			tracks.remove(index);
			pcs.firePropertyChange(StringConstants.event.TRACK_REMOVED, null,
					null);
		}
	}

	/**
	 * Adds a track to the collection to the specified index.
	 * 
	 * @param index
	 *            Where to add the track.
	 * @param t
	 *            The Track instance to add.
	 */
	public void addTrack(int index, Track t) {
		if (t != null) {
			tracks.add(index, t);
			pcs.firePropertyChange(StringConstants.event.TRACK_ADDED, null,
					null);
		}
	}

	/**
	 * Adds a track to the end of the collection.
	 * 
	 * @param t
	 */
	public void addTrack(Track t) {
		addTrack(tracks.size(), t);
	}

	/**
	 * Add a collection of tracks.
	 * 
	 * @param c
	 *            Collection that contains references to the tracks
	 */

	public void addTracks(Collection<Track> c) {
		for (Track t : c) {
			addTrack(t);
		}
	}

	/**
	 * Swap location of two tracks.
	 * 
	 * @param firstIndex
	 * @param secondIndex
	 */
	public void swap(int firstIndex, int secondIndex) {
		try {
			Collections.swap(tracks, firstIndex, secondIndex);
			// TODO: see catch; does not always swap?
			pcs.firePropertyChange(StringConstants.event.TRACK_SWAPPED, null,
					null);
		} catch (IndexOutOfBoundsException e) {
			Log.e(TAG,
					" attempting to move a track from/to illegal index. Skipping operation.");
		}
	}

	/**
	 * Move the track at the specified index to a new index.
	 * 
	 * @param oldIndex
	 *            index of the track
	 * @param newIndex
	 */
	public void move(int from, int to) {
		if (tracks.size() < from && tracks.size() < to) {
			Track temp = tracks.remove(from);
			tracks.add(to, temp);
			pcs.firePropertyChange(StringConstants.event.TRACK_MOVED, null,
					null);
		} else {
			Log.e(TAG,
					" attempting to move a track from/to illegal index. Skipping operation.");
		}
	}

	// TODO: test/delete
	public void moveTrack(int from, int to) {
		Collections.rotate(tracks.subList(from, to + 1), -1);
	}

	// ------
	/**
	 * Returns the number of elements (tracks) in the book.
	 * 
	 * @return a Number of elements (tracks).
	 */
	public int getNumberOfTracks() {
		return tracks.size();
	}

	/**
	 * 
	 * @return the path that is playing (or is ready to be played).
	 */
	public Track getCurrentTrack() {
		return this.tracks.get(trackIndex);
	}

	/**
	 * @param index
	 * @return The track at index <i>index</i> of the book.
	 */
	public Track getTrackAt(int index) {
		return tracks.get(index);
	}

	/**
	 * @return The list of tracks.
	 */
	public List<Track> getTracks() {
		return tracks;
	}

	// ----

	/**
	 * Set the bookmark to point at a track and a specific time in that track.
	 * 
	 * @param trackIndex
	 *            Index of the track in the book
	 * @param time
	 *            The time at which to add the bookmark (in ms).
	 */
	public void setBookmark(int trackIndex, int time) {
		// this.bookmark = new Bookmark(trackIndex, time);
		pcs.firePropertyChange(StringConstants.event.BOOKMARK_SET, null, null);
	}

	/**
	 * Sets the track index of the book. Rolls over if the index is out of
	 * bounds.
	 * 
	 * @param index
	 */
	public void setCurrentTrackIndex(int index) {
		this.trackIndex = index % this.tracks.size();
		pcs.firePropertyChange(StringConstants.event.TRACK_INDEX_CHANGED, null,
				null);
	}

	/**
	 * @return the index of the path either currently open or ready to be opened
	 *         by the player
	 */
	public int getCurrentTrackIndex() {
		return this.trackIndex;
	}

	/**
	 * Increases the index to be played by the player by 1, or sets it to 0 if
	 * there isn't a later element in the queue.
	 */
	public void incrementTrackIndex() {
		setCurrentTrackIndex(this.trackIndex + 1);
	}

	/**
	 * Decreases the index to be played by the player by 1, or sets it to the
	 * index of the last element if the current element index is 0.
	 */
	public void decrementTrackIndex() {
		setCurrentTrackIndex(this.trackIndex - 1);
	}

	/**
	 * Returns a list containing references to all tracks contained in this
	 * book.
	 * 
	 * @return List<String> the list
	 */
	public List<String> getPaths() {
		LinkedList<String> l = new LinkedList<String>();
		for (Track t : tracks) {
			l.add(t.getTrackPath());
		}
		return l;
	}

	/**
	 * Corrects the duration of the book to the sum of the duration of its
	 * tracks.
	 */
	public void updateDuration() {
		for (Track t : tracks) {
			duration += t.getTrackDuration();
		}
		pcs.firePropertyChange(StringConstants.event.BOOK_DURATION_CHANGED,
				null, null);
	}

	public int getTrackDuration() {
		return tracks.get(trackIndex).getTrackDuration();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

}
