package edu.chalmers.dat255.audiobookplayer.util;

import java.util.ArrayList;
import java.util.List;

import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import junit.framework.TestCase;

/*
 * JUnit test for BookCreator class.
 * 
 * NOTE: in order for the test to pass, a valid path
 * to an audio track on the device must be specified 
 * in VALID_PATH.
 */

public class BookCreatorTest extends TestCase {
	// MUST BE SPECIFIED IN ORDER FOR TESTS TO PASS
	private final String VALID_PATH = "/sdcard/Music/01-nocturne-ube.mp3";
	private final int AMOUNT_OF_TRACKS = 125;
	private BookCreator bc;
	private Bookshelf bs;
	private List<String> paths;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		// Instantiate a BookCreator and a bookshelf for testing
		bc = BookCreator.getInstance();
		bs = new Bookshelf();
		
		// We do not wish to update any view components during testing
		bs.addPropertyChangeListener(null);
		
		// Set the bookcreator to use the dummy bookshelf
		bc.setBookshelf(bs);
		
		// Populate the list with valid paths
		paths = new ArrayList<String>();
		for(int i = 0; i < AMOUNT_OF_TRACKS; i++) {
			paths.add(VALID_PATH);
		}
	}
	
	public void testSingleton() {
		// We should always get the same instance
		assertEquals(bc, BookCreator.getInstance());
	}
	
	public void testCreateBook() {
		// Try creating a book with a valid path
		bc.createBook(paths, "Testbook", "Tester");
		
		// Assert that all tracks that were provided were
		// added to the book
		assertEquals(paths.size(), bs.getBookAt(0).getTrackPaths().size());
		
		// Also assert that the paths are correct
		assertEquals(paths, bs.getBookAt(0).getTrackPaths());
	}
}
