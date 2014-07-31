package it.lma5.incorporesound.Adapters;

import it.lma5.incorporesound.R;
import it.lma5.incorporesound.Activities.MainActivity;
import it.lma5.incorporesound.AsyncTasks.DbTaskDeletePlaylist;
import it.lma5.incorporesound.Entities.Playlist;
import it.lma5.incorporesound.SqliteHelpers.InCorporeSoundHelper;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Adapter used for list of playlists in MainActivity.
 * @author Andrea Di Lonardo, Luca Fanelli, Michele Coppola
 * 
 */
public class PlayListAdapter extends ArrayAdapter<Playlist> implements
		OnClickListener {

	private ArrayList<Playlist> list;
	private Button btPlay;
	private Button btChange;
	private Button btDelete;
	private InCorporeSoundHelper helper;
	private MainActivity mainActivity;

	public PlayListAdapter(Context context, int resource,
			ArrayList<Playlist> objects, InCorporeSoundHelper helper,
			MainActivity mainActivity) {
		super(context, resource, objects);
		this.helper = helper;
		list = objects;
		this.mainActivity = mainActivity;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			rowView = li.inflate(R.layout.playlist_row_layout, null);
		}

		String playlistName = list.get(position).getName();

		if (playlistName != null) {
			TextView tvPlaylist = (TextView) rowView
					.findViewById(R.id.tvPlaylistName);
			tvPlaylist.setText(playlistName);
			btPlay = (Button) rowView.findViewById(R.id.btPlay);
			btChange = (Button) rowView.findViewById(R.id.btChange);
			btDelete = (Button) rowView.findViewById(R.id.btDelete);
			btPlay.setOnClickListener(mainActivity);
			btChange.setOnClickListener(mainActivity);
			btDelete.setOnClickListener(this);
			btPlay.setTag(position);
			btChange.setTag(playlistName);
			btDelete.setTag(position);
		}

		return rowView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btDelete) {
			Playlist toRemove = list.get((Integer) v.getTag());
			remove(toRemove);
			DbTaskDeletePlaylist run = new DbTaskDeletePlaylist(helper);
			run.execute(toRemove);
			notifyDataSetChanged();

		}

	}

}
