package it.lma5.incorporesound;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {

	public static String PLAY_NOTIFICATION = "play";
	public static String STOP_NOTIFICATION = "stop";
	public static String PAUSE_NOTIFICATION = "pause";
	public static String FORWARD_NOTIFICATION = "forward";
	public static String BACKWARD_NOTIFICATION = "backward";

	private InCorporeSoundHelper helper;
	private Playlist playlist;
	private MediaPlayer mediaPlayer;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		helper = new InCorporeSoundHelper(getApplication()
				.getApplicationContext());

		String playlistName = intent.getStringExtra("PL_ID");
		playlist = helper.getPlaylistFromId(playlistName);
		Log.v("PLAYLIST NAME", playlistName);
		play();

		return Service.START_STICKY_COMPATIBILITY;
	}

	public void play() {

		ArrayList<Song> toPlay;

		toPlay = playlist.getSongList();

		Log.v("SONO IN PLAY", "toPlay" + toPlay.size());

		for (int i = 0; i < toPlay.size(); i++) {
			Song songToPlay = toPlay.get(i);
			try {
				mediaPlayer = new MediaPlayer();


				mediaPlayer.setDataSource(songToPlay.getPath().toString());

				mediaPlayer.prepare();

				mediaPlayer.seekTo(songToPlay.getBeginTime());

				CountDownTimer cntr_aCounter = new CountDownTimer(
						songToPlay.getUserDuration()*1000, songToPlay.getUserDuration()*1000) {

					public void onTick(long millisUntilFinished) {

						mediaPlayer.start();
					}

					public void onFinish() {
						// code fire after finish
						mediaPlayer.stop();
					}
				};
				cntr_aCounter.start();

				Thread.sleep(playlist.getFadeIn() * 1000);

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
