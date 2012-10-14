package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.util.LinkedList;
import java.util.List;

import android.test.AndroidTestCase;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Test case for BookshelfController
 * 
 * @author Aki Käkelä
 * 
 */
public class BookshelfControllerTest extends AndroidTestCase {
	
	// Books
	private Book book0;
	private Book book1;
	private Book book2;
	private Book book3;

	// Tracks
	private Track track0;
	private Track track1;
	private Track track2;
	private Track track3;

	// Bookshelf
	private Bookshelf bs;

	protected void setUp() throws Exception {
		super.setUp();

		bs = new Bookshelf();

		// Create tracks
		track0 = new Track("path0", 0);
		track1 = new Track("path1", 1);
		track2 = new Track("path2", 2);
		track3 = new Track("path3", 3);

		// Track list
		List<Track> tracks = new LinkedList<Track>();
		tracks.add(track0);
		tracks.add(track1);
		tracks.add(track2);
		tracks.add(track3);

		// Create books
		book0 = new Book(tracks, "title0");
		book1 = new Book("title1");
		book2 = new Book("title2");
		book3 = new Book("title3");

		// Add them with the indices indicated by the variable name
		bs.addBook(book0);
		bs.addBook(book1);
		bs.addBook(book2);
		bs.addBook(book3);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSetSelectedBook() {
		bs.setSelectedBookIndex(2);
		assertTrue(bs.getSelectedBookIndex() == 2);

		// Try to select an index out of bounds
		try {
			bs.setSelectedBookIndex(16);
			fail("Selected an illegal book index which was not intended.");
		} catch (IllegalArgumentException e) {
			// Assert that the index is the same as it was
			assertTrue(bs.getSelectedBookIndex() == 2);
		}

		/*
		 * The same test with "-1" as in NO_BOOK_SELECTED.
		 * 
		 * Changing this to -1 should not be allowed outside of the Bookshelf
		 * class.
		 */
		try {
			bs.setSelectedBookIndex(-1);
			fail("Selected an illegal book index which was not intended.");
		} catch (IllegalArgumentException e) {
			// Assert that the index is the same as it was
			assertTrue(bs.getSelectedBookIndex() == 2);
		}
	}

	public void testGetSelectedBookPosition() {
		bs.setSelectedBookIndex(2);
		assertTrue(bs.getSelectedBookIndex() == 2);
	}

	public void testGetSelectedBook() {
		bs.setSelectedBookIndex(2);
		assertTrue(bs.getSelectedBook().equals(book2));
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
			bs.removeBookAt(-1);
			fail("Removed a book at an illegal track index (negative out of bounds)");
		} catch (IllegalArgumentException e) {
		}

		// out-of-bounds positive integer
		try {
			bs.removeBookAt(1252125);
			fail("Removed a book at an illegal track index (positive out of bounds)");
		} catch (IllegalArgumentException e) {
		}

		// off-by-one positive integer
		try {
			bs.removeBookAt(4);
			fail("Removed a book at an illegal track index (positive out of bounds by one)");
		} catch (IllegalArgumentException e) {
		}

		// the number of books should not have changed
		assertTrue(bs.getNumberOfBooks() == 4);

		bs.removeBookAt(0);

		// the number of books should now have decremented
		assertTrue(bs.getNumberOfBooks() == 3);

		/*
		 * ensure that the book that is currently at index 0 is the one that was
		 * at index 1 at the beginning (since we removed a book earlier in the
		 * list)
		 */
		assertTrue(bs.getBookAt(0).equals(book1));
	}

	public void testSetBookTitleAt() {
		final String TEST_STRING = "TEST";
		bs.setSelectedBookTitle(TEST_STRING);
		assertTrue(bs.getSelectedBookTitle().equals(TEST_STRING));
	}

	// testRemoveTrack and testSetSelctedTrack implemented in BookTest

}
