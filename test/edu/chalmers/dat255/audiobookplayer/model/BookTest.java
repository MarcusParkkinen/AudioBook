package edu.chalmers.dat255.audiobookplayer.model;

import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * @author Marcus Parkkinen
 * @version 0.1
 */
public class BookTest extends TestCase {
	private ArrayList<Track> bList;
	private String bookName = "MyTestBook";
	private Book b;
	
	//Tracks to test the book with
	private Track t0 = new Track("/thePath/theTrack1.mp3", 5);
	private Track t1 = new Track("/thePath/theTrack2.mp3", 10);
	private Track t2 = new Track("/thePath/theTrack3.mp3", 15);
	private Track t3 = new Track("/thePath/theTrack4.mp3", 20);
	private Track[] tracks = {t0, t1, t2, t3};
	
	private Track t5 = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		bList = new ArrayList<Track>();
		
		for(int i = 0; i < tracks.length; i++) {
			bList.add(tracks[i]);
		}
		
		b = new Book(bList, bookName);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testConstructor() {
		
		// assert that four tracks have been added
		b = new Book(bList, bookName);
		assertEquals(4, b.getNumberOfTracks());
		
		// add a null track to the list
		bList.add(t5);
		
		// assert that the null track doesn't get added to the book
		b = new Book(bList, bookName);
		assertEquals(4, b.getNumberOfTracks());
		
		// assert that the duration of the book has been calculated correctly
		assertEquals(50, b.getDuration());
	}
	
	public void testCopy() {		
		// create a new copy of the book
		Book newBook = new Book(b);
				
		// assert that we have two separate objects
		assertFalse(newBook == b);
		
		// assert that the track objects are deep copies as well
		assertFalse(newBook.getCurrentTrack() == b.getCurrentTrack());
				
		// assert that both books are equal
		assertTrue(newBook.equals(b));
		
		// also try this with books that are empty
		b = new Book(bookName);
		newBook = new Book(b);
		
		// assert that we have two separate objects
		assertFalse(newBook == b);
							
		// assert that both books are equal
		assertTrue(newBook.equals(b));
	}
	
	public void testRemoveTrack() {
		// remove the first track
		int duration = b.getDuration();
		
		// for all tracks in the book..
		for(int i = 0; i < tracks.length; i++) {
			// assert that the current track index adjusts accordingly
			assertTrue(b.getCurrentTrack().equals(tracks[i]));
			
			// remove the track that is on the first index
			b.removeTrack(0);
			
			// assert that the amount of tracks is correct
			assertEquals(3-i, b.getNumberOfTracks());
			
			// assert that the duration adjusts accordingly
			assertEquals((duration-=tracks[i].getDuration()), b.getDuration());
		}
		
		// assert that no track is selected if the book is lacking tracks
		assertEquals(-1, b.getCurrentTrackIndex());
	}
	
	public void testAddTrack() {
		int duration = 0;
		b = new Book(bookName);
		
		for(int i = 0; i < tracks.length-1; i++) {
			
			// add a new track to the beginning of the book
			b.addTrackTo(0, tracks[i]);
			
			// assert that the selected track index does not change even
			// when adding tracks to indices before it
			assertTrue(b.getCurrentTrack().equals(tracks[0]));
			
			
			// assert that the amount of tracks changes accordingly
			assertEquals(i+1, b.getNumberOfTracks());
			
			// assert that the duration is correct
			assertEquals((duration+=tracks[i].getDuration()), b.getDuration());
		}
		
		// assert that the selected track index is correct when adding
		// tracks to indices after it
		b.addTrackTo(tracks.length-1, tracks[tracks.length-1]);
		assertTrue(b.getCurrentTrack().equals(tracks[0]));
	}
	
	public void testSwap() {
		b.setCurrentTrackIndex(1);
		
		// swap tracks 0 and 1
		b.swap(0, 1);
		assertTrue(b.getCurrentTrack().equals(t0));
		
		// assert that elapsed time of the book adjusts accordingly
		assertEquals(t1.getDuration(), b.getBookElapsedTime());
		
		// swap tracks 0 and 1 again
		b.swap(0, 1);
		assertTrue(b.getCurrentTrack().equals(t1));
		
		assertEquals(t0.getDuration(), b.getBookElapsedTime());
	}
	
	public void testMoveTrack() {
		b.setCurrentTrackIndex(1);
		
		// assert that the current track is t1
		assertTrue(b.getCurrentTrack().equals(tracks[1]));
		
		// swap t0 and t1
		b.moveTrack(0, 1);
		
		// assert that the current track now is t0
		assertTrue(b.getCurrentTrack().equals(tracks[0]));
		
		// also assert that the elapsed time has adjusted accordingly
		assertEquals(tracks[1].getDuration(), b.getBookElapsedTime());
	}
	
	public void setCurrentTrackIndex() {
		b.setCurrentTrackIndex(tracks.length-1);
		
		// assert that the index is set correctly
		assertEquals(tracks.length-1, b.getCurrentTrackIndex());
		
		// assert that the elapsed time of the book is adjusted
		// accordingly
		int elapsedTime = 0;
		for(int i = 0; i < tracks.length-1; i++) {
			elapsedTime += tracks[i].getDuration();
		}
		
		assertEquals(elapsedTime, b.getBookElapsedTime());
	}
	
	public void testIncrementCurrentTrackIndex() {
		int trackIndex = b.getCurrentTrackIndex();
		b.incrementTrackIndex();
		
		// assert that we increment the track index correctly
		assertEquals(trackIndex+1, b.getCurrentTrackIndex());
		
		// increment the track index past the final track of the book ...
		while(b.getCurrentTrackIndex() != tracks.length-1) {
			b.incrementTrackIndex();
		}
		b.incrementTrackIndex();
		
		// ... and assert that no track is selected
		assertEquals(-1, b.getCurrentTrackIndex());
		
		// also assert that this is the case even if we add more tracks to the book
		b.addTrackTo(0, t0);
		assertEquals(-1, b.getCurrentTrackIndex());
	}
	
	public void testSetBookTitle() {
		// assert that we cannot set the book name with a null string
		try{
			b.setBookTitle(null);
			fail("managed to set book title with null string");
		} catch(IllegalArgumentException e) {
			//assert that the old name still applies
			assertEquals(bookName, b.getTitle());
		}
		
		// but that we can set it to a new valid name
		String anotherTitle = "e";
		b.setBookTitle(anotherTitle);
		assertEquals(anotherTitle, b.getTitle());
	}
}
