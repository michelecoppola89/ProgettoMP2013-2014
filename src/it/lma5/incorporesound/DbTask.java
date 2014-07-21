package it.lma5.incorporesound;

import java.util.ArrayList;

import android.os.AsyncTask;

public class DbTask extends AsyncTask<Playlist, Integer, Playlist> {
	
	private InCorporeSoundHelper helper;
	
	public DbTask(InCorporeSoundHelper helper) {
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
