package it.lma5.incorporesound;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SongListToPlayAdapter extends ArrayAdapter<Song> {
	private ArrayList<Song> list;
	private Integer positionPlay;

	public SongListToPlayAdapter(Context context, int resource, ArrayList<Song> songList) {
		
		super(context, resource, songList);
		list = songList;
		positionPlay = 0;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			rowView = li.inflate(R.layout.song_list_play_row_layout, null);
		}
		Song rowSong = list.get(position);
		if (rowSong != null) {

			String songlistName = rowSong.getName();
			String artistName = rowSong.getArtist();
			
			TextView tvTitlePlay = (TextView) rowView
					.findViewById(R.id.tvTitlePlay);
			TextView tvArtistPlay = (TextView) rowView
					.findViewById(R.id.tvArtistPlay);
			ImageView ivPlayingSong = (ImageView) rowView.findViewById(R.id.ivPlayingSongImg);
			
			tvTitlePlay.setText(songlistName);
			tvArtistPlay.setText(artistName);
			
			if(positionPlay!=position)
				ivPlayingSong.setVisibility(View.INVISIBLE);
			else
				ivPlayingSong.setVisibility(View.VISIBLE);
		}

		return rowView;
	}

	public Integer getPositionPlay() {
		return positionPlay;
	}

	public void setPositionPlay(Integer positionPlay) {
		this.positionPlay = positionPlay;
	}

	public ArrayList<Song> getList() {
		return list;
	}

	public void setList(ArrayList<Song> list) {
		this.list = list;
	}
	
	
	

}
