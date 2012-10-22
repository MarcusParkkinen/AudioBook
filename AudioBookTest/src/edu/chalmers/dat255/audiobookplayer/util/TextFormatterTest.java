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

import android.util.Log;
import junit.framework.TestCase;
import edu.chalmers.dat255.audiobookplayer.constants.Constants;

/**
 * @author Aki Käkelä
 * @version 0.1
 * 
 */
public class TextFormatterTest extends TestCase {
	private static final String TAG = "TextFormatterText";

	// Conversions
	private static final int MSECS_IN_SECOND = 1000;
	private static final int SECS_IN_MINUTE = 60;
	private static final int MINS_IN_HOUR = 60;
	private static final int HOURS_IN_DAY = 24;

	// The 'magic numbers'
	// has to be between 0 and 9
	private static final int ONE_DIGIT = 3;
	// has to be between 10 and 23 (24 hours = 1 day)
	// values of 25 and up will cause the int to roll over to zero.
	private static final int TWO_DIGITS = 23;

	private static final int NO_OF_TRACKS = 25; // "limit"

	// Converting to milliseconds (ms)
	private static final int SECONDS = MSECS_IN_SECOND;
	private static final int MINUTES = MSECS_IN_SECOND * SECS_IN_MINUTE;
	private static final int HOURS = MSECS_IN_SECOND * SECS_IN_MINUTE
			* MINS_IN_HOUR;
	private static final int DAYS = MSECS_IN_SECOND * SECS_IN_MINUTE
			* MINS_IN_HOUR * HOURS_IN_DAY;

	private static final String NOT_EQUAL = "!=";

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.util.TextFormatter#formatTimeFromMillis(int)}
	 * .
	 */
	public final void testFormatTimeFromMillis() {
		// the expected result
		String expected;

		// the result
		String result;

		// test zero
		expected = "00:00";
		result = TextFormatter.formatTimeFromMillis(0);
		assertTrue("zeroes" + "[" + expected + NOT_EQUAL + result + "]",
				expected.equals(result));

		// test single-digit seconds
		expected = "00:0" + ONE_DIGIT;
		result = TextFormatter.formatTimeFromMillis(ONE_DIGIT * SECONDS);
		assertTrue("single-digit seconds" + "[" + expected + NOT_EQUAL + result
				+ "]", expected.equals(result));

		// test double-digit seconds
		expected = "00:" + TWO_DIGITS;
		result = TextFormatter.formatTimeFromMillis(TWO_DIGITS * SECONDS);
		assertTrue("double-digit seconds" + "[" + expected + NOT_EQUAL + result
				+ "]", expected.equals(result));

		// test single-digit minutes
		expected = "0" + ONE_DIGIT + ":00";
		result = TextFormatter.formatTimeFromMillis(ONE_DIGIT * MINUTES);
		assertTrue("single-digit minutes" + "[" + expected + NOT_EQUAL + result
				+ "]", expected.equals(result));

		// test double-digit minutes
		expected = TWO_DIGITS + ":00";
		result = TextFormatter.formatTimeFromMillis(TWO_DIGITS * MINUTES);
		assertTrue("double-digit minutes" + "[" + expected + NOT_EQUAL + result
				+ "]", expected.equals(result));

		// test single-digit hour
		expected = ONE_DIGIT + ":00:00";
		result = TextFormatter.formatTimeFromMillis(ONE_DIGIT * HOURS);
		assertTrue("single-digit hour" + "[" + expected + NOT_EQUAL + result
				+ "]", expected.equals(result));

		// test double-digit hour
		expected = TWO_DIGITS + ":00:00";
		result = TextFormatter.formatTimeFromMillis(TWO_DIGITS * HOURS);
		Log.d(TAG, "expected: " + expected + ", received: " + result);
		assertTrue("double-digit hour" + "[" + expected + NOT_EQUAL + result
				+ "]", expected.equals(result));

		// test single-digit day
		expected = ONE_DIGIT + ":00:00:00";
		result = TextFormatter.formatTimeFromMillis(ONE_DIGIT * DAYS);
		Log.d(TAG, "expected: " + expected + ", received: " + result);
		assertTrue("single-digit day" + "[" + expected + NOT_EQUAL + result
				+ "]", expected.equals(result));

		// test double-digit day
		expected = TWO_DIGITS + ":00:00:00";
		result = TextFormatter.formatTimeFromMillis(TWO_DIGITS * DAYS);
		Log.d(TAG, "expected: " + expected + ", received: " + result);
		assertTrue("double-digit day" + "[" + expected + NOT_EQUAL + result
				+ "]", expected.equals(result));

		/*
		 * Combinations
		 */

		// test only 2-digit hours and 2-digit seconds
		expected = TWO_DIGITS + ":00:" + TWO_DIGITS;
		result = TextFormatter.formatTimeFromMillis(TWO_DIGITS * HOURS
				+ TWO_DIGITS * SECONDS);
		assertTrue("2-digit hours and 2-digit seconds" + "[" + expected
				+ NOT_EQUAL + result + "]", expected.equals(result));

		// test seconds >= 60
		expected = TWO_DIGITS + ":00";
		result = TextFormatter.formatTimeFromMillis(TWO_DIGITS * SECS_IN_MINUTE
				* SECONDS);
		assertTrue("seconds >= 60" + "[" + expected + NOT_EQUAL + result + "]",
				expected.equals(result));

		// test minutes >= 60 and < 600 (10 hours)
		expected = ONE_DIGIT + ":00:00";
		result = TextFormatter.formatTimeFromMillis(ONE_DIGIT * MINS_IN_HOUR
				* MINUTES);
		assertTrue("minutes >= 60 and < 600 (10 hours)" + "[" + expected
				+ NOT_EQUAL + result + "]", expected.equals(result));

		// test a large amount of days (NOTE: max 24 days with integers)
		expected = TWO_DIGITS + ":00:00:00";
		result = TextFormatter.formatTimeFromMillis(TWO_DIGITS * DAYS);
		assertTrue("large amount of days" + "[" + expected + NOT_EQUAL + result
				+ "]", expected.equals(result));

	}

	/**
	 * Test method for
	 * {@link edu.chalmers.dat255.audiobookplayer.util.TextFormatter#formatCounter(int, int)}
	 * .
	 */
	public final void testFormatCounter() {
		// the expected result
		String expected;

		// the result
		String result;

		/*
		 * The following two tests should work as intended.
		 * 
		 * Note that the formatter's first parameter is an index, and the other
		 * a number (index + 1).
		 */

		// test 0
		expected = "1/" + NO_OF_TRACKS;
		result = TextFormatter.formatCounter(0, NO_OF_TRACKS);
		assertTrue("zero" + "[" + expected + NOT_EQUAL + result + "]",
				expected.equals(result));

		// test position = limit
		expected = NO_OF_TRACKS + "/" + NO_OF_TRACKS;
		result = TextFormatter.formatCounter(NO_OF_TRACKS - 1, NO_OF_TRACKS);
		assertTrue("position = limit" + "[" + expected + NOT_EQUAL + result
				+ "]", expected.equals(result));

		/*
		 * The following three should return special case messages.
		 */

		// test with "-1"; no selection
		expected = "" + NO_OF_TRACKS;
		result = TextFormatter.formatCounter(Constants.Value.NO_TRACK_SELECTED,
				NO_OF_TRACKS);
		assertTrue("no selection" + "[" + expected + NOT_EQUAL + result + "]",
				expected.equals(result));

		// test limit = 0
		expected = Constants.Message.NO_TRACKS_FOUND;
		result = TextFormatter.formatCounter(0, 0);
		assertTrue("limit = 0" + "[" + expected + NOT_EQUAL + result + "]",
				expected.equals(result));

		// test position > limit
		expected = Constants.Message.TRACK_INDEX_ERROR;
		result = TextFormatter.formatCounter(NO_OF_TRACKS + 1, NO_OF_TRACKS);
		assertTrue("position > limit" + "[" + expected + NOT_EQUAL + result
				+ "]", expected.equals(result));

	}
}
