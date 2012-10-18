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

import android.content.Context;
import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;

/**
 * Loads and saves bookshelf data.
 * 
 * @author Aki Käkelä, Marcus Parkkinen
 * @version 0.1
 * 
 */
public final class BookshelfHandler {
	
	private BookshelfHandler() {
	} // to defeat instantiation

	/**
	 * Attempt to load the model object tree from a file. This method
	 * instantiates the member variable with either a new Bookshelf or a
	 * existing one from a file.
	 * 
	 * A reference to the bookshelf is also returned to the calling method.
	 * 
	 * @param Context
	 *            context
	 * @param String
	 *            username
	 */
	public static Bookshelf loadBookshelf(Context c, String username) {
		try {
			// read
			Object obj = JsonParser.fromJSON(FileParser
					.readFromInternalStorage(username + ".bookmark", c),
					Bookshelf.class);
			if (obj instanceof Bookshelf) {
				// if we found a bookshelf type object, return it
				return ((Bookshelf) obj);
			}
		} catch (Exception e) {
			// If anything goes wrong, just continue..
		}
		// return a new bookshelf instance if nothing was found
		return (new Bookshelf());
	}

	/**
	 * Save a JSON representation of the model object tree to file.
	 * 
	 * @param bs
	 * 
	 * @param Context
	 *            context
	 * @param String
	 *            username
	 */
	public static boolean saveBookshelf(Context c, String username, Bookshelf bs) {
		try {
			// write
			FileParser.writeToInternalStorage(username + ".bookmark", c,
					JsonParser.toJSON(bs));
		} catch (IOException e) {
			// the saving failed; show it by returning false
			return false;
		}
		// saving was successful
		return true;
	}

}
