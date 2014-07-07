package it.lma5.incorporesound;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Build;

public class CreatePlaylistActivity extends Activity implements OnClickListener {

	private Button btAddSong;
	private Button btSavePlaylist;
	private ListView lvSongList;
	private ArrayList<Song> songList = new ArrayList<Song>();
	private SongListAdapter slAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_playlist);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();

		}

		btAddSong = (Button) findViewById(R.id.btAddSong);

		btAddSong.setOnClickListener(this);

		lvSongList = (ListView) findViewById(R.id.lvSongList);

		slAdapter = new SongListAdapter(this, R.layout.song_list_row_layout,
				songList);
		lvSongList.setAdapter(slAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_playlist, menu);
		
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			if(!songList.isEmpty())
				Toast.makeText(this,songList.get(1).getUserDuration().toString() ,Toast.LENGTH_LONG).show();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_create_playlist,
					container, false);
			return rootView;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btAddSong) {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("audio/*");
			startActivityForResult(intent, 10);
		} 
//		else if (v.getId() == R.id.action_settings) {
//			Toast.makeText(this, "acchiappa 'sto toast!!!!!!!", Toast.LENGTH_LONG).show();
//		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK && requestCode == 10) {
			Uri uriSong;
			uriSong = data.getData();

			Log.v("PROVA", "URI:" + uriSong.toString());
			MediaMetadataRetriever retriever = new MediaMetadataRetriever();
			try {
				retriever.setDataSource(this, uriSong);
				String mimetype = retriever
						.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
				if (!mimetype.contains("audio")) {
					Toast.makeText(this, "Wrong format", Toast.LENGTH_SHORT)
							.show();
				} else {
					// Toast.makeText(this, mimetype, Toast.LENGTH_LONG).show();

					String name = retriever
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
					
					Integer duration = new Integer(
							retriever
									.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

					String artist = retriever
							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
					
					

					Song songTi = new Song(name, uriSong, 0, 15, duration,
							artist);
					slAdapter.add(songTi);
				}

			} catch (RuntimeException e) {
				Toast.makeText(this, "Wrong format", Toast.LENGTH_SHORT).show();
			}

		}
	}
}
