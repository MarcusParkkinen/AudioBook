package edu.chalmers.dat255.audiobookplayer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.chalmers.dat255.audiobookplayer.core.PlayerQueueControls;

import android.util.Log;

/**
 * A queue that contains a pointer and a collection of paths to audio files.
 * 
 * @author Aki Käkelä
 * @version 0.2
 */
public class PlayerQueue implements PlayerQueueControls {
	private int currentTrackIndex;
	private List<String> trackPaths;
	// TODO: List<Track> tracks

	// TODO: delete this
	private String tag = "PlayerQueue";

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

	public void moveTrack(int from, int to) {
		Collections.rotate(trackPaths.subList(from, to + 1), -1);
	}

	public void addTrack(String path) {
		trackPaths.add(path);
		Log.i(tag, "addTrack: " + path + ", size: " + trackPaths.size());
	}

	public void removeTrack(int index) {
		trackPaths.remove(index);
	}

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
	 * Increases the index to be played by the player by 1, or sets it to 0 if
	 * there isn't a later element in the queue.
	 */
	public void incrementTrackIndex() {
		this.currentTrackIndex = (this.currentTrackIndex + 1)
				% this.trackPaths.size();
	}

	/**
	 * Decreases the index to be played by the player by 1, or sets it to the
	 * index of the last element if the current element index is 0.
	 */
	public void decrementTrackIndex() {
		this.currentTrackIndex = (this.currentTrackIndex - 1)
				% this.trackPaths.size();
	}

	/**
	 * Adds the track paths of a Book into the queue.
	 * 
	 * @param book
	 */
	public void addBook(Book book) {
		trackPaths.addAll(book.getPaths());
	}

	/**
	 * Sets the queue to contain the track paths of a Book.
	 * 
	 * @param book
	 */
	public void setBook(Book book) {
		this.setQueue(book.getPaths());
	}

	public void setQueue(List<String> paths) {
		this.trackPaths = paths;
	}

	public void moveTracks(int[] tracks, int to) {
		for (int i = 0; i < tracks.length; i++) {
			moveTrack(tracks[i], to);
		}
	}

}
