package it.lma5.incorporesound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;

public class PlayTimer extends CountDownTimer {
	
	private ArrayList<Song> songList;
	private Integer songPosition;
	private MediaPlayer mediaPlayer;
	private Song songToPlay;
	private MusicServiceReceiver receiver;
	private Context context;

	// used to start new song
	public PlayTimer(long millisInFuture, long countDownInterval,
			ArrayList<Song> songList, Integer songPosition,
			MusicServiceReceiver receiver, Context context) throws IllegalArgumentException,
			SecurityException, IllegalStateException, IOException {

		super(millisInFuture, countDownInterval);
		this.songList = songList;
		this.songPosition = songPosition;
		this.receiver = receiver;
		this.context = context;
		
		
		mediaPlayer = new MediaPlayer();
		
		MusicService.setMediaPlayer(mediaPlayer);
		receiver.setMediaPlayer(mediaPlayer);
		songToPlay = songList.get(songPosition);	
		mediaPlayer.setDataSource(context,
				Uri.parse(songToPlay.getPath().toString()));
		mediaPlayer.prepare();
		
		if(songToPlay.getBeginTime()!=0){
			
			Integer interval = songToPlay.getDuration()
					- songToPlay.getUserDuration();
			Random rand = new Random();
			Integer beginTime = rand.nextInt(interval+1);
			songToPlay.setBeginTime(beginTime);
			
		}
		mediaPlayer.seekTo(songToPlay.getBeginTime());
			
	}
	
	// used when a song starts after pause
	public PlayTimer(long millisInFuture, long countDownInterval,
			ArrayList<Song> songList, Integer songPosition,
			MusicServiceReceiver receiver, Context context, MediaPlayer player) throws IllegalArgumentException,
			SecurityException, IllegalStateException, IOException {

		super(millisInFuture, countDownInterval);
		this.songList = songList;
		this.songPosition = songPosition;
		this.receiver = receiver;
		this.context = context;
		
		
		mediaPlayer = player;
		
		songToPlay = songList.get(songPosition);	
	}

	@Override
	public void onFinish() {
		songPosition++;
		mediaPlayer.stop();
		mediaPlayer.reset();
		//mediaPlayer.release();

		
		if (songPosition >= songList.size()) {
			mediaPlayer.release();
			return;
		}
			
		
		Log.v("PLAYTIMER", "canzone sucessiva");
		Log.v("PLAYTIMER", "position "+songPosition);
		// play next song
		songToPlay = songList.get(songPosition);
		try {
			mediaPlayer.setDataSource(context,
					Uri.parse(songToPlay.getPath().toString()));
			mediaPlayer.prepare();
			mediaPlayer.seekTo(songToPlay.getBeginTime());

			PlayTimer timer = new PlayTimer(
					songToPlay.getUserDuration() * 1000, 1000, songList,
					songPosition, receiver,context);
			receiver.setCntr_aCounter(timer);
			receiver.setSongPosition(songPosition);
			receiver.setSongToPlay(songToPlay);
			timer.start();

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

	}

	@Override
	public void onTick(long millisUntilFinished) {
		mediaPlayer.start();
		Log.v("onTic", "----------------------");

	}

}
