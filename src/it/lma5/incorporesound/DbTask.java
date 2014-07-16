package it.lma5.incorporesound;

import java.util.ArrayList;

import android.os.AsyncTask;

public class DbTask extends AsyncTask<Playlist, Integer, ArrayList<Playlist>> {
	
	private PlayListAdapter adapter;
	private InCorporeSoundHelper helper;
	
	public DbTask(PlayListAdapter adapter, InCorporeSoundHelper helper) {
		this.adapter = adapter;
		this.helper=helper;
	}


	@Override
	protected ArrayList<Playlist> doInBackground(Playlist... params) {
		Playlist ti= params[0];
		ArrayList<Playlist> ret = new ArrayList<Playlist>();
		ret.add(ti);
		
		helper.addPlaylist(ti);
		
		return ret;
	}


	@Override
	protected void onPostExecute(ArrayList<Playlist> result) {
	
		for(int i=0; i<result.size(); i++){
			adapter.add(result.get(i));
		}

	
	}

}
