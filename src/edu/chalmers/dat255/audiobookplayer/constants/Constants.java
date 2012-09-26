package edu.chalmers.dat255.audiobookplayer.constants;

/**
 * Holds event property names for events.
 * 
 * @version 0.3
 */
public final class Constants {
	public static final class event {
		// Events for creation of Books and Tracks?

		// Bookshelf.class
		public static final String BOOK_ADDED = "!bookAdded";
		public static final String BOOK_MOVED = "!bookMoved";
		public static final String BOOK_REMOVED = "!bookRemoved";
		public static final String BOOK_SELECTED = "!bookSelected";
		
		// PlayerController:
		// BOOK_SELECTED
		
		// PlayerFragment:
		// BOOK_SELECTED, BOOK_DURATION_CHANGED, TRACK_TIME_CHANGED (BOOKMARK_SET, TAG_SET)

		// Book.class
		public static final String TRACK_REMOVED = "!trackRemoved";
		public static final String TRACK_ADDED = "!trackAdded";
		public static final String TRACK_SWAPPED = "!trackSwapped";
		public static final String TRACK_MOVED = "!trackMoved";
		public static final String BOOKMARK_SET = "!bookmarkSet";
//		public static final String TAG_SET = "!tagSet";
		public static final String TRACK_INDEX_CHANGED = "!trackIndexChanged";
		public static final String BOOK_DURATION_CHANGED = "!bookDurationChanged";

		// Track.class
		public static final String TRACK_TIME_CHANGED = "!timeChanged";
	}
}