package it.lma5.incorporesound;

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
	private CountDownTimer cntr_aCounter;
	private Integer timeOfLastPause;

	public MusicServiceReceiver(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(MusicService.PLAY_NOTIFICATION)) {
			
			Integer temp = songToPlay.getLastTimeMillis()-timeOfLastPause;
			Log.v("ENTRATO IN PLAY", temp.toString());
			cntr_aCounter = new CountDownTimer(
					songToPlay.getLastTimeMillis()-timeOfLastPause, 10000) {

				public void onTick(long millisUntilFinished) {

					mediaPlayer.start();
					Log.v("onTic", "----------------------");
				}

				public void onFinish() {
					// code fire after finish
					mediaPlayer.stop();
				}
			};
			cntr_aCounter.start();

		} else if(intent.getAction().equals(MusicService.STOP_NOTIFICATION)) {
			cntr_aCounter.cancel();
		}
		else if (intent.getAction().equals(MusicService.PAUSE_NOTIFICATION)) {
			timeOfLastPause=mediaPlayer.getCurrentPosition();
			mediaPlayer.pause();
			cntr_aCounter.cancel();
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
	
}
