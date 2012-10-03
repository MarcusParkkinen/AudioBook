package edu.chalmers.dat255.audiobookplayer.interfaces;

/**
 * Used to assert that implementing classes can handle updates on Track
 * instances.
 * 
 * @author Aki Käkelä
 * @version 0.6
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
