package edu.chalmers.dat255.audiobookplayer.constants;

/**
 * Holds unique property names ensuring that for example event property names
 * match if intended to do so and that property name changing is simple.
 * 
 * @author Aki Käkelä
 * @version 0.6
 */
public final class Constants {
	/**
	 * Contains update event property names.
	 * 
	 * @author Aki Käkelä, Marcus Parkkinen
	 * 
	 */
	public static final class Event {
		public static final String BOOKSHELF_UPDATED = "!bookshelfUpdated";

		public static final String BOOK_ADDED = "!bookAdded";
		public static final String BOOK_MOVED = "!bookMoved";
		public static final String BOOK_REMOVED = "!bookRemoved";
		public static final String BOOK_SELECTED = "!bookSelected";

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

		public static final String BOOK_FINISHED = "!bookFinished";
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
	public static final class Values {
		// The frequency for GUI updates. Lower value means updates more often.
		public static final int UPDATE_FREQUENCY = 100; // default 1000
		// Increases the number of visual jumps when the bars are updated.
		public static final int NUMBER_OF_SEEK_BAR_ZONES = 500; // default 100
	}

	/**
	 * Contains string messages.
	 * 
	 * @author Aki Käkelä, Marcus Parkkinen
	 * 
	 */
	public static final class Messages {
		public static final String NO_AUDIO_FILES_FOUND = "No audio files found.";
	}

}