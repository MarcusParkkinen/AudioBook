package edu.chalmers.dat255.audiobookplayer.ctrl;

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
	
	public void createBook(String[] filePaths) {
		Book book = new Book();
		for(int i = 0; i < filePaths.length; i++) {
			if(isValidPath(filePaths[i])){
				book.addTrack(new Track(filePaths[i], 0));
			}
		}
		bookshelf.addNewBook(book);
	}
	
	/**
	 * Check whether the format of a file path is valid or not.
	 * 
	 * @param path the file path
	 * @return boolean indicating yes or no
	 */
	
	private boolean isValidPath(String path) {
		//Temporary solution
		return true;
	}
}
