/**
 *  This work is licensed under the Creative Commons Attribution-NonCommercial-
 *  NoDerivs 3.0 Unported License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to 
 *  Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 
 *  94041, USA.
 * 
 *  Use of this work is permitted only in accordance with license rights granted.
 *  Materials provided "AS IS"; no representations or warranties provided.
 * 
 *  Copyright © 2012 Marcus Parkkinen, Aki Käkelä, Fredrik Åhs.
 **/

package edu.chalmers.dat255.audiobookplayer.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates;
import edu.chalmers.dat255.audiobookplayer.interfaces.ITrackUpdates;

/**
 * Represents a collection of Track objects. Null tracks are not allowed (and
 * will be ignored when added).
 * 
 * @author Marcus Parkkinen, Aki K�kel�
 * @version 0.6
 */

public final class Book implements ITrackUpdates, IBookUpdates, Serializable {
	private static final String TAG = "Book.java";
	private static final int NO_TRACK_SELECTED = -1;
	private static final long serialVersionUID = 2;

	private LinkedList<Track> tracks;
	private int selectedTrackIndex = NO_TRACK_SELECTED;
	private String author; // immutable
	private String title;
	private int duration;
	private LinkedList<Tag> tags;

	/* To be implemented later: */
	// private Bookmark bookmark;

	// private Stats stats;

	// TODO: only used by test.

	/**
	 * Used when no author is given.
	 * 
	 * @param title
	 */
	public Book(String title) {
		this(title, "N/A");
	}

	// --

	/**
	 * Creates an empty book with the given title and author.
	 */
	public Book(String title, String author) {
		tracks = new LinkedList<Track>();
		setBookTitle(title);

		// when a book is created, it should be ensured that the author is
		// appropriate.
		this.author = author;
	}

	/**
	 * Creates a book from the referenced collection of Tracks.
	 * 
	 * @param col
	 *            The collection of tracks.
	 * @param title
	 *            The title of the book.
	 * @param author
	 *            The author of the book.
	 */
	public Book(Collection<Track> col, String title, String author) {
		this(title, author);

		for (Track t : col) {
			if (t != null) {
				tracks.add(t);
				duration += t.getDuration();
			}
		}

		// adjust the track index now that we have tracks
		selectedTrackIndex = 0;
	}

	// TODO: only used by test.
	public Book(Collection<Track> col, String title) {
		this(col, title, "N/A");
	}

	//

	/**
	 * Copy constructor.
	 * 
	 * @param original
	 */
	public Book(Book original) {
		this(original.getBookTitle(), original.getAuthor());

		// copy primitive member variables
		this.duration = original.duration;
		this.selectedTrackIndex = original.selectedTrackIndex;
		setAuthor(original.getAuthor());

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
		if (isLegalTrackIndex(index)) {

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

	public void swapTracks(int firstIndex, int secondIndex) {
		if (isLegalTrackIndex(firstIndex) && isLegalTrackIndex(secondIndex)
				&& firstIndex < secondIndex) {
			Collections.swap(tracks, firstIndex, secondIndex);
		} else {
			Log.e(TAG,
					"attempting to move a track from/to illegal index. Skipping operation.");
		}
	}

	public void moveTrack(int from, int to) {
		if (isLegalTrackIndex(from) && isLegalTrackIndex(to) && from != to) {
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

	public void setBookTitle(String title) throws IllegalArgumentException {
		if (title != null) {
			this.title = title;
		} else {
			throw new IllegalArgumentException(
					" Trying to set illegal title to book. Skipping operation");
		}

	}

	public void updateBookDuration() {
		this.duration = 0;
		for (Track t : tracks) {
			this.duration += t.getDuration();
		}
	}

	/* End IBookUpdates */

	/* ITrackUpdates */

	public void setSelectedTrackElapsedTime(int newTime) {
		if (isLegalTrackIndex(selectedTrackIndex)) {
			this.tracks.get(selectedTrackIndex).setSelectedTrackElapsedTime(
					newTime);
		}
	}

	/* End ITrackUpdates */

	/**
	 * Checks whether a provided index is within the legal bounds of the list of
	 * tracks.
	 * 
	 * @param index
	 *            Index to check.
	 * @return True if the given index is within bounds of the track list.
	 */
	public boolean isLegalTrackIndex(int index) {
		// Log.d(TAG, (index >= 0 && index < tracks.size()) ? "legal" :
		// "illegal"
		// + " index: 0 <= " + index + " < " + tracks.size());
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
	public int getSelectedTrackIndex() {
		return this.selectedTrackIndex;
	}

	/**
	 * The duration of the current track in milliseconds.
	 * 
	 * @return
	 */
	public int getSelectedTrackDuration() throws IllegalArgumentException {
		if (!isLegalTrackIndex(selectedTrackIndex)) {
			throw new IllegalArgumentException(
					"Tried to get selected track duration when index is illegal.");
		}
		return tracks.get(selectedTrackIndex).getDuration();
	}

	/**
	 * Returns the elapsed time of the selected track in the selected book.
	 * 
	 * @return elapsed time
	 */
	public int getSelectedTrackElapsedTime() throws IllegalArgumentException {
		if (!isLegalTrackIndex(selectedTrackIndex)) {
			throw new IllegalArgumentException(
					"Tried to get selected track elapsed time when index is illegal.");
		}
		return this.tracks.get(selectedTrackIndex).getElapsedTime();
	}

	/**
	 * Gets the track path of the currently selected track.
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 *             If the track index is illegal (negative or larger than the
	 *             elements in the track list).
	 */
	public String getSelectedTrackPath() throws IllegalArgumentException {
		if (!isLegalTrackIndex(selectedTrackIndex)) {
			throw new IllegalArgumentException(
					"Tried to get selected track path when index is illegal.");
		}
		return tracks.get(selectedTrackIndex).getTrackPath();
	}

	/**
	 * Returns a list containing references to all tracks contained in this
	 * book.
	 * 
	 * @return the list
	 */
	public List<String> getTrackPaths() {
		List<String> paths = new LinkedList<String>();
		for (Track t : tracks) {
			paths.add(t.getTrackPath());
		}
		return paths;
	}

	/**
	 * Get the title of the book.
	 * 
	 * @return Title
	 */
	public String getBookTitle() {
		return title;
	}

	/*
	 * Get a list of all the tracktitles of the book.
	 * 
	 * @return Title
	 */
	public List<String> getTrackTitles() {
		List<String> trackTitles = new LinkedList<String>();
		for (Track t : tracks) {
			trackTitles.add(t.getTrackTitle());
		}
		return trackTitles;
	}

	/**
	 * Gets the duration of the book.
	 * 
	 * @return duration (ms)
	 */
	public int getDuration() {
		return duration;
	}

	// TODO: not needed, tests rely on it
	/**
	 * Gets the currently selected track.
	 * 
	 * @return the selected track
	 */
	public Track getSelectedTrack() {
		if (!isLegalTrackIndex(selectedTrackIndex))
			throw new IllegalArgumentException(
					"Tried to get selected track when index is illegal.");
		return this.tracks.get(selectedTrackIndex);
	}

	// --

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
		if (!isLegalTrackIndex(selectedTrackIndex))
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
		if (!isLegalTrackIndex(selectedTrackIndex))
			throw new IllegalArgumentException(
					"Tried to get selected track title when index is illegal.");
		return this.tracks.get(selectedTrackIndex).getTrackTitle();
	}

	/**
	 * Convenience method.
	 * <p>
	 * Sets the selected track index to '-1', which means unselected.
	 */
	private void deselectTrack() {
		selectedTrackIndex = NO_TRACK_SELECTED;
	}

	public void addTagToCurrentBook(int time) {
		addTagTo(this.selectedTrackIndex, time);
	}

	public void addTagTo(int index, int time) {
		this.tags.add(new Tag(time));
	}

	public void removeTag(int index) {
		if (isLegalTagIndex(index))
			this.tags.remove(index);
	}

	private boolean isLegalTagIndex(int index) {
		return index >= 0 && index < tags.size();
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void addTag(int time) {
		// TODO Auto-generated method stub

	}

	public void removeTag() {
		// TODO Auto-generated method stub

	}

	public void removeTagAt(int tagIndex) {
		// TODO Auto-generated method stub

	}

}
