package edu.chalmers.dat255.audiobookplayer.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import edu.chalmers.dat255.audiobookplayer.R;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
