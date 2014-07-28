package it.lma5.incorporesound;

import java.io.IOException;
import java.util.ArrayList;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

public class MusicServiceReceiver extends BroadcastReceiver {

	private MediaPlayer mediaPlayer;
	private Song songToPlay;
	private Integer timeOfLastPause;
	
	private CountDownTimer cntr_aCounter;
	private ArrayList<Song> toPlay;
	private Integer songPosition;

	public MusicServiceReceiver(MediaPlayer mediaPlayer, Context context) {
		
		this.mediaPlayer = mediaPlayer;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(MusicService.PLAY_NOTIFICATION)) {

			Integer temp = songToPlay.getLastTimeMillis() - timeOfLastPause;
			Log.v("ENTRATO IN PLAY", "rimanenti "+temp.toString()+ "da "+timeOfLastPause);

			try {

				cntr_aCounter = new PlayTimer(songToPlay.getLastTimeMillis()
						- timeOfLastPause, 1000, toPlay, songPosition,this,context, mediaPlayer);		
				cntr_aCounter.start();
				
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

		} else if (intent.getAction().equals(MusicService.STOP_NOTIFICATION)) {
			
			cntr_aCounter.cancel();
			
			
		} else if (intent.getAction().equals(MusicService.PAUSE_NOTIFICATION)) {
			
			cntr_aCounter.cancel();
			mediaPlayer.pause();
			timeOfLastPause = mediaPlayer.getCurrentPosition();
			Log.v("ENTRATO IN PAUSA", timeOfLastPause.toString());

		} else if (intent.getAction().equals(MusicService.FORWARD_NOTIFICATION)) {

		} else if (intent.getAction()
				.equals(MusicService.BACKWARD_NOTIFICATION)) {

		}
	}

	public CountDownTimer getCntr_aCounter() {
		return cntr_aCounter;
	}

	public void setCntr_aCounter(CountDownTimer cntr_aCounter) {
		this.cntr_aCounter = cntr_aCounter;
	}

	public Song getSongToPlay() {
		return songToPlay;
	}

	public void setSongToPlay(Song songToPlay) {
		this.songToPlay = songToPlay;
	}

	public ArrayList<Song> getToPlay() {
		return toPlay;
	}

	public void setToPlay(ArrayList<Song> toPlay) {
		this.toPlay = toPlay;
	}

	public Integer getSongPosition() {
		return songPosition;
	}

	public void setSongPosition(Integer songPosition) {
		this.songPosition = songPosition;
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}
	
	

}
