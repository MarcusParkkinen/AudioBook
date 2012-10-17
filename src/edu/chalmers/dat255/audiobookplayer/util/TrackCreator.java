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
 *  Copyright � 2012 Marcus Parkkinen, Aki K�kel�, Fredrik �hs.
 **/

package edu.chalmers.dat255.audiobookplayer.util;

import java.io.File;

import android.media.MediaMetadataRetriever;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Creates Track instances (filling them with metadata).
 * 
 * @author Aki K�kel�
 * @version 0.6
 */
public class TrackCreator {
	private TrackCreator() {
	} // to defeat instantiation

	public static Track createTrack(String path) {
		// Instantiate a metadata retriever object and set its
		// data source path to the provided path
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(path);

		// Get the duration of the track
		String durationText = mmr
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		int duration = Integer.parseInt(durationText);

		// Get the title of the track
		String title = mmr
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

		// If the track name is invalid, use the name of the track file
		if (title == null || title.length() == 0) {
			title = (new File(path)).getName();
			int periodPosition = title.lastIndexOf('.');
			if (periodPosition != -1) {
				title = title.substring(0, periodPosition);
			}
		}
		return new Track(path, title, duration);
	}

}
