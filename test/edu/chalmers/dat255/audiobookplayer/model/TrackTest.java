
package edu.chalmers.dat255.audiobookplayer.model;

import junit.framework.TestCase;

/**
 * @author Marcus Parkkinen
 * @version 0.1
 */

public class TrackTest extends TestCase {
	private final String trackPath = "MyTrack.mp3";
	private final int trackLength = 1238921;
	private final int elapsedTime = 238238;
	private Track t;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		t = new Track(trackPath, trackLength);
		t.setElapsedTime(elapsedTime);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public TrackTest() {
		super();
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
	
	public void testSetElapsedTime() {
		//test legal bound values
		t.setElapsedTime(trackLength);
		t.setElapsedTime(0);
		
		//try setting a negative value for the track
		try{
			t.setElapsedTime(-1);
			fail("managed to set time to negative value.");
		} catch(IllegalArgumentException e) {
			
		}
		
		//try setting a time > duration
		try{
			t.setElapsedTime(trackLength+1);
			fail("managed to set time to negative value.");
		} catch(IllegalArgumentException e) {
			
		}
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
