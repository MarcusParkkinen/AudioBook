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

package edu.chalmers.dat255.audiobookplayer.util;

import edu.chalmers.dat255.audiobookplayer.constants.Constants;

/**
 * Utility class that formats given values to specific texts.
 * 
 * @author Aki Käkelä
 * @version 0.3
 * 
 */
public final class TextFormatter {

	private TextFormatter() {
	} // to defeat instantiation

	// Conversions
	private static final int MSECONDS_IN_SECOND = 1000;
	private static final int SECONDS_IN_MINUTE = 60;
	private static final int MINUTES_IN_HOUR = 60;
	private static final int HOURS_IN_DAY = 24;

	// Help values for making the code clearer
	private static final int LARGEST_SINGLE_DIGIT = 9;
	private static final String SEPARATOR = ":";

	/**
	 * Formats a given time in milliseconds to a text containing days, hours,
	 * minutes, and seconds.
	 * <p>
	 * Days are only shown if the number of hours exceeds 23.
	 * <p>
	 * Hours are only shown if the number of minutes exceeds 59.
	 * <p>
	 * Minutes and seconds are always in the MM:SS format.
	 * <p>
	 * Possibilities:
	 * <p>
	 * <b>MM:SS</b>, <b>H:MM:SS</b>, <b>HH:MM:SS</b>, <b>D:HH:MM:SS</b>,
	 * <b>DD:HH:MM:SS</b>
	 * 
	 * NOTE: ms is currently kept as int (integer) meaning that the maximum
	 * number of days (before a roll-over from zero again) is 24.855.. ~= 24
	 * days.
	 * 
	 * @param ms
	 *            Amount of milliseconds to format.
	 * @return Formatted text from given input.
	 */
	public static String formatTimeFromMillis(int ms) {
		// get the seconds, minutes and hours from the given ms
		int iSeconds = (ms / (MSECONDS_IN_SECOND)) % SECONDS_IN_MINUTE;
		int iMinutes = (ms / (MSECONDS_IN_SECOND * SECONDS_IN_MINUTE))
				% MINUTES_IN_HOUR;
		int iHours = (ms / (MSECONDS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR))
				% HOURS_IN_DAY;
		int iDays = (ms / (MSECONDS_IN_SECOND * SECONDS_IN_MINUTE
				* MINUTES_IN_HOUR * HOURS_IN_DAY));

		/*
		 * Return the days text only if it is above 0.
		 * 
		 * Return the hour text only if it is above 0.
		 * 
		 * Make the hour text 2 integers long only if the days text is above 0.
		 * 
		 * If the days text is beyond 99 (i.e. 3 symbols wide) do something else
		 * in the future to make sure that the text is not truncated (i.e.
		 * "2:07:2...") or similar.
		 * 
		 * Always make the minutes and seconds 2 integers long.
		 * 
		 * If the days text is above 0, make sure that the hours text is 2
		 * integers long.
		 * 
		 * Add a zero to the minutes counter text if it needs one to make it 2
		 * integers long. Only add a zero to the seconds counter text if the
		 * number of minutes is zero and it needs one for 2 symbols.
		 */

		String days, hours;

		// If there are days to display, the hour text should always be 2
		// integers. Otherwise it should be displayed as it is (1-2 integers).
		if (iDays == 0) {
			// do not add a separator
			days = "";

			if (iHours == 0) {
				// do not add a separator
				hours = "";
			} else {
				// there are no days to show, so write the hours as they are
				hours = "" + iHours + SEPARATOR;
			}
		} else {
			// there are days to display

			/*
			 * The days will currently be shown as they are (anywhere from 1 to
			 * any number of integers) if above zero.
			 * 
			 * Future versions should probably show this in a different way
			 * (e.g. "more than x days") but this is not important in a media
			 * player and will thus be ignored for now.
			 */
			days = "" + iDays + SEPARATOR;

			// always write the hours as 2 integers since we have days to
			// display as well.
			hours = toTwoIntegers(iHours) + SEPARATOR;
		}

		// always show minutes and seconds as 2 integers.
		String minutes = toTwoIntegers(iMinutes) + SEPARATOR;
		String seconds = toTwoIntegers(iSeconds);

		return days + hours + minutes + seconds;
	}

	/**
	 * Helper class. It is expected that the time does not surpass 2 integers
	 * (i.e. proper modulus conversion is already made).
	 * 
	 * @param time
	 *            Time to format to 2 integers.
	 * @return Formatted time to 2 integers.
	 */
	private static String toTwoIntegers(int time) {
		String result;

		if (time == 0) {
			result = "00";
		} else if (time <= LARGEST_SINGLE_DIGIT) {
			result = "0" + time;
		} else {
			// larger than 1 integer.
			result = "" + time;
		}

		return result;
	}

	/**
	 * Formats a given position and limit to a text.
	 * 
	 * @param trackIndex
	 * @param numberOfTracks
	 * @return Formatted text.
	 */
	public static String formatCounter(int trackIndex, int numberOfTracks) {
		// no tracks
		if (numberOfTracks == 0) {
			return Constants.Message.NO_TRACKS_FOUND;
		}

		// illegal track index
		if (trackIndex != Constants.Value.NO_TRACK_SELECTED
				&& (trackIndex < 0 || trackIndex > numberOfTracks)) {
			return Constants.Message.TRACK_INDEX_ERROR;
		}

		// none selected
		if (trackIndex == Constants.Value.NO_TRACK_SELECTED) {
			return numberOfTracks + "";
		}

		// a valid track is selected
		// change the index to a track number (i.e. starts from 1).
		return (trackIndex + 1) + "/" + numberOfTracks;
	}
}
