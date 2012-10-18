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

import java.util.LinkedList;
import java.util.List;

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

	// since we will be testing with 3 books, 3 is an illegal index (0-2 are
	// legal)
	private static final int ILLEGAL_BOOK_INDEX = 3;

	// since we will be testing with 3 tracks, 3 is an illegal index (0-2 are
	// legal)
	private static final int ILLEGAL_TRACK_INDEX = 3;

	private static final String PATH0 = "p0";
	private static final String PATH1 = "p1";
	private static final String PATH2 = "p2";

	private static final int DURATION0 = 5000;
	private static final int DURATION1 = 6000;
	private static final int DURATION2 = 7000;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.AndroidTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		// NOTE: do not change these values; the tests depend on them

		// Create tracks
		Track t0 = new Track(PATH0, DURATION0);
		Track t1 = new Track(PATH1, DURATION1);
		Track t2 = new Track(PATH2, DURATION2);

		// Put them in a list
		List<Track> tracks = new LinkedList<Track>();
		tracks.add(t0);
		tracks.add(t1);
		tracks.add(t2);

		// Create books
		Book b0 = new Book(tracks, TITLE0);
		Book b1 = new Book(tracks, TITLE1);
		Book b2 = new Book(tracks, TITLE2);

		// Create a new bookshelf to create the controller with
		Bookshelf bookshelf = new Bookshelf();

		// Add books to the bookshelf (to add to the allowed indices)
		bookshelf.addBook(b0);
		bookshelf.addBook(b1);
		bookshelf.addBook(b2);

		// Create a new controller with the bookshelf
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
				LEGAL_TRACK_INDEX);

		// ensure that the indices are changed properly
		assertTrue(indicesAreLegal());

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
			// ensure that nothing changed.
			assertTrue(indicesAreLegal());
		}

		// setting to illegal book index, legal track index
		try {
			bookshelfController.setSelectedTrack(ILLEGAL_BOOK_INDEX,
					LEGAL_TRACK_INDEX);
			fail(OUT_OF_BOUNDS_FAILURE_MESSAGE);
		} catch (IllegalArgumentException e) {
			// ensure that nothing changed.
			assertTrue(indicesAreLegal());
		}

		// setting to legal book index, illegal track index
		try {
			bookshelfController.setSelectedTrack(LEGAL_BOOK_INDEX,
					ILLEGAL_TRACK_INDEX);
			fail(OUT_OF_BOUNDS_FAILURE_MESSAGE);
		} catch (IllegalArgumentException e) {
			// ensure that nothing changed.
			assertTrue(indicesAreLegal());
		}

	}

	/**
	 * @return True if the selected book and selected track indices are the
	 *         legal ones specified in BookshelfControllerTest (i.e. changed).
	 */
	private boolean indicesAreLegal() {
		return bookshelfController.getSelectedBookIndex() == LEGAL_BOOK_INDEX
				&& bookshelfController.getSelectedTrackIndex() == LEGAL_TRACK_INDEX;
	}

}
