package edu.chalmers.dat255.audiobookplayer.model;

import java.security.InvalidParameterException;

/**
 * Represents a single audio Track.
 * 
 * @author Marcus Parkkinen
 * @version 1.0
 * 
 */

public class Track {
	private String path;
	private int length;
	
	/**
	 * Constructor for a track. Path to the data source as well as the length of the track
	 * must be provided.
	 * 
	 * @param path source (path) to the track
	 * @param length playing time of the track in ms
	 */
	public Track(String path, int length) {
		if( path != null ) {
			this.path = path;
			this.length = length;
		} else{
			throw new InvalidParameterException();
		}
	}
	
	protected String getPath() {
		return path;
	}
	
	protected int getLength() {
		return length;
	}
}
