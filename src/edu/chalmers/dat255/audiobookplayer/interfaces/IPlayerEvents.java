package edu.chalmers.dat255.audiobookplayer.interfaces;

/**
 * Specifies the possible events coming from the Player, including user
 * interface controls and playback functionality.
 * 
 * @author Aki Käkelä
 * @version 0.6
 * 
 */
public interface IPlayerEvents {
	/**
	 * Toggles play/pause depending on the current status.
	 * <p>
	 * Pauses the audio if the player is playing. Resumes or starts otherwise.
	 */
	public void playPause();

	/**
	 * Sets the selected track to the previous track.
	 * <p>
	 * If the selected track was zero, it will do nothing.
	 * <p>
	 * If there was no selected track, it will do nothing.
	 */
	public void previousTrack();

	/**
	 * Sets the selected track to the next track.
	 * <p>
	 * If the selected track was the last track, it will deselect any track.
	 * <p>
	 * If there was no selected track, it will select the first track.
	 */
	public void nextTrack();

	/**
	 * Seeks to the left (rewinds).
	 */
	public void seekLeft();

	/**
	 * Seeks to the right (fast-forwards).
	 */
	public void seekRight();

	/**
	 * Seeks to the given percentage in a track.
	 * 
	 * @param percentage
	 *            0 <= x <= 1
	 */
	public void seekToPercentageInTrack(double percentage);

	/**
	 * Seeks to the given percentage in a book.
	 * 
	 * @param percentage
	 *            0 <= x <= 1
	 */
	public void seekToPercentageInBook(double percentage);
}
