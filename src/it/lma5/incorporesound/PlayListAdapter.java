package it.lma5.incorporesound;

import java.io.Serializable;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlayListAdapter extends ArrayAdapter<Playlist> implements Serializable{

	private static final long serialVersionUID = 1L;
	private ArrayList<Playlist> list;

	public PlayListAdapter(Context context, int resource,
			ArrayList<Playlist> objects) {
		super(context, resource, objects);
		list = objects;

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
		
		if(playlistName!=null){
			TextView tvPlaylist = (TextView) rowView.findViewById(R.id.tvPlaylistName);
			
			tvPlaylist.setText(playlistName);
		}

		return rowView;
	}


}
