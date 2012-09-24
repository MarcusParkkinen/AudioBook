package edu.chalmers.dat255.audiobookplayer.core;

import java.util.List;

public interface PlayerQueueControls {
	/**
	 * Adds the given path to the queue.
	 * 
	 * @param path
	 */
	public void addTrack(String path);

	/**
	 * Removes the path at index <i>index</i>
	 * 
	 * @param index
	 */
	public void removeTrack(int index);

	/**
	 * Moves the selected track to the given position and adjusts the indices of
	 * the tracks inbetween.
	 * 
	 * @param from
	 * @param to
	 */
	public void moveTrack(int from, int to);

	/**
	 * Moves the selected tracks to the given position and adjusts the indices
	 * of the tracks inbetween.
	 * 
	 * @param from
	 * @param to
	 */
	public void moveTracks(int[] tracks, int to);

	/**
	 * Clears the queue.
	 */
	public void clearTracks();

	/**
	 * Sets a given list of paths as the queue for the player.
	 * 
	 * @param paths
	 */
	public void setQueue(List<String> trackList);
}
