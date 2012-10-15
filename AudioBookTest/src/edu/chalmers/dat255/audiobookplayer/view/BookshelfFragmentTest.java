/**
 * 
 */
package edu.chalmers.dat255.audiobookplayer.view;

import android.support.v4.app.FragmentActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.chalmers.dat255.audiobookplayer.R;

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

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		FragmentActivity mActivity = getActivity();
		assertNotNull(mActivity.getSupportFragmentManager());
/*
		assertNotNull((Button) mActivity.findViewById(R.id.addBook));
		assertNotNull((ExpandableListView) mActivity.findViewById(R.id.bookshelfList));

		assertNotNull((TextView)mActivity.findViewById(R.id.bookshelfTrackTitle));
		assertNotNull((TextView)mActivity.findViewById(R.id.bookshelfTrackTitle));
		assertNotNull((TextView)mActivity.findViewById(R.id.bookshelfTrackTime));
		assertNotNull((TextView)mActivity.findViewById(R.id.bookshelfBookTitle));
		assertNotNull((TextView)mActivity.findViewById(R.id.bookshelfAuthor));
		assertNotNull((TextView)mActivity.findViewById(R.id.bookshelfBookPosition));

		assertNotNull((TextView)mActivity.findViewById(R.id.bookshelfBookDuration));
		assertNotNull((ProgressBar) mActivity.findViewById(R.id.bookshelfProgressBar));
		assertNotNull((ImageView)mActivity.findViewById(R.id.bookshelfBookCover));
*/

	}

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	/*
	/**
	 * Test method for {@link edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment#onAttach(android.app.Activity)}.
	 */
	public void testOnAttachActivity() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}.
	 */
	public void testOnCreateViewLayoutInflaterViewGroupBundle() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)}.
	 */
	public void testOnCreateContextMenuContextMenuViewContextMenuInfo() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment#onContextItemSelected(android.view.MenuItem)}.
	 */
	public void testOnContextItemSelectedMenuItem() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment#childClicked(int, int)}.
	 */
	public void testChildClicked() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment#groupClicked(int)}.
	 */
	public void testGroupClicked() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment#bookAdded(edu.chalmers.dat255.audiobookplayer.model.Book)}.
	 */
	public void testBookAdded() {
		fail("Not yet implemented");
	}

}
