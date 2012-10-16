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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;
import edu.chalmers.dat255.audiobookplayer.interfaces.IBookshelfGUIEvents;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;

/**
 * Graphical representation of the bookshelf.
 * 
 * @author Marcus Parkkinen, Fredrik �hs
 * @version 0.6
 */

public class BookshelfFragment extends Fragment implements IBookshelfGUIEvents {
	private static final String TAG = "BookshelfFragment.class";
	private List<Entry<Book, List<String>>> listData;
	private ExpandableBookshelfAdapter adapter;
	private IBookshelfGUIEvents fragmentOwner;

	/**
	 * Simple interface forcing the enum classes to provide a method to get text. 
	 *
	 */
	private interface IContextMenuItem {
		/**
		 * This method should be used to provide the text to be shown by the menu items.
		 * @return The text to be displayed.
		 */
		public String getText();
	}

	/**
	 * Enum providing name and ID for context menu items
	 *
	 */
	private enum GroupContextMenuItem implements IContextMenuItem {
		Delete {
			public String getText() {
				return "Delete";
			}
		},
		Edit {
			public String getText() {
				return "Edit";
			}
		};
	}
	/**
	 * Enum providing name and ID for context menu items
	 *
	 */
	private enum ChildContextMenuItem implements IContextMenuItem {
		Delete {
			public String getText() {
				return "Delete";
			}
		},
		Edit {
			public String getText() {
				return "Edit";
			}
		};
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			fragmentOwner = (IBookshelfGUIEvents) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " does not implement "
					+ IBookshelfGUIEvents.class.getName());
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

		ImageButton addButton = (ImageButton) view.findViewById(R.id.addBook);
		//adds a listener to the button
		addButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Get the parent activity

				// Make sure that the activity is not at the end of its
				// lifecycle
				if (getActivity() != null) {
					//inform mainactivity this button has been pressed
					addBookButtonPressed();
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

		/*
		* hides the by default visible arrow which indicates whether a group is
		* expanded or not
		 */
		bookshelfList.setGroupIndicator(null);
		//set the bookshelf list as a context menu
		registerForContextMenu(bookshelfList);
		//sets the bookshelf lists adapter
		bookshelfList.setAdapter(adapter);
		// Access the bookshelf reference
		if (getArguments().getSerializable(Constants.Reference.BOOKSHELF) instanceof Bookshelf) {
			Bookshelf b = (Bookshelf) getArguments().getSerializable(
					Constants.Reference.BOOKSHELF);
			for (int i = 0; i < b.getNumberOfBooks(); i++) {
				bookAdded(b.getBookAt(i));
			}
		}
		//return the view when it has been processed.
		return view;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		//check that the menuInfo is of the correct type
		if (menuInfo instanceof ExpandableListContextMenuInfo) {
			ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;
			//get the provided book position
			int bookIndex = ExpandableListView
					.getPackedPositionGroup(info.packedPosition);
			// trackIndex will be -1 if group is clicked
			int trackIndex = ExpandableListView
					.getPackedPositionChild(info.packedPosition);
			//get the type of the context menu
			int type = ExpandableListView
					.getPackedPositionType(info.packedPosition);
			//create an empty array to prevent trying to loop over an uninitialized variable
			IContextMenuItem[] menuItems = new IContextMenuItem[0];
			String title = null;
			// fill the context menu with the correct items
			if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
				//get all menu items from the child context menu
				menuItems = ChildContextMenuItem.values();
				//set the context menu's title to that of the value of the child
				title = listData.get(bookIndex).getValue().get(trackIndex);
			} else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
				//get all menu items from the group context menu
				menuItems = GroupContextMenuItem.values();
				//set the context menu's title to that of the value of the book
				title = listData.get(bookIndex).getKey().getSelectedBookTitle();
			}
			// set the title
			menu.setHeaderTitle(title);
			// populate the context menu with items in the order they were
			// declared in the enum declaration.
			for (IContextMenuItem item : menuItems) {
				//as this only loops when menuItems is of either of type 
				//GroupContextMenuItem[] or ChildContextMenuItem[], Enum can be used as a raw type
				menu.add(Menu.NONE, ((Enum) item).ordinal(),
						((Enum) item).ordinal(), item.getText());
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item
				.getMenuInfo();
		//get the provided book position
		int bookIndex = ExpandableListView
				.getPackedPositionGroup(info.packedPosition);
		// will be -1 if the type is group
		int trackIndex = ExpandableListView
				.getPackedPositionChild(info.packedPosition);
		//get the type of the context menu
		int type = ExpandableListView
				.getPackedPositionType(info.packedPosition);

		//create an empty array to prevent trying to loop over an uninitialized variable
		IContextMenuItem[] menuItems = new IContextMenuItem[0];
		//fill the array with the correct items
		if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			menuItems = ChildContextMenuItem.values();
		} else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
			menuItems = GroupContextMenuItem.values();
		}

		// check that item has correct id (should not be needed)
		int itemId = item.getItemId();
		if (itemId >= menuItems.length || itemId < 0) {
			return false;
		}
		//get an item and store it with its dynamic type 
		IContextMenuItem menuItem = menuItems[itemId];

		// if the type of the context menu is group
		if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP
				&& menuItem instanceof GroupContextMenuItem
				&& getActivity() != null) {

			// perform the correct task
			switch ((GroupContextMenuItem) menuItem) {
			case Delete:
				removeBook(bookIndex);
				break;
			case Edit:
				setBookTitleAt(bookIndex, "NEWNAME");
				break;
			default:
				break;
			}

		}
		// if the type of the context menu is that of a child
		else if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD
				&& menuItem instanceof ChildContextMenuItem) {
			// perform the correct task
			switch ((ChildContextMenuItem) menuItem) {
			// TODO(Children Context Menu Implementation) : Add cases and methods to add menu options to the child
			default:
				break;

			}
		}
		return true;
	}

	private void childClicked(int bookIndex, int trackIndex) {
		if (getActivity() != null) {
			Log.d(TAG, "Child clicked at + [" + bookIndex + ", " + trackIndex
					+ "]");
			setSelectedTrack(bookIndex, trackIndex);
		}
	}

	private void groupClicked(int bookIndex) {
		if (getActivity() != null) {
			setSelectedBook(bookIndex);
		}
	}

	/**
	 * Private help class that holds an entry with a key and a value.
	 * 
	 * @author Fredrik �hs
	 * @version 0.1
	 */
	private class BookshelfEntry<K, V> implements Map.Entry<K, V> {

		private K key;
		private V value;

		/**
		 * Constructor of entry.
		 * @param key The key.
		 * @param value The value.
		 */
		public BookshelfEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * @return returns the key of the entry.
		 */
		public K getKey() {
			return key;
		}

		/**
		 * @return returns the value of the entry.
		 */
		public V getValue() {
			return value;
		}

		/**
		 * @param object sets the entry of the value;
		 * @return returns the value of the entry
		 */
		public V setValue(V object) {
			this.value = object;
			return getValue();
		}
	}

	/**
	 * Updates the class with a newly added book and informs its adapter that
	 * data has been added.
	 * 
	 * @param b
	 *            The newly added book.
	 */
	public void bookAdded(Book b) {
		Log.d(TAG, "Book added");
		// Add a new entry to 'values'
		listData.add(new BookshelfEntry<Book, List<String>>(b, b
				.getTrackTitles()));
		// Notify the adapter that the list has changed
		adapter.notifyDataSetChanged();
	}

	/**
	 * Private class used to populate the ExpandableListView used in
	 * BookshelfFragment.
	 * 
	 * @author Fredrik �hs
	 * 
	 */
	private class ExpandableBookshelfAdapter extends BaseExpandableListAdapter {

		private Context context;
		private List<Entry<Book, List<String>>> listData;
		private int selectedIndex;

		/**
		 * Constructs an ExpandableListAdapter with context and listData.
		 * @param context The context of the application.
		 * @param listData The data to be used to fill the list.
		 */
		public ExpandableBookshelfAdapter(Context context,
				List<Entry<Book, List<String>>> listData) {
			this.context = context;
			this.listData = listData;
			selectedIndex = -1;
		}
		/**
		 * @param bookIndex The position of the book.
		 * @param trackIndex The position of the track.
		 * @return The title of the track at given position.
		 */
		public String getChild(int bookIndex, int trackIndex) {
			return listData.get(bookIndex).getValue().get(trackIndex);
		}

		/**
		 * @param bookIndex The position of the book.
		 * @param trackIndex The position of the track.
		 * @return The id of the track at given position.
		 */
		public long getChildId(int bookIndex, int trackIndex) {
			return trackIndex;
		}


		/**
		 * @param bookIndex The position of the book.
		 * @return The amount of tracks in the book at given position.
		 */
		public int getChildrenCount(int bookIndex) {
			return listData.get(bookIndex).getValue().size();
		}

		/**
		 * Converts the view of a track to display properly.
		 * @param bookIndex The position of the book.
		 * @param trackIndex The position of the track.
		 * @param isLastChild Whether the track is the last child in the expandable list view group.
		 * @param convertView The view to be converted.
		 * @param parent The view of the ExpandableListView.
		 * @return The converted view.
		 */
		public View getChildView(final int bookIndex, final int trackIndex,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				//inflate it into parent.
				convertView = vi.inflate(R.layout.bookshelf_child_row, parent,
						false);
			}

			//the expandable list view is stored as final to use it in listeners
			final ExpandableListView expandableListView = (ExpandableListView) parent;
			//set the text of the child 
			setTextViewText(convertView, R.id.bookshelfTrackTitle,
					getChild(bookIndex, trackIndex));
			//get the position of the track from the book
			int duration = getGroup(bookIndex).getTrackDurationAt(trackIndex) / 1000;
			// convert and set the duration of the track
			setTextViewText(convertView, R.id.bookshelfTrackTime,
					DateUtils.formatElapsedTime(duration));

			convertView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					//store the index of the book of the track clicked for correct redrawal
					selectedIndex = bookIndex;
					//force the expandable list view to redraw
					expandableListView.invalidateViews();
					//inform the bookshelfFragment's listener that a child has been selected
					BookshelfFragment.this.childClicked(bookIndex, trackIndex);
				}
			});

			// set long click to show the child's context menu
			convertView.setOnLongClickListener(new OnLongClickListener() {

				public boolean onLongClick(View v) {
					expandableListView.showContextMenu();
					return false;
				}

			});

			return convertView;
		}

		/**
		 * @param bookIndex The position of the book.
		 * @return Book at given position
		 */
		public Book getGroup(int bookIndex) {
			return listData.get(bookIndex).getKey();
		}

		/**
		 * @return The number of books.
		 */
		public int getGroupCount() {
			return listData.size();
		}

		/**
		 * @param bookIndex The position of the book.
		 * @return The id of the book.
		 */
		public long getGroupId(int bookIndex) {
			return bookIndex;
		}

/**
		 * Converts the view of a book to display it properly.
		 * @param bookIndex The position of the book.
		 * @param isExpanded Whether the book is expanded or not.
		 * @param convertView The book's view.
		 * @param parent The expandable list view
		 * @return The converted view. 
		 */
		public View getGroupView(final int bookIndex, final boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				//inflate it into its parent
				convertView = vi.inflate(R.layout.bookshelf_group_row, parent,
						false);
			}
			//set book as final to use in listeners
			final Book book = getGroup(bookIndex);
			//set the expandableListView as final to use in listeners
			final ExpandableListView expandableListView = (ExpandableListView) parent;
			//get duration from book
			int duration = book.getDuration() / 1000;
			// prevent problems with a duration of 0
			if (duration == 0) {
				return null;
			}
			//get the elapsed time of the book
			int time = book.getBookElapsedTime() / 1000;

			// set title, author, time and duration of book
			setTextViewText(convertView, R.id.bookshelfBookTitle,
					book.getSelectedBookTitle());
			//set the color of the books title to red if selected,
			if (bookIndex == selectedIndex) {
				setTextViewTextColor(convertView, R.id.bookshelfBookTitle,
						Color.RED);
			} 
			//and white otherwise.
			else {
				setTextViewTextColor(convertView, R.id.bookshelfBookTitle,
						Color.WHITE);
			}
			setTextViewText(convertView, R.id.bookshelfAuthor,
					book.getSelectedBookAuthor());
			//format the string of the elapsed time of the book
			String timeString = time == 0 ? "N/A" : DateUtils
					.formatElapsedTime(time);
			setTextViewText(convertView, R.id.bookshelfBookPosition,
					"Position: " + timeString);
			setTextViewText(convertView, R.id.bookshelfBookDuration,
					"Duration: " + DateUtils.formatElapsedTime(duration));

			// prevent a higher value than 100
			int progress = time > duration ? 100 : 100 * time / duration;
			// set the progress of the progress bar
			ProgressBar progressBar = (ProgressBar) convertView
					.findViewById(R.id.bookshelfProgressBar);
			//make sure progress is within its boundaries before setting it
			if (progress >= 0 && progress <= 100) {
				progressBar.setProgress(progress);
			}
			// set cover art
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.bookshelfBookCover);
			// TODO(bookshelf cover art) : acquire correct cover art
			imageView.setImageResource(R.drawable.img_no_cover);
			// click the cover art to toggle the books tracks visibility
			imageView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (isExpanded) {
						expandableListView.collapseGroup(bookIndex);
					} else {
						expandableListView.expandGroup(bookIndex);
					}
				}
			});
			//sets the on click listener for the rest of the view
			convertView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// expand the selected item
					if (!isExpanded) {
						expandableListView.expandGroup(bookIndex);
					}
					// scroll to the selected item
					expandableListView.setSelectionFromTop(bookIndex, 0);
					// sets the currently selected index
					selectedIndex = bookIndex;
					// invalidates views to force redraw thus setting the
					// correct textcolor
					expandableListView.invalidateViews();
					//inform the BookshelfFragment that this button has been pressed.
					BookshelfFragment.this.groupClicked(bookIndex);
				}
			});
			// set long click to show the group's context menu
			convertView.setOnLongClickListener(new OnLongClickListener() {

				public boolean onLongClick(View v) {
					expandableListView.showContextMenu();
					return false;
				}

			});

			return convertView;
		}

		/**
		 * Private help class used to prevent code duplication
		 * @param view The TextView to where the text will be displayed.
		 * @param id The id of the TextView.
		 * @param text The text to be displayed.
		 */
		private void setTextViewText(View view, int id, String text) {
			if(view != null && view instanceof TextView) {
				TextView textView = (TextView) view.findViewById(id);
				if (textView != null) {
					textView.setText(text);
				}	
			}
		}

		/**
		 * Private help class used to prevent code duplication.
		 * @param view The TextView to where the text color will be set.
		 * @param id The id of the TextView.
		 * @param color The color to be set.
		 */
		private void setTextViewTextColor(View view, int id, int color) {
			if(view != null && view instanceof TextView) {
				TextView textView = (TextView) view.findViewById(id);
				if (textView != null) {
					textView.setTextColor(color);
				}
			}
		}

		
		public boolean hasStableIds() {
			return true;
		}

		/**
		 * @param bookIndex The position of the book.
		 * @param trackIndex The position of the track.
		 * @return Whether the track at given position is selectable.
		 */
		public boolean isChildSelectable(int bookIndex, int trackIndex) {
			return true;
		}
	}

	public void bookLongPress(int index) {
		fragmentOwner.bookLongPress(index);
	}

	public void addBookButtonPressed() {
		fragmentOwner.addBookButtonPressed();
	}

	public void setSelectedBook(int bookIndex) {
		fragmentOwner.setSelectedBook(bookIndex);
	}

	public void setSelectedTrack(int bookIndex, int trackIndex) {
		fragmentOwner.setSelectedTrack(bookIndex, trackIndex);
	}

	public void removeBook(int bookIndex) {
		fragmentOwner.removeBook(bookIndex);
	}

	public void removeTrack(int trackIndex) {
		fragmentOwner.removeTrack(trackIndex);
	}

	public void setBookTitleAt(int bookIndex, String newTitle) {
		fragmentOwner.setBookTitleAt(bookIndex, "NEWNAME");
	}

}
