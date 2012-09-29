package edu.chalmers.dat255.audiobookplayer.model;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Aki Käkelä
 * @version 0.1
 *
 */
public class BookshelfTest extends TestCase {

	private Bookshelf bookshelf;

	public BookshelfTest(String name) {
		super(name);
	}

	protected static void setUpBeforeClass() throws Exception {
	}

	protected static void tearDownAfterClass() throws Exception {
	}

	protected void setUp() throws Exception {
		super.setUp();
		bookshelf = new Bookshelf();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testBookshelf() {
		assertTrue(bookshelf.getCurrentBookIndex() == -1); // not selected
		try {
			bookshelf.getCurrentBook();
			fail("Current book on an empty bookshelf should not exist.");
		} catch (IndexOutOfBoundsException e) { }
	}

	public void testBookshelfBookshelf() {
		// empty
		Bookshelf testBookshelf = new Bookshelf(bookshelf);
		assertFalse(testBookshelf == bookshelf);
		assertTrue(bookshelf.getCurrentBookIndex() == testBookshelf.getCurrentBookIndex());
		assertTrue(bookshelf != null);
		assertTrue(testBookshelf != null);
		
		assertTrue(bookshelf.equals(testBookshelf));
		
		// not empty
		List<Track> tracks = new LinkedList<Track>();
		tracks.add(new Track("path1", 111));
		tracks.add(new Track("path2", 222));
		tracks.add(new Track("path3", 333));
		Book book1 = new Book("1");
		Book book2 = new Book("2");
		Book book3 = new Book(tracks, "3");
		bookshelf.addBook(book1);
		bookshelf.addBook(book2);
		bookshelf.addBook(book3);
		
		testBookshelf = new Bookshelf(bookshelf);
	}

	public void testSetSelectedBook() {
		// Test a negative integer
		bookshelf.setSelectedBookIndex(-5);
		assertFalse(bookshelf.getCurrentBookIndex() == -5);
		
		Book b = new Book("Test");
		bookshelf.addBook(b);
		bookshelf.addBook(b);
		bookshelf.addBook(b);
		bookshelf.addBook(b);
		
		// Test a positive integer within the allowed indices
		bookshelf.setSelectedBookIndex(2);
		assertTrue(bookshelf.getCurrentBookIndex() == 2);
		
		// Test a positive integer beyond the allowed indices
		bookshelf.setSelectedBookIndex(13);
		assertFalse(bookshelf.getCurrentBookIndex() == 13);
		
		// Test index when adding a book
		bookshelf = new Bookshelf();
		bookshelf.addBook(b);
		bookshelf.addBook(b);
		bookshelf.addBook(b);
		bookshelf.addBook(b);
		bookshelf.setSelectedBookIndex(2);
		bookshelf.addBook(b);
		assertTrue(bookshelf.getCurrentTrackIndex() == 2);
		
		// Test index when removing a book
		bookshelf.setSelectedBookIndex(3);
		bookshelf.removeBook(5);
		assertTrue(bookshelf.getCurrentTrackIndex() == 3);
		bookshelf.removeBook(2);
		assertTrue(bookshelf.getCurrentTrackIndex() == (3-1));
		
		bookshelf = new Bookshelf();
		bookshelf.removeBook(0);
	}

	public void testAddBook() {
		Book b = new Book("testBook");
		bookshelf.addBook(b);
	}

	public void testRemoveBook() {
	}

	public void testMoveBook() {
	}

	public void testGetCurrentBook() {
	}

	public void testGetNumberOfBooks() {
	}

	public void testEqualsObject() {
	}

}
