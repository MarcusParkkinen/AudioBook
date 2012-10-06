package edu.chalmers.dat255.audiobookplayer.view;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Test case for the player fragment class.
 * 
 * @author Aki Käkelä
 * 
 */
public class PlayerFragmentTest extends ActivityInstrumentationTestCase2 {

	public PlayerFragmentTest(Class activityClass) {
		super(activityClass);
	}

	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testOnAttachActivity() {
		fail("Not yet implemented");
	}

	public void testOnCreateViewLayoutInflaterViewGroupBundle() {
		fail("Not yet implemented");
	}

	public void testStateDestroy() {
		// TODO: mock activity
	}

	// Updates
	public void testUpdateBookSeekBar() {
		fail("Not yet implemented");
	}

	public void testUpdateTrackSeekBar() {
		fail("Not yet implemented");
	}

	public void testUpdateBookTitleLabel() {
		fail("Not yet implemented");
	}

	public void testUpdateTrackTitleLabel() {
		fail("Not yet implemented");
	}

	public void testUpdateBookElapsedTimeLabel() {
		fail("Not yet implemented");
	}

	public void testUpdateTrackElapsedTimeLabel() {
		fail("Not yet implemented");
	}

	public void testUpdateBookDurationLabel() {
		fail("Not yet implemented");
	}

	public void testUpdateTrackDurationLabel() {
		fail("Not yet implemented");
	}

}
