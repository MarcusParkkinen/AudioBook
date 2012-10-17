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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Marcus Parkkinen
 * @version 0.1
 */
public class BookTest extends TestCase {
	private List<Track> bList;
	private String bookName = "MyTestBook";
	private String bookAuthor = "MyTestBookAuthor";
	private Book b;
	
	//Tracks to test the book with
	private Track t0 = new Track("/thePath/theTrack1.mp3", 5);
	private Track t1 = new Track("/thePath/theTrack2.mp3", 10);
	private Track t2 = new Track("/thePath/theTrack3.mp3", 15);
	private Track t3 = new Track("/thePath/theTrack4.mp3", 20);
	private Track[] tracks = {t0, t1, t2, t3};
	
	protected void setUp() throws Exception {
		super.setUp();
		
		bList = new ArrayList<Track>();
		
		for(int i = 0; i < tracks.length; i++) {
			bList.add(tracks[i]);
		}
		
		b = new Book(bList, bookName, bookAuthor);
	}
	
	public void testConstructor() {
		
		// assert that four tracks have been added
		b = new Book(bList, bookName, bookAuthor);
		assertEquals(4, b.getNumberOfTracks());
		
		// add a null track to the list
		bList.add(null);
		
		// assert that the null track doesn't get added to the book
		b = new Book(bList, bookName, bookAuthor);
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
		assertFalse(newBook.getSelectedTrack() == b.getSelectedTrack());
				
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
			assertTrue(b.getSelectedTrack().equals(tracks[i]));
			
			// remove the track that is on the first index
			b.removeTrack(0);
			
			// assert that the amount of tracks is correct
			assertEquals(3-i, b.getNumberOfTracks());
			
			// assert that the duration adjusts accordingly
			duration -= tracks[i].getDuration();
			assertEquals(duration, b.getDuration());
		}
		
		// assert that no track is selected if the book is lacking tracks
		assertEquals(-1, b.getSelectedTrackIndex());
	}
	
	public void testAddTrack() {
		int duration = 0;
		b = new Book(bookName);
		
		for(int i = 0; i < tracks.length-1; i++) {
			
			// add a new track to the beginning of the book
			b.addTrack(tracks[i]);
			
			// assert that the selected track index does not change even
			// when adding tracks to indices before it
			assertTrue(b.getSelectedTrack().equals(tracks[0]));
			
			
			// assert that the amount of tracks changes accordingly
			assertEquals(i+1, b.getNumberOfTracks());
			
			// assert that the duration is correct
			duration += tracks[i].getDuration();
			assertEquals(duration, b.getDuration());
		}

	}
	
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
	
	public void setCurrentTrackIndex() {
		b.setSelectedTrackIndex(tracks.length-1);
		
		// assert that the index is set correctly
		assertEquals(tracks.length-1, b.getSelectedTrackIndex());
		
		// assert that the elapsed time of the book is adjusted
		// accordingly
		int elapsedTime = 0;
		for(int i = 0; i < tracks.length-1; i++) {
			elapsedTime += tracks[i].getDuration();
		}
		
		assertEquals(elapsedTime, b.getBookElapsedTime());
	}
	
	public void testSetBookTitle() {
		// assert that we cannot set the book name with a null string
		try{
			b.setSelectedBookTitle(null);
			fail("managed to set book title with null string");
		} catch(IllegalArgumentException e) {
			//assert that the old name still applies
			assertEquals(bookName, b.getSelectedBookTitle());
		}
		
		// but that we can set it to a new valid name
		String anotherTitle = "e";
		b.setSelectedBookTitle(anotherTitle);
		assertEquals(anotherTitle, b.getSelectedBookTitle());
	}
	
}
