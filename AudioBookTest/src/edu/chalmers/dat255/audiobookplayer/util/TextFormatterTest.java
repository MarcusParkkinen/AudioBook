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
import junit.framework.TestCase;

/**
 * @author Aki Käkelä
 * @version 0.1
 * 
 */
public class TextFormatterTest extends TestCase {

	// Conversions
	private static final int MSECS_IN_SECOND = 1000;
	private static final int SECS_IN_MINUTE = 60;
	private static final int MINS_IN_HOUR = 60;
	private static final int HOURS_IN_DAY = 24;

	// The 'magic numbers'
	private static final int ONE_DIGIT = 3;
	private static final int TWO_DIGITS = 59;

	// Converting from milliseconds (ms)
	private static final int SECONDS = MSECS_IN_SECOND;
	private static final int MINUTES = MSECS_IN_SECOND * SECS_IN_MINUTE;
	private static final int HOURS = MSECS_IN_SECOND * SECS_IN_MINUTE
			* MINS_IN_HOUR;
	private static final int DAYS = MSECS_IN_SECOND * SECS_IN_MINUTE
			* MINS_IN_HOUR * HOURS_IN_DAY;

	private static final int NO_OF_TRACKS = 25; // "limit"
	private static final int NO_SELECTION = -1;

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.util.TextFormatter#formatTimeFromMillis(int)}
	 * .
	 */
	public final void testFormatTimeFromMillis() {
		String dummy;

		// test zero
		dummy = "00:00";
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(0)));

		// test single-digit seconds
		dummy = "00:0" + ONE_DIGIT;
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(ONE_DIGIT
				* SECONDS)));

		// test double-digit seconds
		dummy = "00:" + TWO_DIGITS;
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(TWO_DIGITS
				* SECONDS)));

		// test single-digit minutes
		dummy = "0" + ONE_DIGIT + ":00";
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(ONE_DIGIT
				* MINUTES)));

		// test double-digit minutes
		dummy = TWO_DIGITS + ":00";
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(TWO_DIGITS
				* MINUTES)));

		// test single-digit hour
		dummy = ONE_DIGIT + ":00:00";
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(ONE_DIGIT
				* HOURS)));

		// test double-digit hour
		dummy = TWO_DIGITS + ":00:00";
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(TWO_DIGITS
				* HOURS)));

		// test only hours and seconds
		dummy = TWO_DIGITS + ":00:" + TWO_DIGITS;
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(TWO_DIGITS
				* HOURS + TWO_DIGITS * SECONDS)));

		// test seconds >= 60
		dummy = TWO_DIGITS + ":00";
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(TWO_DIGITS
				* SECS_IN_MINUTE * SECONDS)));

		// test minutes >= 60
		dummy = ONE_DIGIT + ":00:00";
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(ONE_DIGIT
				* MINS_IN_HOUR * MINUTES)));

		// test hours >= 24
		dummy = TWO_DIGITS + ":" + TWO_DIGITS + ":" + TWO_DIGITS;
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(523 * DAYS
				+ TWO_DIGITS * HOURS + TWO_DIGITS * MINUTES + TWO_DIGITS
				* SECONDS)));

	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.util.TextFormatter#formatCounter(int, int)}
	 * .
	 */
	public final void testFormatCounter() {
		String dummy;

		// The following two tests should work as intended

		// test 0
		dummy = "0/" + NO_OF_TRACKS;
		assertTrue(dummy.equals(TextFormatter.formatCounter(0, NO_OF_TRACKS)));

		// test position = limit
		dummy = NO_OF_TRACKS + "/" + NO_OF_TRACKS;
		assertTrue(dummy.equals(TextFormatter.formatCounter(NO_OF_TRACKS,
				NO_OF_TRACKS)));

		// The following three should return special case messages

		// test with "-1"; no selection
		dummy = Constants.Message.NO_TRACK_SELECTED;
		assertTrue(dummy.equals(TextFormatter.formatCounter(NO_SELECTION,
				NO_OF_TRACKS)));

		// test limit = 0
		dummy = Constants.Message.NO_TRACKS_FOUND;
		assertTrue(dummy.equals(TextFormatter.formatCounter(0, 0)));

		// test position > limit
		dummy = Constants.Message.TRACK_INDEX_ERROR;
		assertTrue(dummy.equals(TextFormatter.formatCounter(NO_OF_TRACKS + 1,
				NO_OF_TRACKS)));

	}

}
