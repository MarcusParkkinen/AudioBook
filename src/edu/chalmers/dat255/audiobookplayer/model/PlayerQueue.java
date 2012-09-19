package edu.chalmers.dat255.audiobookplayer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A queue that contains a pointer and a collection of paths to audio files.
 * 
 * @author Aki Käkelä
 * @version 0.1
 */
public class PlayerQueue {
	private int currentTrackIndex;
	private List<String> trackPaths;

	/**
	 * Creates a Queue instance without any track paths.
	 */
	public PlayerQueue() {
		init();
		trackPaths = new ArrayList<String>();
	}

	/**
	 * Creates a Queue instance with the given track paths.
	 * 
	 * @param trackPaths
	 */
	public PlayerQueue(List<String> trackPaths) {
		init();
		this.trackPaths = trackPaths;
	}

	private void init() {
		currentTrackIndex = 0;
	}

	/*
	 * Track manipulation within the playlist (player queue)
	 */
	/**
	 * Moves the chosen track to the chosen location. Adjusts the positions of
	 * the other elements.
	 * 
	 * @param from
	 * @param to
	 */
	public void moveTrack(int from, int to) {
		Collections.rotate(trackPaths.subList(from, to + 1), -1);
	}

	/**
	 * Adds the given path to the queue.
	 * 
	 * @param path
	 */
	public void addTrack(String path) {
		trackPaths.add(path);
	}

	/**
	 * Removes the path at index <i>index</i>
	 * 
	 * @param index
	 */
	public void removeTrack(int index) {
		trackPaths.remove(index);
	}

	/**
	 * Clears the queue.
	 */
	public void clearTracks() {
		trackPaths.clear();
	}

	/**
	 * Returns the number of elements in the queue.
	 * 
	 * @return a value, x, x>=0
	 */
	public int getNumberOfTracks() {
		return trackPaths.size();
	}

	/**
	 * 
	 * @return the path that is playing (or is ready to be played).
	 */
	public String getCurrentTrackPath() {
		return this.trackPaths.get(currentTrackIndex);
	}

	/**
	 * @param index
	 * @return the path at index <i>index</i> of the queue.
	 */
	public String getTrackPathAt(int index) {
		return trackPaths.get(index);
	}

	/**
	 * @return a list of paths to the tracks of the queue.
	 */
	public List<String> getTrackPaths() {
		return trackPaths;
	}

	/**
	 * @param index
	 */
	public void setCurrentTrackIndex(int index) {
		this.currentTrackIndex = index;
	}

	/**
	 * @return the index of the path either currently open or ready to be opened
	 *         by the player
	 */
	public int getCurrentTrackIndex() {
		return this.currentTrackIndex;
	}

	/**
	 * Increases the index to be played by the player by 1.
	 */
	public void incrementTrackIndex() {
		this.currentTrackIndex++;
	}

	/*
	 * Books to add or set as a playlist.
	 */
	/**
	 * Adds the track paths of a Book into the queue.
	 * @param book
	 */
	public void addBook(Book book) {
		trackPaths.addAll(book.getPaths());
	}

	/**
	 * Sets the queue to contain the track paths of a Book.
	 * @param book
	 */
	public void setBook(Book book) {
		this.setQueue(book.getPaths());
	}

	/**
	 * Sets a given list of paths as the queue for the player.
	 * 
	 * @param paths
	 */
	public void setQueue(List<String> paths) {
		this.trackPaths = paths;
	}

}
