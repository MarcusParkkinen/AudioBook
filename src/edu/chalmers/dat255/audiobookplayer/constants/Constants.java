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

package edu.chalmers.dat255.audiobookplayer.constants;

/**
 * Contains hard-coded values.
 * 
 * @author Aki Käkelä
 * @version 0.3
 */
public final class Constants {
	/**
	 * Holds unique property names ensuring that property names match if
	 * intended to do so and that property name changing is simple.
	 * 
	 * @author Aki Käkelä, Marcus Parkkinen
	 * 
	 */
	public static final class Event {

		// Bookshelf update events
		public static final String BOOKSHELF_UPDATED = "!bookshelfUpdated";

		// Book update events
		/**
		 * Called when books are removed, added, or moved.
		 */
		public static final String BOOK_LIST_CHANGED = "!booksChanged";
		/**
		 * Called when tracks are removed, added, or moved.
		 */
		public static final String TRACK_LIST_CHANGED = "!tracksChanged";

		/**
		 * Called when a book is selected.
		 */
		public static final String BOOK_SELECTED = "!bookSelected";
		/**
		 * Called when the title of a book is changed.
		 */
		public static final String BOOK_TITLE_CHANGED = "!bookTitleChanged";

		/**
		 * Called when the elapsed time of a track (usually, or probably
		 * exclusively, the currently selected track in the currently selected
		 * book) has changed.
		 */
		public static final String ELAPSED_TIME_CHANGED = "!elapsedTimeChanged";
		/**
		 * Called when the track index of a book is changed.
		 */
		public static final String TRACK_INDEX_CHANGED = "!trackIndexChanged";

		/*
		 * Unimplemented properties below.
		 */
		/**
		 * Called when a tag is added to a track.
		 */
		public static final String TAG_ADDED = "!tagAdded";
		/**
		 * Called when a tag is removed from a track.
		 */
		public static final String TAG_REMOVED = "!tagRemoved";

	}

	/**
	 * Contains String-type reference used in serialization.
	 * 
	 * @author Marcus Parkkinen
	 * 
	 */
	public static final class Reference {
		public static final String BOOKSHELF = "@bookshelf";
	}

	/**
	 * Contains hard-coded integer values.
	 * 
	 * @author Aki Käkelä
	 * 
	 */
	public static final class Value {
		/**
		 * The frequency for GUI updates. Lower value means updates more often.
		 */
		public static final int UPDATE_FREQUENCY = 200;

		/**
		 * The number of zones in a seek bar; the possible values its progress
		 * can take.
		 * <p>
		 * A higher value means smaller visual jumps when the bars are updated,
		 * thus making it smoother.
		 */
		public static final int NUMBER_OF_SEEK_BAR_ZONES = 500; // default 100

		/**
		 * The index that the system should interpret as "none selected"
		 */
		public static final int NO_BOOK_SELECTED = -1;
		/**
		 * The index that the system should interpret as "none selected"
		 */
		public static final int NO_TRACK_SELECTED = -1;

		/**
		 * The maximum title length allowed in the GUI before the text is
		 * truncated (cut off).
		 */
		public static final int MAX_TITLE_CHARACTER_LENGTH = 30;

	}

	/**
	 * Contains GUI-messages.
	 * 
	 * @author Aki Käkelä
	 * 
	 */
	public static final class Message {
		/**
		 * Fixing the 'magic number' complaints in Sonar.
		 */
		public static final String NOT_AVAILABLE = "N/A";

		public static final String NO_AUDIO_FILES_FOUND = "No audio files found.";

		public static final String NO_BOOK_TITLE = NOT_AVAILABLE;
		public static final String NO_TRACK_TITLE = NOT_AVAILABLE;

		public static final String NO_TRACK_ELAPSED_TIME = NOT_AVAILABLE;
		public static final String NO_BOOK_ELAPSED_TIME = NOT_AVAILABLE;

		public static final String NO_TRACK_DURATION = NOT_AVAILABLE;
		public static final String NO_BOOK_DURATION = NOT_AVAILABLE;

		public static final String NO_AUTHOR = NOT_AVAILABLE;

		public static final String NO_TRACK_SELECTED = "--";

		public static final String NO_TRACKS_FOUND = "None";

		public static final String TRACK_INDEX_ERROR = "ERROR";
	}

}