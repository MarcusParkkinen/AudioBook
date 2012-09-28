package edu.chalmers.dat255.audiobookplayer.constants;

/**
 * Holds unique event property names for events.
 * 
 * @version 0.4
 */
public final class Constants {
	public static final class event {
		// Bookshelf.class
		public static final String BOOK_ADDED = "!bookAdded";
		public static final String BOOK_MOVED = "!bookMoved";
		public static final String BOOK_REMOVED = "!bookRemoved";
		public static final String BOOK_SELECTED = "!bookSelected";

		// Book.class
		public static final String TRACK_REMOVED = "!trackRemoved";
		public static final String TRACK_ADDED = "!trackAdded";
		public static final String TRACK_ORDER_CHANGED = "!trackOrderChanged";
//		public static final String BOOKMARK_SET = "!bookmarkSet";
//		public static final String TAG_SET = "!tagSet";
		public static final String TRACK_INDEX_CHANGED = "!trackIndexChanged";
		public static final String BOOK_TITLE_CHANGED = "!bookTitleChanged";

		// Track.class
		public static final String TRACK_TIME_CHANGED = "!timeChanged";
	}
}