package edu.chalmers.dat255.audiobookplayer.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.model.Book;

/**
 * Graphical representation of the bookshelf.
 * 
 * @author Marcus Parkkinen, Fredrik Åhs
 * @version 0.5
 */

public class BookshelfFragment extends Fragment {
	private static final String TAG = "BookshelfFragment.class";
	private List<Entry<String, List<String>>> listData;
	private ExpandableBookshelfAdapter adapter;
	private BookshelfUIEventListener parentFragment;

	public interface BookshelfUIEventListener {
		public void bookSelected(int groupPosition);

		public void bookLongPress(int index);

		public void addButtonPressed(View v);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			parentFragment = (BookshelfUIEventListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " does not implement BookshelfUIEventListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Provide the layout of this fragment to the parent container
		View view = inflater.inflate(R.layout.fragment_bookshelf, container,
				false);

		/**************************************************************/
		/**//**/
		/* Instantiate member variables */
		/**//**/
		/**************************************************************/
		if (listData == null && adapter == null) {
			listData = new ArrayList<Entry<String, List<String>>>();
			adapter = new ExpandableBookshelfAdapter(view.getContext(),
					listData, this);
		}

		/**************************************************************/
		/**//**/
		/* Get button layout, and add a listener method to it */
		/**//**/
		/**************************************************************/

		Button addButton = (Button) view.findViewById(R.id.addBook);

		addButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Get the parent activity

				// Make sure that the activity is not at the end of its
				// lifecycle
				if (getActivity() != null) {
					parentFragment.addButtonPressed(v);
				}
			}
		});

		/******************************************************************************/
		/**//**/
		/* Get the list layout, and add listener methods and an adapter to it */
		/**//**/
		/******************************************************************************/
		 ExpandableListView bookshelfList = (ExpandableListView) view
				.findViewById(R.id.bookshelfList);

		// called when a list item is clicked
		/*
		 * Does not function at the moment, workaround implemented in adapter.
		 * bookshelfList.setOnGroupClickListener(new OnGroupClickListener() {
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

				if (getActivity() != null) {
					parentFragment.bookSelected(groupPosition);
				}

				return true;
			}
		});*/
		

		// called when a sub-list item is clicked
		bookshelfList.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (getActivity() != null) {
					// parentFragment.childSelected();
				}
				return true;
			}
		});
		
		
		bookshelfList.setAdapter(adapter);

		return view;
	}
	
	
	protected void groupClicked(int groupPosition) {
		if (getActivity() != null) {
			parentFragment.bookSelected(groupPosition);
		}
	}

	/**
	 * Private help class that holds an entry with a key and a value
	 * 
	 * @author Fredrik �hs
	 * @version 0.1
	 */
	private class BookshelfEntry<K, V> implements Map.Entry<K, V> {

		private K key;
		private V value;

		public BookshelfEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		public V setValue(V object) {
			this.value = object;
			return value;
		}
	}

	/**
	 * @param b
	 */
	public void bookAdded(Book b) {
		Log.d(TAG, "Book added");
		// Add a new entry to 'values'
		listData.add(new BookshelfEntry<String, List<String>>(b.getTitle(), b
				.getPaths()));
		// Notify the adapter that the list has changed
		adapter.notifyDataSetChanged();
	}
}
