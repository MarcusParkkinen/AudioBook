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

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Test case for the main activity of the application.
 * 
 * @author Aki K�kel�
 * 
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private MainActivity activity;

	public MainActivityTest() {
		super("edu.chalmers.dat255.audiobookplayer.view.MainActivity", MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		activity = getActivity();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	// test states
	//instr.callactivity*(activity);
	public void testOnDestroy() {
		fail("Not yet implemented");
	}
	
	public void testStatePause() {
		Instrumentation instr = this.getInstrumentation();
		instr.callActivityOnPause(activity);
	}

	public void testOnCreateBundle() {
		fail("Not yet implemented");
	}

	public void testPreviousTrack() {
		fail("Not yet implemented");
	}

	public void testPlayPause() {
		fail("Not yet implemented");
	}

	public void testNextTrack() {
		fail("Not yet implemented");
	}

	public void testSeekLeft() {
		fail("Not yet implemented");
	}

	public void testSeekRight() {
		fail("Not yet implemented");
	}

	public void testSeekToPercentageInBook() {
		fail("Not yet implemented");
	}

	public void testSeekToPercentageInTrack() {
		fail("Not yet implemented");
	}

	public void testBookSelected() {
		fail("Not yet implemented");
	}

	public void testBookLongPress() {
		fail("Not yet implemented");
	}

	public void testAddButtonPressed() {
		fail("Not yet implemented");
	}

	public void testBookmarkSet() {
		fail("Not yet implemented");
	}

	public void testTagSet() {
		fail("Not yet implemented");
	}

	public void testPropertyChange() {
		fail("Not yet implemented");
	}

}
