package edu.chalmers.dat255.audiobookplayer.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * A graphical UI fragment to PlayerController.
 * 
 * @author Aki Käkelä, Marcus Parkkinen
 * @version 0.4
 */

public class PlayerFragment extends Fragment implements PropertyChangeListener {
	private PlayerUIEventListener fragmentOwner;
	private PlayerAdapter adapter;

	public interface PlayerUIEventListener {
		public void previousTrack();

		public void playPause();

		public void nextTrack();

		public void seekLeft();

		public void seekRight();

		public void seekToPercentage(double seekPercentage);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			fragmentOwner = (PlayerUIEventListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement PlayerUIEventListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.player_fragment, container, false);
		
		if (adapter == null /* && */) {
			adapter = new PlayerAdapter(view.getContext());
		}

		// TODO: call to start playing audio
		// pc.start();

		Button prevTrack = (Button) view.findViewById(R.id.prevTrack);
		prevTrack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				fragmentOwner.previousTrack();
			}
		});
		prevTrack.setEnabled(false);

		Button seekLeft = (Button) view.findViewById(R.id.seekLeft);
		seekLeft.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					//fragmentOwner.seekLeft();
					Log.i("Seek", "Seeking LEFT");
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					Log.i("Seek", "Stopped seeking LEFT");
				}
				return false;
			}
		});
		
		Button seekRight = (Button) view.findViewById(R.id.seekRight);
		seekRight.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					//fragmentOwner.seekRight();
					Log.i("Seek", "Seeking RIGHT");
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					Log.i("Seek", "Stopped seeking RIGHT");
				}
				return false;
			}
		});

		Button playPause = (Button) view.findViewById(R.id.playPause);
		playPause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				fragmentOwner.playPause();
			}
		});

		Button nextTrack = (Button) view.findViewById(R.id.nextTrack);
		nextTrack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				fragmentOwner.nextTrack();
			}
		});

		SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
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

				fragmentOwner.seekToPercentage(seekPercentage);
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

		return view;
	}

	public void propertyChange(PropertyChangeEvent event) {
		String eventName = event.getPropertyName();
		if (eventName.equals(Constants.event.BOOK_SELECTED)) {
			;
			// What to get:
			// BOOK_SELECTED
			// TRACK_INDEX_CHANGED (change audio file)
		}
	}
}
