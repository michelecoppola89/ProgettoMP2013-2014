package it.lma5.incorporesound.AsyncTasks;

import it.lma5.incorporesound.Entities.Song;
import it.lma5.incorporesound.SqliteHelpers.InCorporeSoundHelper;

import java.util.ArrayList;

import android.os.AsyncTask;

/**
 * Task used to delete songs in database.
 * 
 * @author Andrea Di Lonardo, Luca Fanelli, Michele Coppola
 *
 */
public class DbTaskDeleteSongs extends AsyncTask<ArrayList<Song>, Integer, ArrayList<Song>> {
	
	private InCorporeSoundHelper helper;

	public DbTaskDeleteSongs(InCorporeSoundHelper helper) {
		this.helper = helper;
	}

	@Override
	protected ArrayList<Song> doInBackground(ArrayList<Song>... params) {
		
		ArrayList<Song> toDelete= params[0];
		
		for (int i=0; i<toDelete.size(); i++){
			
			helper.deleteSong(toDelete.get(i).getId());
		}
			
		return null;
	}

}
