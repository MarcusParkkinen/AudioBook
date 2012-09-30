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
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.chalmers.dat255.audiobookplayer.R;

public class BrowserActivity extends Activity {

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

	//checkedItems is used to save the checkedState of files while navigating through the file tree.
	private Set<File> checkedItems;
	private File currentDirectory;
	private TreeMap<TypedFile, List<TypedFile>> childMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);

		populateChildMap();
		setUpComponents();
		
		fill(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
	}

	/**
	 * Sets up the components of this activity.
	 */
	private void setUpComponents() {
		checkedItems = new TreeSet<File>();
		listView = (ListView) findViewById(R.id.browserList);
		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				TypedFile file = adapter.getItem(position);
				//if a folder or the 'parent directory' is clicked, open it
				if(file.getType().equals(FILETYPE.FOLDER) || file.getType().equals(FILETYPE.PARENT)){
					fill(file);
				}
				else
				{
					onFileClick(file);
				}
			}
		});

		//set listener for button "Create Book"
		Button button = (Button) findViewById(R.id.createBook);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
//				bookCreator.addBook(new Book("BOK", checkedItems));
				List<String> tracks = new ArrayList<String>();
				String name = null;
				for(File f : checkedItems) {
					//only add tracks, not folders
					if(f.isFile()) {
						if(name == null) {
							name = f.getParentFile().getName();
						}
						tracks.add(f.getAbsolutePath());
					}
				}
				if (name == null) {
					name = "TITLE";
				}
				//bookCreator.addBook(new Book(name, tracks);

				//empty checkedItems and fill the list with unchecked items
				checkedItems = new TreeSet<File>();
				fill(currentDirectory);
			}
		});

	}

	/**
	 * Fills the ListView with the children to root.
	 * @param root The folder which contents is to be listed.
	 */
	private void fill(File root) {

		// in case root is not a directory, no items will be listed (should never happen)
		if ( root.isFile()) {
			return;
		}
		currentDirectory = root;
		List<TypedFile> directories = new ArrayList<TypedFile>();
		//store files separately for correct sorting
		List<TypedFile> files = new ArrayList<TypedFile>();        	
		
		for(TypedFile f : childMap.get(root)) {
			if (f.isDirectory()) {
				directories.add(f);
			} else {
				files.add(f);
			}
		}
		
		//sort found directories and files
		Collections.sort(directories);
		Collections.sort(files);
		//add all files under the directories
		directories.addAll(files);
		
		// adds an item listed as ".." of type parent topmost in the list
		if( ! root.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
			directories.add(0, new TypedFile(FILETYPE.PARENT, root.getParent()));
		}

		//create a new adapter with the found files/directories and add it to the listview
		adapter = new BrowserArrayAdapter(this, R.layout.file_view, directories, checkedItems, childMap);
		listView.setAdapter(adapter);
	}


	/**
	 * Populates a map which holds all files to be displayed, and their parents
	 */
	private void populateChildMap() {
		//this will prevent files such as notifications and ringtones to appear in the list
		String filtering = MediaStore.Audio.Media.IS_MUSIC + " != 0";
		
		String[] projection = {
				MediaStore.Audio.Media.DATA //path to the audiofile
		};
		
		//this cursor will point at the paths of all music
		Cursor cursor = this.managedQuery(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				projection,
				filtering,
				null,
				null);
		
		TreeMap<File, File> parentMap = new TreeMap<File, File>();
		//cursor.moveToNext will iterate through all music on the device
		while(cursor.moveToNext()){
			File child = new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
			//loop through all tracks and put them with their parents as value 
			while (! child.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
				if(parentMap.containsKey(child)) {
					//if the file is already in the map, so is all its parents and therefore further looping is redundant
					break;
				}
				parentMap.put(child, child.getParentFile());
				child = child.getParentFile();
			}
		}

		//reverse the list for easier iterating
		childMap = new TreeMap<TypedFile, List<TypedFile>>();
		for(Entry<File, File> entry : parentMap.entrySet()) {
			//if the map does not contain the parent, add the parent with a new list
			TypedFile parent = new TypedFile(FILETYPE.FOLDER, entry.getValue().getAbsolutePath());
			if( ! childMap.containsKey(parent)) {
				childMap.put(parent, new ArrayList<TypedFile>());
			}
			//add a child to the parents list of children
			File child = entry.getKey();
			if(child.isDirectory()) {
				childMap.get(parent).add(new TypedFile(FILETYPE.FOLDER, child.getAbsolutePath()));
			} else {
				childMap.get(parent).add(new TypedFile(FILETYPE.FILE, child.getAbsolutePath()));
			}
		}
	}

	private void onFileClick(TypedFile o)
	{
		Toast.makeText(this, "File Clicked: " + o.getName(), Toast.LENGTH_SHORT).show();
	}
	
	private class BrowserArrayAdapter extends ArrayAdapter<TypedFile> {
		private Context c;
		private int id;
		private List<TypedFile> files;
		private Set<File> checkedItems;
		private Map<TypedFile, List<TypedFile>> childMap;

		/**
		 * Constructor
		 * @param context The current context.
		 * @param textViewResourceId The resource ID for the layout file used to display the information.
		 * @param filesToDisplay The list of the files to be displayed.
		 * @param checkedItems Storage for the items that are checked at the moment.
		 * @param childMap A map containing information regarding all the children which will be checked if a folder is checked.
		 */
		public BrowserArrayAdapter(Context context, int textViewResourceId,
				List<TypedFile> filesToDisplay, Set<File> checkedItems, Map<TypedFile, List<TypedFile>> childMap) {
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
			if (view == null) {
				LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(id, null);
			}
			//get file as final since it wont change and it's needed in an anonymous class
			final TypedFile file = files.get(position);
			if (file != null) {
				//sets the text in the listview item
				TextView tv1 = (TextView) view.findViewById(R.id.TextView01);
				TextView tv2 = (TextView) view.findViewById(R.id.TextView02);

				if(tv1 != null)
					tv1.setText(file.getName());
				if(tv2 != null)
					tv2.setText(file.getType().toString());

			}

			//set the state of the according to the checkedItems list
			CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox);
			boolean checkState = checkedItems.contains(file);
			cb.setChecked(checkState);
			
			cb.setOnClickListener(new OnClickListener() {				
				public void onClick(View v) {
					CheckBox c = (CheckBox) v;
					//checks all children if a folder, or the file otherwise
					checkAllChildren(file, c.isChecked());
				}
			});
			
			return view;
		}

		private void checkAllChildren(File file, boolean checkState) {
			//if file is a regular file
			if(file.isFile()) {
				//check it and return
				checkItem(file, checkState);
				return;
			}			
			List<TypedFile> list;
			if(childMap.containsKey(file) && (list = childMap.get(file)) != null) {
				 for(File f : list) {
					checkItem(f, checkState);
					checkAllChildren(f, checkState);
				}
			} 
		}
		
		private void checkItem(File file, boolean checkState) {
			if(checkState) {
				checkedItems.add(file);
			} else {
				checkedItems.remove(file);
			}
		}
	}
	
	/**
	 * Simple class which extends File by also having a FILETYPE which identifies it in the list.
	 * @author Fredrik
	 *
	 */
	private class TypedFile extends File {

		private FILETYPE type;
		
		public TypedFile(FILETYPE type, String path) {
			super(path);
			this.type = type;
		}
		
		public FILETYPE getType() {
			return type;
		}
		
		@Override
		public String getName() {
			if(type.equals(FILETYPE.PARENT)) {
				return "..";
			}
			return super.getName();
		}
	}
	
}
