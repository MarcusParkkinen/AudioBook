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

package edu.chalmers.dat255.audiobookplayer.util;

import junit.framework.TestCase;
import android.os.Environment;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * JUnit test for TrackCreator class.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * 
 *         NOTE: in order for the test to pass, a valid path to an audio track
 *         on the device must be specified in VALID_PATH.
 * 
 */
public class TrackCreatorTest extends TestCase {
	/**
	 * NOTE:
	 * 
	 * MUST BE VALID IN ORDER FOR TESTS TO PASS
	 * 
	 */
	private static final String FILE_NAME = "/audiobooks/huckleberry finn/huck_finn_chap01-text.mp3";
	/**
	 * NOTE:
	 * 
	 * MUST BE VALID IN ORDER FOR TESTS TO PASS
	 * 
	 */
	private static final String VALID_PATH = Environment
			.getExternalStorageDirectory().getPath() + FILE_NAME;

	private static final String INVALID_PATH = "/invalidPath/myInvalidTrack.mp3";

	/**
	 * Tests if creating tracks works as intended.
	 */
	public void testCreateTrack() {

		// If trying to create a track with an invalid path, we should
		// receive an exception
		try {
			TrackCreator.createTrack(INVALID_PATH);
			fail("managed to create a track object with an invalid path");
		} catch (IllegalArgumentException e) {
			// everything ok
		}

		// If we however specify a valid path, track creation should work
		Track t = null;
		try {
			t = TrackCreator.createTrack(VALID_PATH);
			assertNotNull(t);
		} catch (IllegalArgumentException e) {
			/*
			 * See the class description or FILE_NAME, VALID_PATH. These were
			 * not set correctly if this exception is thrown.
			 */
			assertNull(t);
		}
	}
}
