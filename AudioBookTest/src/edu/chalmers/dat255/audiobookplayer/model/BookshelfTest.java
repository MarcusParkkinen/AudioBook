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

import junit.framework.TestCase;

/**
 * @author Aki K�kel�
 * @version 0.1
 * 
 */
public class BookshelfTest extends TestCase {

	private Bookshelf bookshelf;

	// Tracks
	private final Track t0 = new Track("path1", 111);
	private final Track t1 = new Track("path2", 222);
	private final Track t2 = new Track("path2", 333);

	// List of tracks
	private final List<Track> tracks = new LinkedList<Track>();

	// Books
	private final Book b0 = new Book("testBook0");
	private final Book b1 = new Book("testBook1");
	private final Book b2 = new Book("testBook2");
	private final Book b3 = new Book("testBook3");
	private final Book b4 = new Book("testBook4");

	public BookshelfTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		bookshelf = new Bookshelf();
		bookshelf.addPropertyChangeListener(null);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testBookshelf() {
		assertTrue(bookshelf.getSelectedBookIndex() == -1); // not selected
		try {
			bookshelf.getSelectedBook();
			fail("Current book on an empty bookshelf should not exist.");
		} catch (IndexOutOfBoundsException e) {
		}
	}

	public void testBookshelfBookshelf() {
		/*
		 * Testing empty bookshelves
		 */
		Bookshelf testBookshelf = new Bookshelf(bookshelf);

		// Assert that created bookshelves are not null
		assertTrue(bookshelf != null);
		assertTrue(testBookshelf != null);

		// Assert that bookshelves created through the copy constructor don't
		// have
		// the same reference
		assertFalse(testBookshelf == bookshelf);

		// Assert that bookshelves created through the copy constructor are
		// equal
		assertTrue(bookshelf.equals(testBookshelf));

		/*
		 * Testing non-empty bookshelves
		 */
		// Create one with tracks
		tracks.add(t0);
		tracks.add(t1);
		tracks.add(t2);
		Book book3 = new Book(tracks, "3", "author");

		// Add it into the same bookshelf as two other books without tracks
		bookshelf.addBook(b0);
		bookshelf.addBook(b1);
		bookshelf.addBook(book3);

		// Make a copy
		testBookshelf = new Bookshelf(bookshelf);

		// Check that they are equal (but not with the same reference)
		assertEquals(bookshelf, testBookshelf);
		assertFalse(testBookshelf == bookshelf);
	}

	public void testSetSelectedBook() {
		// Add elements to the bookshelf (to add to the allowed indices)
		bookshelf.addBook(b0);
		bookshelf.addBook(b1);
		bookshelf.addBook(b2);
		bookshelf.addBook(b3);

		// Test the accessor
		bookshelf.setSelectedBookIndex(2);
		assertTrue(bookshelf.getSelectedBookIndex() == 2);

		// Test a positive integer beyond the allowed indices
		bookshelf.setSelectedBookIndex(13);
		assertFalse(bookshelf.getSelectedBookIndex() == 13);

		// Test a negative integer (which are always beyond the allowed indices)
		bookshelf.setSelectedBookIndex(-5);
		assertFalse(bookshelf.getSelectedBookIndex() == -5);

		// Test a positive integer within the allowed indices
		bookshelf.setSelectedBookIndex(2);
		assertTrue(bookshelf.getSelectedBookIndex() == 2);

		// Test index when adding a book
		bookshelf = new Bookshelf();
		bookshelf.addPropertyChangeListener(null);
		bookshelf.addBook(b0); // index 0
		bookshelf.addBook(b1);
		bookshelf.addBook(b2);
		bookshelf.addBook(b3); // index 3
		bookshelf.setSelectedBookIndex(2);
		bookshelf.addBook(b4);
		bookshelf.addBook(b1);
		bookshelf.addBook(b3); // index 6
		assertTrue(bookshelf.getSelectedBookIndex() == 2);

		// Assert that the number of books is the same if the index is illegal
		int nbrOfBooks = bookshelf.getNumberOfBooks();
		assertTrue(nbrOfBooks == 7);
		bookshelf.removeBookAt(7);
		assertTrue(bookshelf.getNumberOfBooks() == nbrOfBooks);
		bookshelf.removeBookAt(-1);
		assertTrue(bookshelf.getNumberOfBooks() == nbrOfBooks);

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
		// Assert that the book is added
		bookshelf.addBook(b0);

		// Since this is the first book, it should be the 'current' book
		assertTrue(bookshelf.getSelectedBook() == b0);
		bookshelf.addBook(b1);
		bookshelf.addBook(b2);
		bookshelf.addBook(b3);

		// Check that we have the correct number of books
		assertTrue(bookshelf.getNumberOfBooks() == 4);
	}

	public void testRemoveBook() {
		// Assert that removing a book does nothing to an empty bookshelf
		bookshelf.removeBookAt(0);
		Bookshelf testBookshelf = new Bookshelf(bookshelf);
		assertTrue(testBookshelf.equals(bookshelf));

		// Assert that a book is removed and that it is the correct one
		bookshelf.addBook(b0);
		bookshelf.addBook(b1);
		bookshelf.addBook(b2);
		bookshelf.removeBookAt(0);
		assertTrue(bookshelf.getBookAt(0).equals(b1));
		assertTrue(bookshelf.getBookAt(1).equals(b2));

		// Assert that the number of books is correct
		assertTrue(bookshelf.getNumberOfBooks() == 2);
	}

	public void testMoveBook() {
		// Add books to the bookshelf
		bookshelf.addBook(b0);
		bookshelf.addBook(b1);
		bookshelf.addBook(b2);
		bookshelf.addBook(b3);
		bookshelf.addBook(b4);

		Bookshelf copy = new Bookshelf(bookshelf);

		// Try to move from an illegal index to an illegal index
		bookshelf.moveBook(5, 7);
		assertTrue(copy.equals(bookshelf));

		// Try to move from an illegal index to a legal index
		bookshelf.moveBook(5, 3);
		assertTrue(copy.equals(bookshelf));

		// Try to move from a legal index to an illegal index
		bookshelf.moveBook(1, 7);
		assertTrue(copy.equals(bookshelf));

		// Try to move from a legal index to a legal index
		bookshelf.moveBook(1, 3);
		assertFalse(copy.equals(bookshelf));

		// Assert that it was moved correctly
		assertTrue(bookshelf.getBookAt(3).equals(copy.getBookAt(1)));
		assertTrue(bookshelf.getBookAt(2).equals(copy.getBookAt(2)));
		assertTrue(bookshelf.getBookAt(1).equals(copy.getBookAt(3)));
	}

	public void testGetNumberOfBooks() {
		// An empty bookshelf should have zero books
		assertTrue(bookshelf.getNumberOfBooks() == 0);

		// Add 3 books
		bookshelf.addBook(b0);
		bookshelf.addBook(b1);
		bookshelf.addBook(b2);

		// Assert that the number of books is now 3
		assertTrue(bookshelf.getNumberOfBooks() == 3);
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
