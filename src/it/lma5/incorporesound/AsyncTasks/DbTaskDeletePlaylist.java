package it.lma5.incorporesound.AsyncTasks;

import it.lma5.incorporesound.Entities.Playlist;
import it.lma5.incorporesound.SqliteHelpers.InCorporeSoundHelper;
import android.os.AsyncTask;

/**
 * Task used to delete a playlist in database.
 * 
 * @author Andrea Di Lonardo, Luca Fanelli, Michele Coppola
 * 
 */
public class DbTaskDeletePlaylist extends AsyncTask<Playlist, Integer,Playlist> {

	private InCorporeSoundHelper helper;
	public DbTaskDeletePlaylist(InCorporeSoundHelper helper) {
		super();
		this.helper = helper;
	}
	@Override
	protected Playlist doInBackground(Playlist... params) {
		Playlist tr= params[0];
		helper.deletePlaylist(tr.getName());
		
		return tr;
	}

}
