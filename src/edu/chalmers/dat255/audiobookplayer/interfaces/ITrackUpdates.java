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
 * @author Aki K�kel�
 * @version 0.6
 * 
 */
public interface ITrackUpdates {

	/**
	 * Set the elapsed time of the track to a specified amount.
	 * 
	 * @param elapsedTime
	 * @throws InvalidParameterException
	 *             If the elapsedTime is negative.
	 */
	public void setSelectedTrackElapsedTime(int elapsedTime) throws IllegalArgumentException;

	/*
	 * Tags
	 */
	/**
	 * Adds a tag with the given time.
	 * 
	 * @param time
	 */
	public void addTag(int time) throws IllegalArgumentException;

	/**
	 * Removes the tag at the specified index. Does nothing if the list is
	 * empty.
	 * 
	 * @param tagIndex
	 */
	public void removeTagAt(int tagIndex) throws IllegalArgumentException;

}
