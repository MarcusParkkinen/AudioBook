package edu.chalmers.dat255.audiobookplayer.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates;
import edu.chalmers.dat255.audiobookplayer.interfaces.ITrackUpdates;

import android.util.Log;

/**
 * Represents a collection of Track objects that collectively form a book. Null
 * tracks are not allowed (and will be ignored when added).
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.6
 */

public final class Book implements ITrackUpdates, IBookUpdates {
	private static final String TAG = "Book.java";
	private static final int NO_TRACK_SELECTED = -1;

	private LinkedList<Track> tracks;
	private int selectedTrackIndex = NO_TRACK_SELECTED;
	private String title;
	private int duration;

	/* To be implemented later: */
	// private Bookmark bookmark;
	private LinkedList<Tag> tags;

	// private Stats stats;

	/**
	 * Create an empty book.
	 */
	public Book(String title) {
		tracks = new LinkedList<Track>();
		setBookTitle(title);
	}

	/**
	 * Create a book from the referenced collection of Tracks.
	 * 
	 * @param c
	 *            A collection containing Track instances.
	 */
	public Book(Collection<Track> c, String title) {
		this(title);

		for (Track t : c) {
			if (t != null) {
				tracks.add(t);
				duration += t.getDuration();
			}
		}

		// adjust the track index now that we have tracks
		selectedTrackIndex = 0;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param original
	 */
	public Book(Book original) {
		this(original.getTitle());

		// copy primitive member variables
		this.duration = original.duration;
		this.selectedTrackIndex = original.selectedTrackIndex;

		// also create deep copies of the tracks
		for (Track t : original.tracks) {
			this.tracks.add(new Track(t));
		}
	}

	/* IBookUpdates */
	/**
	 * Remove a track from the book.
	 * 
	 * @param int index >= 0
	 */
	public void removeTrack(int index) {
		if (isLegalIndex(index)) {

			// remove the track and adjust the duration
			duration -= tracks.remove(index).getDuration();

			// check whether this was the last track
			if (tracks.size() == 0) {
				deselectTrack();
			} else {
				if (index < selectedTrackIndex) {
					// adjust the index if we removed one earlier in the list
					selectedTrackIndex--;
				} else if (index == selectedTrackIndex) {
					// if we removed the selected one then mark the first
					selectedTrackIndex = 0;
				}
			}

		} else {
			Log.e(TAG,
					"attempting to remove a track from an illegal index. Skipping operation.");
		}
	}

	/**
	 * Add a track to the list.
	 * 
	 * @param Track
	 *            track to add
	 */
	public void addTrack(Track t) {
		if (t != null) {
			// add the track
			tracks.add(t);

			// adjust the duration
			duration += t.getDuration();

			if (tracks.size() == 1) {
				selectedTrackIndex = 0;
			}
		}
	}

	/**
	 * Swap the position of two tracks in the list.
	 * 
	 * @param int first index
	 * @param int second index
	 */
	public void swap(int firstIndex, int secondIndex) {
		if (isLegalIndex(firstIndex) && isLegalIndex(secondIndex)
				&& firstIndex < secondIndex) {
			Collections.swap(tracks, firstIndex, secondIndex);
		} else {
			Log.e(TAG,
					"attempting to move a track from/to illegal index. Skipping operation.");
		}
	}

	/**
	 * Move a track from a given index to a given index. Indices inbetween will
	 * be adjusted.
	 * 
	 * @param int from index
	 * @param int to index
	 */
	public void moveTrack(int from, int to) {
		if (isLegalIndex(from) && isLegalIndex(to) && from != to) {
			Track t = tracks.remove(from);
			tracks.add(to, t);
		} else {
			Log.e(TAG,
					" attempting to move a track from/to illegal index. Skipping operation.");
		}
		// TODO: test
		// Collections.rotate(tracks.subList(from, to + 1), -1);
	}

	// public void setBookmark(int trackIndex, int time) { }
	// public void setTag(int trackIndex, int time) { }

	/**
	 * Set the index of the currently selected track.
	 * 
	 * @param int new index
	 */

	public void setSelectedTrackIndex(int index) {
		Log.d(TAG, "Attempting to set trackIndex to " + index + " (listSize: "
				+ this.tracks.size() + ")");
		if (index >= 0) {
			if (index < tracks.size()) {
				selectedTrackIndex = index;
				return;
			}
			// the track list is completed, so deselect the track.
			deselectTrack();
			Log.d(TAG, "New trackIndex: " + selectedTrackIndex);
		} else {
			Log.e(TAG,
					" attempting to select a track at an illegal index. Skipping operation.");
		}
	}

	/* End IBookUpdates */

	/* ITrackUpdates */

	public void setElapsedTime(int newTime) {
		if (isLegalIndex(selectedTrackIndex)) {
			this.tracks.get(selectedTrackIndex).setElapsedTime(newTime);
		}
	}

	/* End ITrackUpdates */

	/**
	 * Private method that checks whether a provided index is within the legal
	 * bounds of the list of tracks.
	 * 
	 * @param index
	 *            index
	 * @return boolean
	 */
	public boolean isLegalIndex(int index) {
		Log.d(TAG, (index >= 0 && index < tracks.size()) ? "legal" : "illegal"
				+ " index: 0 <= " + index + " < " + tracks.size());
		return index >= 0 && index < tracks.size();
	}

	/**
	 * Returns the number of elements (tracks) in the book.
	 * 
	 * @return a Number of elements (tracks).
	 */
	public int getNumberOfTracks() {
		return tracks.size();
	}

	/**
	 * Returns the index of the currently selected track.
	 * 
	 * @return int index
	 */
	public int getSelectedTrackIndex() throws IllegalArgumentException {
		return this.selectedTrackIndex;
	}

	/**
	 * The duration of the current track in milliseconds.
	 * 
	 * @return
	 */
	public int getTrackDuration() throws IllegalArgumentException {
		if (!isLegalIndex(selectedTrackIndex))
			throw new IllegalArgumentException(
					"Tried to get selected track duration when index is illegal.");
		return tracks.get(selectedTrackIndex).getDuration();
	}

	/**
	 * Returns elapsed time of the book.
	 * 
	 * @return elapsed time
	 */
	public int getElapsedTime() {
		if (!isLegalIndex(selectedTrackIndex))
			throw new IllegalArgumentException(
					"Tried to get selected track elapsed time when index is illegal.");
		return this.tracks.get(selectedTrackIndex).getElapsedTime();
	}

	/**
	 * Returns a list containing references to all tracks contained in this
	 * book.
	 * 
	 * @return the list
	 */
	public List<String> getPaths() {
		List<String> paths = new LinkedList<String>();
		for (Track t : tracks) {
			paths.add(t.getTrackPath());
		}
		return paths;
	}

	/**
	 * Gets the track path of the currently selected track.
	 * 
	 * @return
	 */
	public String getSelectedTrackPath() {
		if (!isLegalIndex(selectedTrackIndex))
			throw new IllegalArgumentException(
					"Tried to get selected track path when index is illegal.");
		return tracks.get(selectedTrackIndex).getTrackPath();
	}

	/**
	 * Get the title of the book.
	 * 
	 * @return Title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the title of the book.
	 * 
	 * @param String
	 *            title
	 */
	public void setBookTitle(String title) throws IllegalArgumentException {
		if (title != null && title.length() >= 1) {
			this.title = title;
		} else {
			throw new IllegalArgumentException(
					" Trying to set illegal title to book. Skipping operation");
		}

	}

	/**
	 * Update the duration of the book.
	 * 
	 */
	public void updateBookDuration() {
		this.duration = 0;
		for (Track t : tracks) {
			this.duration += t.getDuration();
		}
	}

	/**
	 * Get the duration of the book.
	 * 
	 * @return duration (ms)
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Get the currently selected track.
	 * 
	 * @return the selected track
	 */
	public Track getCurrentTrack() {
		if (!isLegalIndex(selectedTrackIndex))
			throw new IllegalArgumentException(
					"Tried to get selected track when index is illegal.");
		return this.tracks.get(selectedTrackIndex);
	}

	/**
	 * Returns the elapsed time of the current book.
	 * 
	 * @return
	 */
	public int getBookElapsedTime() {
		int bookElapsedTime = 0;
		int i = 0;
		while (i < this.selectedTrackIndex) {
			bookElapsedTime += tracks.get(i).getDuration();
			i++;
		}
		bookElapsedTime += tracks.get(i).getElapsedTime();

		return bookElapsedTime;
	}

	/**
	 * @param index
	 * @return
	 */
	public int getTrackDurationAt(int index) {
		if (!isLegalIndex(selectedTrackIndex))
			throw new IllegalArgumentException(
					"Tried to get 'track duration at' when index is illegal.");
		return tracks.get(index).getDuration();
	}

	/**
	 * Returns the title of the track.
	 * 
	 * @return
	 */
	public String getTrackTitle() {
		if (!isLegalIndex(selectedTrackIndex))
			throw new IllegalArgumentException(
					"Tried to get selected track title when index is illegal.");
		return this.tracks.get(selectedTrackIndex).getTrackTitle();
	}

	public void addTagToCurrentBook(int time) {
		addTagTo(this.selectedTrackIndex, time);
	}

	public void addTagTo(int index, int time) {
		this.tags.add(new Tag(index, time));
	}

	public void removeTag(int index) {
		if (isLegalTagIndex(index))
			this.tags.remove(index);
	}

	private boolean isLegalTagIndex(int index) {
		return index >= 0 && index < tags.size();
	}

	private void deselectTrack() {
		selectedTrackIndex = NO_TRACK_SELECTED;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + duration;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + selectedTrackIndex;
		result = prime * result + ((tracks == null) ? 0 : tracks.hashCode());
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
		Book other = (Book) obj;
		if (duration != other.duration)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (selectedTrackIndex != other.selectedTrackIndex)
			return false;
		if (tracks == null) {
			if (other.tracks != null)
				return false;
		} else if (!tracks.equals(other.tracks))
			return false;
		return true;
	}

}
