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
import edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents;

/**
 * A graphical UI representing the audio player.
 * 
 * @author Aki Käkelä, Marcus Parkkinen
 * @version 0.6
 * 
 */
public class PlayerFragment extends Fragment {
	private static final String TAG = "PlayerFragment.class";
	private SeekBar bookBar;
	private SeekBar trackBar;
	private TextView bookTitle;
	private TextView trackTitle;
	private TextView bookDuration;
	private TextView trackDuration;
	private TextView bookElapsedTime;
	private TextView trackElapsedTime;
	IPlayerEvents parentFragment;

	// private Picker timePicker;

	// TODO: make some methods protected

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			parentFragment = (IPlayerEvents) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " does not implement " + IPlayerEvents.class.getName());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.fragment_player, container, false);

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
				parentFragment.playPause();
			}
		});

		Button nextTrack = (Button) view.findViewById(R.id.nextTrack);
		nextTrack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				parentFragment.nextTrack();
			}
		});

		Button prevTrack = (Button) view.findViewById(R.id.prevTrack);
		prevTrack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				parentFragment.previousTrack();
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

					parentFragment.seekToPercentageInBook(seekMultiplier);
				} else {
					// Log.d(TAG, "Code used bookBar");
				}
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// Log.d(TAG, "started tracking");
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// Log.d(TAG, "stopped tracking");
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
							* (1.0 / (double) seekBar.getMax());

					parentFragment.seekToPercentageInTrack(seekPercentage);
				} else {
					// Log.d(TAG, "Code used trackBar");
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
		trackTitle.setText("track title");

		bookTitle = (TextView) view.findViewById(R.id.bookTitle);
		bookTitle.setText("book title");

		bookDuration = (TextView) view.findViewById(R.id.bookDuration);
		bookDuration.setText("BD");

		trackDuration = (TextView) view.findViewById(R.id.trackDuration);
		trackDuration.setText("TD");

		bookElapsedTime = (TextView) view.findViewById(R.id.bookElapsedTime);
		bookElapsedTime.setText("BET");

		trackElapsedTime = (TextView) view.findViewById(R.id.trackElapsedTime);
		trackElapsedTime.setText("TET");

		return view;
	}

	// Seek bars
	/**
	 * Sets the progress on the book seek bar to the given percentage.
	 * 
	 * @param percentage
	 */
	public void updateBookSeekBar(double percentage) {
		int progress = (int) ((double) bookBar.getMax() * percentage);
		// Log.d(TAG, "Seeking book bar to " + progress + "%");
		// calls 'onProgressChanged' from code:
		bookBar.setProgress(progress);
	}

	/**
	 * Sets the progress on the track seek bar to the given percentage.
	 * 
	 * @param percentage
	 */
	public void updateTrackSeekBar(double percentage) {
		int progress = (int) (trackBar.getMax() * percentage);
		// Log.d(TAG, "Seeking track bar to " + progress + "%");
		// calls 'onProgressChanged' from code:
		trackBar.setProgress(progress);
	}

	/* Titles */
	/**
	 * Sets the title of the book to the given title.
	 * 
	 * @param title
	 */
	public void updateBookTitleLabel(String title) {
		bookTitle.setText(title);
	}

	/**
	 * Sets the title of the track to the given title.
	 * 
	 * @param title
	 */
	public void updateTrackTitleLabel(String title) {
		trackTitle.setText(title);
	}

	/* Elapsed time labels */
	/**
	 * Sets the elapsed time label of the book to the given time in
	 * milliseconds. Formats the time (see {@link #formatTime(int)})
	 * 
	 * @param ms
	 */
	public void updateBookElapsedTimeLabel(int ms) {
		bookElapsedTime.setText(formatTime(ms));
	}

	/**
	 * Sets the elapsed time label of the track to the given time in
	 * milliseconds. Formats the time (see {@link #formatTime(int)})
	 * 
	 * @param ms
	 */
	public void updateTrackElapsedTimeLabel(int ms) {
		trackElapsedTime.setText(formatTime(ms));
	}

	/* Duration labels */
	/**
	 * Sets the duration label of the book to the given time in milliseconds.
	 * Formats the time (see {@link #formatTime(int)})
	 * 
	 * @param ms
	 */
	public void updateBookDurationLabel(int ms) {
		bookDuration.setText(formatTime(ms));
	}

	/**
	 * Sets the duration label of the track to the given time in milliseconds.
	 * Formats the time (see {@link #formatTime(int)})
	 * 
	 * @param ms
	 */
	public void updateTrackDurationLabel(int ms) {
		trackDuration.setText(formatTime(ms));
	}

	/**
	 * Formats a given time in milliseconds to MM:SS.
	 * <p>
	 * 
	 * If the number of hours is greater than zero or nine the time will be
	 * formatted to H:MM:SS or HH:MM:SS, respectively. If it surpasses 23, it
	 * will start from zero again (modulus 24).
	 * 
	 * @param ms
	 * @return
	 */
	private String formatTime(int ms) {
		int seconds = (ms / 1000) % 60;
		int minutes = (ms / (1000 * 60)) % 60;
		int hours = (ms / (1000 * 60 * 60)) % 24;

		return (hours > 0 ? hours + ":" : "") + (minutes <= 9 ? "0" : "")
				+ minutes + ":" + (seconds <= 9 ? "0" : "") + seconds;
	}

}
