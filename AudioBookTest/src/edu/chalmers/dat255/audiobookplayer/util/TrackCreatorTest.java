package edu.chalmers.dat255.audiobookplayer.util;

import junit.framework.TestCase;
import android.os.Environment;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * JUnit test for TrackCreator class.
 * 
 * NOTE: in order for the test to pass, a valid path to an audio track on the
 * device must be specified in VALID_PATH.
 * 
 */
public class TrackCreatorTest extends TestCase {
	// MUST BE SPECIFIED IN ORDER FOR TEST TO PASS
	private static final String FILE_NAME = "/Music/01-nocturne-ube.mp3";
	private static final String VALID_PATH = Environment
			.getExternalStorageDirectory().getPath() + FILE_NAME;
	private String invalidPath;

	protected void setUp() throws Exception {
		super.setUp();

		invalidPath = "/invalidPath/myInvalidTrack.mp3";
	}

	public void testCreateTrack() {

		// If trying to create a track with an invalid path, we should
		// receive an exception
		try {
			TrackCreator.createTrack(invalidPath);
			fail("managed to create a track object with an invalid path");
		} catch (IllegalArgumentException e) {
			// everything ok
		}

		// If we however specify a valid path, track creation should work
		Track t = TrackCreator.createTrack(VALID_PATH);
		assertNotNull(t);
	}
}
