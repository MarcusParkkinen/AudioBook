package edu.chalmers.dat255.audiobookplayer.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

/**
 * Represents a collection of Track objects that collectively form a book. Null
 * tracks are not allowed (and will be ignored when added).
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.4
 */

public class Book implements ITrackUpdates, IBookUpdates {
	private static final String TAG = "Book.java";

	private LinkedList<Track> tracks;
	private int trackIndex; // When trackIndex == -1, the book is finished
	private String title;
	private int duration;

	/*
	 * To be implemented later: private Bookmark bookmark; private Tag[] tags;
	 * private Stats stats;
	 */

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

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 */
	public Book(Book other) {
		this();
		this.duration = other.duration;
		this.title = other.title;
		this.trackIndex = other.trackIndex;
		for (Track t: tracks) {
			this.tracks.add(new Track(t));
		}
	}
	
	public Object clone() throws CloneNotSupportedException {
		Book clone = (Book) super.clone();
		// Immutable values
		clone.duration = this.duration;
		clone.title = this.title;
		clone.trackIndex = this.trackIndex;
		
		// Shallow copy of the object of type Track
		clone.tracks = (LinkedList<Track>) clone.tracks;
		clone.tracks.clear();
		for (Track t : tracks) {
			Track copyTrack = (Track)t.clone();
			clone.addTrackTo(clone.tracks.size(), copyTrack);
		}
	    return clone;
	  }

	/* IBookUpdates */
	public void removeTrack(int index) {
		if (tracks.size() < index) {
			tracks.remove(index);
			if (index < trackIndex)
				trackIndex--;
		}
	}

	public void addTrackTo(int index, Track t) {
		if (t != null && index <= tracks.size()) {
			// index sent from GUI should never be < 0
			tracks.add(index, t);
			// if we removed a track earlier in the list, compensate:
			if (index < trackIndex)
				trackIndex++; // this will not call TRACK_INDEX_CHANGED_BY_USER
		}
	}

	public void swap(int firstIndex, int secondIndex) {
		if (firstIndex < tracks.size() && secondIndex < tracks.size()
				&& firstIndex != secondIndex) {
			Collections.swap(tracks, firstIndex, secondIndex);
		} else {
			Log.e(TAG,
					"attempting to move a track from/to illegal index. Skipping operation.");
		}
	}

	public void moveTrack(int from, int to) {
		if (from < tracks.size() && to < tracks.size()) {
			tracks.add(to, tracks.remove(from));
		} else {
			Log.e(TAG,
					" attempting to move a track from/to illegal index. Skipping operation.");
		}

		// TODO: test
		// Collections.rotate(tracks.subList(from, to + 1), -1);
	}

	// public void setBookmark(int trackIndex, int time) { }
	// public void setTag(int trackIndex, int time) { }

	public void setCurrentTrackIndex(int index) {
		boolean loop = false; // TODO: change in settings?
		// Log.d(TAG, "Changing trackIndex from " + trackIndex + " to " +
		// index);
		trackIndex = index;
		if (trackIndex < 0) {
			if (loop) {
				trackIndex = this.tracks.size() - 1; // last index
			} else {
				trackIndex = -1; // end
			}
		}
		if (trackIndex > tracks.size() - 1) {
			if (loop) {
				trackIndex = 0; // first index
			} else {
				trackIndex = -1; // end; book finished
			}
		}
		// TODO: test
		// this.trackIndex = index % this.tracks.size();
		Log.d(TAG, "new index: " + trackIndex);
	}

	public void setBookTitle(String newTitle) {
		if (newTitle != null)
			this.title = newTitle;
	}

	/* End IBookUpdates */

	/* ITrackUpdates */
	public void setElapsedTime(int elapsedTime) {
		// Log.d(TAG, "Track time changing from " + elapsedTime + " to " +
		// time);
		this.tracks.get(trackIndex).setElapsedTime(elapsedTime);
	}

	/* End ITrackUpdates */

	/**
	 * Returns the number of elements (tracks) in the book.
	 * 
	 * @return a Number of elements (tracks).
	 */
	public int getNumberOfTracks() {
		return tracks.size();
	}

	/**
	 * @return the index of the path either currently open or ready to be opened
	 *         by the player
	 */
	public int getCurrentTrackIndex() {
		return this.trackIndex;
	}

	/**
	 * The track duration in milliseconds.
	 * 
	 * @return
	 */
	public int getTrackDuration() {
		return tracks.get(trackIndex).getDuration();
	}

	/**
	 * @return
	 */
	public int getElapsedTime() {
		return this.tracks.get(trackIndex).getElapsedTime();
	}

	/**
	 * Returns a list containing references to all tracks contained in this
	 * book.
	 * 
	 * @return List<String> the list
	 */
	public List<String> getPaths() {
		List<String> paths = new LinkedList<String>();
		for (Track t : tracks) {
			paths.add(t.getTrackPath());
		}
		return paths;
	}

	public String getCurrentTrackPath() {
		return tracks.get(trackIndex).getTrackPath();
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public void updateBookDuration() {
		this.duration = 0;
		for (Track t : tracks) {
			this.duration += t.getDuration();
		}
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public Track getCurrentTrack() {
		return this.tracks.get(trackIndex);
	}

}
