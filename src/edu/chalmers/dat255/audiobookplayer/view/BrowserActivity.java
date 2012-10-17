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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;
import edu.chalmers.dat255.audiobookplayer.util.BookCreator;

/**
 * This class is used to display and add new books to the bookshelf. It lists
 * all audio files and their file trees up to the ExternalStorageDirectory.
 * 
 * @author Fredrik Åhs
 * 
 */
public class BrowserActivity extends Activity {
	protected static final String TAG = "BrowserActivity";

	/**
	 * The different possible file types when browsing.
	 * 
	 * @author Fredrik Åhs
	 * 
	 */
	public enum FILETYPE {
		FILE {
			public String toString() {
				return "File";
			}
		},
		FOLDER {
			public String toString() {
				return "Folder";
			}
		},
		PARENT {
			public String toString() {
				return "Parent Folder";
			}
		};
	}

	private BrowserArrayAdapter adapter;
	private ListView listView;

	// checkedItems is used to save the checkedState of files while navigating
	// through the file tree.
	private Set<File> checkedItems;
	private File currentDirectory;
	private Map<TypedFile, List<TypedFile>> childMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);

		// generate the childMap
		childMap = populateChildMap();
		// set up the components
		setUpComponents();

		// get the file to the root of the file system
		File f = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath());

		// fill the list view from the root
		fill(f);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
	}

	@Override
	public void onBackPressed() {
		Log.i(TAG, "Back pressed in Browser Activity");
		finish();
	}

	/**
	 * Sets up the components of this activity.
	 */
	private void setUpComponents() {
		checkedItems = new TreeSet<File>();
		listView = (ListView) findViewById(R.id.browserList);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				TypedFile file = adapter.getItem(position);
				// if a folder or the 'parent directory' is clicked, open it
				if (file.getType().equals(FILETYPE.FOLDER)
						|| file.getType().equals(FILETYPE.PARENT)) {
					fill(file);
				}
				// otherwise, call onclick method
				else {
					onFileClick(file);
				}
			}
		});

		// set listener for button "Create Book"
		Button createBookButton = (Button) findViewById(R.id.createBook);
		createBookButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// check that some items are checked
				if (!checkedItems.isEmpty()) {
					// create a list of tracks to add
					List<String> tracks = new ArrayList<String>();
					String name = null;
					for (File f : checkedItems) {
						// only add tracks, not folders
						if (f.isFile()) {
							if (tracks.size() == 0) {
								// this name is but a backup in the case that
								// there is no album id3
								name = f.getParentFile().getName();
							}
							tracks.add(f.getAbsolutePath());
						}
					}
					BookCreator.getInstance().createBook(tracks, name, null);
					// create a toast to notify the user that a book has been
					// added.
					Toast.makeText(BrowserActivity.this, "Book added: " + name,
							Toast.LENGTH_SHORT).show();
					// empty checkedItems and fill the list with unchecked items
					checkedItems = new TreeSet<File>();
					// refill the ListView from currentDirectory
					fill(currentDirectory);
				}
			}
		});

		// create and listen to the back button
		ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// finish this activity
				finish();
				Log.d(TAG, "Backed from browser.");
			}

		});

	}

	/**
	 * Fills the ListView with the children to root.
	 * 
	 * @param root
	 *            The folder which contents is to be listed.
	 */
	private void fill(File root) {

		// in case root is not a directory, no items will be listed (should
		// never happen)
		if (root.isFile()) {
			return;
		}
		currentDirectory = root;
		List<TypedFile> directories = new ArrayList<TypedFile>();
		// store files separately for correct sorting
		List<TypedFile> files = new ArrayList<TypedFile>();
		// check that the file exists in the previously populated childMap
		if (childMap.get(root) != null) {
			// add all files under root in childMap
			for (TypedFile f : childMap.get(root)) {
				if (f.isDirectory()) {
					directories.add(f);
				} else {
					files.add(f);
				}
			}

			// sort found directories and files
			Collections.sort(directories);
			Collections.sort(files);
			// add all files under the directories
			directories.addAll(files);

			// adds an item listed as ".." of type parent topmost in the list
			if (!root.getAbsolutePath()
					.equals(Environment.getExternalStorageDirectory()
							.getAbsolutePath())) {
				directories.add(0,
						new TypedFile(FILETYPE.PARENT, root.getParent()));
			}

			// create a new adapter with the found files/directories and add it
			// to the listview
			adapter = new BrowserArrayAdapter(this, R.layout.file_view,
					directories, checkedItems, childMap);
			listView.setAdapter(adapter);
		} else {
			// notify the user that no files were found on the system
			Toast t = Toast.makeText(getApplicationContext(),
					Constants.Message.NO_AUDIO_FILES_FOUND, Toast.LENGTH_SHORT);
			t.show();
		}

	}

	/**
	 * Finds all the tracks on the device and adds them and their parents to a
	 * map
	 * 
	 * @return The map containing the files and their parents.
	 */
	private Map<TypedFile, List<TypedFile>> populateChildMap() {
		/*
		 * this will prevent files such as notifications and ringtones to appear
		 * in the list
		 */
		String filtering = MediaStore.Audio.Media.IS_MUSIC + " != 0";

		/*
		 * path to the audiofile
		 */
		String[] projection = { MediaStore.Audio.Media.DATA };

		// this cursor will point at the paths of all music
		Cursor cursor = this.managedQuery(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
				filtering, null, null);

		Map<File, File> parentMap = new TreeMap<File, File>();
		// cursor.moveToNext will iterate through all music on the device
		while (cursor.moveToNext()) {
			File child = new File(cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
			// loop through all tracks and put them with their parents as value
			while (!child.getAbsolutePath()
					.equals(Environment.getExternalStorageDirectory()
							.getAbsolutePath())) {
				if (parentMap.containsKey(child)) {
					/*
					 * if the file is already in the map, so is all its parents
					 * and therefore further looping is redundant
					 */
					break;
				}
				parentMap.put(child, child.getParentFile());
				child = child.getParentFile();
			}
		}

		// reverse the list for easier iterating
		Map<TypedFile, List<TypedFile>> fileMap = new TreeMap<TypedFile, List<TypedFile>>();

		for (Entry<File, File> entry : parentMap.entrySet()) {
			// if the map does not contain the parent, add the parent with a new
			// list
			TypedFile parent = new TypedFile(FILETYPE.FOLDER, entry.getValue()
					.getAbsolutePath());
			if (!fileMap.containsKey(parent)) {
				fileMap.put(parent, new ArrayList<TypedFile>());
			}
			// add a child to the parents list of children
			File child = entry.getKey();
			if (child.isDirectory()) {
				fileMap.get(parent)
						.add(new TypedFile(FILETYPE.FOLDER, child
								.getAbsolutePath()));
			} else {
				fileMap.get(parent).add(
						new TypedFile(FILETYPE.FILE, child.getAbsolutePath()));
			}
		}
		return fileMap;
	}

	/**
	 * Method to be called when a file of type file is clicked.
	 * 
	 * @param file
	 *            The file that was clicked.
	 */
	private void onFileClick(TypedFile file) {
		Toast.makeText(this, "File Clicked: " + file.getName(),
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Adapter to the browser.
	 * 
	 * @author Fredrik Åhs
	 * 
	 */
	private class BrowserArrayAdapter extends ArrayAdapter<TypedFile> {
		private Context c;
		private int id;
		private List<TypedFile> files;
		private Set<File> checkedItems;
		private Map<TypedFile, List<TypedFile>> childMap;

		/**
		 * Constructor.
		 * 
		 * @param context
		 *            The current context.
		 * @param textViewResourceId
		 *            The resource ID for the layout file used to display the
		 *            information.
		 * @param filesToDisplay
		 *            The list of the files to be displayed.
		 * @param checkedItems
		 *            Storage for the items that are checked at the moment.
		 * @param childMap
		 *            A map containing information regarding all the children
		 *            which will be checked if a folder is checked.
		 */
		public BrowserArrayAdapter(Context context, int textViewResourceId,
				List<TypedFile> filesToDisplay, Set<File> checkedItems,
				Map<TypedFile, List<TypedFile>> childMap) {
			super(context, textViewResourceId, filesToDisplay);
			this.c = context;
			this.id = textViewResourceId;
			this.files = filesToDisplay;
			this.checkedItems = checkedItems;
			this.childMap = childMap;
		}

		/**
		 * Creates the view of each listview item
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			// if the view is null,
			if (view == null) {
				LayoutInflater vi = (LayoutInflater) c
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				// inflate the view into itself
				view = vi.inflate(id, null);
			}
			// get file as final since it wont change and it's needed in an
			// anonymous class
			final TypedFile file = files.get(position);
			if (file != null) {
				// get the TextView in the ListView item
				TextView tv1 = (TextView) view.findViewById(R.id.TextView01);
				TextView tv2 = (TextView) view.findViewById(R.id.TextView02);

				if (tv1 != null) {
					tv1.setText(file.getName());
				}
				if (tv2 != null) {
					tv2.setText(file.getType().toString());
				}

			}

			// set the state of the checkbox according to the checkedItems list
			CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox);
			boolean checkState = checkedItems.contains(file);
			cb.setChecked(checkState);
			// set the on click listener of the checkbox
			cb.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CheckBox c = (CheckBox) v;
					// checks all children if a folder, or the file otherwise
					checkAllChildren(file, c.isChecked());
				}
			});

			return view;
		}

		/**
		 * (Un)check all the files within file recursively (in case it's a
		 * folder).
		 * 
		 * @param file
		 *            The file (folder) to check.
		 * @param checkState
		 *            Whether to check or to uncheck the files.
		 */
		private void checkAllChildren(File file, boolean checkState) {
			// if file is a regular file
			if (file.isFile()) {
				// check it and return
				checkItem(file, checkState);
				return;
			}
			// if the file is a folder
			// declare the list outside the if clause to only get file once from
			// map and to only use one if clause.
			List<TypedFile> list;
			// if childMap contains the file, instantiate the list
			if (childMap.containsKey(file)
					&& (list = childMap.get(file)) != null) {
				for (File f : list) {
					// check the item
					checkItem(f, checkState);
					// recurse
					checkAllChildren(f, checkState);
				}
			}
		}

		/**
		 * Adds or removes the file from the checkedItems list
		 * 
		 * @param file
		 *            The file to add or remove.
		 * @param checkState
		 *            Whether to add or remove the file.
		 */
		private void checkItem(File file, boolean checkState) {
			if (checkState) {
				checkedItems.add(file);
			} else {
				checkedItems.remove(file);
			}
		}
	}

	/**
	 * Simple class which extends File by also having a FILETYPE which
	 * identifies it in the list.
	 * 
	 * @author Fredrik Åhs
	 * 
	 */
	private class TypedFile extends File {

		private static final long serialVersionUID = 1L;
		private FILETYPE type;

		/**
		 * Constructor
		 * 
		 * @param type
		 *            The filetype of the file.
		 * @param path
		 *            The path to the file.
		 */
		public TypedFile(FILETYPE type, String path) {
			super(path);
			this.type = type;
		}

		/**
		 * 
		 * @return the type of the file.
		 */
		public FILETYPE getType() {
			return type;
		}

		@Override
		public String getName() {
			// the name of a parent folder should be ..
			if (type.equals(FILETYPE.PARENT)) {
				return "..";
			}
			return super.getName();
		}
	}

}
