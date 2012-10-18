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
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import edu.chalmers.dat255.audiobookplayer.R;

/**
 * Tests the BrowserActivity class.
 * 
 * @author Fredrik Åhs
 * 
 */
public class BrowserActivityTest extends
		ActivityInstrumentationTestCase2<BrowserActivity> {
	private ListView browserListView;
	private BrowserActivity bActivity;
	private Button createBookButton;

	/**
	 * @param name
	 */
	public BrowserActivityTest(String name) {
		super(BrowserActivity.class);
		setName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	@SmallTest
	protected void setUp() throws Exception {
		super.setUp();
		bActivity = getActivity();
		assertNotNull("BrowserActivity null", bActivity);
		browserListView = (ListView) bActivity.findViewById(R.id.browserList);
		assertNotNull("BrowserActivity browserList null", browserListView);
		createBookButton = (Button) bActivity.findViewById(R.id.createBook);
		assertNotNull("BrowserActivity createBook null", createBookButton);

	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.view.BrowserActivity#onCreate(android.os.Bundle)}
	 * .
	 */
	@SmallTest
	public void testOnCreateBundle() {
		assertNotNull(bActivity);
	}

	/**
	 * Asserts that all fields are visible on the screen.
	 */
	@SmallTest
	public void testFieldsOnScreen() {
		final View origin = bActivity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(origin, browserListView);
		ViewAsserts.assertOnScreen(origin, createBookButton);
	}

	/**
	 * Asserts that the list was filled
	 */
	public void testListViewFilled() {
		assertTrue("browserListView not filed",
				browserListView.getChildCount() > 0);
	}

	/**
	 * Asserts that checkboxes can be clicked to be selected and that they are
	 * initiated as false
	 */
	public void testCheckBox() {
		for (int i = 0; i < browserListView.getChildCount(); i++) {
			CheckBox cb = (CheckBox) browserListView.getChildAt(i)
					.findViewById(R.id.checkBox);
			assertNotNull("checkbox null", cb);
			assertFalse("checkbox initiated as true", cb.isChecked());
			TouchUtils.clickView(this, cb);
			ViewAsserts.assertOnScreen(browserListView.getChildAt(i), cb);
			assertTrue("checkbox could not check", cb.isChecked());
		}
	}

	/**
	 * Tests that a folder is clickable. This requires the device to have at
	 * least one audio file within a folder (not directly on sdcard).
	 */
	public void testClickFolder() {
		View child = browserListView.getChildAt(0);
		String preString = child.toString();
		TouchUtils.clickView(this, child);
		View clicked = browserListView.getChildAt(0);
		assertNotNull("Clicked null", clicked);
		// assert the file was clicked. Variable clicked will be a view with the
		// title ".." and the description "Parent Folder"
		assertFalse(
				"Clicked did not change (this test requires there to be at least one audio file within a directory).",
				preString.equals(clicked.toString()));
	}

	/**
	 * Tests whether the button to create a new book works.
	 */
	public void testCreateButton() {
		for (int i = 0; i < browserListView.getChildCount(); i++) {
			CheckBox cb = (CheckBox) browserListView.getChildAt(i)
					.findViewById(R.id.checkBox);
			if (!cb.isChecked()) {
				TouchUtils.clickView(this, cb);
			}
			assertTrue(cb.isChecked());
		}
		// TODO(testCreateButton) : BookCreator's Bookshelf is null, bypass this
		// somehow
		// As bookshelf is null in BookCreator and there seems to be no possible
		// way to
		// check whether a toast was displayed or not, this method is deemed to
		// be too hard to implement.
		// TouchUtils.clickView(this,createBookButton);-
	}

}
