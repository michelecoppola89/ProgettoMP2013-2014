package it.lma5.incorporesound.Entities;

import it.lma5.incorporesound.Activities.PlayActivity;
import it.lma5.incorporesound.Receivers.MusicServiceReceiver;
import it.lma5.incorporesound.Receivers.NotificationReceiver;
import it.lma5.incorporesound.Services.MusicService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;

/**
 * * This class is used to play songs of playlist.
 * 
 * @author Andrea Di Lonardo, Luca Fanelli, Michele Coppola
 * 
 */
public class PlayTimer extends CountDownTimer {

	private ArrayList<Song> songList;
	private Integer songPosition;
	private Integer numOfIteration;
	private Integer startNumOfIteration;
	private Integer fadeIn;
	private MediaPlayer mediaPlayer;
	private Song songToPlay;
	private MusicServiceReceiver receiver;
	private Context context;
	private Boolean isInfinite;
	private float volume;
	private float deltaVolume;
	private long countDownInterval;

	/**
	 * used to start new song
	 * 
	 * @param millisInFuture
	 *            duration of CountDownTimer
	 * @param countDownInterval
	 *            update frequency of CountDownTimer
	 * @param songList
	 *            list of songs to play
	 * @param songPosition
	 *            position of current playing song
	 * @param receiver
	 *            Music service receiver
	 * @param context
	 * @param numOfIteration
	 * @param fadeIn
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public PlayTimer(long millisInFuture, long countDownInterval,
			ArrayList<Song> songList, Integer songPosition,
			MusicServiceReceiver receiver, Context context,
			Integer numOfIteration, Integer fadeIn)
			throws IllegalArgumentException, SecurityException,
			IllegalStateException, IOException {

		super(millisInFuture, countDownInterval);
		this.songList = songList;
		this.numOfIteration = numOfIteration;
		this.startNumOfIteration = numOfIteration;
		this.countDownInterval = countDownInterval;
		volume = 0.0f;
		if (numOfIteration == 0)
			isInfinite = true;
		else
			isInfinite = false;
		this.songPosition = songPosition;
		this.receiver = receiver;
		this.context = context;
		this.fadeIn = fadeIn;
		deltaVolume = (countDownInterval / (float) (fadeIn * 1000 / 2));

		mediaPlayer = new MediaPlayer();

		MusicService.setMediaPlayer(mediaPlayer);
		receiver.setMediaPlayer(mediaPlayer);
		Log.v("Play timer","song position: "+songPosition);
		songToPlay = songList.get(songPosition);

		// modify adapter
		Intent intent = new Intent(PlayActivity.PLAYSONG_PLAYLIST_NOTIFICATION);
		intent.putExtra("idSong", songToPlay.getId());
		context.sendBroadcast(intent);

		Log.v("Play timer","song to play: "+songToPlay.getName());
		mediaPlayer.setDataSource(context,
				Uri.parse(songToPlay.getPath().toString()));
		mediaPlayer.prepare();

		if (songToPlay.getBeginTime() != 0) {

			Integer interval = songToPlay.getDuration()
					- songToPlay.getUserDuration();
			Random rand = new Random();
			Integer beginTime = rand.nextInt(interval + 1);
			songToPlay.setBeginTime(beginTime);

		}
		mediaPlayer.seekTo(songToPlay.getBeginTime());

		// change artist and title in notification
		Intent i1 = new Intent(NotificationReceiver.NOTIFICATION_PLAY);
		i1.putExtra("artist", songToPlay.getArtist());
		i1.putExtra("title", songToPlay.getName());
		context.sendBroadcast(i1);

	}

	/**
	 * used when a song starts after pause
	 * 
	 * @param millisInFuture
	 *            duration of CountDownTimer
	 * @param countDownInterval
	 *            update frequency of CountDownTimer
	 * @param songList
	 *            list of songs to play
	 * @param songPosition
	 *            position of current playing song
	 * @param receiver
	 *            Music service receiver
	 * @param context
	 * @param player
	 *            MediaPlayer of playing song
	 * @param numOfIteration
	 * @param fadeIn
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public PlayTimer(long millisInFuture, long countDownInterval,
			ArrayList<Song> songList, Integer songPosition,
			MusicServiceReceiver receiver, Context context, MediaPlayer player,
			Integer numOfIteration, Integer fadeIn)
			throws IllegalArgumentException, SecurityException,
			IllegalStateException, IOException {

		super(millisInFuture, countDownInterval);
		this.songList = songList;
		this.songPosition = songPosition;
		this.receiver = receiver;
		this.context = context;
		this.numOfIteration = numOfIteration;
		this.startNumOfIteration = numOfIteration;
		this.fadeIn = fadeIn;
		volume = 1;
		this.countDownInterval = countDownInterval;
		deltaVolume = (countDownInterval / (float) (fadeIn * 1000 / 2));

		if (numOfIteration == 0)
			isInfinite = true;
		else
			isInfinite = false;

		mediaPlayer = player;

		songToPlay = songList.get(songPosition);

		// modify adapater
		Intent intent = new Intent(PlayActivity.PLAYSONG_PLAYLIST_NOTIFICATION);
		intent.putExtra("idSong", songToPlay.getId());
		context.sendBroadcast(intent);

		// change artist and title in notification
		Intent i1 = new Intent(NotificationReceiver.NOTIFICATION_PLAY);
		i1.putExtra("artist", songToPlay.getArtist());
		i1.putExtra("title", songToPlay.getName());
		context.sendBroadcast(i1);
	}

	@Override
	public void onFinish() {
		songPosition++;
		try {
			mediaPlayer.stop();
			mediaPlayer.reset();

		} catch (IllegalStateException e) {

			this.cancel();
			return;
		}

		if (!isInfinite && songPosition >= songList.size()) {
			numOfIteration--;
			receiver.setNumOfIteration(numOfIteration);

			if (numOfIteration == 0) {
				Log.v("PLAYTIMER","songposition = "+ songPosition);
				songPosition=songList.size();
				receiver.setSongPosition(songPosition);
				receiver.setNumOfIteration(startNumOfIteration);
				mediaPlayer.release();
				Intent i = new Intent(PlayActivity.STOP_PLAYLIST_NOTIFICATION);
				context.sendBroadcast(i);
				Intent i2 = new Intent(NotificationReceiver.NOTIFICATION_PAUSE);
				context.sendBroadcast(i2);
				
				return;
			}
		}

		songPosition = songPosition % songList.size();

		Log.v("PLAYTIMER", "canzone sucessiva");
		Log.v("PLAYTIMER", "position " + songPosition);
		// play next song
		songToPlay = songList.get(songPosition);
		try {
			mediaPlayer.setDataSource(context,
					Uri.parse(songToPlay.getPath().toString()));
			mediaPlayer.prepare();
			mediaPlayer.seekTo(songToPlay.getBeginTime());

			PlayTimer timer = new PlayTimer(
					songToPlay.getUserDuration() * 1000, 100, songList,
					songPosition, receiver, context, numOfIteration, fadeIn);
			receiver.setCntr_aCounter(timer);
			receiver.setSongPosition(songPosition);
			receiver.setSongToPlay(songToPlay);
			timer.start();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onTick(long millisUntilFinished) {

		if (millisUntilFinished >= (songToPlay.getUserDuration() * 1000 - fadeIn * 1000 / 2)) {
			fadeIn(countDownInterval);
			Log.v("PLAYTIMER", "fade in");
		}

		try {

			mediaPlayer.start();

		} catch (IllegalStateException e) {

			this.cancel();
			return;
		}

		if (millisUntilFinished <= (fadeIn * 1000 / 2)) {
			fadeOut(countDownInterval);
			Log.v("PLAYTIMER", "faccio fade out");
		}
		publishProgress(millisUntilFinished);

	}

	/**
	 * Used to add fade-out to song
	 * 
	 * @param deltaTime
	 *            time of fade-out
	 */

	private void fadeOut(float deltaTime) {

		try {
			mediaPlayer.setVolume(volume, volume);
		} catch (IllegalStateException e) {
			this.cancel();
			return;
		}
		if (volume - deltaVolume < 0)
			volume = 0.0f;
		else
			volume -= deltaVolume;

	}

	/**
	 * Used to add fade-in to song
	 * 
	 * @param deltaTime
	 *            time of fade-in
	 */
	private void fadeIn(float deltaTime) {
		try {
			mediaPlayer.setVolume(volume, volume);
		} catch (IllegalStateException e) {

			this.cancel();
			return;
		}
		if (volume + deltaVolume > 1)
			volume = 1.0f;
		else
			volume += deltaVolume;

	}

	/**
	 * publish progress in progress bar of activity
	 * 
	 * @param millisUntilFinished
	 *            milliseconds until song finishes
	 */
	private void publishProgress(long millisUntilFinished) {
		int progress = (int) (100 * (1 - ((float) millisUntilFinished / (songToPlay
				.getUserDuration() * 1000))));
		Intent i = new Intent(PlayActivity.PROGRESS_PLAYLIST_NOTIFICATION);
		i.putExtra("progress", progress);
		context.sendBroadcast(i);
	}

}
