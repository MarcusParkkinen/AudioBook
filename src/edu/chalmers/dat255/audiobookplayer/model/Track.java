package edu.chalmers.dat255.audiobookplayer.model;

import java.security.InvalidParameterException;

public class Track {
	private String path;
	private int length;
	//private String checksum; Kanske? Unikt Id
	
	public Track(String path, int length) {
		if( path != null ) {
			this.path = path;
			this.length = length;
		} else{
			throw new InvalidParameterException();
		}
	}
	
	protected String getPath() {
		return path;
	}
	
	protected int getLength() {
		return length;
	}
}
