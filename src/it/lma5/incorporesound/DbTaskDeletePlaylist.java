package it.lma5.incorporesound;

import android.os.AsyncTask;

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
