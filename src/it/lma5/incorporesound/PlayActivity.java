package it.lma5.incorporesound;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
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
	private TextView tvRunningPlaylistName;
	private TextView tvRandomMode;
	private ProgressBar pbPlaySong;
	private SongListToPlayAdapter adapter;
	private ListView lvPlaySongList;
	private Notification notification;
	private NotificationManager notificationManager;
	public static String STOP_PLAYLIST_NOTIFICATION = "it.lma5.incorporesound.PlayActivity.stopPlaylistNotification";
	public static String PROGRESS_PLAYLIST_NOTIFICATION = "it.lma5.incorporesound.PlayActivity.progressPlaylistNotification";
	public static String PLAYSONG_PLAYLIST_NOTIFICATION = "it.lma5.incorporesound.PlayActivity.playSongPlaylistNotification";
	public static String CLOSE_SERVICE_NOTIFICATION = "it.lma5.incorporesound.closeService";
	private PlaylistActivityReceiver receiver;

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
		btPlaySong.setEnabled(false);
		btPauseSong = (Button) findViewById(R.id.btPauseSong);
		btPauseSong.setOnClickListener(this);
		btBackwardSong = (Button) findViewById(R.id.btBackwardSong);
		btBackwardSong.setOnClickListener(this);
		btForwardSong = (Button) findViewById(R.id.btForwardSong);
		btForwardSong.setOnClickListener(this);
		lvPlaySongList = (ListView) findViewById(R.id.lvPlaySongList);
		tvRunningPlaylistName = (TextView) findViewById(R.id.tvRunninPlaylistName);
		tvRandomMode = (TextView) findViewById(R.id.tvRandomMode);
		pbPlaySong = (ProgressBar) findViewById(R.id.pbPlaySong);
		pbPlaySong.setProgress(0);
		pbPlaySong.setMax(100);
		helper = new InCorporeSoundHelper(this);
		String playlistName = getIntent().getStringExtra("PLAYLIST_ID");
		Log.v("PlayActivity!!!!", playlistName);

		playlist = helper.getPlaylistFromId(playlistName);
		tvRunningPlaylistName.setText(playlist.getName());
		if (playlist.is_random())
			tvRandomMode.setText("Random Mode");
		else
			tvRandomMode.setText("Straight Mode");
		Log.v("PlayActivity", playlistName);

		if (playlist == null)
			Log.v("PlayActivity", "ERR");

		adapter = new SongListToPlayAdapter(this,
				R.layout.song_list_play_row_layout, playlist.getSongList());
		lvPlaySongList.setAdapter(adapter);

		checkSongs();

		serviceIntent = new Intent(getApplicationContext(), MusicService.class);
		serviceIntent.putExtra("PL_ID", playlistName);
		startService(serviceIntent);

		receiver = new PlaylistActivityReceiver(this, adapter);

		initilizeNotification();

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
			btBackwardSong.setEnabled(true);
			btForwardSong.setEnabled(true);
			btPauseSong.setEnabled(true);
			btPlaySong.setEnabled(false);
			btStopSong.setEnabled(true);
		} else if (v.getId() == R.id.btPauseSong) {
			Intent i = new Intent(MusicService.PAUSE_NOTIFICATION);
			sendBroadcast(i);
			btBackwardSong.setEnabled(false);
			btForwardSong.setEnabled(false);
			btPauseSong.setEnabled(false);
			btPlaySong.setEnabled(true);
		} else if (v.getId() == R.id.btForwardSong) {
			Intent i = new Intent(MusicService.FORWARD_NOTIFICATION);
			sendBroadcast(i);
		} else {

			Intent i = new Intent(MusicService.BACKWARD_NOTIFICATION);
			sendBroadcast(i);

		}
	}
	


	@Override
	protected void onResume() {

		super.onResume();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(STOP_PLAYLIST_NOTIFICATION);
		intentFilter.addAction(PROGRESS_PLAYLIST_NOTIFICATION);
		intentFilter.addAction(PLAYSONG_PLAYLIST_NOTIFICATION);
		intentFilter.addAction(CLOSE_SERVICE_NOTIFICATION);
		registerReceiver(receiver, intentFilter);

	}

	public void checkSongs() {
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		ArrayList<Integer> missingSongPosition = new ArrayList<Integer>();
		ArrayList<Song> toDelete = new ArrayList<Song>();

		for (int i = 0; i < playlist.getSongList().size(); i++) {
			Song temp = playlist.getSongList().get(i);
			try {
				retriever.setDataSource(this, temp.getPath());
			} catch (IllegalArgumentException e) {
				missingSongPosition.add(i);
				toDelete.add(temp);
			}
		}

		for (int i = missingSongPosition.size() - 1; i >= 0; i--) {
			adapter.remove(playlist.getSongList().get(
					missingSongPosition.get(i)));
		}

		if (missingSongPosition.size() > 0) {

			DbTaskDeleteSongs dbTaskDeleteSongs = new DbTaskDeleteSongs(helper);
			dbTaskDeleteSongs.execute(toDelete);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("There are songs removed from device")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}



	@Override
	protected void onDestroy() {
//		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//		notificationManager.notify(0, notification);
		super.onDestroy();
		unregisterReceiver(receiver);
		
	}

	public Intent getServiceIntent() {
		return serviceIntent;
	}
	
	
	
	public NotificationManager getNotificationManager() {
		return notificationManager;
	}

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	public void initilizeNotification() {
		// prepare intent which is triggered if the
		// notification is selected

		Intent intent = new Intent(this, PlayActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		
		Intent iStop = new Intent(CLOSE_SERVICE_NOTIFICATION);
		PendingIntent pStop = PendingIntent.getBroadcast(getApplicationContext(), 0, iStop, 0);

		// Create remote view and set bigContentView.
		RemoteViews expandedView = new RemoteViews(this.getPackageName(),
				R.layout.notification_layout);

		// build notification
		// the addAction re-use the same intent to keep the example short

		notification = new Notification.Builder(this).setContentTitle("PROVA")
				.setContentText("Subject").setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pIntent).setAutoCancel(false)
				.setContent(expandedView).setDeleteIntent(pStop).build();
		notification.bigContentView = expandedView;
		

		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);

	}
	
	

}
