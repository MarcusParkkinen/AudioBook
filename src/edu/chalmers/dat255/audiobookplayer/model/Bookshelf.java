package edu.chalmers.dat255.audiobookplayer.model;

import java.util.LinkedList;

import android.util.Log;

/**
 * The bookshelf class contains a collection of books.
 * 
 * @author Marcus Parkkinen
 * @version 1.0
 * 
 */

public class Bookshelf {
	private LinkedList<Book> books;
	private static final String TAG = "Bookshelf.java";
	
	/**
	 * Create an empty bookshelf.
	 */
	public Bookshelf() {
		books = new LinkedList<Book>();
	}
	
	/**
	 * 
	 * Add a new book to the bookshelf.
	 * 
	 * @param b new book to be added
	 */
	public void addNewBook(Book b) {
		books.addLast(b);
	}
	
	/**
	 * Change the location of a book in the bookshelf.
	 * 
	 * @param oldIndex old index of the book
	 * @param newIndex new index of the book
	 */
	public void move(int oldIndex, int newIndex) {
		if(oldIndex <= (books.size()-1) &&
				newIndex <= (books.size()-1)) {
			Book temp = books.remove();
			books.add(newIndex, temp);
			
		} else{
			Log.e(TAG, " attempting to move book from/to an index out of bounds. Skipping operation.");
		}
	}
	
	/**
	 * Remove a book from the bookshelf
	 * 
	 * @param index index of the book to be removed.
	 */
	public void remove(int index) {
		try{
			books.remove(index);
		} catch(IndexOutOfBoundsException e) {
			Log.e(TAG, " attempting to remove book from an index out of bounds. Skipping operation.");
		}
	}
}
