package it.lma5.incorporesound;

import java.util.ArrayList;

import android.os.AsyncTask;

public class DbTaskDeleteSongs extends AsyncTask<ArrayList<Song>, Integer, ArrayList<Song>> {
	
	private InCorporeSoundHelper helper;

	public DbTaskDeleteSongs(InCorporeSoundHelper helper) {
		this.helper = helper;
	}

	@Override
	protected ArrayList<Song> doInBackground(ArrayList<Song>... params) {
		// TODO Auto-generated method stub
		
		ArrayList<Song> toDelete= params[0];
		
		for (int i=0; i<toDelete.size(); i++){
			
			helper.deleteSong(toDelete.get(i).getId());
		}
		
		
		return null;
	}

}
