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
 * @author Aki Käkelä
 * @version 0.6
 * 
 */
public interface IPlayerEvents {

	/**
	 * Resumes playback if the player is already in a started state.
	 * <p>
	 * Does nothing if the player was already paused.
	 */
	void resume();

	/**
	 * Pauses playback if the player is already in a started state.
	 * <p>
	 * Does nothing if the player was already playing.
	 */
	void pause();

	/**
	 * Sets the selected track to the previous track.
	 * <p>
	 * If the selected track was zero, it will do nothing.
	 * <p>
	 * If there was no selected track, it will do nothing.
	 */
	void previousTrack();

	/**
	 * Sets the selected track to the next track.
	 * <p>
	 * If the selected track was the last track, it will deselect any track.
	 * <p>
	 * If there was no selected track, it will select the first track.
	 */
	void nextTrack();

	/**
	 * Seeks to the left (rewinds).
	 * 
	 * @param seek
	 *            Starts or keeps seeking if true. Stops if false.
	 */
	void seekLeft(boolean seek);

	/**
	 * Seeks to the right (fast-forwards).
	 * 
	 * @param seek
	 *            Starts or keeps seeking if true. Stops if false.
	 */
	void seekRight(boolean seek);

	/**
	 * Seeks to the given percentage in a track.
	 * 
	 * @param percentage
	 *            0 <= x <= 1
	 */
	void seekToPercentageInTrack(double percentage);

	/**
	 * Seeks to the given percentage in a book.
	 * 
	 * @param percentage
	 *            0 <= x <= 1
	 */
	void seekToPercentageInBook(double percentage);

	/**
	 * Checks whether audio is playing.
	 * 
	 * @return True if playing audio.
	 */
	boolean isPlaying();

	/**
	 * Checks whether audio has been started.
	 * 
	 * @return True if started (playing or paused).
	 */
	boolean isStarted();

}
