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

import java.io.IOException;

import android.test.AndroidTestCase;

/**
 * Test case for FileParser utility class.
 * 
 * @author Marcus Parkkinen
 * @version 0.1
 * 
 */
public class FileParserTest extends AndroidTestCase {
	private String textString;
	private String fileName;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.AndroidTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		// Initialize strings
		textString = "Random text for testing";
		fileName = "FileParserTestFile";
		super.setUp();
	}

	/**
	 * As the name describes.
	 */
	public void testWriteAndReadFromInternalStorage() {
		// Just assert that the write operation does not generate an exception
		try {
			FileParser.writeToInternalStorage(fileName, getContext(),
					textString);
		} catch (IOException e) {
			fail("Write to internal storage resulted in a IOException: "
					+ e.getMessage());
		}

		// Just assert that the read operation does not generate an exception
		String readResult = "";
		try {
			readResult = FileParser.readFromInternalStorage(fileName,
					getContext());
		} catch (IOException e) {
			fail("Read from internal storage resulted in a IOException: "
					+ e.getMessage());
		}

		// Assert that the text strings are equal
		assertEquals(textString, readResult);
	}
}
