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


package edu.chalmers.dat255.audiobookplayer.ctrl;

import android.test.AndroidTestCase;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Test case for BookshelfController
 * 
 * @author Aki Käkelä
 * @version 0.1
 * 
 */
public class BookshelfControllerTest extends AndroidTestCase {
	// Bookshelf
	private Bookshelf bookshelf;
	
	// Tracks
	private static final String PATH0 = "path000";
	private static final String PATH1 = "path111";
	private static final String PATH2 = "path222";
	
	private static final int DURATION0 = 1234;
	private static final int DURATION1 = 2345;
	private static final int DURATION2 = 3456;
	
	private static final Track TRACK0 = new Track(PATH0, DURATION0);
	private static final Track TRACK1 = new Track(PATH1, DURATION1);
	private static final Track TRACK2 = new Track(PATH2, DURATION2);

	// Books
	private static final String TITLE0 = "title0";
	private static final String TITLE1 = "title1";
	private static final String TITLE2 = "title2";
	private static final String TITLE3 = "title3";
	private static final String TITLE4 = "title4";
	
	private static final String AUTHOR0 = "author0";
	private static final String AUTHOR1 = "author1";
	
	// One modifiable book
	private Book book0;
	private static final Book BOOK1 = new Book(TITLE1, AUTHOR1);
	private static final Book BOOK2 = new Book(TITLE2);
	private static final Book BOOK3 = new Book(TITLE3);
	private static final Book BOOK4 = new Book(TITLE4);

	
	private static final String TEST_STRING = "TEST";

	protected void setUp() throws Exception {
		super.setUp();

		// NOTE: do not change these values; the tests depend on them

		// create a new bookshelf
		bookshelf = new Bookshelf();
		
		// create the single modifiable book
		book0 = new Book(TITLE0, AUTHOR0);

		// Add tracks to one of the books
		book0.addTrack(TRACK0);
		book0.addTrack(TRACK1);
		book0.addTrack(TRACK2);

		// Add books to the bookshelf (to add to the allowed indices)
		bookshelf.addBook(book0);
		bookshelf.addBook(BOOK1);
		bookshelf.addBook(BOOK2);
		bookshelf.addBook(BOOK3);
		bookshelf.addBook(BOOK4);
	}

	public void testSetSelectedBook() {
		bookshelf.setSelectedBookIndex(2);
		assertTrue(bookshelf.getSelectedBookIndex() == 2);

		// Try to select an index out of bounds
		try {
			bookshelf.setSelectedBookIndex(16);
			fail("Selected an illegal book index which was not intended.");
		} catch (IllegalArgumentException e) {
			// Assert that the index is the same as it was
			assertTrue(bookshelf.getSelectedBookIndex() == 2);
		}

		/*
		 * The same test with "-1" as in NO_BOOK_SELECTED.
		 * 
		 * Changing this to -1 should not be allowed outside of the Bookshelf
		 * class.
		 */
		try {
			bookshelf.setSelectedBookIndex(-1);
			fail("Selected an illegal book index which was not intended.");
		} catch (IllegalArgumentException e) {
			// Assert that the index is the same as it was
			assertTrue(bookshelf.getSelectedBookIndex() == 2);
		}
	}

	public void testGetSelectedBookPosition() {
		bookshelf.setSelectedBookIndex(2);
		assertTrue(bookshelf.getSelectedBookIndex() == 2);
	}

	public void testGetSelectedBook() {
		bookshelf.setSelectedBookIndex(2);
		assertTrue(bookshelf.getSelectedBook().equals(BOOK2));
	}

	public void testSetSelectedTrack() {
		// book0 has a list of 4 tracks
		book0.setSelectedTrackIndex(2);
		assertTrue(book0.getSelectedTrackIndex() == 2);

		// Try to select an index out of bounds
		try {
			book0.setSelectedTrackIndex(16);
			fail("Selected an illegal book index which was not intended.");
		} catch (IllegalArgumentException e) {
			// Assert that the index is the same as it was
			assertTrue(book0.getSelectedTrackIndex() == 2);
		}

		/*
		 * The same test with "-1" as in NO_TRACK_SELECTED.
		 * 
		 * Changing this to -1 should not be allowed outside of the Bookshelf
		 * class.
		 */
		try {
			book0.setSelectedTrackIndex(-1);
			fail("Selected an illegal book index which was not intended.");
		} catch (IllegalArgumentException e) {
			// Assert that the index is the same as it was
			assertTrue(book0.getSelectedTrackIndex() == 2);
		}
	}

	public void testRemoveBookAt() {
		/*
		 * we have book indices 0-3 (the 3 tracks), so negative integers and
		 * integers greater than 3 should fail.
		 */
		// negative integer
		try {
			bookshelf.removeBookAt(-1);
			fail("Removed a book at an illegal track index (negative out of bounds)");
		} catch (IllegalArgumentException e) {
		}

		// out-of-bounds positive integer
		try {
			bookshelf.removeBookAt(1252125);
			fail("Removed a book at an illegal track index (positive out of bounds)");
		} catch (IllegalArgumentException e) {
		}

		// off-by-one positive integer
		try {
			bookshelf.removeBookAt(4);
			fail("Removed a book at an illegal track index (positive out of bounds by one)");
		} catch (IllegalArgumentException e) {
		}

		// the number of books should not have changed
		assertTrue(bookshelf.getNumberOfBooks() == 4);

		bookshelf.removeBookAt(0);

		// the number of books should now have decremented
		assertTrue(bookshelf.getNumberOfBooks() == 3);

		/*
		 * ensure that the book that is currently at index 0 is the one that was
		 * at index 1 at the beginning (since we removed a book earlier in the
		 * list)
		 */
		assertTrue(bookshelf.getBookAt(0).equals(BOOK1));
	}

	public void testSetBookTitleAt() {
		bookshelf.setSelectedBookTitle(TEST_STRING);
		assertTrue(bookshelf.getSelectedBookTitle().equals(TEST_STRING));
	}

	// testRemoveTrack and testSetSelctedTrack implemented in BookTest

}
