package edu.chalmers.dat255.audiobookplayer.util;

public class TimeFormatter {
	/**
	 * Formats a given time in milliseconds to MM:SS.
	 * <p>
	 * 
	 * If the number of hours is greater than zero or nine the time will be
	 * formatted to H:MM:SS or HH:MM:SS, respectively. If it surpasses 23, it
	 * will start from zero again (modulus 24).
	 * 
	 * @param ms
	 * @return
	 */
	public static String formatTimeFromMillis(int ms) {
		int seconds = (ms / 1000) % 60;
		int minutes = (ms / (1000 * 60)) % 60;
		int hours = (ms / (1000 * 60 * 60)) % 24;

		return (hours > 0 ? hours + ":" : "") + (minutes <= 9 ? "0" : "")
				+ minutes + ":" + (seconds <= 9 ? "0" : "") + seconds;
	}
}
