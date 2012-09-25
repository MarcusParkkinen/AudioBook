package edu.chalmers.dat255.audiobookplayer.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import edu.chalmers.dat255.audiobookplayer.R;

public class AudioBookActivity extends FragmentActivity {
	private static final Fragment STARTING_FRAGMENT = new BookshelfFragment();
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_audiobook);
		
		//Ger a fragment manager to handle the fragments
		FragmentManager fm = getSupportFragmentManager();
		
		//Switch to firstFragment
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.audiobook_layout, STARTING_FRAGMENT);
		ft.commit();
	}
}
