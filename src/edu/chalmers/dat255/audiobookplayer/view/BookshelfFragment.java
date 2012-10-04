package edu.chalmers.dat255.audiobookplayer.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Graphical representation of the bookshelf.
 * 
 * @author Marcus Parkkinen, Fredrik Ã…hs
 * @version 0.5
 */

public class BookshelfFragment extends Fragment {
	private static final String TAG = "BookshelfFragment.class";
	private List<Entry<Book, List<String>>> listData;
	private ExpandableBookshelfAdapter adapter;
	private BookshelfUIEventListener parentFragment;

	public interface BookshelfUIEventListener {
		/**
		 * Informs the listener that the currently selected book should change.
		 * @param groupPosition The index of the book that now should be selected.
		 */
		public void bookSelected(int groupPosition);

		/**
		 * Informs the listener that a book has been long pressed.
		 * @param index The index of the book pressed.
		 */
		public void bookLongPress(int index);

		/**
		 * Informs the listener that the add button has been pressed.
		 * @param v
		 */
		public void addButtonPressed(View v);

		/**
		 * Informs the listener that the currently selected child should change.
		 * @param groupPosition Position of the book.
		 * @param childPosition Position of the track.
		 */
		public void childSelected(int groupPosition, int childPosition);

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
			listData = new ArrayList<Entry<Book, List<String>>>();
			adapter = new ExpandableBookshelfAdapter(view.getContext(),
					listData);
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

		//hides the by default visible arrow indicating a whether a group is expanded or not
		bookshelfList.setGroupIndicator(null);
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
		/*
		bookshelfList.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (getActivity() != null) {
					// parentFragment.childSelected();
				}
				return true;
			}
		});
		 */

		bookshelfList.setAdapter(adapter);

		return view;
	}

	protected void childClicked(int groupPosition, int childPosition) {
		if (getActivity() != null) {
			parentFragment.childSelected(groupPosition, childPosition);
		}
	}

	protected void groupClicked(int groupPosition) {
		if (getActivity() != null) {
			parentFragment.bookSelected(groupPosition);
		}
	}


	/**
	 * Private help class that holds an entry with a key and a value
	 * 
	 * @author Fredrik Åhs
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
		listData.add(new BookshelfEntry<Book, List<String>>(b, b.getTrackTitles()));
		// Notify the adapter that the list has changedz
		adapter.notifyDataSetChanged();
	}

	/**
	 * Private class used to populate the ExpandableListView used in BookshelfFragment
	 * @author LASER
	 *
	 */
	private class ExpandableBookshelfAdapter extends BaseExpandableListAdapter {

		private Context context;
		private List<Entry<Book, List<String>>> listData;
		private int selectedIndex; 

		public ExpandableBookshelfAdapter(Context context, List<Entry<Book, List<String>>> listData) { //IndexedMap<String, List<String>> listData) {
			this.context = context;
			this.listData = listData;
			selectedIndex = -1;
		}

		public String getChild(int groupPosition, int childPosition) {
			return listData.get(groupPosition).getValue().get(childPosition);
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return listData.get(groupPosition).getValue().size();
		}

		public View getChildView(final int groupPosition, final int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.bookshelf_child_row, parent, false);
			}
			setTextViewText(convertView, R.id.bookshelfTrackTitle, getChild(groupPosition, childPosition));

			int duration = getGroup(groupPosition).getTrackDurationAt(childPosition) / 1000;
			//set the duration of the track
			setTextViewText(convertView, R.id.bookshelfTrackTime, DateUtils.formatElapsedTime(duration));

			//inform bookshelffragments listener a child has been selected
			convertView.setOnClickListener(new OnClickListener() {			
				public void onClick(View v) {
					BookshelfFragment.this.childClicked(groupPosition, childPosition);
				}
			});
			return convertView;
		}

		public Book getGroup(int groupPosition) {
			return listData.get(groupPosition).getKey();
		}

		public int getGroupCount() {
			return listData.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}
		
		public View getGroupView(final int groupPosition, final boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.bookshelf_group_row, parent, false);
				}		
			final Book book = getGroup(groupPosition);
			int duration = book.getDuration()/1000;
			//prevent problems with a duration of 0
			if (duration == 0) {
				return null;
			}
			int time = book.getBookElapsedTime()/1000;			

			//set title, author, time and duration of book
			setTextViewText(convertView, R.id.bookshelfBookTitle, book.getTitle());
			if(groupPosition == selectedIndex) {
				setTextViewTextColor(convertView, R.id.bookshelfBookTitle, Color.RED);
			} else {
				setTextViewTextColor(convertView, R.id.bookshelfBookTitle, Color.WHITE);
			}
			setTextViewText(convertView, R.id.bookshelfAuthor, book.getAuthor());
			String timeString = time == 0 ? "N/A" : DateUtils.formatElapsedTime(time);
			setTextViewText(convertView, R.id.bookshelfBookPosition, "Position: " + timeString );			
			setTextViewText(convertView, R.id.bookshelfBookDuration, "Duration: " + DateUtils.formatElapsedTime(duration));

			//prevent a higher value than 100
			int progress = time > duration ? 100 : 100 * time / duration;
			//set the progress of the progressbar
			ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.bookshelfProgressBar);
			if(progress >= 0 && progress <= 100) {
				progressBar.setProgress(progress);
			}
			//set coverart
			final ExpandableListView expandableListView = (ExpandableListView) parent;
			ImageView imageView = (ImageView)convertView.findViewById(R.id.bookshelfBookCover);
			imageView.setImageResource(R.drawable.no_cover);
			//click the coverart to 'open or close the book' and show tracks (expands/collapses book)
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if(isExpanded){
						expandableListView.collapseGroup(groupPosition);		
					}
					else{
						expandableListView.expandGroup(groupPosition);
					}
				}
			});
			//ongroupclick in bookshelffragment no longer functions correctly, this is a workaround.
			convertView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					//expand the selected item
					if( ! isExpanded) {
						expandableListView.expandGroup(groupPosition);
					}
					//scroll to the selected item 
					expandableListView.setSelectionFromTop(groupPosition,0);
					//sets the currently selected index
					selectedIndex = groupPosition;
					//invalidates views to force redraw thus setting the correct textcolor
					expandableListView.invalidateViews();
					
					BookshelfFragment.this.groupClicked(groupPosition);
				}				
			});
			return convertView;
		}

		private void setTextViewText(View view, int id, String text) {
			TextView textView = (TextView) view.findViewById(id);
			if(textView != null) {
				textView.setText(text);
			}
		}
		
		private void setTextViewTextColor(View view, int id,
				int color) {
			TextView textView = (TextView) view.findViewById(id);
			if(textView != null) {
				textView.setTextColor(color);
			}			
		}
		
		public boolean hasStableIds() {
			return true;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}	
	}
}
