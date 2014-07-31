package it.lma5.incorporesound.Receivers;

import it.lma5.incorporesound.R;
import it.lma5.incorporesound.Activities.PlayActivity;
import it.lma5.incorporesound.Services.MusicService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

/**
 * Update notification bar when song is stopped or played.
 * @author Andrea Di Lonardo, Luca Fanelli, Michele Coppola
 */
public class NotificationReceiver extends BroadcastReceiver {

	private PlayActivity playActivity;

	private Button btPlaySong;
	private Button btPauseSong;
	private Button btForwardSong;
	private Button btBackwardSong;

	public static String NOTIFICATION_PAUSE = "it.lma5.incorporesound.NotificationReceiver.notification_pause";
	public static String NOTIFICATION_PLAY = "it.lma5.incorporesound.NotificationReceiver.notification_play";

	public NotificationReceiver(PlayActivity playActivity) {

		this.playActivity = playActivity;
		btPlaySong = playActivity.getBtPlaySong();
		btPauseSong = playActivity.getBtPauseSong();
		btForwardSong = playActivity.getBtForwardSong();
		btBackwardSong = playActivity.getBtBackwardSong();

	}

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(NOTIFICATION_PLAY)) {
			
			String artist,title;
			artist=intent.getStringExtra("artist");
			title=intent.getStringExtra("title");

			btBackwardSong.setEnabled(true);
			btForwardSong.setEnabled(true);
			btPauseSong.setEnabled(true);
			btPlaySong.setEnabled(false);

			Intent intentAct = new Intent(playActivity, PlayActivity.class);
			PendingIntent pIntentAct = PendingIntent.getActivity(playActivity,
					0, intentAct, 0);

			Intent iStop = new Intent(PlayActivity.CLOSE_SERVICE_NOTIFICATION);
			PendingIntent pStop = PendingIntent.getBroadcast(
					playActivity.getApplicationContext(), 0, iStop, 0);

			Intent iForward = new Intent(MusicService.FORWARD_NOTIFICATION);
			PendingIntent pForward = PendingIntent.getBroadcast(
					playActivity.getApplicationContext(), 0, iForward, 0);

			Intent iBackward = new Intent(MusicService.BACKWARD_NOTIFICATION);
			PendingIntent pBackward = PendingIntent.getBroadcast(
					playActivity.getApplicationContext(), 0, iBackward, 0);

			Intent iPause = new Intent(MusicService.PAUSE_NOTIFICATION);
			PendingIntent pPause = PendingIntent.getBroadcast(
					playActivity.getApplicationContext(), 0, iPause, 0);

			Intent iPlay = new Intent(MusicService.PLAY_NOTIFICATION);
			PendingIntent pPlay = PendingIntent.getBroadcast(
					playActivity.getApplicationContext(), 0, iPlay, 0);

			// Create remote view and set bigContentView.
			RemoteViews expandedView = new RemoteViews(
					playActivity.getPackageName(), R.layout.notification_layout);

			expandedView.setOnClickPendingIntent(R.id.btNotificationForward,
					pForward);
			expandedView.setOnClickPendingIntent(R.id.btNotificationBackward,
					pBackward);
			expandedView.setOnClickPendingIntent(R.id.btNotificationpause,
					pPause);
			expandedView
					.setOnClickPendingIntent(R.id.btNotificationPlay, pPlay);

			expandedView.setTextViewText(R.id.tvNotificationPlaylistName,
					playActivity.getPlaylist().getName());

			expandedView.setViewVisibility(R.id.btNotificationPlay, View.GONE);
			expandedView.setViewVisibility(R.id.btNotificationpause,
					View.VISIBLE);
			
			expandedView.setTextViewText(R.id.tvNotificationSongTitle, title);
			expandedView.setTextViewText(R.id.tvNotificationArtist, artist);

			// build notification
			// the addAction re-use the same intent to keep the example short

			Notification notification = new Notification.Builder(playActivity)
					.setContentTitle("PROVA").setContentText("Subject")
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentIntent(pIntentAct).setAutoCancel(false)
					.setContent(expandedView).setDeleteIntent(pStop).build();
			notification.bigContentView = expandedView;

			NotificationManager notificationManager = (NotificationManager) playActivity
					.getSystemService(PlayActivity.NOTIFICATION_SERVICE);
			notificationManager.notify(0, notification);

		} else if (intent.getAction().equals(NOTIFICATION_PAUSE)) {
			
			String artist,title;
			artist=intent.getStringExtra("artist");
			title=intent.getStringExtra("title");

			btBackwardSong.setEnabled(false);
			btForwardSong.setEnabled(false);
			btPauseSong.setEnabled(false);
			btPlaySong.setEnabled(true);

			Intent intentAct = new Intent(playActivity, PlayActivity.class);
			PendingIntent pIntentAct = PendingIntent.getActivity(playActivity,
					0, intentAct, 0);

			Intent iStop = new Intent(PlayActivity.CLOSE_SERVICE_NOTIFICATION);
			PendingIntent pStop = PendingIntent.getBroadcast(
					playActivity.getApplicationContext(), 0, iStop, 0);

			Intent iForward = new Intent(MusicService.FORWARD_NOTIFICATION);
			PendingIntent pForward = PendingIntent.getBroadcast(
					playActivity.getApplicationContext(), 0, iForward, 0);

			Intent iBackward = new Intent(MusicService.BACKWARD_NOTIFICATION);
			PendingIntent pBackward = PendingIntent.getBroadcast(
					playActivity.getApplicationContext(), 0, iBackward, 0);

			Intent iPause = new Intent(MusicService.PAUSE_NOTIFICATION);
			PendingIntent pPause = PendingIntent.getBroadcast(
					playActivity.getApplicationContext(), 0, iPause, 0);

			Intent iPlay = new Intent(MusicService.PLAY_NOTIFICATION);
			PendingIntent pPlay = PendingIntent.getBroadcast(
					playActivity.getApplicationContext(), 0, iPlay, 0);

			// Create remote view and set bigContentView.
			RemoteViews expandedView = new RemoteViews(
					playActivity.getPackageName(), R.layout.notification_layout);

			expandedView.setOnClickPendingIntent(R.id.btNotificationForward,
					pForward);
			expandedView.setOnClickPendingIntent(R.id.btNotificationBackward,
					pBackward);
			expandedView.setOnClickPendingIntent(R.id.btNotificationpause,
					pPause);
			expandedView
					.setOnClickPendingIntent(R.id.btNotificationPlay, pPlay);

			expandedView.setTextViewText(R.id.tvNotificationPlaylistName,
					playActivity.getPlaylist().getName());

			expandedView.setViewVisibility(R.id.btNotificationPlay,
					View.VISIBLE);
			expandedView.setViewVisibility(R.id.btNotificationpause, View.GONE);
			
			expandedView.setTextViewText(R.id.tvNotificationSongTitle, title);
			expandedView.setTextViewText(R.id.tvNotificationArtist, artist);

			// build notification
			// the addAction re-use the same intent to keep the example short

			Notification notification = new Notification.Builder(playActivity)
					.setContentTitle("PROVA").setContentText("Subject")
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentIntent(pIntentAct).setAutoCancel(false)
					.setContent(expandedView).setDeleteIntent(pStop).build();
			notification.bigContentView = expandedView;

			NotificationManager notificationManager = (NotificationManager) playActivity
					.getSystemService(PlayActivity.NOTIFICATION_SERVICE);
			notificationManager.notify(0, notification);

		}

	}

}
