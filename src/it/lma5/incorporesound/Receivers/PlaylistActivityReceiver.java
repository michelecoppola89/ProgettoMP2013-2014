package it.lma5.incorporesound.Receivers;

import it.lma5.incorporesound.R;
import it.lma5.incorporesound.Activities.PlayActivity;
import it.lma5.incorporesound.Adapters.SongListToPlayAdapter;
import it.lma5.incorporesound.Entities.Song;
import it.lma5.incorporesound.Services.MusicService;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * Update progress bar, current playing song and PlayActivity when a playlist 
 * is stopped. 
 * @author Andrea Di Lonardo, Luca Fanelli, Michele Coppola
 * 
 */
public class PlaylistActivityReceiver extends BroadcastReceiver {

	private PlayActivity playActivity;
	private SongListToPlayAdapter adapter;
	private Button btPlaySong;
	private Button btStopSong;
	private Button btBackwardSong;
	private Button btForwardSong;
	private Button btPauseSong;
	private ProgressBar pbPlaySong;
	private Intent serviceIntent;

	public PlaylistActivityReceiver(PlayActivity playActivity,
			SongListToPlayAdapter adapter) {

		this.playActivity = playActivity;
		this.adapter = adapter;
		btBackwardSong = (Button) playActivity
				.findViewById(R.id.btBackwardSong);
		btForwardSong = (Button) playActivity.findViewById(R.id.btForwardSong);
		btPauseSong = (Button) playActivity.findViewById(R.id.btPauseSong);
		btPlaySong = (Button) playActivity.findViewById(R.id.btPlaySong);
		btStopSong = (Button) playActivity.findViewById(R.id.btStopSong);
		pbPlaySong = (ProgressBar) playActivity.findViewById(R.id.pbPlaySong);
		serviceIntent = playActivity.getServiceIntent();
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(PlayActivity.STOP_PLAYLIST_NOTIFICATION)) {
			btBackwardSong.setEnabled(false);
			btForwardSong.setEnabled(false);
			btPauseSong.setEnabled(false);
			btStopSong.setEnabled(false);
			btPlaySong.setEnabled(true);

		} else if (intent.getAction().equals(
				PlayActivity.PROGRESS_PLAYLIST_NOTIFICATION)) {

			int progress = intent.getIntExtra("progress", 0);
			pbPlaySong.setProgress(progress);
		}

		else if (intent.getAction().equals(
				PlayActivity.PLAYSONG_PLAYLIST_NOTIFICATION)) {

			Integer idSong = intent.getIntExtra("idSong", 0);
			ArrayList<Song> songList = adapter.getList();
			Integer pos = getPositionBySongId(idSong, songList);
			adapter.setPositionPlay(pos);
			adapter.notifyDataSetChanged();

		} else if (intent.getAction().equals(
				PlayActivity.CLOSE_SERVICE_NOTIFICATION)) {
			Intent i = new Intent(MusicService.STOP_NOTIFICATION);
			context.sendBroadcast(i);
		
			context.stopService(serviceIntent);
			playActivity.finish();
			

		}

	}

	/** 
	 * @param idSong id of requested song
	 * @param songList list of songs to play
	 * @return position of song in songList
	 */
	private Integer getPositionBySongId(Integer idSong, ArrayList<Song> songList) {

		Integer ret = 0;
		for (int i = 0; i < songList.size(); i++) {
			Song temp = songList.get(i);
			if (temp.getId() == idSong) {
				ret = i;
				break;
			}
		}

		return ret;
	}

}
