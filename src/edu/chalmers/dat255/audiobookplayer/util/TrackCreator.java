package edu.chalmers.dat255.audiobookplayer.util;

import android.media.MediaMetadataRetriever;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Creates Track instances (filling them with metadata).
 * @author Aki Käkelä
 * @version 0.4
 */
public class TrackCreator {

	public static Track createTrack(String path) {
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(path);
		int duration = MediaMetadataRetriever.METADATA_KEY_DURATION;
		return new Track(path, duration);
	}

}
