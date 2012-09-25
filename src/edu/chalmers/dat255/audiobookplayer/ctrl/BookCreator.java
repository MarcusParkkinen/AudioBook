package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.util.ArrayList;

import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * A Singleton class that is responsible for the creation of books.
 * 
 * @author Aki Käkelä
 * @version 0.3
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

	public void createBook(String[] paths /* , int duration */, String name) {
		ArrayList<Track> list = new ArrayList<Track>();
		for (int i = 0; i < paths.length; i++) {
			list.add(new Track(paths[i], 0));
		}
		bsh.addBook(new Book(list, name));
	}
	
	public void createTestBook() {
		System.out.println("ADDED NEW BOOK!");
		createBook(new String[]{"path1", "path2", "path3"}, "TestBook");
	}
}