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
 * @author Marcus Parkkinen
 * @version 0.1
 */

public class TrackTest extends TestCase {
	private static final String trackPath = "MyTrack.mp3";
	private static final int trackLength = 1238921;
	private static final int elapsedTime = 238238;
	private Track t;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		t = new Track(trackPath, trackLength);
		t.setSelectedTrackElapsedTime(elapsedTime);
	}
	
	public void testCopy() {
		//create a new copy of the track
		Track newTrack = new Track(t);
		
		//assert that we have two separate objects
		assertFalse(newTrack == t);
		
		//but assert that they are equal
		assertTrue(newTrack.equals(t));
	}
	
	public void testGetTrackPath() {
		assertTrue(trackPath.equals(t.getTrackPath()));
	}
	
	public void testGetElapsedTime() {
		assertEquals(elapsedTime, t.getElapsedTime());
	}
	
	public void testSetSelectedTrackElapsedTime() {
		//test legal bound values
		t.setSelectedTrackElapsedTime(trackLength);
		t.setSelectedTrackElapsedTime(0);
		
		//try setting a negative value for the track
		try{
			t.setSelectedTrackElapsedTime(-1);
			fail("managed to set time to negative value.");
		} catch(IllegalArgumentException e) {
			
		}
		
		//try setting a time > duration
		t.setSelectedTrackElapsedTime(trackLength+1);
		assertEquals(trackLength, t.getElapsedTime());
	}
	
	public void testConstructor() {
		//try creating a track with 'null' as path
		try{
			t = new Track(null, trackLength);
			fail("Constructor did not throw exception for null path.");
		} catch(IllegalArgumentException e) {
			
		}
		
		//try creating a track with illegal path string
		try{
			t = new Track("", trackLength);
			fail("Constructor did not throw exception for empty string as path");
		} catch(IllegalArgumentException e) {
			
		}
		
		//try creating a track with 0 as duration
		try{
			t = new Track(trackPath, 0);
			fail("Constructor did not throw exception for zero duration.");
		} catch(IllegalArgumentException e) {
			
		}
		
		//try creating a track with -1 as duration
		try{
			t = new Track(trackPath, -1);
			fail("Constructor did not throw exception for negative duration.");
		} catch(IllegalArgumentException e) {
			
		}
	}
}
