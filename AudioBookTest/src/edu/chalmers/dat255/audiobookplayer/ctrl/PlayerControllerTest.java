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

package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.util.LinkedList;
import java.util.List;

import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.model.Track;
import junit.framework.TestCase;

/**
 * Test case for PlayerController (MediaPlayer wrapper class).
 * 
 * @author Aki K�kel�
 * @version 0.1
 * 
 */
public class PlayerControllerTest extends TestCase {

	// the controller to be tested
	private PlayerController pc;

	// create a bookshelf to have something to mutate
	private Bookshelf bs;

	// some hard-coded fields of objects at test
	private static final int TIME = 1212923;
	private static final String PATH = "/mnt/sdcard/game.mp3";
	private static final String BOOKNAME = "Lord of the Rings";
	private static final String AUTHOR = "Tolkien";

	// the 2:nd track counted from zero
	private static final int TRACK_INDEX = 1;

	/**
	 * @param name
	 */
	public PlayerControllerTest(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		// re-create the bookshelf and player for each test
		bs = new Bookshelf();

		// put some content into the bookshelf
		Track t0 = new Track(PATH, TIME);
		Track t1 = new Track(PATH, TIME);
		Track t2 = new Track(PATH, TIME);

		List<Track> tracks = new LinkedList<Track>();
		tracks.add(t0);
		tracks.add(t1);
		tracks.add(t2);

		Book b1 = new Book(tracks, BOOKNAME, AUTHOR);
		bs.addBook(b1);

		pc = new PlayerController(bs);

		// set a track index for testing purposes
		bs.setSelectedTrackIndex(TRACK_INDEX);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();

		// stop the audio player and updater
		pc.stopTimer();
		pc.stop();
	}

	/**
	 * Constructor.
	 * 
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#PlayerController(edu.chalmers.dat255.audiobookplayer.model.Bookshelf)}
	 * .
	 */
	public final void testPlayerController() {
		// ensure that the player and bookshelf are both instantiated
		assertNotNull(bs);
		assertNotNull(pc);
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#stopTimer()}
	 * .
	 */
	public final void testStopTimer() {
		// start the timer
		pc.startTimer();

		// stop it and ensure that pc is no longer playing
		pc.stopTimer();
		assertFalse(pc.isStarted());

		// check that we have interrupted the thread (by calling stopTimer)
		assertTrue(pc.getTrackTimeUpdateThread().isInterrupted());
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#startTimer()}
	 * .
	 */
	public final void testStartTimer() {
		// start the timer and make sure pc is started
		pc.startTimer();
		assertTrue(pc.isStarted());

		// check that we have started the thread (by calling startTimer)
		assertTrue(pc.getTrackTimeUpdateThread().isAlive());
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#stop()}.
	 */
	public final void testStop() {
		pc.start();
		pc.stop();
		
		// it should not be started nor playing
		assertFalse(pc.isPlaying());
		assertFalse(pc.isStarted());
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#start()}
	 * .
	 */
	public final void testStart() {
		pc.start();
		
		// it should not be both started and playing
		assertTrue(pc.isPlaying());
		assertTrue(pc.isStarted());
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#startAt(int)}
	 * .
	 */
	public final void testStartAt() {
		fail("Not yet implemented - SEEK"); // TODO
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#pause()}
	 * .
	 */
	public final void testPause() {
		// pause and start
		pc.start();
		pc.pause();
		
		// it SHOULD be started
		assertTrue(pc.isStarted());
		
		// it should NOT be playing audio
		assertFalse(pc.isPlaying());
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#resume()}
	 * .
	 */
	public final void testResume() {
		// start pc
		pc.start();

		// pause it to resume it again.
		pc.pause();

		pc.resume();
		// resumed means that pc is both started and playing
		// (see javadoc for the difference between the two).
		assertTrue(pc.isPlaying());

		assertTrue(pc.isStarted());
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#previousTrack()}
	 * .
	 */
	public final void testPreviousTrack() {
		// since we have 3 tracks, it should simply go from index 1 to index 0
		pc.previousTrack();
		assertTrue(pc.getBs().getSelectedTrackIndex() == TRACK_INDEX - 1);
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#nextTrack()}
	 * .
	 */
	public final void testNextTrack() {
		// since we have 3 tracks, it should simply go from index 1 to index 2
		pc.nextTrack();
		assertTrue(pc.getBs().getSelectedTrackIndex() == TRACK_INDEX + 1);
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#seekRight(boolean)}
	 * .
	 */
	public final void testSeekRight() {
		fail("Not yet implemented - SEEK"); // TODO
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#seekLeft(boolean)}
	 * .
	 */
	public final void testSeekLeft() {
		fail("Not yet implemented - SEEK"); // TODO
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#seekToPercentageInTrack(double)}
	 * .
	 */
	public final void testSeekToPercentageInTrack() {
		fail("Not yet implemented - SEEK"); // TODO
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#seekToPercentageInBook(double)}
	 * .
	 */
	public final void testSeekToPercentageInBook() {
		fail("Not yet implemented - SEEK"); // TODO
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#seekTo(int)}
	 * .
	 */
	public final void testSeekTo() {
		pc.seekTo(TIME);
		fail("Not yet implemented - SEEK"); // TODO
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#isStarted()}
	 * .
	 */
	public final void testIsStarted() {
		// should not be started initially
		assertFalse(pc.isStarted());

		// start pc and check again
		pc.start();
		assertTrue(pc.isStarted());

		// pause and make sure it is still started
		pc.pause();
		assertTrue(pc.isStarted());
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController#isPlaying()}
	 * .
	 */
	public final void testIsPlaying() {
		// should not be playing initially
		assertFalse(pc.isStarted());

		// start pc and check again
		pc.start();
		assertTrue(pc.isStarted());

		// pause and check again
		pc.pause();
		assertFalse(pc.isPlaying());

		// resume and make sure it is now playing
		pc.resume();
		assertTrue(pc.isPlaying());
	}

}
