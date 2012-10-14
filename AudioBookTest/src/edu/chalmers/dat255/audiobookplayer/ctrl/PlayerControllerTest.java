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

package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Test case for PlayerController (MediaPlayer wrapper class).
 * 
 * @author Aki K�kel�
 * 
 */
public class PlayerControllerTest extends TestCase {

	private PlayerController pc;
	private Bookshelf bs;
	
	private static final int TIME = 1212923;
	private static final String PATH = "/mnt/sdcard/game.mp3";
	private static final String BOOKNAME = "Lord of the Rings";

	public PlayerControllerTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();

		bs = new Bookshelf();
		pc = new PlayerController(bs);
		
		Track t1 = new Track(PATH, TIME);
		
		List<Track> tracks = new LinkedList<Track>();
		tracks.add(t1);
		
		Book b1 = new Book(tracks, BOOKNAME);
		bs.addBook(b1);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPlayerController() {
		assertTrue(pc != null);
	}

	public void testStop() {
		pc.stop();
		pc.start();
	}

	public void testStart() {
		// it should not be started yet
		assertTrue(!pc.isStarted());

		pc.start();

		// make sure that it is now
		assertTrue(pc.isStarted());

		// check the threads
		assertTrue(pc.getTrackTimeUpdateThread().isAlive());
		assertTrue(!pc.getTrackTimeUpdateThread().isInterrupted());
	}

	public void testStartAt() {
		pc.startAt(0);

		// test very long (596+ hours) start time that should be out of bounds
		pc.startAt(Integer.MAX_VALUE);
	}

	public void testPlayPause() {
		fail("Not yet implemented");
	}

	public void testPreviousTrack() {
		fail("Not yet implemented");
	}

	public void testNextTrack() {
		fail("Not yet implemented");
	}

	public void testSeekRight() {
		fail("Not yet implemented");
	}

	public void testSeekLeft() {
		fail("Not yet implemented");
	}

	public void testSeekToPercentageInTrack() {
		fail("Not yet implemented");
	}

	public void testSeekToPercentageInBook() {
		fail("Not yet implemented");
	}

	public void testSeekTo() {
		fail("Not yet implemented");
//		pc.seekTo(15102501);
	}

}
