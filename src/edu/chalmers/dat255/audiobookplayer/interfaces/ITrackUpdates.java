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

import java.security.InvalidParameterException;

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
	 * Set the elapsed time of the track to a specified amount.
	 * 
	 * @param elapsedTime
	 * @throws InvalidParameterException
	 *             If the given time is negative.
	 */
	void setSelectedTrackElapsedTime(int elapsedTime)
			throws InvalidParameterException;

	/*
	 * Tags
	 */
	/**
	 * Adds a tag with the given time.
	 * 
	 * @param time
	 * @throws InvalidParameterException
	 *             If the given time is negative.
	 */
	void addTag(int time) throws InvalidParameterException;

	/**
	 * Removes the tag at the specified index. Does nothing if the list is
	 * empty.
	 * 
	 * @param tagIndex
	 * @throws IndexOutOfBoundsException
	 *             if the given tag index is out of bounds (illegal).
	 */
	void removeTagAt(int tagIndex) throws IndexOutOfBoundsException;

}
