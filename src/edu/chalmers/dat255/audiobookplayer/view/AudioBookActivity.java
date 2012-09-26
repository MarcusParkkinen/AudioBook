package edu.chalmers.dat255.audiobookplayer.view;

import java.beans.PropertyChangeListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.ctrl.BookCreator;
import edu.chalmers.dat255.audiobookplayer.ctrl.BookshelfController;
import edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment.BookshelfUIEventListener;
import edu.chalmers.dat255.audiobookplayer.view.PlayerFragment.PlayerUIEventListener;


/**
 * Main activity of the application. This class should handle all fragment functionality.
 * 
 * @author Marcus Parkkinen
 * @version 0.2
 */
public class AudioBookActivity extends FragmentActivity implements BookshelfUIEventListener, PlayerUIEventListener {
	
	//Temporary solution: a new Bookshelf is created every time
	private Bookshelf bookshelf;
	
	//Fragment classes
	private final BookshelfFragment bookshelfFragment = new BookshelfFragment();
	private final PlayerFragment playerFragment = new PlayerFragment();
	
	//Controller classes
	private BookshelfController bsc;
	private PlayerController pc;
	private BookCreator bc;
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_audiobook);
		
		//Temporary solution: always create a new bookshelf
		bookshelf = new Bookshelf();
		
		
		//Instantiate Controller classes
		bsc = new BookshelfController(bookshelf);
		pc = new PlayerController(bookshelf);
		bc = BookCreator.getInstance();
		
		bc.setBookshelf(bookshelf);
		
		System.out.println(bookshelf);
		
		//Make sure that audiobook_layout is being used as layout for this activity and
		//that we are not resuming from a previous state as this could result in overlapping
		//fragments
		if(findViewById(R.id.audiobook_layout) != null &&
				savedInstance == null) {
			
			//In case arguments have been sent through an Intent, forward these to the
			//fragments as well
			bookshelfFragment.setArguments(getIntent().getExtras());
			
			//Add the STARTING_FRAGMENT to this layout
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add(R.id.audiobook_layout, bookshelfFragment);
			ft.commit();
		}
	}

	public void bookSelected(int index) {
		if(findViewById(R.id.audiobook_layout) != null) {
			playerFragment.setArguments(getIntent().getExtras());
			
			switchFragment(bookshelfFragment, playerFragment);
		}		
	}
	
	private void switchFragment(Fragment oldFragment, Fragment newFragment) {
		FragmentManager fm = getSupportFragmentManager();
		fm.saveFragmentInstanceState(oldFragment);
		
		FragmentTransaction ft = fm.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.replace(R.id.audiobook_layout, playerFragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	public void bookLongPress(int index) {
		// TODO
	}

	public void addButtonPressed() {
		bc.createTestBook();
	}

	public void previousTrack() {
		// TODO Auto-generated method stub
		
	}

	public void playPause() {
		// TODO Auto-generated method stub
		
	}

	public void nextTrack() {
		// TODO Auto-generated method stub
		
	}

	public void setBookshelfListener(PropertyChangeListener listener) {
		bookshelf.addPropertyChangeListener(listener);
	}
}
