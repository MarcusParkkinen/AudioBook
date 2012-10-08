package edu.chalmers.dat255.audiobookplayer.util;

import java.util.LinkedList;
import java.util.List;

import android.media.MediaMetadataRetriever;

import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Creates Book instances (filling them with metadata).
 * 
 * @author Aki Käkelä
 * @version 0.6
 * 
 */
public class BookCreator {
	private static final String TAG = "BookCreator.class";
	private static BookCreator instance = null;
	private Bookshelf bsh;

	private BookCreator() {
	} // To disable outside instantiation

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
	 * @param paths A list paths to all the tracks that will be added.
	 * @param title The title of the book.
	 * @param author The author of the book.
	 */
	public void createBook(List<String> paths, String title, String author) {
		String tmpTitle = title;
		String tmpAuthor = author;
		//if no tracks, do not create book
		if (paths.size() == 0) { 
			return; 
		}
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		//assumes 
		try {
			mmr.setDataSource(paths.get(0));

		} catch (IllegalArgumentException e) {
			System.out.println("BOOK PATH TO TRACK 0 INVALID");
			return;
		}
		
		//try to retrieve metadata ALBUM which is where the title of the book should typically be stored
		tmpTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
		if(tmpTitle == null || tmpTitle.length() == 0) {
			tmpTitle = title;
		}
		//try to retrieve metadata ARTIST which is where the author of the book should typically be stored
		tmpAuthor = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		if(tmpAuthor == null || tmpAuthor.length() == 0) {
			tmpAuthor = author;
		}
		List<Track> trackList = new LinkedList<Track>();
		for (String path : paths) {
			try {
				trackList.add(TrackCreator.createTrack(path));
			} catch(NumberFormatException e) { 
				// If a track contains malformed data, do not add it to the book
				continue;
			}
		}
		bsh.addBook(new Book(trackList, tmpTitle, tmpAuthor));
	}

}
