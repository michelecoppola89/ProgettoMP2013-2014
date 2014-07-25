package it.lma5.incorporesound;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {

	public static String PLAY_NOTIFICATION = "it.lma5.incorporesound.play";
	public static String STOP_NOTIFICATION = "it.lma5.incorporesound.stop";
	public static String PAUSE_NOTIFICATION = "it.lma5.incorporesound.pause";
	public static String FORWARD_NOTIFICATION = "it.lma5.incorporesound.forward";
	public static String BACKWARD_NOTIFICATION = "it.lma5.incorporesound.backward";

	private InCorporeSoundHelper helper;
	private Playlist playlist;
	private MediaPlayer mediaPlayer;
	private MusicServiceReceiver musicServiceReceiver;
	private CountDownTimer cntr_aCounter;
	private Integer songPosition;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		helper = new InCorporeSoundHelper(getApplication()
				.getApplicationContext());

		String playlistName = intent.getStringExtra("PL_ID");
		playlist = helper.getPlaylistFromId(playlistName);
		Log.v("PLAYLIST NAME", playlistName);
		mediaPlayer = new MediaPlayer();
		musicServiceReceiver=new MusicServiceReceiver(mediaPlayer);
		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction(BACKWARD_NOTIFICATION);
		intentFilter.addAction(FORWARD_NOTIFICATION);
		intentFilter.addAction(PLAY_NOTIFICATION);
		intentFilter.addAction(STOP_NOTIFICATION);
		intentFilter.addAction(PAUSE_NOTIFICATION);
		registerReceiver(musicServiceReceiver, intentFilter);
		songPosition = 0;
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
				
				mediaPlayer.setDataSource(getApplicationContext(),Uri.parse(songToPlay.getPath().toString()));
				mediaPlayer.prepare();

				mediaPlayer.seekTo(songToPlay.getBeginTime());

				cntr_aCounter = new CountDownTimer(
						songToPlay.getUserDuration()*1000, 10000) {

					public void onTick(long millisUntilFinished) {

						mediaPlayer.start();
						Log.v("onTic", "----------------------");
					}

					public void onFinish() {
						// code fire after finish
						mediaPlayer.stop();
						mediaPlayer.reset();
					}
				};
				musicServiceReceiver.setCntr_aCounter(cntr_aCounter);
				musicServiceReceiver.setSongToPlay(songToPlay);
				cntr_aCounter.start();
				Thread.sleep(playlist.getFadeIn() * 1000);
				Log.v("MUSIC SERVICE", "dopo il fade in");

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
	public void onDestroy() {
		super.onDestroy();
		mediaPlayer.stop();
		mediaPlayer.release();
		unregisterReceiver(musicServiceReceiver);
		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void playSong() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
		
		Song songToPlay = playlist.getSongList().get(songPosition);
		
		mediaPlayer.setDataSource(getApplicationContext(),Uri.parse(songToPlay.getPath().toString()));
		mediaPlayer.prepare();
		mediaPlayer.seekTo(songToPlay.getBeginTime());
		cntr_aCounter = new CountDownTimer(
				songToPlay.getUserDuration()*1000, 10000) {

			public void onTick(long millisUntilFinished) {

				mediaPlayer.start();
				Log.v("onTic", "----------------------");
			}

			public void onFinish() {
				// code fire after finish
				mediaPlayer.stop();
				mediaPlayer.reset();
			}
		};
		musicServiceReceiver.setCntr_aCounter(cntr_aCounter);
		musicServiceReceiver.setSongToPlay(songToPlay);
		cntr_aCounter.start();
		
	}

}
