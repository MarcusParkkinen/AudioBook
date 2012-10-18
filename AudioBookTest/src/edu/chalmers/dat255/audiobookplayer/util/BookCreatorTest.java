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

package edu.chalmers.dat255.audiobookplayer.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import android.os.Environment;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;

/**
 * JUnit test for BookCreator class.
 * 
 * NOTE: in order for the test to pass, a valid path to an audio track on the
 * device must be specified in VALID_PATH.
 * 
 */
public class BookCreatorTest extends TestCase {
	// MUST BE SPECIFIED IN ORDER FOR TESTS TO PASS
	private static final String FILE_NAME = "/Music/01-nocturne-ube.mp3";
	private static final String VALID_PATH = Environment
			.getExternalStorageDirectory().getPath() + FILE_NAME;
	private static final int NUMBER_OF_TRACKS = 125;
	private BookCreator bc;
	private Bookshelf bs;
	private List<String> paths;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
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
		for (int i = 0; i < NUMBER_OF_TRACKS; i++) {
			paths.add(VALID_PATH);
		}
	}

	/**
	 * Tests the singleton getInstance method.
	 */
	public void testSingleton() {
		// We should always get the same instance
		assertEquals(bc, BookCreator.getInstance());
	}

	/**
	 * Tests creating a book.
	 */
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
