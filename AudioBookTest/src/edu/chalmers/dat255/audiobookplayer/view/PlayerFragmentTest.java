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

package edu.chalmers.dat255.audiobookplayer.view;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Test case for the player fragment class.
 * 
 * @author Aki K�kel�
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
