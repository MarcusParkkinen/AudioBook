package edu.chalmers.dat255.audiobookplayer.test;

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
		
		Track[] t = new Track[]{new Track("trackPath", 1)};
		newBook = new Book(Arrays.asList(t), "BookTitle");
		//newBookshelf = new Bookshelf();
		//newBookshelf.addBook(newBook);
		
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