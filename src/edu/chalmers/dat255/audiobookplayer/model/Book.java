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

import edu.chalmers.dat255.audiobookplayer.constants.Constants;
import edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates;

/**
 * Represents a collection of Track objects. Null tracks are not allowed (and
 * will be ignored when added).
 * 
 * @author Marcus Parkkinen, Aki K&auml;kel&auml;
 * @version 0.6
 */

public final class Book implements IBookUpdates, Serializable {
	private static final String TAG = "Book.java";
	private static final String TRACK_INDEX_ILLEGAL = " Track index is illegal";

	private static final int NO_TRACK_SELECTED = Constants.Value.NO_TRACK_SELECTED;
	private static final long serialVersionUID = 2;

	private List<Track> tracks;
	private int selectedTrackIndex = NO_TRACK_SELECTED;
	private String author; // immutable
	private String title;
	private int duration;

	/**
	 * Used when no author is given.
	 * 
	 * @param title
	 */
	public Book(String title) {
		this(title, Constants.Message.NO_AUTHOR);
	}

	/**
	 * Creates an empty book with the given title and author.
	 */
	public Book(String title, String author) {
		tracks = new LinkedList<Track>();
		setSelectedBookTitle(title);

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

	/**
	 * Copy constructor.
	 * 
	 * @param original
	 */
	public Book(Book original) {
		this(original.getSelectedBookTitle(), original.getSelectedBookAuthor());

		// copy primitive member variables
		this.duration = original.duration;
		this.selectedTrackIndex = original.selectedTrackIndex;
		setAuthor(original.getSelectedBookAuthor());

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
	public void removeTrack(int index) throws IndexOutOfBoundsException {
		if (!isLegalTrackIndex(index)) {
			throw new IndexOutOfBoundsException(TAG + " removeTrack"
					+ TRACK_INDEX_ILLEGAL);
		}

		// remove the track and adjust the duration
		duration -= tracks.remove(index).getDuration();

		// check whether this was the last track
		if (tracks.size() == 0) {
			setSelectedTrackIndex(NO_TRACK_SELECTED);
		} else {
			if (index < selectedTrackIndex) {
				// adjust the index if we removed one earlier in the list
				selectedTrackIndex--;
			} else if (index == selectedTrackIndex) {
				// if we removed the selected one then mark the first
				selectedTrackIndex = 0;
			}
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

	public void swapTracks(int firstIndex, int secondIndex)
			throws IndexOutOfBoundsException {
		if (!isLegalTrackIndex(firstIndex) || !isLegalTrackIndex(secondIndex)) {
			throw new IndexOutOfBoundsException(TAG + " swapTracks"
					+ TRACK_INDEX_ILLEGAL);
		}
		Collections.swap(tracks, firstIndex, secondIndex);
	}

	public void moveTrack(int from, int to) throws IndexOutOfBoundsException {
		if (!isLegalTrackIndex(from) || !isLegalTrackIndex(to)) {
			throw new IndexOutOfBoundsException(TAG + " moveTrack"
					+ TRACK_INDEX_ILLEGAL);
		}
		if (from != to) {
			Track t = tracks.remove(from);
			tracks.add(to, t);
		}
	}

	public void setSelectedTrackIndex(int index)
			throws IndexOutOfBoundsException {
		if (index < -1 || index > this.tracks.size() + 1) {
			throw new IndexOutOfBoundsException(TAG
					+ " setSelectedTrackIndex with index out of bounds: "
					+ index + ", list size: " + this.tracks.size());
		}

		selectedTrackIndex = index;
	}

	public void setSelectedBookTitle(String title) {
		if (title == null) {
			throw new IndexOutOfBoundsException(TAG
					+ " setBookTitle to null title is illegal");
		}
		this.title = title;

	}

	public void updateBookDuration() {
		this.duration = 0;
		for (Track t : tracks) {
			this.duration += t.getDuration();
		}
	}

	/* End IBookUpdates */

	/* ITrackUpdates */

	public void setSelectedTrackElapsedTime(int newTime)
			throws IndexOutOfBoundsException {
		if (!isLegalTrackIndex(selectedTrackIndex)) {
			throw new IndexOutOfBoundsException(TAG
					+ " setSelectedTrackElapsedTime" + TRACK_INDEX_ILLEGAL);
		}
		this.tracks.get(selectedTrackIndex)
				.setSelectedTrackElapsedTime(newTime);
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
	public int getSelectedTrackDuration() {
		if (!isLegalTrackIndex(selectedTrackIndex)) {
			throw new IndexOutOfBoundsException(
					"getSelectedTrackDuration (illegal track index "
							+ selectedTrackIndex + ")");
		}
		return tracks.get(selectedTrackIndex).getDuration();
	}

	/**
	 * Returns the elapsed time of the selected track in the selected book.
	 * 
	 * @return elapsed time
	 */
	public int getSelectedTrackElapsedTime() {
		if (!isLegalTrackIndex(selectedTrackIndex)) {
			throw new IndexOutOfBoundsException(
					"getSelectedTrackElapsedTime (illegal track index "
							+ selectedTrackIndex + ")");
		}
		return this.tracks.get(selectedTrackIndex).getElapsedTime();
	}

	/**
	 * Gets the track path of the currently selected track.
	 * 
	 * @return
	 * @throws IndexOutOfBoundsException
	 *             If the track index is illegal (negative or larger than the
	 *             elements in the track list).
	 */
	public String getSelectedTrackPath() {
		if (!isLegalTrackIndex(selectedTrackIndex)) {
			throw new IndexOutOfBoundsException(
					"getSelectedTrackPath (illegal track index "
							+ selectedTrackIndex + ")");
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

	public String getSelectedBookTitle() {
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

	/**
	 * Returns the elapsed time of the current book.
	 * 
	 * @return
	 */
	public int getBookElapsedTime() {
		int bookElapsedTime = 0;

		// add the duration of all previous tracks
		for (int i = 0; i < this.selectedTrackIndex; i++) {
			bookElapsedTime += tracks.get(i).getDuration();
		}

		// add the elapsed time of the current track
		bookElapsedTime += tracks.get(this.selectedTrackIndex).getElapsedTime();

		return bookElapsedTime;
	}

	/**
	 * Returns the duration (in ms) of the track located at the specified index.
	 * 
	 * @param index
	 *            Index of the track
	 * @return int duration (ms)
	 */
	public int getTrackDurationAt(int index) {
		if (!isLegalTrackIndex(index)) {
			throw new IndexOutOfBoundsException(
					"getTrackDurationAt (illegal track index "
							+ selectedTrackIndex + ")");
		}
		return tracks.get(index).getDuration();
	}

	/**
	 * Returns the title of the track.
	 * 
	 * @return String title
	 */
	public String getTrackTitle() {
		if (!isLegalTrackIndex(selectedTrackIndex)) {
			throw new IndexOutOfBoundsException(
					"getTrackTitle (illegal track index " + selectedTrackIndex
							+ ")");
		}
		return this.tracks.get(selectedTrackIndex).getTrackTitle();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + duration;
		result = prime * result + selectedTrackIndex;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((tracks == null) ? 0 : tracks.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Book)) {
			return false;
		}
		Book other = (Book) obj;
		if (author == null) {
			if (other.author != null) {
				return false;
			}
		} else if (!author.equals(other.author)) {
			return false;
		}
		if (duration != other.duration) {
			return false;
		}
		if (selectedTrackIndex != other.selectedTrackIndex) {
			return false;
		}
		if (title == null) {
			if (other.title != null) {
				return false;
			}
		} else if (!title.equals(other.title)) {
			return false;
		}
		if (tracks == null) {
			if (other.tracks != null) {
				return false;
			}
		} else if (!tracks.equals(other.tracks)) {
			return false;
		}
		return true;
	}

	public String getSelectedBookAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void addTag(int time) throws IndexOutOfBoundsException {
		if (!isLegalTrackIndex(this.selectedTrackIndex)) {
			throw new IndexOutOfBoundsException(TAG + " addTag"
					+ TRACK_INDEX_ILLEGAL);
		}

		this.tracks.get(selectedTrackIndex).addTag(time);
	}

	public void removeTagAt(int tagIndex) throws IndexOutOfBoundsException {
		if (!isLegalTrackIndex(this.selectedTrackIndex)) {
			throw new IndexOutOfBoundsException(TAG + " removeTagAt"
					+ TRACK_INDEX_ILLEGAL);
		}

		this.tracks.get(selectedTrackIndex).removeTagAt(tagIndex);
	}

	public int[] getTagTimes() throws IndexOutOfBoundsException {
		if (!isLegalTrackIndex(this.selectedTrackIndex)) {
			throw new IndexOutOfBoundsException(TAG + " getTagTimes"
					+ TRACK_INDEX_ILLEGAL);
		}

		return this.tracks.get(selectedTrackIndex).getTagTimes();
	}

	/*
	 * TODO(!!): FOR TESTING PURPOSES ONLY! REMOVE
	 */
	public Object getSelectedTrack() {
		// TODO Auto-generated method stub
		return null;
	}

}
