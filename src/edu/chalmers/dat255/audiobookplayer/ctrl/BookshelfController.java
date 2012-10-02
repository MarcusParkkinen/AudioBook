package edu.chalmers.dat255.audiobookplayer.ctrl;

import java.io.IOException;

import android.content.Context;

import com.google.gson.Gson;

import edu.chalmers.dat255.audiobookplayer.model.Bookshelf;
import edu.chalmers.dat255.audiobookplayer.util.FileParser;
import edu.chalmers.dat255.audiobookplayer.util.JSONParser;

/**
 * BookController class - used to edit and create all the Books stored in the users bookshelf.
 * Wraps a Bookshelf instance.
 * 
 * @author Marcus Parkkinen, Aki Käkelä
 * @version 0.4
 */

public class BookshelfController {
	private Bookshelf shelf;

	/**
	 * Bookshelf constructor.
	 * 
	 * @param Bookshelf a reference to the Bookshelf to control.
	 */
	public BookshelfController(Context c, String username) {
		loadBookshelf(c, username);
	}
	/**
	 * Selects a given book in the bookshelf.
	 * @param index
	 */
	public void setSelectedBook(int index) {
		shelf.setSelectedBookIndex(index);
	}
	
	/**
	 * Save a JSON representation of the model object tree to file.
	 * @param Context context
	 * @param String username
	 */
	public boolean saveBookshelf(Context c, String username) {	
		try{
			FileParser.writeToInternalStorage(username + ".bookmark", c, JSONParser.toJSON(shelf));
		} catch(IOException e) {
			return false;
		}
		System.out.println("Successfully saved.");
		return true;
	}
	
	public void loadBookshelf(Context c, String username) {
		try{
			Object obj = JSONParser.fromJSON(
					FileParser.readFromInternalStorage(username +".bookmark", c),
					Bookshelf.class);
			if(obj instanceof Bookshelf) {
				shelf = (Bookshelf) obj;
			}
		} catch(Exception e) {
			shelf = new Bookshelf();
		}
	}
	
	public Bookshelf getBookshelf() {
		return shelf;
	}

	//+ sort, swap, move, edit book
}