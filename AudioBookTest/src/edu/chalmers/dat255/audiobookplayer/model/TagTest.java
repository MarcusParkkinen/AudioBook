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

import junit.framework.TestCase;

/**
 * Test case for Tag.
 * 
 * @author Aki Käkelä
 * @version 0.1
 * 
 */
public class TagTest extends TestCase {
	private Tag tag;
	private static final int TIME = 21600000; // 6 hours

	public TagTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();

		tag = new Tag(TIME);
	}

	public void testGetTime() {
		assertTrue(tag.getTime() == TIME);
	}

	public void testEqualsObject() {
		Tag dummy = new Tag(tag.getTime());
		Tag otherDummy = new Tag(dummy.getTime());

		// reflexive test
		assertTrue(dummy.equals(dummy));
		assertTrue(tag.equals(tag));

		// symmetric test
		assertTrue(tag.equals(dummy));
		assertTrue(dummy.equals(tag));

		// transitive test
		assertTrue(tag.equals(otherDummy));
	}

}
