package it.lma5.incorporesound;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MusicServiceReceiver extends BroadcastReceiver {
	
	

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Bundle bundle = arg1.getExtras();

		if (bundle != null) {

			if (arg1.getAction().equals(MusicService.PLAY_NOTIFICATION)) {
				

			} else if (arg1.getAction().equals(MusicService.STOP_NOTIFICATION)) {
				

			} else if (arg1.getAction().equals(MusicService.PAUSE_NOTIFICATION)) {
				

			} else if (arg1.getAction().equals(
					MusicService.FORWARD_NOTIFICATION)) {
				

			} else if (arg1.getAction().equals(
					MusicService.BACKWARD_NOTIFICATION)) {

			}
		}
	}
}
