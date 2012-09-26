package edu.chalmers.dat255.audiobookplayer.view;

import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.view.BookshelfFragment.BookshelfUIEventListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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

public class PlayerFragment extends Fragment {
	private PlayerUIEventListener fragmentOwner;
	
	public interface PlayerUIEventListener {
		public void previousTrack();
		public void playPause();
		public void nextTrack();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try{
			fragmentOwner = (PlayerUIEventListener) activity;
		} catch(ClassCastException e) {
			throw new ClassCastException(activity.toString() +
					" must implement PlayerUIEventListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.player_fragment, container, false);
		
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
				seekLeft.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// TODO: hold down to seek continuously
						//pc.seekLeft(pc.getTrackDuration() / 10);
					}
				});

				Button playPause = (Button) view.findViewById(R.id.playPause);
				playPause.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						fragmentOwner.playPause();
					}
				});

				Button seekRight = (Button) view.findViewById(R.id.seekRight);
				seekRight.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// TODO: hold down to seek continuously
						//pc.seekRight(pc.getTrackDuration() / 10);
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

						//pc.seekTo((int) (pc.getTrackDuration() * seekPercentage));
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
}
