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
import android.widget.TextView;
import edu.chalmers.dat255.audiobookplayer.R;

/**
 * A graphical UI fragment of AudioBookActivity in charge of PlayerController.
 * 
 * @author Aki Käkelä, Marcus Parkkinen
 * @version 0.6
 */
public class PlayerFragment extends Fragment {
	@SuppressWarnings("unused")
	private static final String TAG = "PlayerFragment.class";
	private PlayerUIEventListener fragmentOwner;
	private SeekBar trackBar;
	private SeekBar bookBar;
	private TextView bookTitle;
	private TextView trackTitle;
	private TextView bookDuration;
	private TextView trackDuration;
	private TextView bookElapsedTime;
	private TextView trackElapsedTime;

	// 2 Title labels: Track, Book
	// 2 Time labels: Track, Book
	// (Picker to seek to given time)

	// TODO: make some methods protected

	public interface PlayerUIEventListener {
		public void previousTrack();

		public void playPause();

		public void nextTrack();

		public void seekLeft();

		public void seekRight();

		public void seekToPercentageInTrack(double percentage);

		public void seekToPercentageInBook(double percentage);

		// public int getTrackDuration();

		// public int getBookDuration();

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

		Button prevTrack = (Button) view.findViewById(R.id.prevTrack);
		prevTrack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				fragmentOwner.previousTrack();
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

					// fragmentOwner.seekToTimeInBook(time);
					fragmentOwner.seekToPercentageInBook(seekMultiplier);
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
					Log.d(TAG, "User used SeekBar");
					
					double seekPercentage = (double) progress
							* (1.0 / seekBar.getMax());

					fragmentOwner.seekToPercentageInTrack(seekPercentage);
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
		
		trackTitle = (TextView) view.findViewById(R.id.trackTitle);
		trackTitle.setText("track time");
		
		bookTitle = (TextView) view.findViewById(R.id.bookTitle);
		bookTitle.setText("book time");
		
		bookDuration = (TextView) view.findViewById(R.id.bookDuration);
		bookDuration.setText("XX");
		
		trackDuration = (TextView) view.findViewById(R.id.trackDuration);
		trackDuration.setText("YY");
		
		bookElapsedTime = (TextView) view.findViewById(R.id.bookElapsedTime);
		bookElapsedTime.setText("AA");
		
		trackElapsedTime = (TextView) view.findViewById(R.id.trackElapsedTime);
		trackElapsedTime.setText("BB");
		
		return view;
	}

	public void updateBookSeekBar(double percentage) {
		int progress = (int) (bookBar.getMax() * percentage);
		// calls 'onProgressChanged' from code:
		bookBar.setProgress(progress);
	}

	public void updateTrackSeekBar(double percentage) {
		int progress = (int) (trackBar.getMax() * percentage);
		// calls 'onProgressChanged' from code:
		trackBar.setProgress(progress);
	}

	public void updateBookElapsedTimeLabel(String label) {
		bookElapsedTime.setText(label);
	}

	public void updateTrackElapsedTimeLabel(String label) {
		trackElapsedTime.setText(label);
	}

	public void updateBookTitleLabel(String label) {
		bookTitle.setText(label);
	}

	public void updateTrackTitleLabel(String label) {
		trackTitle.setText(label);
	}

	public void updateBookDurationLabel(String label) {
		bookDuration.setText(label);
	}

	public void updateTrackDurationLabel(String label) {
		trackDuration.setText(label);
	}
}
