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

package edu.chalmers.dat255.audiobookplayer.model;

import java.io.Serializable;

/**
 * A time stamp to be put in a track. Used to keep track of certain times.
 * <p>
 * The time is immutable, so the object must be destroyed and recreated to
 * "move" a tag.
 * 
 * @author Aki K�kel�
 * @version 0.6
 * 
 */
public class Tag implements Serializable {
	private static final long serialVersionUID = 4L;
	private int time;

	/**
	 * Creates a tag which contains a track index and a time in milliseconds.
	 * 
	 * @param time
	 * @param trackIndex
	 */
	public Tag(int time) {
		this.time = time;
	}

	/**
	 * @return Time in milliseconds.
	 */
	public int getTime() {
		return time;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + time;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Tag)) {
			return false;
		}
		Tag other = (Tag) obj;
		if (time != other.time) {
			return false;
		}
		return true;
	}

}
