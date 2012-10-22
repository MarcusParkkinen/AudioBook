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
 * @version 0.1
 * 
 */
public class BookshelfHandlerTest extends AndroidTestCase {
	private static final String USERNAME = "BookshelfHandlerTest";

	private Bookshelf bs;
	private Book newBook;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.AndroidTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		bs = new Bookshelf();

		// Create a book object for testing
		newBook = new Book(
				Arrays.asList(new Track[] { new Track("trackPath", 1) }),
				"BookTitle", "BookAuthor");

		super.setUp();
	}

	/**
	 * Tests to see if loading bookshelves works as intended.
	 */
	public void testLoadBookshelf() {
		// Assert that we always get a bookshelf instance
		assertTrue(BookshelfHandler.loadBookshelf(this.getContext(), USERNAME) instanceof Bookshelf);
	}

	/**
	 * Tests to see if saving bookshelves works as intended.
	 */
	public void testSaveBookshelf() {
		// Assert that saving the bookshelf is successful
		assertTrue(BookshelfHandler.saveBookshelf(this.getContext(), USERNAME,
				bs));
	}

	/**
	 * Tests to see if saving and loading bookshelves work as intended.
	 */
	public void testSaveAndLoadBookshelf() {
		// Make some changes to the bookshelf ..
		bs.addBook(newBook);

		// .. before saving it again
		BookshelfHandler.saveBookshelf(this.getContext(), USERNAME, bs);

		// Assert that the saved bookshelf is equal to the loaded bookshelf
		Bookshelf loadedBookshelf = BookshelfHandler.loadBookshelf(
				this.getContext(), USERNAME);
		assertEquals(bs, loadedBookshelf);

		// .. but assert that they are not the same object
		assertNotSame(loadedBookshelf, bs);

		// Finally also assert that the loaded bookshelf contains 'newBook'
		assertEquals(newBook, loadedBookshelf.getBookAt(0));
	}

}
