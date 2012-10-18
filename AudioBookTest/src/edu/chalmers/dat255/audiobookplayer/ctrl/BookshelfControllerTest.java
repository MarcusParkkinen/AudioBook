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
 * <p>
 * Ignores testing methods which only relay the calls directly to the model.
 * 
 * @author Aki Käkelä
 * @version 0.2
 * 
 */
public class BookshelfControllerTest extends AndroidTestCase {

	// BookshelfController - the test object
	private BookshelfController bookshelfController;

	// Books
	private static final String TITLE0 = "title0";
	private static final String TITLE1 = "title1";
	private static final String TITLE2 = "title2";

	// Constant values; 'magic numbers'

	// indices to test selecting.
	private static final int LEGAL_BOOK_INDEX = 2;
	private static final int LEGAL_TRACK_INDEX = 2;

	private static final String OUT_OF_BOUNDS_FAILURE_MESSAGE = "Index was out of bounds,"
			+ " but the operation was still completed.";

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
		
		// Create books
		Book b0 = new Book(TITLE0);
		Book b1 = new Book(TITLE1);
		Book b2 = new Book(TITLE2);
		
		// create a new bookshelf to create the controller
		Bookshelf bookshelf = new Bookshelf();

		// Add books to the bookshelf (to add to the allowed indices)
		bookshelf.addBook(b0);
		bookshelf.addBook(b1);
		bookshelf.addBook(b2);
		
		// create a new controller with the bookshelf
		bookshelfController = new BookshelfController(bookshelf);

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
	 * @return True if the selected book and selected track indices are legal
	 *         (i.e. changed).
	 */
	private boolean indexChanged() {
		return bookshelfController.getSelectedBookIndex() == LEGAL_BOOK_INDEX
				&& bookshelfController.getSelectedTrackIndex() == LEGAL_TRACK_INDEX;
	}

}
