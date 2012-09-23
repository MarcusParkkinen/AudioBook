package edu.chalmers.dat255.audiobookplayer.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.util.StringConstants;
import edu.chalmers.dat255.audiobookplayer.ctrl.BookController;

/**
 * The BookshelfActivity class is a graphical representation of the users bookshelf.
 * 
 * @author Marcus Parkkinen
 * @version 1.0
 */

public class BookshelfActivity extends Activity implements PropertyChangeListener {
	private BookController bsc;
	private ArrayList<String> values;
	private ArrayAdapter<String> adapter;
	
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
        if( values == null ) {
        	values = new ArrayList<String>();
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
        ListView bookshelfList = (ListView) findViewById(R.id.bookshelfList);
        Button add = (Button) findViewById(R.id.AddBook);
        
        //Set the listener method for the ListView
        bookshelfList.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view,
        			int pos, long id) {
        		//Make a 'toast' display information about the selection (temporary)
        		Toast.makeText(getApplicationContext(), "You selected item number " + pos, Toast.LENGTH_SHORT).show();
        	}
        });
        
       //Set the listener method for the button
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                bsc.createBook(new String[]{"path1", "path2"}, "testbook" + counter++);
            }
        });
        
        //Create an adapter that gathers its content from 'values' member field
        adapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1, android.R.id.text1, values);
        
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
    	
    	System.out.println("Saving a new instance..");
    	outState.putStringArrayList("values", this.values);
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	
    	savedInstanceState.get("values");
    	if( values != null ) {
    		System.out.println("Restoring state..");
    	}
    }
    
    /**
     * This method is called whenever the model component has changed.
     * 
     * @param PropertyChangeEvent event that contains information about the change
     */
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getPropertyName().equals(StringConstants.event.book_added)) {
			//Add a new entry to 'values'
			values.add(event.getNewValue().toString());
			//Notify the adapter that the list has changed
			adapter.notifyDataSetChanged();
		}
	}
}