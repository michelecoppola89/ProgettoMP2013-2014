package it.lma5.incorporesound.AsyncTasks;

import it.lma5.incorporesound.Entities.Playlist;
import it.lma5.incorporesound.SqliteHelpers.InCorporeSoundHelper;
import android.os.AsyncTask;

/**
 * Task used to add a new playlist in database.
 * @author Andrea Di Lonardo, Luca Fanelli, Michele Coppola
 *
 */
public class DbTaskAddPlaylist extends AsyncTask<Playlist, Integer, Playlist> {
	
	private InCorporeSoundHelper helper;
	
	public DbTaskAddPlaylist(InCorporeSoundHelper helper) {
		this.helper=helper;
	}


	@Override
	protected Playlist doInBackground(Playlist... params) {
		
		Playlist ti= params[0];
		return helper.addPlaylist(ti);
	}


	@Override
	protected void onPostExecute(Playlist result) {
	
		
	
	}

}
