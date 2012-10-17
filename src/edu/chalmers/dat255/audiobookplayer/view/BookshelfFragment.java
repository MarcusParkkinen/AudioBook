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
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;

/**
 * Graphical representation of the bookshelf.
 * 
 * @author Marcus Parkkinen, Fredrik �hs
 * @version 0.6
 */

public class BookshelfFragment extends Fragment implements IBookshelfGUIEvents {
	private static final String TAG = "BookshelfFragment.class";
	private Bookshelf bookshelf;
	private ExpandableBookshelfAdapter adapter;
	private IBookshelfGUIEvents fragmentOwner;
	private ExpandableListView bookshelfList;

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
		MoveUp {
			public String getText() {
				return "Move Up";
			}
		},
		MoveDown {
			public String getText() {
				return "Move Down";
			}
		};
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		boolean ownerImplementsEvents = true;

		try {
			fragmentOwner = (IBookshelfGUIEvents) activity;
		} catch (ClassCastException e) {
			ownerImplementsEvents = false;
		}
		if (!ownerImplementsEvents) {
			throw new ClassCastException(activity.toString()
					+ " does not implement " + IBookshelfGUIEvents.class.getName());
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
		if(bookshelf == null && adapter == null) {
			adapter = new ExpandableBookshelfAdapter(view.getContext(), null);
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
		bookshelfList = (ExpandableListView) view
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
			bookshelfUpdated((Bookshelf) getArguments().getSerializable(
					Constants.Reference.BOOKSHELF));
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
				if(bookshelf != null) {
					title = bookshelf.getTrackTitleAt(bookIndex, trackIndex);
				}
			} else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
				//get all menu items from the group context menu
				menuItems = GroupContextMenuItem.values();
				//set the context menu's title to that of the value of the book
				if(bookshelf != null) {
					title = bookshelf.getBookTitleAt(bookIndex);
				}
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
		if(getActivity() == null) {
			return false;
		}
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
			case Delete:
				removeTrack(bookIndex, trackIndex);
				break;
			case MoveUp:
				moveTrack(bookIndex, trackIndex, -1);
				break;
			case MoveDown:
				moveTrack(bookIndex, trackIndex, 1);
				break;
			default:
				break;

			}
		}
		return true;
	}

	public void moveTrack(int bookIndex, int trackIndex, int offset) {
		fragmentOwner.moveTrack(bookIndex, trackIndex, offset);
	}

	public void removeTrack(int bookIndex, int trackIndex) {
		fragmentOwner.removeTrack(bookIndex, trackIndex);
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
	 * Updates the adapter with the updated bookshelf and informs it that its
	 * data has been added.
	 * 
	 * @param bs
	 *            The updated bookshelf
	 */

	public void bookshelfUpdated(Bookshelf bs) {
		Log.d(TAG, "Book added");
		adapter.setBookshelf(bs);
		adapter.notifyDataSetChanged();
	}

	public void selectedBookElapsedTimeUpdated(int newTime) {
		adapter.selectedBookElapsedTimeUpdated(newTime);		
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


	/**
	 * Private class used to populate the ExpandableListView used in
	 * BookshelfFragment.
	 * 
	 * @author Fredrik �hs
	 * 
	 */
	private class ExpandableBookshelfAdapter extends BaseExpandableListAdapter {

		private Context context;
		//		private List<Entry<Book, List<String>>> listData;
		private Bookshelf bookshelf;

		//used to get synchronized time label and progress bar 
		private int bookElapsedTime;
		private int bookProgress;
		private View selectedBookView;
		private int selectedBookIndex;


		/**
		 * Constructs an ExpandableListAdapter with context and listData.
		 * @param context The context of the application.
		 * @param listData The data to be used to fill the list.
		 */
		public ExpandableBookshelfAdapter(Context context,
				//				List<Entry<Book, List<String>>> listData) {
				Bookshelf bookshelf) {
			this.context = context;
			//			this.listData = listData;
			setBookshelf(bookshelf);

		}

		/**
		 * Updates the adapters class variables.
		 * @param newTime
		 * @return 
		 */
		public void selectedBookElapsedTimeUpdated(int newTime) {
			//newTime will be in millis, convert to seconds
			int newTimeSeconds = newTime/1000;
			//if a second has passed
			if(newTimeSeconds > bookElapsedTime)  {
				//store this new value
				bookElapsedTime = newTimeSeconds;
				//update the label
				setTextViewText(selectedBookView, R.id.bookshelfBookPosition, "Position: " + DateUtils.formatElapsedTime(bookElapsedTime));
				
				//get duration of book in seconds
				int bookDuration = bookshelf.getSelectedBookDuration() / 1000;
				//calculate the progress
				int calculatedProgress = calculateProgress(newTimeSeconds, bookDuration);
				//if there is no errors and the progress has progressed since the last saved progress
				if((calculatedProgress >= 0) && (calculatedProgress <= 100) && (calculatedProgress > bookProgress)) {
					//save this new progress
					bookProgress = calculatedProgress;
					//get the progressbar
					ProgressBar pb = (ProgressBar)selectedBookView.findViewById(R.id.bookshelfProgressBar);
					//check that the progressbar is not null and set it to the new progress
					if(pb != null) {
						pb.setProgress(bookProgress);
					}
				}
			}

		}

		/**
		 * Method that calculates a roofed progress, i.e.
		 * time = 51, duration = 1000 would return a progress of 6
		 * @param elapsedTime The time elapsed.
		 * @param duration The total duration.
		 * @return A roofed progress.
		 */
		private int calculateProgress(int elapsedTime, int duration) {
			if(elapsedTime < 0 || duration <= 0) {
				return 0;
			}
			int floored = 100 * elapsedTime / duration;
			int rest = 100 * elapsedTime % duration;
			if(rest == 0) {
				return floored;
			}
			return floored + 1;
		}

		/**
		 * Set the bookshelf for when the data should update
		 * @param bookshelf The new bookshelf copy.
		 */
		public void setBookshelf(Bookshelf bookshelf) {
			this.bookshelf = bookshelf;
		}

		/**
		 * @param bookIndex The position of the book.
		 * @param trackIndex The position of the track.
		 * @return The title of the track at given position.
		 */
		public Integer getChild(int bookIndex, int trackIndex) {
			if(bookshelf != null) {
				return trackIndex;
			} 
			return 0;
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
			//			return listData.get(bookIndex).getValue().size();
			if(bookshelf != null) {
				return bookshelf.getNumberOfTracksAt(bookIndex);
			}
			return 0;
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
					bookshelf.getTrackTitleAt(bookIndex, trackIndex));
			//get the position of the track from the book
			int duration = bookshelf.getTrackDurationAt(bookIndex, trackIndex) / 1000;
			// convert and set the duration of the track
			setTextViewText(convertView, R.id.bookshelfTrackTime,
					DateUtils.formatElapsedTime(duration));

			convertView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					//store the index of the book of the track clicked for correct redrawal
					//force the expandable list view to redraw
					expandableListView.invalidateViews();
					//inform the bookshelfFragment's listener that a child has been selected
					BookshelfFragment.this.childClicked(bookIndex, trackIndex);
					//store for synchronization
					selectedBookView = expandableListView.getChildAt(bookIndex);
					bookElapsedTime = bookshelf.getBookElapsedTime()/1000;
					bookProgress = calculateProgress(bookElapsedTime, bookshelf.getSelectedBookDuration()/1000);
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
		 * @return given position or 0 if bookshelf is null
		 */
		public Integer getGroup(int bookIndex) {
			if(bookshelf != null) {
				return bookIndex;
			}
			return 0;
		}

		/**
		 * @return The number of books.
		 */
		public int getGroupCount() {
			if (bookshelf != null) {
				return bookshelf.getNumberOfBooks();
			}
			return 0;
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

			//set the expandableListView as final to use in listeners
			final ExpandableListView expandableListView = (ExpandableListView) parent;
			//get duration from book
			int duration = bookshelf.getBookDurationAt(bookIndex) / 1000;

			// prevent problems with a duration of 0
			if (duration == 0) {
				return null;
			}
			//get the elapsed time of the book
			int time = 0;
			int progress = 0;

			// set title, author, time and duration of book
			setTextViewText(convertView, R.id.bookshelfBookTitle,
					bookshelf.getBookTitleAt(bookIndex));
			//set the color of the books title to red if selected,
			if (bookIndex == bookshelf.getSelectedBookIndex()) {
				setTextViewTextColor(convertView, R.id.bookshelfBookTitle,
						Color.RED);
				time = bookElapsedTime;
				progress = bookProgress;
			} 
			//and white otherwise.
			else {
				setTextViewTextColor(convertView, R.id.bookshelfBookTitle,
						Color.WHITE);
				time = bookshelf.getBookElapsedTimeAt(bookIndex) / 1000;
				progress = calculateProgress(time, duration);
			}
			setTextViewText(convertView, R.id.bookshelfAuthor,
					bookshelf.getBookAuthorAt(bookIndex));
			//format the string of the elapsed time of the book
			String timeString = time == 0 ? "N/A" : DateUtils
					.formatElapsedTime(time);
			setTextViewText(convertView, R.id.bookshelfBookPosition,
					"Position: " + timeString);
			setTextViewText(convertView, R.id.bookshelfBookDuration,
					"Duration: " + DateUtils.formatElapsedTime(duration));


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
			//store convertview as final to reach it inside this method
			final View finalConvertView = convertView;
			convertView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// expand the selected item
					if (!isExpanded) {
						expandableListView.expandGroup(bookIndex);
					}
					// scroll to the selected item
					expandableListView.setSelectionFromTop(bookIndex, 0);
					// invalidates views to force redraw thus setting the
					// correct textcolor
					expandableListView.invalidateViews();
					//inform the BookshelfFragment that this button has been pressed.
					BookshelfFragment.this.groupClicked(bookIndex);

					//store the view so that text can update
					selectedBookView = finalConvertView;
					bookElapsedTime = bookshelf.getBookElapsedTime()/1000;
					bookProgress = calculateProgress(bookElapsedTime, bookshelf.getSelectedBookDuration()/1000);

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
			if(view != null) {
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
			if(view != null) {
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
}
