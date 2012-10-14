/**
 *  This work is licensed under the Creative Commons Attribution-NonCommercial-
 *  NoDerivs 3.0 Unported License. To view a copy of this license, visit
 *  http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to 
 *  Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 
 *  94041, USA.
 * 
 *  Use of this work is permitted only in accordance with license rights granted.
 *  Materials provided "AS IS"; no representations or warranties provided.
 * 
 *  Copyright © 2012 Marcus Parkkinen, Aki Käkelä, Fredrik Åhs.
 **/

package edu.chalmers.dat255.audiobookplayer.interfaces;

/**
 * Specifies the possible events coming from the Player, including user
 * interface controls and playback functionality.
 * 
 * @author Aki K�kel�
 * @version 0.6
 * 
 */
public interface IPlayerEvents {

	/**
	 * Resumes playback if the player is already in a started state.
	 * <p>
	 * Does nothing if the player was already paused.
	 */
	public void play();

	/**
	 * Pauses playback if the player is already in a started state.
	 * <p>
	 * Does nothing if the player was already paused.
	 */
	public void pause();

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
	 * <p>
	 * Sets the track progress to (current progress - 0.1 * total track
	 * duration).
	 */
	public void seekLeft();

	/**
	 * Seeks to the right (fast-forwards).
	 * <p>
	 * Sets the track progress to (current progress + 0.1 * total track
	 * duration).
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

	/**
	 * Checks whether audio is playing (to determine the play/pause image).
	 * 
	 * @return True if playing (non-paused).
	 */
	public boolean isPlaying();

}
