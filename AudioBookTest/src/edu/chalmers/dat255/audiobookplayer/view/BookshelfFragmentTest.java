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

import android.support.v4.app.FragmentActivity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Fredrik Åhs
 * 
 */
public class BookshelfFragmentTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	/**
	 * @param name
	 */
	public BookshelfFragmentTest(String name) {
		super(MainActivity.class);
		setName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		FragmentActivity mActivity = getActivity();
		assertNotNull(mActivity.getSupportFragmentManager());
		/*
		 * assertNotNull((Button) mActivity.findViewById(R.id.addBook));
		 * assertNotNull((ExpandableListView)
		 * mActivity.findViewById(R.id.bookshelfList));
		 * 
		 * assertNotNull((TextView)mActivity.findViewById(R.id.bookshelfTrackTitle
		 * ));
		 * assertNotNull((TextView)mActivity.findViewById(R.id.bookshelfTrackTitle
		 * ));
		 * assertNotNull((TextView)mActivity.findViewById(R.id.bookshelfTrackTime
		 * ));
		 * assertNotNull((TextView)mActivity.findViewById(R.id.bookshelfBookTitle
		 * ));
		 * assertNotNull((TextView)mActivity.findViewById(R.id.bookshelfAuthor
		 * ));
		 * assertNotNull((TextView)mActivity.findViewById(R.id.bookshelfBookPosition
		 * ));
		 * 
		 * assertNotNull((TextView)mActivity.findViewById(R.id.bookshelfBookDuration
		 * )); assertNotNull((ProgressBar)
		 * mActivity.findViewById(R.id.bookshelfProgressBar));
		 * assertNotNull((ImageView
		 * )mActivity.findViewById(R.id.bookshelfBookCover));
		 */

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.ActivityInstrumentationTestCase2#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * /** Test method for {@link
	 * edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment
	 * #onAttach(android.app.Activity)}.
	 */
	public void testOnAttachActivity() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}
	 * .
	 */
	public void testOnCreateViewLayoutInflaterViewGroupBundle() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)}
	 * .
	 */
	public void testOnCreateContextMenuContextMenuViewContextMenuInfo() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment#onContextItemSelected(android.view.MenuItem)}
	 * .
	 */
	public void testOnContextItemSelectedMenuItem() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment#childClicked(int, int)}
	 * .
	 */
	public void testChildClicked() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment#groupClicked(int)}
	 * .
	 */
	public void testGroupClicked() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment#bookshelfUpdated(edu.chalmers.dat255.audiobookplayer.model.Book)}
	 * .
	 */
	public void testBookAdded() {
		fail("Not yet implemented");
	}

}
