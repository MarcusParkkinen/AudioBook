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

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Test for Bookshelf.
 * 
 * @author Aki K�kel�
 * @version 0.3
 * 
 */
public class BookshelfTest extends TestCase {

	private Bookshelf bookshelf;

	// Tracks
	private Track t0 = new Track("path1", 111);
	private Track t1 = new Track("path2", 222);
	private Track t2 = new Track("path3", 333);

	// List of tracks
	private List<Track> tracks;

	// Books
	private Book b0;
	private Book b1;
	private Book b2;
	private Book b3;
	private Book b4;

	// Constant values; 'magic numbers'
	private static final int LEGAL_POSITIVE_INDEX = 1;
	private static final int OTHER_LEGAL_POSITIVE_INDEX = 4;

	private static final int ILLEGAL_POSITIVE_INDEX = 5;
	private static final int OTHER_ILLEGAL_POSITIVE_INDEX = 1000;

	private static final int ILLEGAL_NEGATIVE_INDEX = -2;

	private static final int STARTING_NUMBER_OF_BOOKS = 5;

	protected void setUp() throws Exception {
		super.setUp();

		// NOTE: do not change these values; the tests depend on them

		bookshelf = new Bookshelf();
		bookshelf.addPropertyChangeListener(null);

		// Tracks
		t0 = new Track("path1", 111);
		t1 = new Track("path2", 222);
		t2 = new Track("path3", 333);

		// List of tracks
		tracks = new LinkedList<Track>();
		tracks.add(t0);
		tracks.add(t1);
		tracks.add(t2);

		// Books
		b0 = new Book(tracks, "testBook0", "author0");
		b1 = new Book("testBook1");
		b2 = new Book("testBook2");
		b3 = new Book("testBook3", "author3");
		b4 = new Book("testBook4");

		// Add elements to the bookshelf (to add to the allowed indices)
		bookshelf.addBook(b0);
		bookshelf.addBook(b1);
		bookshelf.addBook(b2);
		bookshelf.addBook(b3);
		bookshelf.addBook(b4);
	}

	/**
	 * Tests the constructor.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
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

	@Test(expected = IndexOutOfBoundsException.class)
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

	public void testAddBook() {
		// Make a new (empty) bookshelf
		bookshelf = new Bookshelf();

		// Add a book
		bookshelf.addBook(b0);

		// Assert that the book is added, and at the correct index
		assertEquals(bookshelf.getBookAt(0), b0);

		// Also check that the number of books is correct
		assertTrue(bookshelf.getNumberOfBooks() == 1);

		// Since this is the first book, it should be the 'current' book
		assertTrue(bookshelf.getSelectedBook().equals(b0));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testRemoveBook() {
		// Try to remove a book from an illegal index
		bookshelf.removeBookAt(ILLEGAL_NEGATIVE_INDEX);
		bookshelf.removeBookAt(ILLEGAL_POSITIVE_INDEX);

		// Assert that a book is removed and that it is the correct one
		bookshelf.removeBookAt(2);
		assertTrue(bookshelf.getBookAt(0).equals(b0));
		assertTrue(bookshelf.getBookAt(1).equals(b1));

		// the other books should now have new indices
		assertTrue(bookshelf.getBookAt(2).equals(b3));
		assertTrue(bookshelf.getBookAt(3).equals(b4));

		// Assert that the number of books is correct
		assertTrue(bookshelf.getNumberOfBooks() == STARTING_NUMBER_OF_BOOKS - 1);

		// Assert that removing a book does nothing to an empty bookshelf
		bookshelf = new Bookshelf();
		Bookshelf testBookshelf = new Bookshelf(bookshelf);
		bookshelf.removeBookAt(0);
		assertTrue(testBookshelf.equals(bookshelf));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testMoveBook() {
		// Make a copy to compare with
		Bookshelf copy = new Bookshelf(bookshelf);

		try {
			// Try to move from an illegal index to an illegal index
			bookshelf.moveBook(ILLEGAL_POSITIVE_INDEX,
					OTHER_ILLEGAL_POSITIVE_INDEX);
			assertTrue(copy.equals(bookshelf));
			fail("Index was out of bounds, but the operation was still completed.");
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		try {
			// Try to move from an illegal index to a legal index
			bookshelf.moveBook(ILLEGAL_POSITIVE_INDEX,
					OTHER_LEGAL_POSITIVE_INDEX);
			assertTrue(copy.equals(bookshelf));
			fail("Index was out of bounds, but the operation was still completed.");
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		try {
			// Try to move from a legal index to an illegal index
			bookshelf.moveBook(LEGAL_POSITIVE_INDEX,
					OTHER_ILLEGAL_POSITIVE_INDEX);
			assertTrue(copy.equals(bookshelf));
			fail("Index was out of bounds, but the operation was still completed.");
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		try {
			// Try to move from a legal index to a legal index
			bookshelf
					.moveBook(LEGAL_POSITIVE_INDEX, OTHER_LEGAL_POSITIVE_INDEX);
			assertFalse(copy.equals(bookshelf));
			fail("Index was out of bounds, but the operation was still completed.");
		} catch (IndexOutOfBoundsException e) {
			// expected
		}

		// Assert that it was moved correctly
		assertTrue(bookshelf.getBookAt(3).equals(copy.getBookAt(1)));
		assertTrue(bookshelf.getBookAt(2).equals(copy.getBookAt(2)));
		assertTrue(bookshelf.getBookAt(1).equals(copy.getBookAt(3)));
	}

	public void testGetNumberOfBooks() {
		// Assert that the number of books is correct
		assertTrue(bookshelf.getNumberOfBooks() == STARTING_NUMBER_OF_BOOKS);

		// Assert that an empty bookshelf has zero books
		bookshelf = new Bookshelf();
		assertTrue(bookshelf.getNumberOfBooks() == 0);
	}

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
