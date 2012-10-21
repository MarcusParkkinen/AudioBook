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
 * @version 0.2
 * 
 */
public final class TextFormatter {

	private TextFormatter() {
	} // to defeat instantiation

	private static final int MSECONDS_IN_SECOND = 1000;
	private static final int SECONDS_IN_MINUTE = 60;
	private static final int MINUTES_IN_HOUR = 60;
	private static final int HOURS_IN_DAY = 24;
	private static final int LARGEST_SINGLE_DIGIT = 9;
	private static final String SEPARATOR = ":";

	/**
	 * Formats a given time in milliseconds to a text.
	 * <p>
	 * 
	 * If the number of hours is greater than zero or nine the time will be
	 * formatted to H:MM:SS or HH:MM:SS, respectively. If it surpasses 23, it
	 * will start from zero again (modulus 24).
	 * 
	 * @param ms
	 * @return
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
		 * 
		 * Possibilities:
		 * 
		 * MM:SS
		 * 
		 * H:MM:SS
		 * 
		 * HH:MM:SS
		 * 
		 * D:HH:MM:SS
		 * 
		 * DD:HH:MM:SS
		 */

		String seconds, minutes, hours, days;

		// show mins / secs with 2 integers
		seconds = (iSeconds > LARGEST_SINGLE_DIGIT ? "" + iSeconds : "0" + iSeconds);
		minutes = (iMinutes > LARGEST_SINGLE_DIGIT ? "" + iMinutes : "0" + iMinutes);

		// if there are days to show, make hours 2 integers long,
		// otherwise just show hours as they are.
		hours = (iDays > 0 ? (iHours > LARGEST_SINGLE_DIGIT ? "" + iHours : "0" + iHours) : "" + iHours);

		// always show the days as they are (1, 2, 3... n, digits)
		days = "" + iDays;

		return days  + SEPARATOR + hours + SEPARATOR + minutes + SEPARATOR + seconds;
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
