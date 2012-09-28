package edu.chalmers.dat255.audiobookplayer.view;

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
import edu.chalmers.dat255.audiobookplayer.R;

/**
 * A graphical UI fragment of AudioBookActivity in charge of PlayerController.
 * 
 * @author Aki Käkelä, Marcus Parkkinen
 * @version 0.5
 */
public class PlayerFragment extends Fragment {
	private static final String TAG = "PlayerFragment.class";
	private PlayerUIEventListener fragmentOwner;
	private SeekBar trackBar;
	private SeekBar bookBar;

	// Labels, update methods

	// TODO: make some methods protected

	public interface PlayerUIEventListener {
		public void previousTrack();

		public void playPause();

		public void nextTrack();

		public void seekLeft();

		public void seekRight();

		public void seekToMultiplierInTrack(double seekPercentage);

		public void seekToMultiplierInBook(double percentage);

		public int getTrackDuration();

		public int getBookDuration();

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

		Button prevTrack = (Button) view.findViewById(R.id.prevTrack);
		prevTrack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				fragmentOwner.previousTrack();
			}
		});

		Button seekLeft = (Button) view.findViewById(R.id.seekLeft);
		seekLeft.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int ev = event.getAction();
				if (ev == MotionEvent.ACTION_DOWN) {
					Log.i("Seek", "Seeking LEFT (ACTION_DOWN)");
				} else if (ev == MotionEvent.ACTION_UP) {
					Log.i("Seek", "Stopped seeking LEFT (ACTION_UP)");
				} else if (ev == MotionEvent.ACTION_CANCEL) {
					Log.i("Seek", "Cancelled seeking LEFT (ACTION_CANCEL)");
				}
				return false;
			}
		});

		Button seekRight = (Button) view.findViewById(R.id.seekRight);
		seekRight.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int ev = event.getAction();
				if (ev == MotionEvent.ACTION_DOWN) {
					Log.i("Seek", "Seeking RIGHT (ACTION_DOWN)");
				} else if (ev == MotionEvent.ACTION_UP) {
					Log.i("Seek", "Stopped seeking RIGHT (ACTION_UP)");
				} else if (ev == MotionEvent.ACTION_CANCEL) {
					Log.i("Seek", "Cancelled seeking RIGHT (ACTION_CANCEL)");
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

		bookBar = (SeekBar) view.findViewById(R.id.bookBar);
		bookBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			static final String TAG = "bookBar";

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					Log.d(TAG, "User used SeekBar");

					double seekMultiplier = (double) progress
							* (1.0 / seekBar.getMax());

					// TODO: currently just for debugging
					int displayPercentage = (int) (100 * seekMultiplier);

					Log.d(TAG,
							"progress is " + progress + " out of "
									+ seekBar.getMax() + "("
									+ displayPercentage + "%)");

					// fragmentOwner.seekToTimeInBook(time);
					fragmentOwner.seekToMultiplierInBook(seekMultiplier);
				} else {
					Log.d(TAG, "Code used SeekBar");
				}
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				Log.d(TAG, "started tracking");
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				Log.d(TAG, "stopped tracking");
			}

		});

		trackBar = (SeekBar) view.findViewById(R.id.trackBar);
		trackBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			static final String TAG = "trackBar";

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					// seekTo();
					Log.d(TAG, "User used SeekBar");
					// updateTrackSeekBar();
					double seekPercentage = (double) progress
							* (1.0 / seekBar.getMax());

					// TODO: currently just for debugging
					int displayPercentage = (int) (100 * seekPercentage);

					Log.d(TAG,
							"progress is " + progress + " out of "
									+ seekBar.getMax() + "("
									+ displayPercentage + "%)");

					fragmentOwner.seekToMultiplierInTrack(seekPercentage);
				} else {
					// do not seekTo();
					Log.d(TAG, "Code used SeekBar");
				}
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				Log.d(TAG, "started tracking");
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				Log.d(TAG, "stopped tracking");
			}
		});

		return view;
	}

	// public void updateTrackProgress(int newTime) {
	// // seekBar.setProgress((int)(Math.random()*100));
	// int newProgress = (int) ((trackBar.getMax() * newTime) / fragmentOwner
	// .getTrackDuration());
	// // Log.d("PlayerFragment.class", "timeProgress: " + timeProgress);
	// // Log.d("PlayerFragment.class", "newProgress: " + newProgress);
	// trackBar.setProgress(newProgress);
	// }

	/**
	 * @param i
	 *            Duration in ms of the book.
	 */
	public void updateBookSeekBar(int i) {

	}

	public void updateTrackSeekBar() {

	}

	public void updateBookTitleLabel() {

	}

	public void updateTrackDuration(int newTime) {

	}

	public void updateBookDuration(int newTime) {

	}
}
