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
import edu.chalmers.dat255.audiobookplayer.constants.PlaybackStatus;
import edu.chalmers.dat255.audiobookplayer.interfaces.IPlayerEvents;
import edu.chalmers.dat255.audiobookplayer.util.TextFormatter;

/**
 * A graphical UI representing the audio player.
 * 
 * @author Aki Käkelä
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
	private TextView trackCounter;
	private IPlayerEvents fragmentOwner;

	private ImageButton playPause;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		boolean ownerImplementsEvents = true;

		try {
			fragmentOwner = (IPlayerEvents) activity;
		} catch (ClassCastException e) {
			ownerImplementsEvents = false;
		}
		if (!ownerImplementsEvents) {
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
					Log.i(TAG, "Seeking LEFT (ACTION_DOWN)");
					seekLeft(false);
				} else if (ev == MotionEvent.ACTION_UP) {
					Log.i(TAG, "Stopped seeking LEFT (ACTION_UP)");
				} else if (ev == MotionEvent.ACTION_CANCEL) {
					Log.i(TAG, "Cancelled seeking LEFT (ACTION_CANCEL)");
				}
				return false;
			}
		});

		ImageButton seekRight = (ImageButton) view.findViewById(R.id.seekRight);
		seekRight.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int ev = event.getAction();
				if (ev == MotionEvent.ACTION_DOWN) {
					Log.i(TAG, "Seeking RIGHT (ACTION_DOWN)");
					seekRight(true);
				} else if (ev == MotionEvent.ACTION_UP) {
					Log.i(TAG, "Stopped seeking RIGHT (ACTION_UP)");
				} else if (ev == MotionEvent.ACTION_CANCEL) {
					Log.i(TAG, "Cancelled seeking RIGHT (ACTION_CANCEL)");
				}
				return false;
			}
		});

		playPause = (ImageButton) view.findViewById(R.id.playPause);
		playPause.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/*
				 * if audio is playing when the user presses pause, then pause
				 * and show the play button.
				 */
				if (fragmentOwner.isPlaying()) {
					// it was playing, so pause
					fragmentOwner.pause();
					setPlaybackStatus(PlaybackStatus.PAUSED);
				} else if (fragmentOwner.isStarted()) {
					// was paused, so resume
					fragmentOwner.resume();
					setPlaybackStatus(PlaybackStatus.PLAYING);
				} else {
					// it was stopped, keep it stopped
					setPlaybackStatus(PlaybackStatus.STOPPED);
				}
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

		bookBar = (SeekBar) view.findViewById(R.id.book_seek_bar);
		bookBar.setMax(Constants.Value.NUMBER_OF_SEEK_BAR_ZONES);
		bookBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					// calculate the seek progress by the max value of the bar
					double seekPercentage = (double) progress
							* (1.0 / seekBar.getMax());

					fragmentOwner.seekToPercentageInBook(seekPercentage);
				}
				// else do nothing since this was done by code
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}// do nothing

			public void onStopTrackingTouch(SeekBar seekBar) {
			}// do nothing

		});

		trackBar = (SeekBar) view.findViewById(R.id.track_bar);
		trackBar.setMax(Constants.Value.NUMBER_OF_SEEK_BAR_ZONES);
		trackBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					// calculate the seek progress by the max value of the bar
					double seekPercentage = (double) progress
							* (1.0 / seekBar.getMax());

					fragmentOwner.seekToPercentageInTrack(seekPercentage);
				}
				// else do nothing since this was done by code
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			} // do nothing

			public void onStopTrackingTouch(SeekBar seekBar) {
			} // do nothing

		});

		trackTitle = (TextView) view.findViewById(R.id.track_title);

		bookTitle = (TextView) view.findViewById(R.id.bookTitle);

		bookDuration = (TextView) view.findViewById(R.id.book_duration);

		trackDuration = (TextView) view.findViewById(R.id.track_duration);

		bookElapsedTime = (TextView) view.findViewById(R.id.book_elapsed_time);

		trackElapsedTime = (TextView) view
				.findViewById(R.id.track_elapsed_time);

		trackCounter = (TextView) view.findViewById(R.id.track_counter);

		resetComponents();

		return view;
	}

	protected void seekRight(boolean seek) {
		fragmentOwner.seekRight(seek);
	}

	protected void seekLeft(boolean seek) {
		fragmentOwner.seekLeft(seek);
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
			bookTitle.setText(Constants.Message.NO_BOOK_TITLE);
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
			trackTitle.setText(Constants.Message.NO_TRACK_TITLE);
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
		bookElapsedTime.setText(TextFormatter.formatTimeFromMillis(ms));
	}

	/**
	 * Sets the elapsed time label of the track to the given time in
	 * milliseconds. Formats the time (see {@link #formatTime(int)})
	 * 
	 * @param ms
	 */
	public void updateTrackElapsedTimeLabel(int ms) {
		trackElapsedTime.setText(TextFormatter.formatTimeFromMillis(ms));
	}

	/*
	 * Duration labels
	 */
	/**
	 * Sets the duration label of the book to the given time in milliseconds.
	 * <p>
	 * Formats the time. (see {@link TextFormatter#formatTimeFromMillis(int)
	 * formatTimeFromMillis})
	 * 
	 * @param ms
	 */
	public void updateBookDurationLabel(int ms) {
		bookDuration.setText(TextFormatter.formatTimeFromMillis(ms));
	}

	/**
	 * Sets the duration label of the track to the given time in milliseconds.
	 * <p>
	 * Formats the time (see {@link TextFormatter#formatTimeFromMillis(int)
	 * formatTimeFromMillis}).
	 * 
	 * @param ms
	 */
	public void updateTrackDurationLabel(int ms) {
		trackDuration.setText(TextFormatter.formatTimeFromMillis(ms));
	}

	/**
	 * Sets the track counter label to the given track position and limit.
	 * <p>
	 * Formats the given input (see
	 * {@link PlayerFragment#formatTrackCounter(int, int)}).
	 * 
	 * @param currentTrack
	 * @param numberOfTracks
	 */
	public void updateTrackCounterLabel(int currentTrack, int numberOfTracks) {
		this.trackCounter.setText(formatTrackCounter(currentTrack,
				numberOfTracks));
	}

	/**
	 * Checks if a string is null or empty ("").
	 * 
	 * @param title
	 * @return
	 */
	private boolean isBadTitle(String title) {
		return title == null || title.equals("")
				|| title.length() > Constants.Value.MAX_TITLE_CHARACTER_LENGTH;
	}

	/**
	 * Resets the seek bar progresses, titles, elapsed times and durations to
	 * display their respective default texts.
	 */
	public void resetComponents() {
		Log.d(TAG, "No track selected -- resetting components.");
		// reset progress of seek bars
		this.bookBar.setProgress(0);
		this.trackBar.setProgress(0);

		// reset title texts
		this.bookTitle.setText(Constants.Message.NO_BOOK_TITLE);
		this.trackTitle.setText(Constants.Message.NO_TRACK_TITLE);

		// reset elapsed time texts
		this.trackElapsedTime.setText(Constants.Message.NO_TRACK_ELAPSED_TIME);
		this.bookElapsedTime.setText(Constants.Message.NO_BOOK_ELAPSED_TIME);

		// reset duration texts
		this.trackDuration.setText(Constants.Message.NO_TRACK_DURATION);
		this.bookDuration.setText(Constants.Message.NO_BOOK_DURATION);

		// reset the track counter
		this.trackCounter.setText(Constants.Message.NO_TRACKS_FOUND);

		// reset play/pause button to stopped
		setPlaybackStatus(PlaybackStatus.STOPPED);
	}

	/**
	 * Sets the play/pause status.
	 * 
	 * @param status
	 *            The status to change to.
	 */
	public void setPlaybackStatus(int status) {
		playPause.setBackgroundResource(0);
		switch (status) {
		case PlaybackStatus.PLAYING:
			playPause.setBackgroundResource(R.drawable.pb_pause_default);
			return;
		case PlaybackStatus.PAUSED:
			playPause.setBackgroundResource(R.drawable.pb_play_default);
			return;
		case PlaybackStatus.STOPPED:
		default:
			playPause
					.setBackgroundResource(R.drawable.pb_play_pause_disabled_default);
		}
	}

	/**
	 * Formats a given position and limit.
	 * 
	 * @param currentTrack
	 *            Current position.
	 * @param numberOfTracks
	 *            Limit.
	 * @return Formatted text. If a position is between 0 and the limit the
	 *         current position will be displayed.
	 */
	private String formatTrackCounter(int currentTrack, int numberOfTracks) {
		return TextFormatter.formatCounter(currentTrack, numberOfTracks);
	}

	public void updateTagTimes(int[] tagTimes) {
		// TODO(?): Tags not implemented in GUI.
	}

}
