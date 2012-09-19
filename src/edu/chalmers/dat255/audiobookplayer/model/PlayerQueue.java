package edu.chalmers.dat255.audiobookplayer.model;

import java.util.List;

public class PlayerQueue {
	private int currentTrackIndex;
	private List<String> trackPaths;
	
	public PlayerQueue() {
		currentTrackIndex = 0;
	}
	
	public PlayerQueue(List<String> trackPaths) {
		this();
		this.trackPaths = trackPaths;
	}
	
	public void addTrack(String path) {
		trackPaths.add(path);
	}
	
	public void removeTrack(int index) {
		trackPaths.remove(index);
	}
	
	public void addBook(Book book) {
		trackPaths.addAll(book.getPaths());
	}
	
	public void clearTracks() {
		trackPaths.clear();
	}
	
	public int getNumberOfTracks() {
		return trackPaths.size();
	}
	
	public void setCurrentTrackIndex(int index) {
		this.currentTrackIndex = index;
	}
	
	public int getCurrentTrackIndex() {
		return this.currentTrackIndex;
	}
}
