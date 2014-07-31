package it.lma5.incorporesound.Activities;

import it.lma5.incorporesound.R;
import it.lma5.incorporesound.Adapters.PlayListAdapter;
import it.lma5.incorporesound.Entities.Playlist;
import it.lma5.incorporesound.SqliteHelpers.InCorporeSoundHelper;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import android.widget.ListView;

/**
 * Main activity of application. 
 * @author Andrea Di Lonardo, Luca Fanelli, Michele Coppola
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	private ListView myListView;
	private Button btAddPlaylyst;

	public static InCorporeSoundHelper helper;
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

		adapter = new PlayListAdapter(this, R.layout.playlist_row_layout, alPl,
				helper, this);

		myListView.setAdapter(adapter);

		List<Playlist> list = helper.getAllPlaylists();
		for (int i = 0; i < list.size(); i++) {
			adapter.add(list.get(i));
		}
		adapter.notifyDataSetChanged();

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
		
		if (v.getId() == R.id.btAddPlaylist) {
			Intent intent = new Intent(this, CreatePlaylistActivity.class);
			intent.putExtra("IS_UPDATED", false);
			startActivityForResult(intent, 5);
		}
		
		else if (v.getId() == R.id.btChange) {
			String plName = v.getTag().toString();
			Intent intent = new Intent(this, CreatePlaylistActivity.class);
			intent.putExtra("IS_UPDATED", true);
			intent.putExtra("PLAYLIST_ID", plName);
			startActivityForResult(intent, 5);

		}
		
		else if(v.getId() == R.id.btPlay){
			Intent intent = new Intent(this, PlayActivity.class);
			Integer plPos = Integer.parseInt(v.getTag().toString());
			Playlist pl = adapter.getItem(plPos);
			intent.putExtra("PLAYLIST_ID", pl.getName());
			startActivity(intent);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 5) {
			if (resultCode == RESULT_OK) {
				adapter.clear();
				List<Playlist> list = helper.getAllPlaylists();
				for (int i = 0; i < list.size(); i++) {
					adapter.add(list.get(i));
				}
				adapter.notifyDataSetChanged();

			}
		}
	}

}
