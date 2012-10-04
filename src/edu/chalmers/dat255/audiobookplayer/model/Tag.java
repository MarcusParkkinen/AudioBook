package edu.chalmers.dat255.audiobookplayer.model;

/**
 * A time stamp to be put in a track. Used to keep track of certain times.
 * <p>
 * The time is immutable, so the object must be destroyed and recreated to
 * "move" a tag.
 * 
 * @author Aki Käkelä
 * @version 0.6
 * 
 */
public class Tag {
	private int time;

	// private int trackIndex;

	/**
	 * Creates a tag which contains a track index and a time in milliseconds.
	 * 
	 * @param time
	 * @param trackIndex
	 */
	public Tag(int time/* , int trackIndex */) {
		this.time = time;
		// this.trackIndex = trackIndex;
	}

	/**
	 * @return Time in milliseconds.
	 */
	public int getTime() {
		return time;
	}

	// /**
	// * @param time
	// */
	// public void setTime(int time) {
	// this.time = time;
	// }

	// /**
	// * Get the track index that the tag was saved on.
	// *
	// * @return
	// */
	// public int getTrackIndex() {
	// return trackIndex;
	// }

	// /**
	// * @param trackIndex
	// */
	// public void setTrackIndex(int trackIndex) {
	// this.trackIndex = trackIndex;
	// }

}
