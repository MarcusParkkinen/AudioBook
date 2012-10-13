/**
 *  This work is licensed under the Creative Commons Attribution-NonCommercial-
 *  NoDerivs 3.0 Unported License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to 
 *  Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 
 *  94041, USA.
 * 
 *  Use of this work is permitted only in accordance with license rights granted.
 *  Materials provided "AS IS"; no representations or warranties provided.
 * 
 *  Copyright © 2012 Marcus Parkkinen, Aki Käkelä, Fredrik Åhs.
 **/

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
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import edu.chalmers.dat255.audiobookplayer.R;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;
import edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents;
import edu.chalmers.dat255.audiobookplayer.util.TimeFormatter;

/**
 * A graphical UI representing the audio player.
 * 
 * @author Aki K�kel�, Marcus Parkkinen
 * @version 0.6
 * 
 */
public class PlayerFragment extends Fragment {
	private static final String TAG = "PlayerFragment.class";
	private static final int NUMBER_OF_SEEK_BAR_ZONES = Constants.Value.NUMBER_OF_SEEK_BAR_ZONES;
	private SeekBar bookBar;
	private SeekBar trackBar;
	private TextView bookTitle;
	private TextView trackTitle;
	// private TextView bookDuration;
	// private TextView trackDuration;
	// private TextView bookElapsedTime;
	private TextView trackElapsedTime;
	private IPlayerEvents fragmentOwner;

	private ImageButton playPause;

	private boolean isPlaying = true;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			fragmentOwner = (IPlayerEvents) activity;
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

		ImageButton seekLeft = (ImageButton) view.findViewById(R.id.seekLeft);
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

		ImageButton seekRight = (ImageButton) view.findViewById(R.id.seekRight);
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

		// this is a toggle button
		playPause = (ImageButton) view.findViewById(R.id.playPause);
		playPause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/*
				 * if it is player when the user presses pause, then pause and
				 * show the play button (toggle pause/play and the isPlaying
				 * value).
				 */
				if (isPlaying) {
					fragmentOwner.pause();
					playPause.setImageResource(R.drawable.pb_play_default);
				} else {
					fragmentOwner.play();
					playPause.setImageResource(R.drawable.pb_pause_default);
				}
				isPlaying = !isPlaying;
			}
		});

		ImageButton nextTrack = (ImageButton) view.findViewById(R.id.nextTrack);
		nextTrack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				fragmentOwner.nextTrack();
			}
		});

		ImageButton prevTrack = (ImageButton) view.findViewById(R.id.prevTrack);
		prevTrack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				fragmentOwner.previousTrack();
			}
		});

		bookBar = (SeekBar) view.findViewById(R.id.bookBar);
		bookBar.setMax(NUMBER_OF_SEEK_BAR_ZONES);
		bookBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			static final String TAG = "bookBar";

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					Log.d(TAG, "User used SeekBar");

					double seekMultiplier = (double) progress
							* (1.0 / seekBar.getMax());

					fragmentOwner.seekToPercentageInBook(seekMultiplier);
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
		trackBar.setMax(NUMBER_OF_SEEK_BAR_ZONES);
		trackBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			static final String TAG = "trackBar";

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					Log.d(TAG, "User used SeekBar");

					double seekPercentage = (double) progress
							* (1.0 / (double) seekBar.getMax());

					fragmentOwner.seekToPercentageInTrack(seekPercentage);
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

		// bookDuration = (TextView) view.findViewById(R.id.bookDuration);
		// bookDuration.setText("BD");
		//
		// trackDuration = (TextView) view.findViewById(R.id.trackDuration);
		// trackDuration.setText("TD");
		//
		// bookElapsedTime = (TextView) view.findViewById(R.id.bookElapsedTime);
		// bookElapsedTime.setText(Constants.Value.NO_BOOK_TIME_TO_DISPLAY);

		trackElapsedTime = (TextView) view.findViewById(R.id.trackElapsedTime);
		trackElapsedTime.setText(Constants.Value.NO_TRACK_TIME_TO_DISPLAY);

		return view;
	}

	/*
	 * Seek bars
	 */
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
		// Log.d(TAG, "Seeking track bar to " + progress + "%" + " (" +
		// percentage + "%)");
		// calls 'onProgressChanged' from code:
		trackBar.setProgress(progress);
	}

	/*
	 * Titles
	 */
	/**
	 * Sets the title of the book to the given title.
	 * 
	 * @param title
	 */
	public void updateBookTitleLabel(String title) {
		if (isBadTitle(title)) {
			bookTitle.setText("N/A");
		} else {
			bookTitle.setText(title);
		}
	}

	/**
	 * Sets the title of the track to the given title.
	 * 
	 * @param title
	 */
	public void updateTrackTitleLabel(String title) {
		if (isBadTitle(title)) {
			trackTitle.setText("N/A");
		} else {
			trackTitle.setText(title);
		}
	}

	/*
	 * Elapsed time labels
	 */
	/**
	 * Sets the elapsed time label of the book to the given time in
	 * milliseconds. Formats the time (see {@link #formatTime(int)})
	 * 
	 * @param ms
	 */
	public void updateBookElapsedTimeLabel(int ms) {
		// bookElapsedTime.setText(TimeFormatter.formatTimeFromMillis(ms));
	}

	/**
	 * Sets the elapsed time label of the track to the given time in
	 * milliseconds. Formats the time (see {@link #formatTime(int)})
	 * 
	 * @param ms
	 */
	public void updateTrackElapsedTimeLabel(int ms) {
		trackElapsedTime.setText(TimeFormatter.formatTimeFromMillis(ms));
	}

	/*
	 * Duration labels
	 */
	/**
	 * Sets the duration label of the book to the given time in milliseconds.
	 * <p>
	 * Formates the time. (see {@link TimeFormatter#formatTimeFromMillis(int)
	 * formatTimeFromMillis})
	 * 
	 * @param ms
	 */
	public void updateBookDurationLabel(int ms) {
		// bookDuration.setText(formatTime(ms));
	}

	/**
	 * Sets the duration label of the track to the given time in milliseconds.
	 * <p>
	 * Formates the time. (see {@link TimeFormatter#formatTimeFromMillis(int)
	 * formatTimeFromMillis})
	 * 
	 * @param ms
	 */
	public void updateTrackDurationLabel(int ms) {
		// trackDuration.setText(formatTime(ms));
	}

	/**
	 * Checks if a string is null or empty ("").
	 * 
	 * @param title
	 * @return
	 */
	private boolean isBadTitle(String title) {
		return title == null || title.equals("");
	}

	/**
	 * Sets the play/pause button to show as pause, since audio is playing. Used
	 * when books are selected. Otherwise this class will handle toggling the
	 * state by itself.
	 */
	public void setToPlaying() {
		// playPause.setImageResource(R.drawable.pb_pause_default);
	}

}
