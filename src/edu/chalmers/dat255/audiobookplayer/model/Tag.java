package edu.chalmers.dat255.audiobookplayer.model;

/**
 * A time stamp and a track index to be put in a book to keep track of certain
 * positions.
 * 
 * @author Aki Käkelä
 * 
 */
public class Tag {
	private int time;
	private int trackIndex;

	/**
	 * Creates a tag which contains a track index and a time in milliseconds.
	 * 
	 * @param time
	 * @param trackIndex
	 */
	public Tag(int time, int trackIndex) {
		this.time = time;
		this.trackIndex = trackIndex;
	}

	/**
	 * @return Time in milliseconds.
	 */
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * Get the track index that the tag was saved on.
	 * 
	 * @return
	 */
	public int getTrackIndex() {
		return trackIndex;
	}

	public void setTrackIndex(int trackIndex) {
		this.trackIndex = trackIndex;
	}

}
