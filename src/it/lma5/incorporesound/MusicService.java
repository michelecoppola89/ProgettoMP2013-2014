package it.lma5.incorporesound;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class MusicService extends Service {

	public static String PLAY_NOTIFICATION = "it.lma5.incorporesound.play";
	public static String STOP_NOTIFICATION = "it.lma5.incorporesound.stop";
	public static String PAUSE_NOTIFICATION = "it.lma5.incorporesound.pause";
	public static String FORWARD_NOTIFICATION = "it.lma5.incorporesound.forward";
	public static String BACKWARD_NOTIFICATION = "it.lma5.incorporesound.backward";
	public static String CLOSE_SERVICE_NOTIFICATION = "it.lma5.incorporesound.closeService";
	private static Integer NUMBER_OF_SHUFFLES = 20;
	private NotificationManager notificationManager;
	private InCorporeSoundHelper helper;
	private Playlist playlist;
	private static MediaPlayer mediaPlayer;
	private MusicServiceReceiver musicServiceReceiver;
	private Notification notification;

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
		intentFilter.addAction(CLOSE_SERVICE_NOTIFICATION);
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

		initilizeNotification();

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

	@Override
	public void onDestroy() {
		super.onDestroy();
		mediaPlayer.stop();
		mediaPlayer.release();
		unregisterReceiver(musicServiceReceiver);
		notificationManager.cancel(0);

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
