package edu.chalmers.dat255.audiobookplayer.test.util;

import java.util.Arrays;

import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.model.Track;
import edu.chalmers.dat255.audiobookplayer.util.JSONParser;
import android.test.AndroidTestCase;

/**
 * Test case for JSONParser utility class.
 * 
 * @author Marcus Parkkinen
 * 
 */
public class JSONParserTest extends AndroidTestCase{
	private Bookshelf bs;
	
	protected void setUp() throws Exception {
		// Set up test environment by creating an object tree for testing
		Track[] tracks = new Track[]{new Track("trackPath", 1)};
		Book b = new Book(Arrays.asList(tracks), "BookTitle");
		
		bs = new Bookshelf();
		bs.addPropertyChangeListener(null);
		bs.addBook(b);
		
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testToAndFromJSON() {
		// First convert the object tree to its JSON representation
		String JSONRepresentation = JSONParser.toJSON(bs);
		
		// And then convert it back
		Bookshelf newBookshelf = JSONParser.fromJSON(JSONRepresentation, Bookshelf.class);
		
		// Assert that the new bookshelf is equal to the original
		assertEquals(bs, newBookshelf);
		
		// But assert that they are not the same object
		assertFalse(bs == newBookshelf);
	}
}
