package it.lma5.incorporesound;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

public class PlaylistActivityReceiver extends BroadcastReceiver {

	private Activity playActivity;
	private Button btPlaySong;
	private Button btStopSong;
	private Button btBackwardSong;
	private Button btForwardSong;
	private Button btPauseSong;
	
	
	
	public PlaylistActivityReceiver(Activity playActivity) {
		
		this.playActivity = playActivity;
		btBackwardSong=(Button)playActivity.findViewById(R.id.btBackwardSong);
		btForwardSong=(Button)playActivity.findViewById(R.id.btForwardSong);
		btPauseSong=(Button)playActivity.findViewById(R.id.btPauseSong);
		btPlaySong=(Button)playActivity.findViewById(R.id.btPlaySong);
		btStopSong=(Button)playActivity.findViewById(R.id.btStopSong);
	}



	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equals(PlayActivity.STOP_PLAYLIST_NOTIFICATION)) {
			btBackwardSong.setEnabled(false);
			btForwardSong.setEnabled(false);
			btPauseSong.setEnabled(false);
			btStopSong.setEnabled(false);
			btPlaySong.setEnabled(true);
			
			
			
		}

	}

}
