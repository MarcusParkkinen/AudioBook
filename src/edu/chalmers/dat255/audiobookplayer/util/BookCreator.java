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
 *  Copyright � 2012 Marcus Parkkinen, Aki K�kel�, Fredrik �hs.
 **/

package edu.chalmers.dat255.audiobookplayer.util;

import java.util.LinkedList;
import java.util.List;

import android.media.MediaMetadataRetriever;
import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Creates Book instances (filling them with metadata).
 * 
 * @author Aki K�kel�, Fredrik �hs
 * @version 0.6
 * 
 */
public final class BookCreator {
	private static final String TAG = "BookCreator.class";
	private static BookCreator instance = null;
	private Bookshelf bsh;

	private BookCreator() {
	} // To disable outside instantiation

	/**
	 * @return The instance of this object.
	 */
	public static BookCreator getInstance() {
		if (instance == null) {
			instance = new BookCreator();
		}
		return instance;
	}

	/**
	 * @param bsh
	 */
	public void setBookshelf(Bookshelf bsh) {
		this.bsh = bsh;
	}

	/**
	 * Creates a book with given parameters.
	 * 
	 * @param paths
	 *            A list paths to all the tracks that will be added.
	 * @param title
	 *            The title of the book.
	 * @param author
	 *            The author of the book.
	 */
	public boolean createBookToBookshelf(List<String> paths, String title,
			String author) {
		Book b = createBook(paths, title, author);

		if (b != null) {
			bsh.addBook(b);
			return true;
		}

		return false;
	}

	/**
	 * Helper class.
	 * 
	 * In future versions, this should be used instead of adding directly to the
	 * bookshelf (see Issue #1 on the issue tracker).
	 * 
	 * @param paths
	 *            A list paths to all the tracks that will be added.
	 * @param title
	 *            The title of the book.
	 * @param author
	 *            The author of the book.
	 * @return A book created with given data.
	 */
	private Book createBook(List<String> paths, String title, String author) {
		String tmpTitle = title;
		String tmpAuthor = author;
		// if no tracks, do not create book
		if (paths.size() == 0) {
			return null;
		}
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		// assumes
		try {
			mmr.setDataSource(paths.get(0));

		} catch (IllegalArgumentException e) {
			Log.d(TAG, " invalid path to book provided. Skipping operation.");
			return null;
		}

		// try to retrieve metadata ALBUM which is where the title of the book
		// should typically be stored
		tmpTitle = mmr
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
		if (tmpTitle == null || tmpTitle.length() == 0) {
			tmpTitle = title;
		}
		// try to retrieve metadata ARTIST which is where the author of the book
		// should typically be stored
		tmpAuthor = mmr
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		if (tmpAuthor == null || tmpAuthor.length() == 0) {
			tmpAuthor = author;
		}
		List<Track> trackList = new LinkedList<Track>();
		for (String path : paths) {
			try {
				trackList.add(TrackCreator.createTrack(path));
			} catch (Exception e) {
				// If a track contains malformed data, do not add it to the book
				continue;
			}
		}
		return new Book(trackList, tmpTitle, tmpAuthor);
	}

}
