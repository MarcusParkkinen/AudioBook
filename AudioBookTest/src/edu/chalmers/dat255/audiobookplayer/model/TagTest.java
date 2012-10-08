package edu.chalmers.dat255.audiobookplayer.model;

import junit.framework.TestCase;

/**
 * Test case for Tag.
 * 
 * @author Aki Käkelä
 * 
 */
public class TagTest extends TestCase {
	private Tag tag;
	private int time;

	public TagTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		time = 21600000; // 6 hours
		tag = new Tag(time);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testTag() {
		fail("Not yet implemented");
	}

	public void testGetTime() {
		assertTrue(tag.getTime() == time);
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
