package it.lma5.incorporesound;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
public class SongListAdapter extends ArrayAdapter<Song> {

	private ArrayList<Song> list;

	public SongListAdapter(Context context, int resource,
			ArrayList<Song> objects) {
		super(context, resource, objects);
		list = objects;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		Button btRemove;
		Spinner spSongStart;
		final Spinner spSongDuration;

		if (rowView == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			rowView = li.inflate(R.layout.song_list_row_layout, null);
		}
		
		
		
		String songlistName = list.get(position).getName(); 
		String artistName= list.get(position).getArtist();
		
		if(songlistName!=null){
			
			TextView tvSonglist = (TextView) rowView.findViewById(R.id.tvSongName);
			TextView tvArtist = (TextView) rowView.findViewById(R.id.tvArtist);
		
			tvSonglist.setText(songlistName);
			tvArtist.setText(artistName);
			
			btRemove = (Button) rowView.findViewById(R.id.btRemoveSong);
			spSongDuration = (Spinner) rowView.findViewById(R.id.spSongDuration);
			spSongStart= (Spinner) rowView.findViewById(R.id.spSongStart);
			spSongDuration.setTag(position);
			btRemove.setTag(position);
			btRemove.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					remove(list.get((Integer)v.getTag()));
					notifyDataSetChanged();
					
				}
			});
			spSongDuration.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
				
					Integer duration= Integer.parseInt(spSongDuration.getSelectedItem().toString());
					list.get((Integer)arg0.getTag()).setUserDuration(duration);
				
					
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
		}

		return rowView;
	}


}
