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
import edu.chalmers.dat255.audiobookplayer.constants.Constants;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Test case for BookshelfController.
 * <p>
 * Tests selecting a track and book at the same time (no other features are
 * implemented).
 * 
 * @author Aki Käkelä
 * @version 0.2
 * 
 */
public class BookshelfControllerTest extends AndroidTestCase {

	// Bookshelf
	private Bookshelf bookshelf;

	// BookshelfController - the test object
	private BookshelfController bookshelfController;

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

	// Books, including one not final
	private Book book0 = new Book(TITLE0);
	private static final Book BOOK1 = new Book(TITLE1);
	private static final Book BOOK2 = new Book(TITLE2);

	// Constant values; 'magic numbers'

	// indices to test selecting.
	private static final int LEGAL_BOOK_INDEX = 2;
	private static final int LEGAL_TRACK_INDEX = 2;

	private static final String OUT_OF_BOUNDS_FAILURE_MESSAGE = "Index was out of bounds,"
			+ " but the operation was still completed.";

	private static final String BOOK_TITLE = "TITLE";

	// since we are testing with 3 books, 3 is an illegal index (0-2 are legal)
	private static final int ILLEGAL_BOOK_INDEX = 3;

	// since we are testing with 3 tracks, 3 is an illegal index (0-2 are legal)
	private static final int ILLEGAL_TRACK_INDEX = 3;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.AndroidTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		// NOTE: do not change these values; the tests depend on them

		// create a new bookshelf
		bookshelf = new Bookshelf();
		// create a new controller with the bookshelf
		bookshelfController = new BookshelfController(bookshelf);

		// Add tracks to one of the books
		book0.addTrack(TRACK0);
		book0.addTrack(TRACK1);
		book0.addTrack(TRACK2);

		// Add books to the bookshelf (to add to the allowed indices)
		bookshelf.addBook(book0);
		bookshelf.addBook(BOOK1);
		bookshelf.addBook(BOOK2);
	}

	/**
	 * Tests selecting track and book.
	 */
	public void testSetSelectedTrack() {
		// TODO(?): Should a book/track be selected when adding it, or should
		// they always stay unselected until changed?

		// ensure that there are no selections to begin with
		assertTrue(bookshelfController.getSelectedBookIndex() == Constants.Value.NO_BOOK_SELECTED);
		assertTrue(bookshelfController.getSelectedTrackIndex() == Constants.Value.NO_TRACK_SELECTED);

		// select a book and track at legal indices
		bookshelfController.setSelectedTrack(LEGAL_BOOK_INDEX,
				ILLEGAL_TRACK_INDEX);

		// ensure that the indices changed properly
		assertTrue(indexChanged());

		/*
		 * ensure that nothing is changed if the indices are out of bounds for
		 * either the track or the book, or both.
		 */

		// setting to illegal book index, illegal track index
		try {
			bookshelfController.setSelectedTrack(ILLEGAL_BOOK_INDEX,
					ILLEGAL_TRACK_INDEX);
			fail(OUT_OF_BOUNDS_FAILURE_MESSAGE);
		} catch (IllegalArgumentException e) {
			assertFalse(indexChanged());
		}

		// setting to illegal book index, legal track index
		try {
			bookshelfController.setSelectedTrack(ILLEGAL_BOOK_INDEX,
					LEGAL_TRACK_INDEX);
			fail(OUT_OF_BOUNDS_FAILURE_MESSAGE);
		} catch (IllegalArgumentException e) {
			assertFalse(indexChanged());
		}

		// setting to legal book index, illegal track index
		try {
			bookshelfController.setSelectedTrack(LEGAL_BOOK_INDEX,
					ILLEGAL_TRACK_INDEX);
			fail(OUT_OF_BOUNDS_FAILURE_MESSAGE);
		} catch (IllegalArgumentException e) {
			assertFalse(indexChanged());
		}

	}

	/**
	 * @return True if the selected book and selected track indices are legal.
	 */
	private boolean indexChanged() {
		return bookshelf.getSelectedBookIndex() == LEGAL_BOOK_INDEX
				&& bookshelf.getSelectedTrackIndex() == LEGAL_TRACK_INDEX;
	}

	/**
	 * Tests selecting a book at an index.
	 */
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

	/**
	 * Tests that the selected book is the correct book.
	 */
	public void testGetSelectedBook() {
		bookshelf.setSelectedBookIndex(2);
		assertTrue(bookshelf.getSelectedBook().equals(BOOK2));
	}

	/**
	 * Tests that the book selection is made in the right index.
	 */
	public void testGetSelectedBookIndex() {
		bookshelf.setSelectedBookIndex(2);
		assertTrue(bookshelf.getSelectedBookIndex() == 2);
	}

	/**
	 * Tests removing books at any indices.
	 */
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

	/**
	 * Tests setting the title of a book.
	 */
	public void testSetBookTitleAt() {
		bookshelf.setSelectedBookTitle(BOOK_TITLE);
		assertTrue(bookshelf.getSelectedBookTitle().equals(BOOK_TITLE));
	}

	/*
	 * testRemoveTrack and testSetSelctedTrack and more implemented in model
	 * tests.
	 */

}
