package it.lma5.incorporesound;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

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
	private static Integer NUMBER_OF_SHUFFLES = 20;
	private InCorporeSoundHelper helper;
	private Playlist playlist;
	private static MediaPlayer mediaPlayer;
	private MusicServiceReceiver musicServiceReceiver;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		helper = new InCorporeSoundHelper(getApplication()
				.getApplicationContext());

		String playlistName = intent.getStringExtra("PL_ID");
		playlist = helper.getPlaylistFromId(playlistName);
		Log.v("PLAYLIST NAME", playlistName);
		mediaPlayer = new MediaPlayer();
		musicServiceReceiver = new MusicServiceReceiver(mediaPlayer,
				getApplicationContext());

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BACKWARD_NOTIFICATION);
		intentFilter.addAction(FORWARD_NOTIFICATION);
		intentFilter.addAction(PLAY_NOTIFICATION);
		intentFilter.addAction(STOP_NOTIFICATION);
		intentFilter.addAction(PAUSE_NOTIFICATION);
		registerReceiver(musicServiceReceiver, intentFilter);
		musicServiceReceiver.setSongPosition(0);
		musicServiceReceiver.setToPlay(playlist.getSongList());

		try {
			play();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Service.START_STICKY_COMPATIBILITY;
	}

	public void play() throws IllegalArgumentException, SecurityException,
			IllegalStateException, IOException {

		ArrayList<Song> toPlay;

		toPlay = playlist.getSongList();

		Log.v("SONO IN PLAY", "toPlay" + toPlay.size());

		if (playlist.is_random())
			Collections.shuffle(toPlay);

		Song songToPlay = playlist.getSongList().get(0);
		PlayTimer playTimer = new PlayTimer(
				songToPlay.getUserDuration() * 1000, 100, toPlay, 0,
				musicServiceReceiver, getApplicationContext(),
				playlist.getRound(), playlist.getFadeIn());

		musicServiceReceiver.setCntr_aCounter(playTimer);
		musicServiceReceiver.setSongToPlay(songToPlay);
		musicServiceReceiver.setNumOfIteration(playlist.getRound());
		musicServiceReceiver.setFadeIn(playlist.getFadeIn());
		playTimer.start();

	}

	// shuffle songs
	private void shuffleSongList(ArrayList<Song> toPlay) {
		for (int i = 0; i < NUMBER_OF_SHUFFLES; i++) {
			Random random = new Random();
			Integer a = random.nextInt(toPlay.size());
			Integer b = random.nextInt(toPlay.size());
			Collections.swap(toPlay, a, b);
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

	public static MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public static void setMediaPlayer(MediaPlayer mediaPlayer) {
		MusicService.mediaPlayer = mediaPlayer;
	}

}
