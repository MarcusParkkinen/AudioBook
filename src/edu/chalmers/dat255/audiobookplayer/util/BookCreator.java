package edu.chalmers.dat255.audiobookplayer.util;

import java.util.ArrayList;
import java.util.Collection;
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
 * @version 0.4
 * 
 */
public class BookCreator {
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

	public void setBookshelf(Bookshelf bsh) {
		this.bsh = bsh;
	}

	public void createBook(String[] paths, String path) {
		List<Track> trackList = new LinkedList<Track>();
		for (int i = 0; i < paths.length; i++) {
			trackList.add(TrackCreator.createTrack(paths[i]));
		}
		
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(path);
		String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
		bsh.addBook(new Book(trackList, title));
	}

	public void createTestBook() {
		Collection<Track> list = new ArrayList<Track>();
		String[] paths = { "/game.mp3", "/game2.mp3", "/game.mp3" };
		int[] ms = { 37204, 131291, 37204 };
		for (int i = 0; i < paths.length; i++) {
			list.add(new Track(paths[i], ms[i]));
		}
		bsh.addBook(new Book(
				list,
				"TestBook12312333333333......- ------999999997ujihgyfguhujikolpkojiusduhfjisdofksdpofk"));
	}
}
