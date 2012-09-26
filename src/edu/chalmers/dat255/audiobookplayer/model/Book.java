package edu.chalmers.dat255.audiobookplayer.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;

/**
 * Represents a collection of Track objects that collectively form a book. Note
 * that "index >= 0" is guaranteed by the GUI.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.4
 */

public class Book {
	private static final String TAG = "Book.java";

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private LinkedList<Track> tracks;
	private int trackIndex = 0;
	private String title;
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
	public Book(Collection<Track> c, String title) {
		this();
		this.setTitle(title);

		for (Track t : c) {
			if (t != null) {
				tracks.add(t);
				duration += t.getDuration();
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
			if (index < trackIndex)
				trackIndex--;
			pcs.firePropertyChange(Constants.event.TRACK_REMOVED, null, null);
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
		if (t != null && index <= tracks.size()) {
			tracks.add(index, t);
			// if we removed a track earlier in the list, compensate:
			if (index < trackIndex)
				trackIndex++;
			pcs.firePropertyChange(Constants.event.TRACK_ADDED, null, null);
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
			pcs.firePropertyChange(Constants.event.TRACK_SWAPPED, null, null);
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
			pcs.firePropertyChange(Constants.event.TRACK_MOVED, null, null);
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
	// public Track getTrackAt(int index) {
	// return tracks.get(index);
	// }

	/**
	 * @return The list of tracks.
	 */
	// public List<Track> getTracks() {
	// return tracks;
	// }

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
		pcs.firePropertyChange(Constants.event.BOOKMARK_SET, null, null);
	}

	/**
	 * Sets the track index of the book. Rolls over if the index is out of
	 * bounds.
	 * 
	 * @param index
	 */
	public void setCurrentTrackIndex(int index) {
		pcs.firePropertyChange(Constants.event.TRACK_INDEX_CHANGING, null, null); // make
		Log.d(TAG, "old index: " + trackIndex);
		trackIndex = index;
		if (trackIndex < 0)
			trackIndex = this.tracks.size() - 1;
		if (trackIndex > tracks.size()-1)
			trackIndex = 0;
		// Log.d(TAG, "Changing trackIndex from " + trackIndex + " to " +
		// index);
//		this.trackIndex = index % this.tracks.size();
		Log.d(TAG, "new index: " + trackIndex);
		pcs.firePropertyChange(Constants.event.TRACK_INDEX_CHANGED, null, null);
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
			setDuration(getDuration() + t.getDuration());
		}
		pcs.firePropertyChange(Constants.event.BOOK_DURATION_CHANGED, null,
				null);
	}

	/**
	 * The track duration in milliseconds.
	 * 
	 * @return
	 */
	public int getTrackDuration() {
		return tracks.get(trackIndex).getDuration();
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void addToElapsedTime(int time) {
		this.tracks.get(trackIndex).addToElapsedTime(time);
	}
	
	/**
	 * @param time
	 *            ms
	 */
	public void setElapsedTime(int elapsedTime) {
//		Log.d(TAG, "Track time changing from " + elapsedTime + " to " + time);
		this.tracks.get(trackIndex).setElapsedTime(elapsedTime);
//		pcs.firePropertyChange(Constants.event.TRACK_TIME_CHANGED, getElapsedTime(), elapsedTime);
	}
	
	public int getElapsedTime() {
		return this.tracks.get(trackIndex).getElapsedTime();
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
