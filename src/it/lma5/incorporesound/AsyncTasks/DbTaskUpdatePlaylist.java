package it.lma5.incorporesound.AsyncTasks;

import it.lma5.incorporesound.Entities.Playlist;
import it.lma5.incorporesound.SqliteHelpers.InCorporeSoundHelper;

import android.os.AsyncTask;

/**
 * Task used to update a playlist in database
 * @author Andrea Di Lonardo, Luca Fanelli, Michele Coppola
 *
 */
public class DbTaskUpdatePlaylist extends
		AsyncTask<Playlist, Integer, Playlist> {

	private InCorporeSoundHelper helper;

	public DbTaskUpdatePlaylist(InCorporeSoundHelper helper) {
		super();
		this.helper = helper;
	}

	@Override
	protected Playlist doInBackground(Playlist... params) {

		Playlist toUpd = params[0];
		Playlist modified = params[1];
		helper.updatePlaylist(toUpd, modified.getName(), modified.getRound(),
				modified.is_random(), modified.getFadeIn(),
				modified.getSongList());

		return null;
	}

}
