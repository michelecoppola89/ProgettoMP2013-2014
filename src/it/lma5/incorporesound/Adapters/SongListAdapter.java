package it.lma5.incorporesound.Adapters;

import it.lma5.incorporesound.R;
import it.lma5.incorporesound.Entities.Song;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Adapter used for list of songs in CreatePlaylistActivity.
 * 
 * @author Andrea Di Lonardo, Luca Fanelli, Michele Coppola
 *
 */
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
		final Spinner spSongStart;
		final Spinner spSongDuration;

		if (rowView == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			rowView = li.inflate(R.layout.song_list_row_layout, null);
		}
		Song rowSong = list.get(position);

		if (rowSong != null) {

			String songlistName = rowSong.getName();
			String artistName = rowSong.getArtist();

			Integer songDuration = rowSong.getDuration();

			TextView tvSonglist = (TextView) rowView
					.findViewById(R.id.tvSongTitle);
			TextView tvArtist = (TextView) rowView.findViewById(R.id.tvArtist);

			tvSonglist.setText(songlistName);
			tvArtist.setText(artistName);

			btRemove = (Button) rowView.findViewById(R.id.btRemoveSong);
			btRemove.setTag(position);

			spSongDuration = (Spinner) rowView
					.findViewById(R.id.spSongDuration);
			spSongStart = (Spinner) rowView.findViewById(R.id.spSongStart);
			spSongStart.setTag(position);
			spSongDuration.setTag(position);

			List<String> durationList = new ArrayList<String>(
					Arrays.asList(getContext().getResources().getStringArray(
							R.array.arDurations)));

			if (songDuration < 30000) {

				for (int i = 0; i < 3; i++) {
					durationList.remove(durationList.size() - 1);
				}

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getContext(), android.R.layout.simple_spinner_item,
						durationList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spSongDuration.setAdapter(adapter);

			} else if (songDuration < 45000) {

				for (int i = 0; i < 2; i++) {
					durationList.remove(durationList.size() - 1);
				}

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getContext(), android.R.layout.simple_spinner_item,
						durationList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spSongDuration.setAdapter(adapter);

			} else if (songDuration < 60000) {

				durationList.remove(durationList.size() - 1);

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getContext(), android.R.layout.simple_spinner_item,
						durationList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spSongDuration.setAdapter(adapter);

			} else {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getContext(), android.R.layout.simple_spinner_item,
						durationList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spSongDuration.setAdapter(adapter);
			}

			btRemove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					remove(list.get((Integer) v.getTag()));
					notifyDataSetChanged();

				}
			});
			spSongDuration
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {

							Integer duration = Integer.parseInt(spSongDuration
									.getSelectedItem().toString());
							list.get((Integer) arg0.getTag()).setUserDuration(
									duration);

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub

						}

					});

			spSongStart.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					String beginning = spSongStart.getSelectedItem().toString();
					Song temp = list.get((Integer) arg0.getTag());
					if (beginning.equals("random")) {
						Integer interval = temp.getDuration()
								- temp.getUserDuration();
						if (interval >= 0) {

							Random rand = new Random();
							Integer beginTime = rand.nextInt(interval)+1;
							temp.setBeginTime(beginTime);

						}

					} else {
						//list.get((Integer) arg0.getTag()).setBeginTime(1);
						temp.setBeginTime(0);
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}

			});

			if (rowSong.getUserDuration() == 15)
				spSongDuration.setSelection(0);
			else if (rowSong.getUserDuration() == 30)
				spSongDuration.setSelection(1);
			else if (rowSong.getUserDuration() == 45)
				spSongDuration.setSelection(2);
			else
				spSongDuration.setSelection(3);

			if (rowSong.getBeginTime() == 0)
				spSongStart.setSelection(1);
			else
				spSongStart.setSelection(0);
		}

		return rowView;
	}

}
