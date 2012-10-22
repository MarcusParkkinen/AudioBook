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

package edu.chalmers.dat255.audiobookplayer.model;

import junit.framework.TestCase;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;

/**
 * Test for Bookshelf. Tests constructing and copying a bookshelf,
 * selecting/adding/moving/removing book(s), getting number of books and the
 * equals method for a bookshelf.
 * 
 * @author Aki Käkelä
 * @version 0.3
 * 
 */
public class BookshelfTest extends TestCase {

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

	private static final Book BOOK0 = new Book(TITLE0, AUTHOR0);
	private static final Book BOOK1 = new Book(TITLE1, AUTHOR1);
	private static final Book BOOK2 = new Book(TITLE2);
	private static final Book BOOK3 = new Book(TITLE3);
	private static final Book BOOK4 = new Book(TITLE4);

	// Constant values; 'magic numbers'
	private static final int LEGAL_POSITIVE_INDEX = 1;
	private static final int OTHER_LEGAL_POSITIVE_INDEX = 3;

	private static final int ILLEGAL_POSITIVE_INDEX = 5;
	private static final int OTHER_ILLEGAL_POSITIVE_INDEX = 1000;

	private static final int ILLEGAL_NEGATIVE_INDEX = -2;

	private static final int STARTING_NUMBER_OF_BOOKS = 5;

	private static final String OUT_OF_BOUNDS_MESSAGE = "Index was out of bounds,"
			+ " but the operation was still completed.";

	private static final int FIRST = 0;
	private static final int SECOND = 1;
	private static final int THIRD = 2;
	private static final int FOURTH = 3;
	private static final int LAST = 4;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		// NOTE: do not change these values; the tests depend on them

		// create a new bookshelf
		bookshelf = new Bookshelf();

		// Add tracks to one of the books
		BOOK0.addTrack(TRACK0);
		BOOK0.addTrack(TRACK1);
		BOOK0.addTrack(TRACK2);

		// Add books to the bookshelf (to add to the allowed indices)
		bookshelf.addBook(BOOK0);
		bookshelf.addBook(BOOK1);
		bookshelf.addBook(BOOK2);
		bookshelf.addBook(BOOK3);
		bookshelf.addBook(BOOK4);
	}

	/**
	 * Tests the constructor.
	 */
	public void testBookshelf() {
		/*
		 * According to the design, the bookshelf should select only the first
		 * book added. If it is empty, it will not have a selection. If it has
		 * more than one book, it will still keep the first added book selected.
		 */
		assertTrue(bookshelf.getSelectedBookIndex() == 0); // first one selected

		// now try an empty bookshelf
		bookshelf = new Bookshelf();

		// none selected:
		assertTrue(bookshelf.getSelectedBookIndex() == Constants.Value.NO_BOOK_SELECTED);

		// make sure that getting the book causes an exception.
		try {
			bookshelf.getSelectedBook();
			fail("()" + "Current book on an empty bookshelf should not exist.");
		} catch (IndexOutOfBoundsException e) {
			// expected
		}
	}

	/**
	 * Tests the constructor.
	 */
	public void testConstructor() {
		Bookshelf testBookshelf = new Bookshelf(bookshelf);

		// Assert that created bookshelves are not null
		assertTrue(bookshelf != null);
		assertTrue(testBookshelf != null);

		// Assert that bookshelves created through the copy constructor are
		// equal
		assertEquals(bookshelf, testBookshelf);

		// Test empty bookshelves
		bookshelf = new Bookshelf();
		testBookshelf = new Bookshelf(bookshelf);
		// Assert that bookshelves created through the copy constructor are
		// equal
		assertEquals(bookshelf, testBookshelf);
	}

	/**
	 * Tests selecting a book.
	 */
	public void testSetSelectedBook() {
		// Test the accessor
		bookshelf.setSelectedBookIndex(LEGAL_POSITIVE_INDEX);
		assertTrue(bookshelf.getSelectedBookIndex() == LEGAL_POSITIVE_INDEX);

		// Test a positive integer beyond the allowed indices
		bookshelf.setSelectedBookIndex(ILLEGAL_POSITIVE_INDEX);

		// ensure that nothing changed
		assertTrue(bookshelf.getSelectedBookIndex() == LEGAL_POSITIVE_INDEX);

		// Test a negative integer beyond the allowed indices (-1 is allowed)
		bookshelf.setSelectedBookIndex(ILLEGAL_NEGATIVE_INDEX);

		// ensure that nothing changed
		assertTrue(bookshelf.getSelectedBookIndex() == LEGAL_POSITIVE_INDEX);

		// Test deselecting (should work)
		bookshelf.setSelectedBookIndex(Constants.Value.NO_BOOK_SELECTED);

		// ensure that it changed
		assertTrue(bookshelf.getSelectedBookIndex() == Constants.Value.NO_BOOK_SELECTED);

	}

	/**
	 * Tests adding a book.
	 */
	public void testAddBook() {
		// Make a new (empty) bookshelf
		bookshelf = new Bookshelf();

		// Add a book
		bookshelf.addBook(BOOK0);

		// Assert that the book is added, and at the correct index
		assertEquals(bookshelf.getBookAt(0), BOOK0);

		// Also check that the number of books is correct
		assertTrue(bookshelf.getNumberOfBooks() == 1);

		// Since this is the first book, it should be the 'current' book
		assertTrue(bookshelf.getSelectedBook().equals(BOOK0));
	}

	/**
	 * Tests removing a book.
	 */
	public void testRemoveBook() {
		// Try to remove a book from an illegal index
		try {
			bookshelf.removeBookAt(ILLEGAL_NEGATIVE_INDEX);
			fail("(illegal negative)" + OUT_OF_BOUNDS_MESSAGE);
		} catch (IndexOutOfBoundsException e) {
		} // expected

		try {
			bookshelf.removeBookAt(ILLEGAL_POSITIVE_INDEX);
			fail("(illegal positive)" + OUT_OF_BOUNDS_MESSAGE);
		} catch (IndexOutOfBoundsException e) {
		} // expected

		// Assert that a book is removed and that it is the correct one
		bookshelf.removeBookAt(THIRD);
		assertTrue(bookshelf.getBookAt(FIRST).equals(BOOK0));
		assertTrue(bookshelf.getBookAt(SECOND).equals(BOOK1));

		// the other books should now have new indices
		assertTrue(bookshelf.getBookAt(THIRD).equals(BOOK3));
		assertTrue(bookshelf.getBookAt(FOURTH).equals(BOOK4));

		// Assert that the number of books is correct
		assertTrue(bookshelf.getNumberOfBooks() == STARTING_NUMBER_OF_BOOKS - 1);

		// Assert that removing a book does nothing to an empty bookshelf
		bookshelf = new Bookshelf();

		// copy to compare with
		Bookshelf testBookshelf = new Bookshelf(bookshelf);

		// remove from the original
		try {
			bookshelf.removeBookAt(0);
			fail("Removing " + OUT_OF_BOUNDS_MESSAGE);
		} catch (IndexOutOfBoundsException e) {
		} // expected

		// make sure they are still equal
		assertTrue(testBookshelf.equals(bookshelf));
	}

	/**
	 * Tests moving a book.
	 */
	public void testMoveBook() {
		// Make a copy to compare with
		Bookshelf orig = new Bookshelf(bookshelf);

		try {
			// Try to move from an illegal index to an illegal index
			bookshelf.moveBook(ILLEGAL_POSITIVE_INDEX,
					OTHER_ILLEGAL_POSITIVE_INDEX);
			assertTrue(orig.equals(bookshelf));
			fail("(illegal-illegal)" + OUT_OF_BOUNDS_MESSAGE);
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		try {
			// Try to move from an illegal index to a legal index
			bookshelf.moveBook(ILLEGAL_POSITIVE_INDEX,
					OTHER_LEGAL_POSITIVE_INDEX);
			assertTrue(orig.equals(bookshelf));
			fail("(illegal-legal)" + OUT_OF_BOUNDS_MESSAGE);
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		try {
			// Try to move from a legal index to an illegal index
			bookshelf.moveBook(LEGAL_POSITIVE_INDEX,
					OTHER_ILLEGAL_POSITIVE_INDEX);
			assertTrue(orig.equals(bookshelf));
			fail("(legal-illegal)" + OUT_OF_BOUNDS_MESSAGE);
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		// Try to move from a legal index to a legal index
		// in this case, from position 1 to 3 (indices 0, 4 remain as they were)
		bookshelf.moveBook(LEGAL_POSITIVE_INDEX, OTHER_LEGAL_POSITIVE_INDEX);

		// Test that something changed
		assertFalse(orig.equals(bookshelf));

		// The move also means that the other indices are changed.
		/*
		 * Indices before the lowest index remain as they were. Indices between
		 * will decrement. Indices after will remain as they were.
		 * 
		 * In this case we have that the first and last books will have the same
		 * indices as before. book1 will be moved to book4, and books 2-3 will
		 * decrement indices.
		 */

		// first, last are as they were
		assertTrue(orig.getBookAt(FIRST).equals(bookshelf.getBookAt(FIRST)));
		assertTrue(orig.getBookAt(LAST).equals(bookshelf.getBookAt(LAST)));

		// second is moved to second last
		assertTrue(orig.getBookAt(SECOND).equals(bookshelf.getBookAt(LAST - 1)));

		/*
		 * Those in between are moved one back since an earlier book was moved
		 * ahead.
		 */
		assertTrue(orig.getBookAt(THIRD).equals(bookshelf.getBookAt(THIRD - 1)));
		assertTrue(orig.getBookAt(FOURTH).equals(
				bookshelf.getBookAt(FOURTH - 1)));
	}

	/**
	 * Tests getting the correct number of books.
	 */
	public void testGetNumberOfBooks() {
		// Assert that the number of books is correct
		assertTrue(bookshelf.getNumberOfBooks() == STARTING_NUMBER_OF_BOOKS);

		// Assert that an empty bookshelf has zero books
		bookshelf = new Bookshelf();
		assertTrue(bookshelf.getNumberOfBooks() == 0);
	}

	/**
	 * Tests if the equals method for bookshelves is correct.
	 */
	public void testEqualsObject() {
		// Assert that they are equivalent (reflexive, symmetric and transitive)

		// Assert that it is reflexive
		assertTrue(bookshelf.equals(bookshelf));

		// Assert that it is symmetric
		Bookshelf testBookshelf = new Bookshelf(bookshelf);
		assertTrue(testBookshelf.equals(bookshelf));
		assertTrue(bookshelf.equals(testBookshelf));

		// Assert that it is transitive
		Bookshelf testBookshelf2 = new Bookshelf(testBookshelf);
		assertTrue(testBookshelf2.equals(bookshelf));
		assertTrue(bookshelf.equals(testBookshelf2));
	}

}
