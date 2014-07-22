package it.lma5.incorporesound;

import java.util.ArrayList;

import android.os.AsyncTask;

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
