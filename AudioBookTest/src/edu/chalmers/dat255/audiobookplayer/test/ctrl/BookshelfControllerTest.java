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

package edu.chalmers.dat255.audiobookplayer.test.ctrl;

import java.util.Arrays;

import android.test.AndroidTestCase;
import edu.chalmers.dat255.audiobookplayer.ctrl.BookshelfController;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Test case for BookshelfController class.
 * 
 * @author Marcus Parkkinen
 * 
 */
public class BookshelfControllerTest extends AndroidTestCase {
	private final String USERNAME = "BookshelfControllerTest";
	private BookshelfController bsc;
	private Book newBook;
	
	protected void setUp() throws Exception {
		bsc = new BookshelfController();
		
		// Create a book object for testing
		newBook = new Book(Arrays.asList(new Track[]{new Track("trackPath", 1)}), "BookTitle");
		
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void testLoadBookshelf() {
		// Assert that we always get a bookshelf instance
		assertTrue(bsc.loadBookshelf(this.getContext(), USERNAME) instanceof Bookshelf);
	}
	
	public void testSaveBookshelf() {
		// Assert that saving the bookshelf is successful
		assertTrue(bsc.saveBookshelf(this.getContext(), USERNAME));
	}
	
	public void testSaveAndLoadBookshelf() {
		// Get a bookshelf reference
		Bookshelf savedBookshelf = bsc.loadBookshelf(this.getContext(), USERNAME);
		
		// Make some changes to the bookshelf ..
		savedBookshelf.addPropertyChangeListener(null);
		savedBookshelf.addBook(newBook);
		
		// .. before saving it again
		bsc.saveBookshelf(this.getContext(), USERNAME);
		
		// Assert that the saved bookshelf is equal to the loaded bookshelf
		Bookshelf loadedBookshelf = bsc.loadBookshelf(this.getContext(), USERNAME);
		assertEquals(savedBookshelf, loadedBookshelf);
		
		// .. but assert that they are not the same object
		assertFalse(loadedBookshelf == savedBookshelf);
		
		// Finally also assert that the loaded bookshelf contains 'newBook'
		assertEquals(newBook, loadedBookshelf.getBookAt(0));
	}
}