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

	public BookshelfTest(String name) {
		super(name);
	}

	protected static void setUpBeforeClass() {
	}

	protected static void tearDownAfterClass() {
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
		// Testing empty bookshelves
		Bookshelf testBookshelf = new Bookshelf(bookshelf);
		// Test that created bookshelves are not null
		assertTrue(bookshelf != null);
		assertTrue(testBookshelf != null);
		// Test that bookshelves created through the copy constructor don't have
		// the same reference
		assertFalse(testBookshelf == bookshelf);
		// Test that bookshelves created through the copy constructor are equal
		assertTrue(bookshelf.equals(testBookshelf));

		// Testing non-empty bookshelves
		// Create some books
		Book book1 = new Book("1");
		Book book2 = new Book("2");
		// Create one with tracks
		List<Track> tracks = new LinkedList<Track>();
		tracks.add(new Track("path1", 111));
		tracks.add(new Track("path2", 222));
		tracks.add(new Track("path3", 333));
		Book book3 = new Book(tracks, "3", "author");
		// Add them into the same bookshelf
		bookshelf.addBook(book1);
		bookshelf.addBook(book2);
		bookshelf.addBook(book3);

		// Make a copy and check that they are equal (but not with the same
		// reference)
		testBookshelf = new Bookshelf(bookshelf);
		assertFalse(testBookshelf == bookshelf);
		assertTrue(bookshelf.equals(testBookshelf));
	}

	public void testSetSelectedBook() {
		// Add elements to the bookshelf (to add to the allowed indices)
		Book b = new Book("Test");
		bookshelf.addBook(b);
		bookshelf.addBook(b);
		bookshelf.addBook(b);
		bookshelf.addBook(b);

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
		bookshelf.addBook(b); // index 0
		bookshelf.addBook(b);
		bookshelf.addBook(b);
		bookshelf.addBook(b); // index 3
		bookshelf.setSelectedBookIndex(2);
		bookshelf.addBook(b);
		bookshelf.addBook(b);
		bookshelf.addBook(b); // index 6
		assertTrue(bookshelf.getSelectedBookIndex() == 2);

		// Test that the number of books is the same if the index is illegal
		int nbrOfBooks = bookshelf.getNumberOfBooks();
		assertTrue(nbrOfBooks == 7);
		bookshelf.removeBook(7);
		assertTrue(bookshelf.getNumberOfBooks() == nbrOfBooks);
		bookshelf.removeBook(-1);
		assertTrue(bookshelf.getNumberOfBooks() == nbrOfBooks);

		// Test index when removing a book
		bookshelf.setSelectedBookIndex(2);
		// nothing should happen to selected index since this is the last
		// element
		bookshelf.removeBook(6);
		assertTrue(bookshelf.getSelectedBookIndex() == 2);
		bookshelf.removeBook(1);
		// we removed something earlier in the list, so the index should
		// decrement
		assertTrue(bookshelf.getSelectedBookIndex() == 1);
	}

	public void testAddBook() {
		// Test that the book is added
		Book b = new Book("testBook");
		bookshelf.addBook(b);
		// Since this is the first book, it should be the 'current' book
		assertTrue(bookshelf.getSelectedBook() == b);
		bookshelf.addBook(b);
		bookshelf.addBook(b);
		bookshelf.addBook(b);

		// Check that we have the correct number of books
		assertTrue(bookshelf.getNumberOfBooks() == 4);
	}

	public void testRemoveBook() {
		// Test that a book is removed
		Book b1 = new Book("testBook1");
		Book b2 = new Book("testBook2");
		Book b3 = new Book("testBook3");
		bookshelf.addBook(b1);
		bookshelf.addBook(b2);
		bookshelf.addBook(b3);
		bookshelf.removeBook(0);
		assertTrue(bookshelf.getBookAt(0).equals(b2));
		assertTrue(bookshelf.getBookAt(1).equals(b3));

		// Test that the number of books is correct
		assertTrue(bookshelf.getNumberOfBooks() == 2);

		// Test that removing a book does nothing to an empty bookshelf
		bookshelf = new Bookshelf();
		bookshelf.removeBook(0);
		Bookshelf testBookshelf = new Bookshelf(bookshelf);
		assertTrue(testBookshelf.equals(bookshelf));
	}

	public void testMoveBook() {
		// Create some books
		Book b0 = new Book("testBook0");
		Book b1 = new Book("testBook1");
		Book b2 = new Book("testBook2");
		Book b3 = new Book("testBook3");
		Book b4 = new Book("testBook4");

		// Add them to the bookshelf
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
		// Check that it was moved correctly
		assertTrue(bookshelf.getBookAt(3).equals(copy.getBookAt(1)));
		assertTrue(bookshelf.getBookAt(2).equals(copy.getBookAt(2)));
		assertTrue(bookshelf.getBookAt(1).equals(copy.getBookAt(3)));
	}

	public void testGetCurrentBook() {
		// not yet implemented
	}

	public void testGetNumberOfBooks() {
		// not yet implemented
	}

	public void testEqualsObject() {
		// Test that they are equivalent (reflexive, symmetric and transitive)

		// Test that it is reflexive
		assertTrue(bookshelf.equals(bookshelf));

		// Test that it is symmetric
		Bookshelf testBookshelf = new Bookshelf(bookshelf);
		assertTrue(testBookshelf.equals(bookshelf));
		assertTrue(bookshelf.equals(testBookshelf));

		// Test that it is transitive
		Bookshelf testBookshelf2 = new Bookshelf(testBookshelf);
		assertTrue(testBookshelf2.equals(bookshelf));
		assertTrue(bookshelf.equals(testBookshelf2));
	}

}
