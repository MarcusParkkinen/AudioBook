package edu.chalmers.dat255.audiobookplayer.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.ctrl.PlayerController;

/**
 * A graphical UI to PlayerController.
 * 
 * @author Aki K�kel�
 * @version 0.3
 */
public class PlayerActivity extends Activity {
	private PlayerController pc;
	SeekBar seekBar;
	Button prevTrack, seekLeft, playPause, seekRight, nextTrack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);

		// TODO: call to start playing audio
		// pc.start();

		prevTrack = (Button) findViewById(R.id.prevTrack);
		prevTrack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pc.previousTrack();
			}
		});
		prevTrack.setEnabled(false);

		seekLeft = (Button) findViewById(R.id.seekLeft);
		seekLeft.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO: hold down to seek continuously
				pc.seekLeft(pc.getTrackDuration() / 10);
			}
		});

		playPause = (Button) findViewById(R.id.playPause);
		playPause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pc.playPause();
			}
		});

		seekRight = (Button) findViewById(R.id.seekRight);
		seekRight.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO: hold down to seek continuously
				pc.seekRight(pc.getTrackDuration() / 10);
			}
		});

		nextTrack = (Button) findViewById(R.id.nextTrack);
		nextTrack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pc.nextTrack();
			}
		});

		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			static final String TAG = "seekBar";

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				double seekPercentage = (double) progress
						* (1.0 / seekBar.getMax());
				int displayPercentage = (100 * progress) / (seekBar.getMax());

				Log.d(TAG,
						"progress is " + progress + " out of "
								+ seekBar.getMax() + "(" + displayPercentage
								+ "%)");

				pc.seekTo((int) (pc.getTrackDuration() * seekPercentage));
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				Log.d(TAG, "started tracking");

				// InputMethodManager imm = (InputMethodManager)
				// getSystemService(Context.INPUT_METHOD_SERVICE);
				// imm.hideSoftInputFromWindow(mSeekBar.getWindowToken(), 0);
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				Log.d(TAG, "stopped tracking");
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_player, menu);
		return true;
	}
}
