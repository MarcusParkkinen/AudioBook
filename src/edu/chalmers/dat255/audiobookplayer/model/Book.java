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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import edu.chalmers.dat255.audiobookplayer.constants.Constants;
import edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates;

/**
 * Represents a collection of Track objects. Null tracks are not allowed (and
 * will be ignored when added).
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.6
 */
public final class Book implements IBookUpdates, Serializable {
	private static final String TAG = "Book.java";

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

	/**
	 * Creates a book from the referenced collection of Tracks.
	 * 
	 * @param col
	 *            Collection of tracks.
	 * @param title
	 *            Title of the book.
	 */
	public Book(Collection<Track> col, String title) {
		this(col, title, Constants.Message.NO_AUTHOR);
	}

	/* IBookUpdates */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#removeTrack
	 * (int)
	 */
	public void removeTrack(int trackIndex) {
		checkTrackIndexLegal(trackIndex);

		// remove the track and adjust the duration
		duration -= tracks.remove(trackIndex).getDuration();

		// check whether this was the last track
		if (tracks.size() == 0) {
			setSelectedTrackIndex(NO_TRACK_SELECTED);
		} else {
			if (trackIndex < selectedTrackIndex) {
				// adjust the index if we removed one earlier in the list
				selectedTrackIndex--;
			} else if (trackIndex == selectedTrackIndex) {
				// if we removed the selected one then mark the first
				selectedTrackIndex = 0;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#addTrack(
	 * edu.chalmers.dat255.audiobookplayer.model.Track)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#swapTracks
	 * (int, int)
	 */
	public void swapTracks(int firstIndex, int secondIndex) {
		checkTrackIndexLegal(firstIndex);
		checkTrackIndexLegal(secondIndex);

		Collections.swap(tracks, firstIndex, secondIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#moveTrack
	 * (int, int)
	 */
	public void moveTrack(int fromIndex, int toIndex) {
		checkTrackIndexLegal(fromIndex);
		checkTrackIndexLegal(toIndex);

		Track t = tracks.remove(fromIndex);
		tracks.add(toIndex, t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#
	 * setSelectedTrackIndex(int)
	 */
	public void setSelectedTrackIndex(int index) {
		if (index < -1 || index > this.tracks.size() + 1) {
			throw new IndexOutOfBoundsException(TAG
					+ " setSelectedTrackIndex with index out of bounds: "
					+ index + ", list size: " + this.tracks.size());
		}

		selectedTrackIndex = index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#
	 * setSelectedBookTitle(java.lang.String)
	 */
	public void setSelectedBookTitle(String title) {
		if (title == null) {
			throw new IllegalArgumentException(TAG
					+ " setBookTitle to null title is illegal");
		}
		this.title = title;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#
	 * updateBookDuration()
	 */
	public void updateSelectedBookDuration() {
		this.duration = 0;
		for (Track t : tracks) {
			this.duration += t.getDuration();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#
	 * getSelectedBookAuthor()
	 */
	public String getSelectedBookAuthor() {
		return author;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.IBookUpdates#
	 * getSelectedBookTitle()
	 */
	public String getSelectedBookTitle() {
		return title;
	}

	/* End IBookUpdates */

	/**
	 * @param author
	 *            The author to set to.
	 */
	private void setAuthor(String author) {
		this.author = author;
	}

	/* ITrackUpdates */

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.chalmers.dat255.audiobookplayer.interfaces.ITrackUpdates#
	 * setSelectedTrackElapsedTime(int)
	 */
	public void setSelectedTrackElapsedTime(int newTime) {
		checkTrackIndexLegal(selectedTrackIndex);

		this.tracks.get(selectedTrackIndex)
				.setSelectedTrackElapsedTime(newTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.ITrackUpdates#addTag(int)
	 */
	public void addTag(int time) {
		checkTrackIndexLegal(selectedTrackIndex);

		this.tracks.get(selectedTrackIndex).addTag(time);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.chalmers.dat255.audiobookplayer.interfaces.ITrackUpdates#removeTagAt
	 * (int)
	 */
	public void removeTagAt(int tagIndex) {
		checkTrackIndexLegal(selectedTrackIndex);

		this.tracks.get(selectedTrackIndex).removeTagAt(tagIndex);
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
		checkTrackIndexLegal(selectedTrackIndex);

		return tracks.get(selectedTrackIndex).getDuration();
	}

	/**
	 * Returns the elapsed time of the selected track in the selected book.
	 * 
	 * @return elapsed time
	 */
	public int getSelectedTrackElapsedTime() {
		checkTrackIndexLegal(selectedTrackIndex);

		return this.tracks.get(selectedTrackIndex).getElapsedTime();
	}

	/**
	 * Gets the track path of the currently selected track.
	 * 
	 * @return
	 */
	public String getSelectedTrackPath() {
		// TODO: checkTrackLegal...

		return getTrackPathAt(selectedTrackIndex);
	}

	public String getTrackPathAt(int trackIndex) {
		// TODO: check legal

		return tracks.get(trackIndex).getTrackPath();
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
	 * Get a list of all the tracktitles of the book.
	 * 
	 * @return The title.
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
	 * @param trackIndex
	 *            Index of the track
	 * @return int duration (ms)
	 */
	public int getTrackDurationAt(int trackIndex) {
		checkTrackIndexLegal(trackIndex);

		return tracks.get(trackIndex).getDuration();
	}

	/**
	 * Returns the title of the track.
	 * 
	 * @return String title
	 */
	public String getTrackTitle() {
		checkTrackIndexLegal(selectedTrackIndex);

		return this.tracks.get(selectedTrackIndex).getTrackTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(tracks).append(selectedTrackIndex)
				.append(author).append(title).append(duration).toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Book) {
			final Book other = (Book) obj;
			return new EqualsBuilder().append(tracks, other.tracks)
					.append(selectedTrackIndex, other.selectedTrackIndex)
					.append(author, other.author).append(title, other.title)
					.append(duration, other.duration).isEquals();
		} else {
			return false;
		}
	}

	/**
	 * Returns all tag times.
	 * 
	 * @return Array of all tag times.
	 */
	public int[] getTagTimes() {
		checkTrackIndexLegal(selectedTrackIndex);

		return this.tracks.get(selectedTrackIndex).getTagTimes();
	}

	/*
	 * FOR TESTING PURPOSES ONLY
	 */

	/**
	 * Returns a reference to the currently selected track. NOTE: Only for
	 * testing purposes.
	 * 
	 * @return the currently selected track
	 */
	public Track getSelectedTrack() {
		return tracks.get(selectedTrackIndex);
	}

	/*
	 * END TESTING PURPOSES ONLY
	 */

	/**
	 * Gets the track title of a given track index.
	 * 
	 * @param trackIndex
	 *            Index to get the title from.
	 * @return Track title at given index.
	 */
	public String getTrackTitleAt(int trackIndex) {
		if (trackIndex >= 0 && trackIndex < tracks.size()) {
			return tracks.get(trackIndex).getTrackTitle();
		}
		return null;
	}

	private void checkTrackIndexLegal(int trackIndex) {
		if (!isLegalTrackIndex(trackIndex)) {
			throw new IndexOutOfBoundsException("Track index is illegal: "
					+ trackIndex);
		}
	}

}
