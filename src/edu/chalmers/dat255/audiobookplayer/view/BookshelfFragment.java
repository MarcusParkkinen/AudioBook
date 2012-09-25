package edu.chalmers.dat255.audiobookplayer.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.model.Book;
import edu.chalmers.dat255.audiobookplayer.util.StringConstants;

public class BookshelfFragment extends Fragment implements PropertyChangeListener {
	private List<Entry<String, List<String>>> listData;
	private ExpandableBookshelfAdapter adapter;
	private BookshelfUIEventListener fragmentOwner;
	
	public interface BookshelfUIEventListener{
		public void bookSelected(int index);
		public void bookLongPress(int index);
		public void addButtonPressed();
		public void setBookshelfListener(PropertyChangeListener listener);
	}
	
	/**
	 * Lifecycle method that makes sure that the container activity (AudioBookActivity)
	 * has implemented the OnBookSelectedListener interface.
	 * 
	 * @throws ClassCastException
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try{
			fragmentOwner = (BookshelfUIEventListener) activity;
		} catch(ClassCastException e) {
			throw new ClassCastException(activity.toString() +
					" must implement BookshelfUIEventListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		//Provide the layout of this fragment to the parent container
		View view = inflater.inflate(R.layout.bookshelf_fragment, container, false);
		
		/**************************************************************/
		/**/														/**/
		/*    			Instantiate member variables				  */
		/**/                               							/**/
		/**************************************************************/
		listData = new ArrayList<Entry<String, List<String>>>();
		adapter = new ExpandableBookshelfAdapter(view.getContext(), listData);
		
		//TEMP
		/*listData.add(new BookshelfEntry<String, List<String>>("Booktitle 1", Arrays.asList("Track 1", "Track 2")));
		listData.add(new BookshelfEntry<String, List<String>>("Booktitle 2", Arrays.asList("Track 1", "Track 2")));
		listData.add(new BookshelfEntry<String, List<String>>("Booktitle 3", Arrays.asList("Track 1", "Track 2")));
		listData.add(new BookshelfEntry<String, List<String>>("Booktitle 4", Arrays.asList("Track 1", "Track 2")));
		*/
		
		/**************************************************************/
		/**/														/**/
		/*    Get button layout, and add a listener method to it   	  */
		/**/                               							/**/
		/**************************************************************/
		
		Button addButton = (Button) view.findViewById(R.id.addBook);
		
		addButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	//Get the parent activity
                Activity activity = getActivity();
                
                //Make sure that the activity is not at the end of its lifecycle
                if (activity != null) {
                    fragmentOwner.addButtonPressed();
                }
            }
        });
		
		/******************************************************************************/
		/**/																		/**/
		/*    Get the list layout, and add listener methods and an adapter to it 	  */
		/**/                               											/**/
		/******************************************************************************/
		ExpandableListView bookshelfList = (ExpandableListView) view.findViewById(R.id.bookshelfList);
		
		//called when a list item is clicked
		bookshelfList.setOnGroupClickListener(new OnGroupClickListener() {			
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				//Toast.makeText(v.getContext().getApplicationContext(), listData.get(groupPosition).getKey() , Toast.LENGTH_SHORT).show();
				fragmentOwner.bookSelected(groupPosition);
				return false;
			}
		});
		
		//called when a sub-list item is clicked
		bookshelfList.setOnChildClickListener(new OnChildClickListener() {			
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Toast.makeText(v.getContext().getApplicationContext(), listData.get(groupPosition).getValue().get(childPosition), Toast.LENGTH_SHORT).show();				
				return true;
			}
		});
		
		bookshelfList.setAdapter(adapter);

		//sets PropertyChangeListener in Bookshelf via AudioBookActivity
		fragmentOwner.setBookshelfListener(this);
		
		return view;
	}
	
	
	//never gets called at the moment
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getPropertyName().equals(StringConstants.event.BOOK_ADDED)) {
			//Add a new entry to 'values'
			Book b = (Book)event.getNewValue();			
			listData.add(new BookshelfEntry<String, List<String>>(b.getTitle(), b.getPaths()));
			//Notify the adapter that the list has changed
			adapter.notifyDataSetChanged();	
		}
	}
	
	
	/**
	 * 
	 * Private help class that holds an entry with a key and a value
	 * 
	 * @author Fredrik Åhs
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
}
