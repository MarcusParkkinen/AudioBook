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

import org.junit.Test;

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
	private static final int OTHER_LEGAL_POSITIVE_INDEX = 4;

	private static final int ILLEGAL_POSITIVE_INDEX = 5;
	private static final int OTHER_ILLEGAL_POSITIVE_INDEX = 1000;

	private static final int ILLEGAL_NEGATIVE_INDEX = -2;

	private static final int STARTING_NUMBER_OF_BOOKS = 5;

	private static final String OUT_OF_BOUNDS_MESSAGE = "Index was out of bounds,"
			+ " but the operation was still completed.";

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
		assertTrue(bookshelf.getSelectedBookIndex() == -1); // not selected
		try {
			bookshelf.getSelectedBook();
			fail("Current book on an empty bookshelf should not exist.");
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
		assertFalse(bookshelf.getSelectedBookIndex() == ILLEGAL_POSITIVE_INDEX);

		// Test a negative integer beyond the allowed indices (-1 is allowed)
		bookshelf.setSelectedBookIndex(ILLEGAL_NEGATIVE_INDEX);
		assertFalse(bookshelf.getSelectedBookIndex() == ILLEGAL_NEGATIVE_INDEX);

		// Test -1
		bookshelf.setSelectedBookIndex(-1);
		assertTrue(bookshelf.getSelectedBookIndex() == -1);

		// Test index when removing a book
		bookshelf.setSelectedBookIndex(2);
		// nothing should happen to selected index since this is the last
		// element
		bookshelf.removeBookAt(6);
		assertTrue(bookshelf.getSelectedBookIndex() == 2);
		bookshelf.removeBookAt(1);
		// we removed something earlier in the list, so the index should
		// decrement
		assertTrue(bookshelf.getSelectedBookIndex() == 1);
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
		bookshelf.removeBookAt(ILLEGAL_NEGATIVE_INDEX);
		bookshelf.removeBookAt(ILLEGAL_POSITIVE_INDEX);

		// Assert that a book is removed and that it is the correct one
		bookshelf.removeBookAt(2);
		assertTrue(bookshelf.getBookAt(0).equals(BOOK0));
		assertTrue(bookshelf.getBookAt(1).equals(BOOK1));

		// the other books should now have new indices
		assertTrue(bookshelf.getBookAt(2).equals(BOOK3));
		assertTrue(bookshelf.getBookAt(3).equals(BOOK4));

		// Assert that the number of books is correct
		assertTrue(bookshelf.getNumberOfBooks() == STARTING_NUMBER_OF_BOOKS - 1);

		// Assert that removing a book does nothing to an empty bookshelf
		bookshelf = new Bookshelf();
		Bookshelf testBookshelf = new Bookshelf(bookshelf);
		bookshelf.removeBookAt(0);
		assertTrue(testBookshelf.equals(bookshelf));
	}

	/**
	 * Tests moving a book.
	 */
	public void testMoveBook() {
		// Make a copy to compare with
		Bookshelf copy = new Bookshelf(bookshelf);

		try {
			// Try to move from an illegal index to an illegal index
			bookshelf.moveBook(ILLEGAL_POSITIVE_INDEX,
					OTHER_ILLEGAL_POSITIVE_INDEX);
			assertTrue(copy.equals(bookshelf));
			fail(OUT_OF_BOUNDS_MESSAGE);
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		try {
			// Try to move from an illegal index to a legal index
			bookshelf.moveBook(ILLEGAL_POSITIVE_INDEX,
					OTHER_LEGAL_POSITIVE_INDEX);
			assertTrue(copy.equals(bookshelf));
			fail(OUT_OF_BOUNDS_MESSAGE);
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		try {
			// Try to move from a legal index to an illegal index
			bookshelf.moveBook(LEGAL_POSITIVE_INDEX,
					OTHER_ILLEGAL_POSITIVE_INDEX);
			assertTrue(copy.equals(bookshelf));
			fail(OUT_OF_BOUNDS_MESSAGE);
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		try {
			// Try to move from a legal index to a legal index
			bookshelf
					.moveBook(LEGAL_POSITIVE_INDEX, OTHER_LEGAL_POSITIVE_INDEX);
			assertFalse(copy.equals(bookshelf));
			fail(OUT_OF_BOUNDS_MESSAGE);
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		// Assert that it was moved correctly
		assertTrue(bookshelf.getBookAt(3).equals(copy.getBookAt(1)));
		assertTrue(bookshelf.getBookAt(2).equals(copy.getBookAt(2)));
		assertTrue(bookshelf.getBookAt(1).equals(copy.getBookAt(3)));
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
