package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.io.IOException;

import android.content.Context;

import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.util.FileParser;
import edu.chalmers.dat255.audiobookplayer.util.JSONParser;

/**
 * Manages setting the current book and bookshelf, as well as saving it when the
 * application terminates.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.6
 */

public class BookshelfController {
	private Bookshelf shelf;

	/**
	 * Bookshelf constructor.
	 */
	public BookshelfController() {
		/* ... */
	}

	/**
	 * Selects a given book in the bookshelf.
	 * 
	 * @param index
	 */
	public void setSelectedBook(int index) {
		if (shelf != null) {
			shelf.setSelectedBookIndex(index);
		}
	}

	/**
	 * Save a JSON representation of the model object tree to file.
	 * 
	 * @param Context
	 *            context
	 * @param String
	 *            username
	 */
	public boolean saveBookshelf(Context c, String username) {
		try {
			FileParser.writeToInternalStorage(username + ".bookmark", c,
					JSONParser.toJSON(shelf));
		} catch (IOException e) {
			return false;
		}
		return true;
	}

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
	public Bookshelf loadBookshelf(Context c, String username) {
		try {
			Object obj = JSONParser.fromJSON(FileParser
					.readFromInternalStorage(username + ".bookmark", c),
					Bookshelf.class);
			if (obj instanceof Bookshelf) {
				shelf = (Bookshelf) obj;
			}
		} catch (Exception e) {
			shelf = new Bookshelf();
		}
		return shelf;
	}
	// + sort, swap, move, edit book
}