package edu.chalmers.dat255.audiobookplayer.util;

import java.io.File;

import android.media.MediaMetadataRetriever;
import edu.chalmers.dat255.audiobookplayer.model.Track;

/**
 * Creates Track instances (filling them with metadata).
 * 
 * @author Aki Käkelä
 * @version 0.6
 */
public class TrackCreator {

	public static Track createTrack(String path) throws NumberFormatException {
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		mmr.setDataSource(path);
		int duration = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
		String title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		if(title == null || title.length() == 0) {
			title = (new File(path)).getName();
			int periodPosition;
			if((periodPosition = title.lastIndexOf('.')) != -1)  {
				title = title.substring(0, periodPosition);
			}
		}
		return new Track(path, title, duration);
	}

}
