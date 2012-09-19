package edu.chalmers.dat255.audiobookplayer.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents a collection of file paths to audio tracks that 
 * collectively form a book.
 * 
 * @author Marcus Parkkinen
 * @version 1.0
 * 
 */

public class Book {
	private ArrayList<String> filePaths;
	//private Bookmark bookmark;
	
	/**
	 * Create an empty book.
	 */
	public Book() {
		filePaths = new ArrayList<String>();
	}
	
	/**
	 * Create a book from the referenced collection of
	 * file paths.
	 * 
	 * @param c A collection containing paths to audio files
	 */
	public Book(Collection<String> c) {
		this();
		
		for(String path : c) {
			filePaths.add(path);
		}
	}
	
	/**
	 * Add a file path to the collection.
	 * 
	 * @param index index to insert the path
	 * @param filePath path to file
	 */
	public void addTrack(int index, String filePath) {
		filePaths.add(index, filePath);
	}
	
	/**
	 * Add a collection of file paths.
	 * 
	 * @param c Collection that contains the file paths
	 */
	
	public void addTracks(Collection<String> c) {
		for(String path : c) {
			filePaths.add(path);
		}
	}
	
	/**
	 * Swap location of two paths.
	 * 
	 * @param firstIndex
	 * @param secondIndex
	 */
	public void swap(int firstIndex, int secondIndex) {
		Collections.swap(filePaths, firstIndex, secondIndex);
	}
	
	/**
	 * Set the bookmark to point towards a track and a specific
	 * time.
	 * 
	 * @param trackIndex Index of the track in the book
	 * @param time Time in ms to bookmark
	 */
	public void setBookmark(int trackIndex, int time) {
		/*
		 * 
		 * 
		 */
	}
	
	
	/**
	 * Return the current track index
	 * 
	 * @return int indicating the current index
	 */
	public int getCurrentIndex() {
		return 0;
	}
	
	/**
	 * Returns a list containing paths to all tracks contained in this book.
	 * 
	 * @return List<String> the list
	 */
	public List<String> getPaths() {
		return filePaths;
	}
	
	// more accessors to be added.
}
