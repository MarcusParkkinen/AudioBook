package edu.chalmers.dat255.audiobookplayer.constants;

/**
 * Holds ids for events.
 * 
 * @version 0.3
 */
public final class StringConstants {
	public static final class event {
		// Events for creation of Books and Tracks?

		// Bookshelf.class
		public static final String BOOK_ADDED = "!bookAdded";
		public static final String BOOK_MOVED = "!bookMoved";
		public static final String BOOK_REMOVED = "!bookRemoved";

		// Book.class
		public static final String TRACK_REMOVED = "!trackRemoved";
		public static final String TRACK_ADDED = "!trackAdded";
		public static final String TRACK_SWAPPED = "!trackSwapped";
		public static final String TRACK_MOVED = "!trackMoved";
		public static final String BOOKMARK_SET = "!bookmarkSet";
		public static final String TRACK_INDEX_CHANGED = "!trackIndexChanged";
		public static final String BOOK_DURATION_CHANGED = "!bookDurationChanged";

		// Track.class
		public static final String TRACK_TIME_CHANGED = "!timeChanged";
	}
}