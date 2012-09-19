package edu.chalmers.dat255.audiobookplayer.view;

import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.R.layout;
import edu.chalmers.dat255.audiobookplayer.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BrowserActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_browser, menu);
        return true;
    }
}
