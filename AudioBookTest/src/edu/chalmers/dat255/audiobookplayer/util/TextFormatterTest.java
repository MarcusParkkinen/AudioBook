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

	// The 'magic numbers'
	private static final int MSECS_IN_SECOND = 1000;
	private static final int SECS_IN_MINUTE = 60;
	private static final int MINS_IN_HOUR = 60;
	private static final int HOURS_IN_DAY = 24;

	// Converting from milliseconds (ms)
	private static final int SECONDS = MSECS_IN_SECOND;
	private static final int MINUTES = MSECS_IN_SECOND * SECS_IN_MINUTE;
	private static final int HOURS = MSECS_IN_SECOND * SECS_IN_MINUTE
			* MINS_IN_HOUR;
	private static final int DAYS = MSECS_IN_SECOND * SECS_IN_MINUTE
			* MINS_IN_HOUR * HOURS_IN_DAY;

	private static final int NO_OF_TRACKS = 25; // "limit"
	private static final int NO_SELECTION = -1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

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
		dummy = "00:09";
		assertTrue(dummy
				.equals(TextFormatter.formatTimeFromMillis(9 * SECONDS)));

		// test double-digit seconds
		dummy = "00:12";
		assertTrue(dummy.equals(TextFormatter
				.formatTimeFromMillis(12 * SECONDS)));

		// test single-digit minutes
		dummy = "05:00";
		assertTrue(dummy
				.equals(TextFormatter.formatTimeFromMillis(5 * MINUTES)));

		// test double-digit minutes
		dummy = "55:00";
		assertTrue(dummy.equals(TextFormatter
				.formatTimeFromMillis(55 * MINUTES)));

		// test single-digit hour
		dummy = "5:01:01";
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(5 * HOURS
				+ 1 * MINUTES + 1 * SECONDS)));

		// test double-digit hour
		dummy = "15:05:03";
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(15 * HOURS
				+ 5 * MINUTES + 3 * SECONDS)));

		// test only hours and seconds
		dummy = "23:00:59";
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(23 * HOURS
				+ 0 * MINUTES + 59 * SECONDS)));

		// test seconds >= 60
		dummy = "05:00";
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(5
				* SECS_IN_MINUTE * SECONDS)));

		// test minutes >= 60
		dummy = "5:00:59";
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(5
				* MINS_IN_HOUR * MINUTES)));

		// test hours >= 24
		dummy = "3:15:59";
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(523 * DAYS
				+ 3 * HOURS + 15 * MINUTES + 59 * SECONDS)));

		dummy = "13:15:59";
		assertTrue(dummy.equals(TextFormatter.formatTimeFromMillis(523 * DAYS
				+ 13 * HOURS + 15 * MINUTES + 59 * SECONDS)));

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
