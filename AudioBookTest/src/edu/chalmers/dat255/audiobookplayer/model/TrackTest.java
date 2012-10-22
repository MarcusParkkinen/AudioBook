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

import junit.framework.TestCase;

/**
 * Tests constructing and copying a track, getting the track path, and getting
 * and setting the elapsed time of the selected track.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.2
 */

public class TrackTest extends TestCase {
	// The test object.
	private Track t;

	// Some values for creating tracks.
	private static final String TRACK_PATH = "/audiobooks/huckleberry finn/huck_finn_chap01-text.mp3";
	private static final int TRACK_DURATION = 1238921;
	private static final int ELAPSED_TIME = 238238;

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
		t = new Track(TRACK_PATH, TRACK_DURATION);
		t.setSelectedTrackElapsedTime(ELAPSED_TIME);
	}

	/**
	 * Tests the constructor.
	 */
	public void testConstructor() {
		// try creating a track with 'null' as path
		try {
			t = new Track(null, TRACK_DURATION);
			fail("Constructor did not throw exception for null path.");
		} catch (IllegalArgumentException e) {

		}

		// try creating a track with illegal path string
		try {
			t = new Track("", TRACK_DURATION);
			fail("Constructor did not throw exception for empty string as path");
		} catch (IllegalArgumentException e) {

		}

		// try creating a track with 0 as duration
		try {
			t = new Track(TRACK_PATH, 0);
			fail("Constructor did not throw exception for zero duration.");
		} catch (IllegalArgumentException e) {

		}

		// try creating a track with -1 as duration
		try {
			t = new Track(TRACK_PATH, -1);
			fail("Constructor did not throw exception for negative duration.");
		} catch (IllegalArgumentException e) {

		}
	}

	/**
	 * Tests the copy constructor.
	 */
	public void testCopy() {
		// create a new copy of the track
		Track newTrack = new Track(t);

		// assert that we have two separate objects
		assertNotSame(newTrack, t);

		// but assert that they are equal
		assertTrue(newTrack.equals(t));
	}

	/**
	 * Tests getting the track path.
	 */
	public void testGetTrackPath() {
		assertTrue(TRACK_PATH.equals(t.getTrackPath()));
	}

	/**
	 * Tests getting the elapsed time.
	 */
	public void testGetElapsedTime() {
		assertEquals(ELAPSED_TIME, t.getElapsedTime());
	}

	/**
	 * Tests setting the elapsed time of the selected track.
	 */
	public void testSetSelectedTrackElapsedTime() {
		// test legal bound values
		t.setSelectedTrackElapsedTime(TRACK_DURATION);
		t.setSelectedTrackElapsedTime(0);

		// try setting a negative value for the track
		try {
			t.setSelectedTrackElapsedTime(-1);
			fail("managed to set time to negative value.");
		} catch (IllegalArgumentException e) {

		}

		// try setting a time > duration
		t.setSelectedTrackElapsedTime(TRACK_DURATION + 1);
		assertEquals(TRACK_DURATION, t.getElapsedTime());
	}

}
