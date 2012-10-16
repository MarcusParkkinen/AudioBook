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
 *  Copyright Â© 2012 Marcus Parkkinen, Aki KÃ¤kelÃ¤, Fredrik Ã…hs.
 **/

package edu.chalmers.dat255.audiobookplayer.constants;

/**
 * Holds unique property names ensuring that property names match if intended to
 * do so and that property name changing is simple.
 * <p>
 * Also contains hard-coded fields.
 * 
 * @author Aki Käkelä, Marcus Parkkinen
 */
public final class Constants {
	/**
	 * 
	 * @author Aki Käkelä, Marcus Parkkinen
	 * 
	 */
	public static final class Event {
		public static final String BOOKSHELF_UPDATED = "!bookshelfUpdated";

		public static final String BOOK_SELECTED = "!bookSelected";
		public static final String BOOK_ADDED = "!bookAdded";
		public static final String BOOK_REMOVED = "!bookRemoved";
		public static final String BOOK_MOVED = "!bookMoved";

		public static final String BOOK_TITLE_CHANGED = "!bookTitleChanged";
		public static final String BOOK_DURATION_CHANGED = "!bookDurationChanged";

		public static final String TRACK_REMOVED = "!trackRemoved";
		public static final String TRACK_ADDED = "!trackAdded";
		public static final String TRACK_ORDER_CHANGED = "!trackOrderChanged";
		public static final String ELAPSED_TIME_CHANGED = "!elapsedTimeChanged";
		// public static final String BOOKMARK_SET = "!bookmarkSet";
		// public static final String TAG_SET = "!tagSet";
		public static final String TRACK_INDEX_CHANGED = "!trackIndexChanged";
		// note that this should not be called when the track index changes
		// due to modifying the track order in the book. It should only trigger
		// when the user (through the GUI "next/previous" track) or the audio
		// player (when the track ends) changes the track index.
		// TODO(anyone): change this variable name "_BY_USER_OR_PLAYER" suffix.

		public static final String TAG_ADDED = "!tagAdded";

		public static final String TAG_REMOVED = "!tagRemoved";

		public static final String BOOKS_CHANGED = "!booksChanged";
	}

	/**
	 * 
	 * 
	 * @author Aki Käkelä, Marcus Parkkinen
	 * 
	 */
	public static final class Reference {
		public static final String BOOKSHELF = "@bookshelf";
	}

	/**
	 * Contains hard-coded values.
	 * 
	 * @author Aki Käkelä, Marcus Parkkinen
	 * 
	 */
	public static final class Value {
		// The frequency for GUI updates. Lower value means updates more often.
		public static final int UPDATE_FREQUENCY = 100; // default 1000

		// Increases the number of visual jumps when the bars are updated.
		public static final int NUMBER_OF_SEEK_BAR_ZONES = 500; // default 100
		
		public static final int NO_BOOK_SELECTED = -1;
		public static final int NO_TRACK_SELECTED = -1;

	}

	/**
	 * Contains string messages.
	 * 
	 * @author Aki Käkelä, Marcus Parkkinen
	 * 
	 */
	public static final class Message {
		public static final String NO_AUDIO_FILES_FOUND = "No audio files found.";
		
		public static final CharSequence NO_BOOK_TITLE = "N/A";
		public static final CharSequence NO_TRACK_TITLE = "N/A";
		
		public static final CharSequence NO_TRACK_ELAPSED_TIME = "N/A";
		public static final CharSequence NO_BOOK_ELAPSED_TIME = "N/A";
		
		public static final String NO_TRACK_DURATION = "N/A";
		public static final String NO_BOOK_DURATION = "N/A";

		public static final String NO_AUTHOR = "N/A";
	}

}
