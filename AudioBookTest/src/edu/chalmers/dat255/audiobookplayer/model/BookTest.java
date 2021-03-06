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
 *  Copyright � 2012 Marcus Parkkinen, Aki K�kel�, Fredrik �hs.
 **/

package edu.chalmers.dat255.audiobookplayer.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * Tests the Book class, by testing cloning a book,
 * removing/adding/swapping/moving tracks, selecting tracks and setting the
 * title of the book.
 * 
 * @author Marcus Parkkinen, Aki K�kel�
 * @version 0.2
 */
public class BookTest extends TestCase {
	private List<Track> bList;
	private String bookName = "MyTestBook";
	private String bookAuthor = "MyTestBookAuthor";
	private Book b;

	private static final int DURATION0 = 5;
	private static final int DURATION1 = 10;
	private static final int DURATION2 = 15;
	private static final int DURATION3 = 20;

	private static final int TOTAL_DURATION = DURATION0 + DURATION1 + DURATION2
			+ DURATION3;

	// Tracks to test the book with
	private Track t0 = new Track("/audiobooks/huckleberry finn/huck_finn_chap01-text.mp3", DURATION0);
	private Track t1 = new Track("/audiobooks/huckleberry finn/huck_finn_chap02-text.mp3", DURATION1);
	private Track t2 = new Track("/audiobooks/huckleberry finn/huck_finn_chap03-text.mp3", DURATION2);
	private Track t3 = new Track("/audiobooks/huckleberry finn/huck_finn_chap04-text.mp3", DURATION3);

	private static final int TOTAL_NUMBER_OF_TRACKS = 4;

	private Track[] tracks = { t0, t1, t2, t3 };

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

		bList = new ArrayList<Track>();

		for (int i = 0; i < tracks.length; i++) {
			bList.add(tracks[i]);
		}

		b = new Book(bList, bookName, bookAuthor);
	}

	/**
	 * Tests the constructor.
	 */
	public void testConstructor() {

		// assert that four tracks have been added
		b = new Book(bList, bookName, bookAuthor);
		assertEquals(TOTAL_NUMBER_OF_TRACKS, b.getNumberOfTracks());

		// add a null track to the list
		bList.add(null);

		// assert that the null track doesn't get added to the book
		b = new Book(bList, bookName, bookAuthor);
		assertEquals(TOTAL_NUMBER_OF_TRACKS, b.getNumberOfTracks());

		// assert that the duration of the book has been calculated correctly
		assertEquals(TOTAL_DURATION, b.getDuration());
	}

	/**
	 * Tests the copy constructor.
	 */
	public void testCopy() {
		// create a new copy of the book
		Book newBook = new Book(b);

		// assert that we have two separate objects
		assertNotSame(newBook, b);

		// assert that the track objects are deep copies as well
		assertFalse(newBook.getSelectedTrack() == b.getSelectedTrack());

		// assert that both books are equal
		assertTrue(newBook.equals(b));

		// also try this with books that are empty
		b = new Book(bookName);
		newBook = new Book(b);

		// assert that we have two separate objects
		assertNotSame(newBook, b);

		// assert that both books are equal
		assertTrue(newBook.equals(b));
	}

	/**
	 * Tests removing tracks.
	 */
	public void testRemoveTrack() {
		// remove the first track
		int duration = b.getDuration();

		// for all tracks in the book..
		for (int i = 0; i < tracks.length; i++) {
			// assert that the current track index adjusts accordingly
			assertTrue(b.getSelectedTrack().equals(tracks[i]));

			// remove the track that is on the first index
			b.removeTrack(0);

			// assert that the amount of tracks is correct
			assertEquals(TOTAL_NUMBER_OF_TRACKS - i - 1, b.getNumberOfTracks());

			// assert that the duration adjusts accordingly
			duration -= tracks[i].getDuration();
			assertEquals(duration, b.getDuration());
		}

		// assert that no track is selected if the book is lacking tracks
		assertEquals(-1, b.getSelectedTrackIndex());
	}

	/**
	 * Tests adding tracks.
	 */
	public void testAddTrack() {
		int duration = 0;
		b = new Book(bookName);

		for (int i = 0; i < tracks.length - 1; i++) {

			// add a new track to the beginning of the book
			b.addTrack(tracks[i]);

			// assert that the selected track index does not change even
			// when adding tracks to indices before it
			assertTrue(b.getSelectedTrack().equals(tracks[0]));

			// assert that the amount of tracks changes accordingly
			assertEquals(i + 1, b.getNumberOfTracks());

			// assert that the duration is correct
			duration += tracks[i].getDuration();
			assertEquals(duration, b.getDuration());
		}

	}

	/**
	 * Tests swapping tracks.
	 */
	public void testSwap() {
		b.setSelectedTrackIndex(1);

		// swap tracks 0 and 1
		b.swapTracks(0, 1);
		assertTrue(b.getSelectedTrack().equals(t0));

		// assert that elapsed time of the book adjusts accordingly
		assertEquals(t1.getDuration(), b.getBookElapsedTime());

		// swap tracks 0 and 1 again
		b.swapTracks(0, 1);
		assertTrue(b.getSelectedTrack().equals(t1));

		assertEquals(t0.getDuration(), b.getBookElapsedTime());
	}

	/**
	 * Tests moving tracks.
	 */
	public void testMoveTrack() {
		b.setSelectedTrackIndex(1);

		// assert that the current track is t1
		assertTrue(b.getSelectedTrack().equals(tracks[1]));

		// swap t0 and t1
		b.moveTrack(0, 1);

		// assert that the current track now is t0
		assertTrue(b.getSelectedTrack().equals(tracks[0]));

		// also assert that the elapsed time has adjusted accordingly
		assertEquals(tracks[1].getDuration(), b.getBookElapsedTime());
	}

	/**
	 * Tests selecting tracks.
	 */
	public void setCurrentTrackIndex() {
		b.setSelectedTrackIndex(tracks.length - 1);

		// assert that the index is set correctly
		assertEquals(tracks.length - 1, b.getSelectedTrackIndex());

		// assert that the elapsed time of the book is adjusted
		// accordingly
		int elapsedTime = 0;
		for (int i = 0; i < tracks.length - 1; i++) {
			elapsedTime += tracks[i].getDuration();
		}

		assertEquals(elapsedTime, b.getBookElapsedTime());
	}

	/**
	 * Tests setting the title of a book.
	 */
	public void testSetBookTitle() {
		// assert that we cannot set the book name with a null string
		try {
			b.setSelectedBookTitle(null);
			fail("managed to set book title with null string");
		} catch (IllegalArgumentException e) {
			// assert that the old name still applies
			assertEquals(bookName, b.getSelectedBookTitle());
		}

		// but that we can set it to a new valid name
		String anotherTitle = "e";
		b.setSelectedBookTitle(anotherTitle);
		assertEquals(anotherTitle, b.getSelectedBookTitle());
	}

}
