package it.lma5.incorporesound;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.os.Build;

public class MainActivity extends Activity implements OnClickListener {

	private ListView myListView;
	private Button btAddPlaylyst;
	

	private InCorporeSoundHelper helper;
	private PlayListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		myListView = (ListView) findViewById(R.id.lvPlaylist);
		btAddPlaylyst = (Button) findViewById(R.id.btAddPlaylist);
		

		btAddPlaylyst.setOnClickListener(this);

		helper = new InCorporeSoundHelper(this);

		ArrayList<Playlist> alPl = new ArrayList<Playlist>();

		adapter = new PlayListAdapter(this, R.layout.playlist_row_layout, alPl);

		myListView.setAdapter(adapter);

		// ArrayList<Song> listSongProva = new ArrayList<Song>();
		// Song songProva = new Song(null, null,null, null, null,null);//di
		// prova
		// listSongProva.add(songProva);
		//
		// Playlist plProva = new Playlist("birra", listSongProva, null, false,
		// null);
		// Playlist plProva1 = new Playlist("birra", listSongProva, null, false,
		// null);
		// Playlist plProva2 = new Playlist("birra", listSongProva, null, false,
		// null);
		// Playlist plProva3 = new Playlist("birra", listSongProva, null, false,
		// null);
		// Playlist plProva4 = new Playlist("birra", listSongProva, null, false,
		// null);
		// Playlist plProva5 = new Playlist("birra", listSongProva, null, false,
		// null);
		// Playlist plProva6 = new Playlist("birra", listSongProva, null, false,
		// null);
		// Playlist plProva7 = new Playlist("birra", listSongProva, null, false,
		// null);
		//
		// ArrayList<Playlist> alPl = new ArrayList<Playlist>();
		// alPl.add(plProva);
		// alPl.add(plProva1);
		// alPl.add(plProva2);
		// alPl.add(plProva3);
		// alPl.add(plProva4);
		// alPl.add(plProva5);
		// alPl.add(plProva6);
		// alPl.add(plProva7);ArrayList<Song> listSongProva = new
		// ArrayList<Song>();
		// Song songProva = new Song(null, null,null, null, null,null);//di
		// prova
		// listSongProva.add(songProva);
		//
		// Playlist plProva = new Playlist("birra", listSongProva, null, false,
		// null);
		// Playlist plProva1 = new Playlist("birra", listSongProva, null, false,
		// null);
		// Playlist plProva2 = new Playlist("birra", listSongProva, null, false,
		// null);
		// Playlist plProva3 = new Playlist("birra", listSongProva, null, false,
		// null);
		// Playlist plProva4 = new Playlist("birra", listSongProva, null, false,
		// null);
		// Playlist plProva5 = new Playlist("birra", listSongProva, null, false,
		// null);
		// Playlist plProva6 = new Playlist("birra", listSongProva, null, false,
		// null);
		// Playlist plProva7 = new Playlist("birra", listSongProva, null, false,
		// null);
		//
		// ArrayList<Playlist> alPl = new ArrayList<Playlist>();
		// alPl.add(plProva);
		// alPl.add(plProva1);
		// alPl.add(plProva2);
		// alPl.add(plProva3);
		// alPl.add(plProva4);
		// alPl.add(plProva5);
		// alPl.add(plProva6);
		// alPl.add(plProva7);

		// PlayListAdapter plAdProva = new PlayListAdapter(this,
		// R.layout.playlist_row_layout, alPl);
		//
		// myListView.setAdapter(plAdProva);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btAddPlaylist) {
			Intent intent = new Intent(this, CreatePlaylistActivity.class);
			intent.putExtra("helper", helper);
			intent.putExtra("adapter", adapter);
			startActivity(intent);
		}
	}

}
