package it.lma5.incorporesound.Services;

import it.lma5.incorporesound.Entities.PlayTimer;
import it.lma5.incorporesound.Entities.Playlist;
import it.lma5.incorporesound.Entities.Song;
import it.lma5.incorporesound.Receivers.MusicServiceReceiver;
import it.lma5.incorporesound.SqliteHelpers.InCorporeSoundHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * Service used to play songs.
 * 
 * @author Andrea Di Lonardo, Luca Fanelli, Michele Coppola
 * 
 */
public class MusicService extends Service {

	public static String PLAY_NOTIFICATION = "it.lma5.incorporesound.play";
	public static String STOP_NOTIFICATION = "it.lma5.incorporesound.stop";
	public static String PAUSE_NOTIFICATION = "it.lma5.incorporesound.pause";
	public static String FORWARD_NOTIFICATION = "it.lma5.incorporesound.forward";
	public static String BACKWARD_NOTIFICATION = "it.lma5.incorporesound.backward";

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
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Service.START_STICKY_COMPATIBILITY;
	}

	/**
	 * Start playing songs
	 * 
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private void play() throws IllegalArgumentException, SecurityException,
			IllegalStateException, IOException {

		ArrayList<Song> toPlay;

		toPlay = playlist.getSongList();

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

		try {
			mediaPlayer.stop();
		} 
		catch (IllegalStateException e) {
			NotificationManager notificationManager = 
					(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notificationManager.cancel(0);
		}
		mediaPlayer.release();
		unregisterReceiver(musicServiceReceiver);

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public static MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public static void setMediaPlayer(MediaPlayer mediaPlayer) {
		MusicService.mediaPlayer = mediaPlayer;
	}

}
