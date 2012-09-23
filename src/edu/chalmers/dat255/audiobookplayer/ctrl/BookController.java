package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.beans.PropertyChangeListener;

import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * BookController class - used to edit and create all the Books stored in the users bookshelf.
 * Wraps the Bookshelf class.
 * 
 * @author Marcus Parkkinen
 * @version 1.0
 */


public class BookController {
	private Bookshelf bookshelf;
	
	/**
	 * ...
	 */
	
	public BookController() {
		//Temporary solution, this way the bookshelf is never saved
		bookshelf = new Bookshelf();
		
	}
	
	/**
	 * Create  a new book and add it to the bookshelf.
	 * 
	 * @param filePaths paths to the audio files that compose the book
	 */
	
	public void createBook(String[] filePaths, String title) {
		Book book = new Book(title);
		for(int i = 0; i < filePaths.length; i++) {
			book.addTrack(new Track(filePaths[i], 0));
		}
		bookshelf.addNewBook(book);
	}
	
	public void addBookshelfListener(PropertyChangeListener listener) {
		bookshelf.addListener(listener);
	}
}
 