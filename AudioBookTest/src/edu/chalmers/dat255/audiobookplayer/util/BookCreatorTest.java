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
import android.util.Log;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;

/**
 * JUnit test for BookCreator class.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * 
 *         NOTE: in order for the test to pass, a valid path to an audio track
 *         on the device must be specified in VALID_PATH.
 * 
 */
public class BookCreatorTest extends TestCase {
	/**
	 * NOTE:
	 * 
	 * MUST BE VALID IN ORDER FOR TESTS TO PASS
	 * 
	 */
	private static final String FILE_NAME = "/Music/01-nocturne-ube.mp3";
	/**
	 * NOTE:
	 * 
	 * MUST BE VALID IN ORDER FOR TESTS TO PASS
	 * 
	 */
	private static final String VALID_PATH = Environment
			.getExternalStorageDirectory().getPath() + FILE_NAME;

	private static final int NUMBER_OF_TRACKS = 125;
	private final BookCreator bc = BookCreator.getInstance();
	private final Bookshelf bs = new Bookshelf();
	private List<String> paths;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() {
		try {
			super.setUp();
		} catch (Exception e) {
			// catch exceptions from super.setUp() and fail
			fail("setUp failed + " + e.getMessage());
		}

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
		Log.d("FAILURE", "##### testCreateBook #####");
		// Try creating a book with a valid path
		if (bc.createBookToBookshelf(paths, "Testbook", "Tester")) {
			// Assert that all tracks that were provided were
			// added to the book
			assertEquals(paths.size(), bs.getBookAt(0).getTrackPaths().size());

			Log.d("FAILURE", "----------");

			// Also assert that the paths are correct
			assertEquals(paths, bs.getBookAt(0).getTrackPaths());
		}

	}
}
