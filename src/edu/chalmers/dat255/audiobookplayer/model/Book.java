package edu.chalmers.dat255.audiobookplayer.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

/**
 * Represents a collection of Track objects that 
 * collectively form a book.
 * 
 * @author Marcus Parkkinen
 * @version 1.0
 */

public class Book {
	private static final String TAG = "Book.java";
	private LinkedList<Track> tracks;
	//private int totalLength;
	//private Bookmark bookmark;
	//private HashSet<Tag> tags;
	//private Bookstats stats;
	
	/**
	 * Create an empty book.
	 */
	public Book() {
		tracks = new LinkedList<Track>();
	}
	
	/**
	 * Create a book from the referenced collection of
	 * Tracks.
	 * 
	 * @param c A collection containing Tracks.
	 */
	public Book(Collection<Track> c) {
		this();
		
		for(Track t : c) {
			if(t != null) {
				tracks.add(t);
			}
		}
	}
	
	/**
	 * Add a Track to the collection.
	 * 
	 * @param index index to insert the track
	 * @param t reference to track object to add
	 */
	public void addTrack(int index, Track t) {
		if(t != null) {
			tracks.add(index, t);
		}
	}
	
	public void addTrack(Track t) {
		addTrack(tracks.size(), t);
	}
	
	/**
	 * Add a collection of tracks.
	 * 
	 * @param c Collection that contains references to the tracks
	 */
	
	public void addTracks(Collection<Track> c) {
		for(Track t : c) {
			tracks.add(t);
		}
	}
	
	/**
	 * Swap location of two tracks.
	 * 
	 * @param firstIndex
	 * @param secondIndex
	 */
	public void swap(int firstIndex, int secondIndex) {
		try{
			Collections.swap(tracks, firstIndex, secondIndex);
		} catch(IndexOutOfBoundsException e) {
			Log.e(TAG, " attempting to move a track from/to illegal index. Skipping operation.");
		}
	}
	
	/**
	 * Move the track at the specified index to a new index.
	 * 
	 * @param oldIndex index of the track
	 * @param newIndex 
	 */
	public void move(int oldIndex, int newIndex) {
		if(oldIndex <= (tracks.size()-1) &&
				newIndex <= (tracks.size()-1)) {
			Track temp = tracks.remove(oldIndex);
			tracks.add(newIndex, temp);	
		} else{
			Log.e(TAG, " attempting to move a track from/to illegal index. Skipping operation.");
		}
	}
	
	/**
	 * Set the bookmark to point at a track and a specific
	 * time in that track.
	 * 
	 * @param trackIndex Index of the track in the book
	 * @param time Time in ms to bookmark
	 */
	public void setBookmark(int trackIndex, int time) {
		
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
	 * Returns a list containing references to all tracks contained in this book.
	 * 
	 * @return List<String> the list
	 */
	public List<String> getPaths() {
		LinkedList<String> l = new LinkedList<String>();
		for(Track t : tracks) {
			l.add(t.getPath());
		}
		return l;
	}
	
	// more accessors to be added.
}
