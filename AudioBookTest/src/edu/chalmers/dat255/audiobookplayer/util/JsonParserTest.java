/*
This work is licensed under the Creative Commons Attribution-NonCommercial-
NoDerivs 3.0 Unported License. To view a copy of this license, visit
http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to 
Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 
94041, USA.

Use of this work is permitted only in accordance with license rights granted.
Materials provided "AS IS"; no representations or warranties provided.

Copyright © 2012 Marcus Parkkinen, Aki Käkelä, Fredrik Åhs.
 */

package edu.chalmers.dat255.audiobookplayer.util;

import java.util.Arrays;

import android.test.AndroidTestCase;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Test case for JSONParser utility class.
 * 
 * @author Marcus Parkkinen
 * 
 */
public class JsonParserTest extends AndroidTestCase {
	private Bookshelf bs;

	protected void setUp() throws Exception {
		// Set up test environment by creating an object tree for testing
		Track[] tracks = new Track[] { new Track("trackPath", 1) };
		Book b = new Book(Arrays.asList(tracks), "BookTitle", "BookAuthor");

		bs = new Bookshelf();
		bs.addPropertyChangeListener(null);
		bs.addBook(b);

		super.setUp();
	}

	public void testToAndFromJSON() {
		// First convert the object tree to its JSON representation
		String jsonRepresentation = JsonParser.toJSON(bs);

		// And then convert it back
		Bookshelf newBookshelf = JsonParser.fromJSON(jsonRepresentation,
				Bookshelf.class);

		// Assert that the new bookshelf is equal to the original
		assertEquals(bs, newBookshelf);

		// But assert that they are not the same object
		assertNotSame(newBookshelf, bs);
	}
}
