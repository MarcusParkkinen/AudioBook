package edu.chalmers.dat255.audiobookplayer.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;
import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.ctrl.BookController;
import edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.util.StringConstants;

/**
 * The BookshelfActivity class is a graphical representation of the users bookshelf.
 * 
 * @author Marcus Parkkinen & Fredrik Åhs
 * @version 1.0
 */

public class BookshelfActivity extends Activity implements PropertyChangeListener {
	private BookController bsc;
	private ExpandableBookshelfAdapter adapter;
	private List<Entry<String, List<String>>> listData;
	
	//Testing variables (temporary)
	private int counter = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Make this activity a fullscreen activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
      //Only create a new instance if saved instance state does not contain one already
        if(listData == null) {
        	listData = new ArrayList<Entry<String, List<String>>>();
        }
        //Always instantiate a new controller
        bsc = new BookController();
        
        //Set the content view
        setContentView(R.layout.activity_bookshelf);
        
        //Initiate components
        componentInit();  
    }
    
    /**
     * Private method that handles all component initiation
     */
    private void componentInit() {
        //Get the list and button components
        ExpandableListView bookshelfList = (ExpandableListView) findViewById(R.id.bookshelfList);
        Button add = (Button) findViewById(R.id.addBook);
        adapter = new ExpandableBookshelfAdapter(this, listData);
        
        //Set the listener method for the ListView
        bookshelfList.setOnChildClickListener(new OnChildClickListener() {			
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Toast.makeText(getApplicationContext(), listData.get(groupPosition).getValue().get(childPosition), Toast.LENGTH_SHORT).show();				
				PlayerController pc = new PlayerController();
                pc.addTrack(listData.get(groupPosition).getValue().get(childPosition));
                pc.start();
                pc.play();
				return true;
			}
		});
        
        bookshelfList.setOnGroupClickListener(new OnGroupClickListener() {			
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				Toast.makeText(getApplicationContext(), listData.get(groupPosition).getKey() , Toast.LENGTH_SHORT).show();
        		
				if(parent.isGroupExpanded(groupPosition)) {
					parent.collapseGroup(groupPosition);
					return true;
				} else {
					parent.expandGroup(groupPosition);
					return false;
				}
			}
		});        
        
       //Set the listener method for the button
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	//temporary test
                bsc.createBook(new String[]{Environment.getExternalStorageDirectory().getAbsolutePath()+"/skinnylove.mp3", "testbook"+counter+"/path2"}, "testbook" + counter++);
            }
        });
        
        //Add this activity as a listener
        bsc.addBookshelfListener(this);
 
        //Connect the adapter to the list component
        bookshelfList.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	
    	savedInstanceState.get("values");
    }
    
    /**
     * This method is called whenever the model component has changed.
     * 
     * @param PropertyChangeEvent event that contains information about the change
     */
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getPropertyName().equals(StringConstants.event.book_added)) {
			//Add a new entry to 'values'
			Book b = (Book)event.getNewValue();
			listData.add(new BookshelfEntry<String, List<String>>(b.getTitle(), b.getPaths()));
			
			//Notify the adapter that the list has changed
			adapter.notifyDataSetChanged();			
		}
	}
	
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
	
	private class ExpandableBookshelfAdapter extends BaseExpandableListAdapter {
		
		private Context context;
		private List<Entry<String, List<String>>> listData;
		
		public ExpandableBookshelfAdapter(Context context, List<Entry<String, List<String>>> listData) { //IndexedMap<String, List<String>> listData) {
			this.context = context;
			this.listData = listData;
		}
		
		public Object getChild(int groupPosition, int childPosition) {
			return listData.get(groupPosition).getValue().get(childPosition);
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return listData.get(groupPosition).getValue().size();
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			//temporary		
			TextView textView = getGenericView();
	        textView.setText(getChild(groupPosition, childPosition).toString());
	        return textView;
		}

		private TextView getGenericView() {
			//temporary
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 64);
	        TextView textView = new TextView(context);
	        textView.setLayoutParams(lp);
	        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
	        textView.setPadding(36, 0, 0, 0);
	        return textView;
		}

		public Object getGroup(int groupPosition) {
			return listData.get(groupPosition).getKey();
		}

		public int getGroupCount() {
			return listData.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			//temporary
			TextView textView = getGenericView();
			textView.setText(getGroup(groupPosition).toString());
			return textView;
		}

		public boolean hasStableIds() {
			return true;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}	
	}
}