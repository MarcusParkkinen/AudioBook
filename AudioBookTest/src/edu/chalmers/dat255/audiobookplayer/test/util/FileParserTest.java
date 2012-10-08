package edu.chalmers.dat255.audiobookplayer.test.util;

import java.io.IOException;

import edu.chalmers.dat255.audiobookplayer.util.FileParser;
import android.test.AndroidTestCase;

/**
 * Test case for FileParser utility class.
 * 
 * @author Marcus Parkkinen
 * 
 */
public class FileParserTest extends AndroidTestCase{
	private String textString;
	private String fileName;
	
	protected void setUp() throws Exception {
		// Initialize strings
		textString = "Random text for testing";
		fileName = "FileParserTestFile";
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	public void testWriteAndReadFromInternalStorage() {
		// Just assert that the write operation does not generate an exception
		try{
			FileParser.writeToInternalStorage(fileName, getContext(), textString);
		} catch(IOException e) {
			fail("Write to internal storage resulted in a IOException: "
															+ e.getMessage());
		}
		
		// Just assert that the read operation does not generate an exception
		String readResult = "";
		try{
			readResult = FileParser.readFromInternalStorage(fileName, getContext());
		} catch(IOException e) {
			fail("Read from internal storage resulted in a IOException: "
					+ e.getMessage());
		}
		
		// Assert that the text strings are equal
		assertEquals(textString, readResult);
	}
}
