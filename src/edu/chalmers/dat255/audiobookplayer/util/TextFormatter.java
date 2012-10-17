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
		// to defeat instantiation
	}

	private static final int MSECONDS_IN_SECOND = 1000;
	private static final int SECONDS_IN_MINUTE = 60;
	private static final int MINUTES_IN_HOUR = 60;
	private static final int HOURS_IN_DAY = 24;
	private static final int LARGEST_SINGLE_DIGIT_INTEGER = 9;

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
		int seconds = (ms / MSECONDS_IN_SECOND) % SECONDS_IN_MINUTE;
		int minutes = (ms / (MSECONDS_IN_SECOND * SECONDS_IN_MINUTE))
				% MINUTES_IN_HOUR;
		int hours = (ms / (MSECONDS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR))
				% HOURS_IN_DAY;

		/*
		 * Return the hour text only if it is above 0. Add a zero to the minutes
		 * counter text if it needs one to make it 2 integers long. Only add a
		 * zero to the seconds counter text if the number of minutes is zero and
		 * it needs one for 2 symbols.
		 */

		return (hours % HOURS_IN_DAY > 0 ? hours % HOURS_IN_DAY + ":" : "")
				+ ((minutes <= LARGEST_SINGLE_DIGIT_INTEGER && hours
						% HOURS_IN_DAY > 0) ? "0" : "")
				+ minutes
				+ ":"
				+ ((seconds <= LARGEST_SINGLE_DIGIT_INTEGER && minutes == 0) ? "0"
						: "") + seconds;
	}

	/**
	 * Formats a given position and limit to a text.
	 * 
	 * @param trackNumber
	 * @param numberOfTracks
	 * @return Formatted text.
	 */
	public static String formatCounter(int trackNumber, int numberOfTracks) {
		// no tracks
		if (numberOfTracks == 0) {
			return Constants.Message.NO_TRACKS_FOUND;
		}

		// illegal track index
		if (trackNumber != Constants.Value.NO_TRACK_SELECTED
				&& (trackNumber < 0 || trackNumber > numberOfTracks)) {
			return Constants.Message.TRACK_INDEX_ERROR;
		}

		// none selected
		if (trackNumber == Constants.Value.NO_TRACK_SELECTED) {
			return numberOfTracks + "";
		}

		// a valid track is selected
		return trackNumber + "/" + numberOfTracks;
	}
}
