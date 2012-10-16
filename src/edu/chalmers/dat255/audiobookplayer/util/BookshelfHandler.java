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
public class BookshelfHandler {

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
			Object obj = JsonParser.fromJSON(FileParser
					.readFromInternalStorage(username + ".bookmark", c),
					Bookshelf.class);
			if (obj instanceof Bookshelf) {
				return ((Bookshelf) obj);
			}
		} catch (Exception e) {
			// If anything goes wrong, just continue..
		}
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
			FileParser.writeToInternalStorage(username + ".bookmark", c,
					JsonParser.toJSON(bs));
		} catch (IOException e) {
			return false;
		}
		return true;
	}

}
