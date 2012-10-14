package edu.chalmers.dat255.audiobookplayer.util;

import java.util.Arrays;

import android.test.AndroidTestCase;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Test case for loading and saving bookmarks.
 * 
 * @author Aki Käkelä, Marcus Parkkinen
 * 
 */
public class BookshelfHandlerTest extends AndroidTestCase {
	private static final String USERNAME = "BookshelfHandlerTest";

	private Bookshelf bs;
	private Book newBook;

	protected void setUp() throws Exception {
		bs = new Bookshelf();

		// Create a book object for testing
		newBook = new Book(
				Arrays.asList(new Track[] { new Track("trackPath", 1) }),
				"BookTitle");

		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testLoadBookshelf() {
		// Assert that we always get a bookshelf instance
		assertTrue(BookshelfHandler.loadBookshelf(this.getContext(), USERNAME) instanceof Bookshelf);
	}

	public void testSaveBookshelf() {
		// Assert that saving the bookshelf is successful
		assertTrue(BookshelfHandler.saveBookshelf(this.getContext(), USERNAME,
				bs));
	}

	public void testSaveAndLoadBookshelf() {
		// Make some changes to the bookshelf ..
		bs.addPropertyChangeListener(null);
		bs.addBook(newBook);

		// .. before saving it again
		BookshelfHandler.saveBookshelf(this.getContext(), USERNAME, bs);

		// Assert that the saved bookshelf is equal to the loaded bookshelf
		Bookshelf loadedBookshelf = BookshelfHandler.loadBookshelf(
				this.getContext(), USERNAME);
		assertEquals(bs, loadedBookshelf);

		// .. but assert that they are not the same object
		assertFalse(loadedBookshelf == bs);

		// Finally also assert that the loaded bookshelf contains 'newBook'
		assertEquals(newBook, loadedBookshelf.getBookAt(0));
	}

}
