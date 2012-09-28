package edu.chalmers.dat255.audiobookplayer.model;

/**
 * Used to assert that implementing classes can handle updates on Track
 * instances.
 * 
 * @author Aki Käkelä
 * 
 */
public interface ITrackUpdates {
	/**
	 * Sets the elapsed time of the track.
	 * 
	 * @param elapsedTime
	 */
	public void setElapsedTime(int elapsedTime);
}
