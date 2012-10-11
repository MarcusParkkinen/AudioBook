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
 * Holds unique event property names for events.
 * 
 * @author Aki K�kel�
 * @version 0.6
 */
public final class Constants {
	public static final class event {
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
		// TODO: change name? "_BY_USER_OR_PLAYER" suffix.

		public static final String BOOK_FINISHED = "!bookFinished";
	}

	public static final class reference {
		public static final String BOOKSHELF = "@bookshelf";
	}

	public static final class values {
		// The frequency for GUI updates. Lower value means updates more often.
		public static final int UPDATE_FREQUENCY = 100; // default 1000
		// Increases the number of visual jumps when the bars are updated.
		public static final int NUMBER_OF_SEEK_BAR_ZONES = 500; // default 100
	}

	public static final class messages {
		public static final String NO_AUDIO_FILES_FOUND = "No audio files found.";
	}
	
}