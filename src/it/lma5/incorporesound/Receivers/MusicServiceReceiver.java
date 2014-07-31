package it.lma5.incorporesound.Receivers;

import it.lma5.incorporesound.Entities.PlayTimer;
import it.lma5.incorporesound.Entities.Song;
import it.lma5.incorporesound.Services.MusicService;

import java.io.IOException;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

/**
 * Receives messages from PlayActivity when button of play, pause, 
 * stop, backward, forward are clicked.
 * 
 * @author Andrea Di Lonardo, Luca Fanelli, Michele Coppola
 *
 */
public class MusicServiceReceiver extends BroadcastReceiver {

	private MediaPlayer mediaPlayer;
	private Song songToPlay;
	private Integer timeOfLastPause;

	private CountDownTimer cntr_aCounter;
	private ArrayList<Song> toPlay;
	private Integer songPosition;
	private Integer numOfIteration;
	private Integer fadeIn;

	public MusicServiceReceiver(MediaPlayer mediaPlayer, Context context) {

		this.mediaPlayer = mediaPlayer;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(MusicService.PLAY_NOTIFICATION)) {

			try {
				if (songPosition >= toPlay.size()) {
					// play from start
					songPosition = 0;
					songToPlay = toPlay.get(songPosition);
					cntr_aCounter = new PlayTimer(songToPlay.getBeginTime(),
							100, toPlay, songPosition, this, context,
							numOfIteration,fadeIn);
					cntr_aCounter.start();

				} else {
					// resume after pause
					cntr_aCounter = new PlayTimer(
							songToPlay.getLastTimeMillis() - timeOfLastPause,
							100, toPlay, songPosition, this, context,
							mediaPlayer, numOfIteration,fadeIn);
					cntr_aCounter.start();
				}
				


			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (intent.getAction().equals(MusicService.STOP_NOTIFICATION)) {

			cntr_aCounter.cancel();
			

		} else if (intent.getAction().equals(MusicService.PAUSE_NOTIFICATION)) {

			cntr_aCounter.cancel();
			mediaPlayer.pause();
			timeOfLastPause = mediaPlayer.getCurrentPosition();
			
			Intent intentNot = new Intent(NotificationReceiver.NOTIFICATION_PAUSE);
			intentNot.putExtra("artist", songToPlay.getArtist());
			intentNot.putExtra("title", songToPlay.getName());
			context.sendBroadcast(intentNot);

		} else if (intent.getAction().equals(MusicService.FORWARD_NOTIFICATION)) {

			cntr_aCounter.cancel();
			mediaPlayer.stop();
			mediaPlayer.release();

			try {
				songPosition = (songPosition + 1) % toPlay.size();
				songToPlay = toPlay.get(songPosition);


				cntr_aCounter = new PlayTimer(
						songToPlay.getUserDuration() * 1000, 100, toPlay,
						songPosition, this, context, numOfIteration,fadeIn);
				cntr_aCounter.start();

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (intent.getAction()
				.equals(MusicService.BACKWARD_NOTIFICATION)) {

			cntr_aCounter.cancel();
			mediaPlayer.stop();
			mediaPlayer.release();

			try {

				songPosition = (songPosition - 1);
				if (songPosition == -1)
					songPosition = toPlay.size() - 1;

				songToPlay = toPlay.get(songPosition);
				cntr_aCounter = new PlayTimer(
						songToPlay.getUserDuration() * 1000, 100, toPlay,
						songPosition, this, context, numOfIteration,fadeIn);
				cntr_aCounter.start();
				

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

	public Integer getNumOfIteration() {
		return numOfIteration;
	}

	public void setNumOfIteration(Integer numOfIteration) {
		this.numOfIteration = numOfIteration;
	}

	public Integer getFadeIn() {
		return fadeIn;
	}

	public void setFadeIn(Integer fadeIn) {
		this.fadeIn = fadeIn;
	}
	

}
