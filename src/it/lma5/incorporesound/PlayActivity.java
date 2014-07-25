package it.lma5.incorporesound;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Build;

public class PlayActivity extends Activity implements OnClickListener {

	private Intent serviceIntent;
	private InCorporeSoundHelper helper;
	private Playlist playlist;
	private Button btPlaySong;
	private Button btPauseSong;
	private Button btStopSong;
	private Button btForwardSong;
	private Button btBackwardSong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		btStopSong = (Button) findViewById(R.id.btStopSong);
		btStopSong.setOnClickListener(this);
		btPlaySong = (Button) findViewById(R.id.btPlaySong);
		btPlaySong.setOnClickListener(this);
		btPauseSong = (Button) findViewById(R.id.btPauseSong);
		btPauseSong.setOnClickListener(this);
		btBackwardSong = (Button) findViewById(R.id.btBackwardSong);
		btBackwardSong.setOnClickListener(this);
		btForwardSong = (Button) findViewById(R.id.btForwardSong);
		btForwardSong.setOnClickListener(this);
		helper = new InCorporeSoundHelper(this);
		String playlistName = getIntent().getStringExtra("PLAYLIST_ID");
		Log.v("PlayActivity!!!!", playlistName);

		playlist = helper.getPlaylistFromId(playlistName);

		Log.v("PlayActivity", playlistName);

		if (playlist == null)
			Log.v("PlayActivity", "ERR");

		serviceIntent = new Intent(getApplicationContext(), MusicService.class);
		serviceIntent.putExtra("PL_ID", playlistName);
		startService(serviceIntent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_play, container,
					false);
			return rootView;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == R.id.btStopSong) {
			
			Intent i = new Intent(MusicService.STOP_NOTIFICATION);
			sendBroadcast(i);
			stopService(serviceIntent);
			finish();

		}

		else if (v.getId() == R.id.btPlaySong) {
			Intent i = new Intent(MusicService.PLAY_NOTIFICATION);
			sendBroadcast(i);

		} else if (v.getId() == R.id.btPauseSong) {
			Intent i = new Intent(MusicService.PAUSE_NOTIFICATION);
			sendBroadcast(i);

		} else if (v.getId() == R.id.btForwardSong) {
			stopService(serviceIntent);

		} else {

		}
	}

}
