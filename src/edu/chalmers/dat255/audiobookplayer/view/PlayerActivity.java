package edu.chalmers.dat255.audiobookplayer.view;

import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.R.layout;
import edu.chalmers.dat255.audiobookplayer.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * @author Aki Käkelä
 *
 */
public class PlayerActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_player, menu);
        return true;
    }
}
