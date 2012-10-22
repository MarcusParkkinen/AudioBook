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
 * GUI methods required for Bookshelf.
 * 
 * @author Aki Käkelä
 * @version 0.1
 * 
 */
public interface IBookshelfGUIEvents extends IBookshelfEvents {

	/**
	 * Called when the add button has been pressed in the bookshelf GUI.
	 */
	void addBookButtonPressed();
}
