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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

/**
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.1
 * 
 */
public final class FileParser {

	private FileParser() {
	} // to defeat instantiation

	/**
	 * Writes to internal storage given a file name.
	 * 
	 * @param file
	 *            The file name to read from.
	 * @param c
	 *            Context.
	 * @param content
	 *            JSON String to be written.
	 */
	public static void writeToInternalStorage(String file, Context c,
			String content) throws IOException {

		FileOutputStream outputStream = c.openFileOutput(file,
				Context.MODE_PRIVATE);
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

		dataOutputStream.writeUTF(content);
		dataOutputStream.close();
	}

	/**
	 * Reads from internal storage given a file name.
	 * 
	 * @param file
	 *            The file name to read from.
	 * @param c
	 *            Context.
	 * @return JSON String to be read.
	 */
	public static String readFromInternalStorage(String file, Context c)
			throws IOException {
		FileInputStream inputStream = c.openFileInput(file);
		DataInputStream dataInputStream = new DataInputStream(inputStream);

		String result = dataInputStream.readUTF();
		dataInputStream.close();

		// return the JSON String representation of the object.
		return result;
	}
}
