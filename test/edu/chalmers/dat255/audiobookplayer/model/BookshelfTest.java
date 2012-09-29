package edu.chalmers.dat255.audiobookplayer.model;

import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * @author Marcus Parkkinen
 * @version 0.1
 */
public class BookshelfTest extends TestCase {
	private ArrayList<Book> books;
	
	//Create books and tracks
	private Track t0 = new Track("/thePath/theTrack1.mp3", 5);
	private Track t1 = new Track("/thePath/theTrack2.mp3", 10);
	
	private Track t2 = new Track("/thePath/theTrack3.mp3", 15);
	private Track t3 = new Track("/thePath/theTrack4.mp3", 20);
	
	private Track t4 = new Track("/thePath/theTrack3.mp3", 25);
	private Track t5 = new Track("/thePath/theTrack4.mp3", 30);
	
	private Book b1;
	private Book b2;
	private Book b3;
	
	
	private Bookshelf bookshelf;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		b1.addTrackTo(0, t0);
		b1.addTrackTo(0, t1);
		
		b2.addTrackTo(0, t2);
		b2.addTrackTo(0, t3);
		
		b3.addTrackTo(0, t4);
		b3.addTrackTo(0, t5);
		
		books = new ArrayList<Book>();
		books.add(b1);
		books.add(b2);
		books.add(b3);
		
		bookshelf = new Bookshelf();
		for(Book b : books) {
			bookshelf.addBook(b);
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCopy() {
		// create a new copy of the bookshelf
		Bookshelf newBookshelf = new Bookshelf(bookshelf);
						
		// assert that we have two separate objects
		assertFalse(newBookshelf == bookshelf);
						
		// assert that both books are equal
		assertTrue(newBookshelf.equals(bookshelf));
				
		// also try this with bookshelves that are empty
		bookshelf = new Bookshelf();
		newBookshelf = new Bookshelf(bookshelf);
				
		// assert that we have two separate objects
		assertFalse(newBookshelf == bookshelf);
									
		// assert that both books are equal
		assertTrue(newBookshelf.equals(bookshelf));
	}
	
	public void testSetSelectedBook() {
		bookshelf.setSelectedBook(3);
		
		// WIP //
	}
}
