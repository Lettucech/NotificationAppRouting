package com.gmail.brianbridge.notificationapprouting.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gmail.brianbridge.notificationapprouting.R;
import com.gmail.brianbridge.notificationapprouting.activity.MainActivity;
import com.gmail.brianbridge.notificationapprouting.activity.SecondActivity;
import com.gmail.brianbridge.notificationapprouting.activity.StandaloneActivity;
import com.gmail.brianbridge.notificationapprouting.activity.ThirdActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
	public static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
	private static final String KEY_CODE = "code";
	private static final String KEY_URL = "url";

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		Log.d(TAG, "Notification received: " + remoteMessage.toString());
		sendNotification(remoteMessage);
	}

	private void sendNotification(RemoteMessage remoteMessage) {
		PendingIntent pendingIntent = null;

		/** In FCM 9.2.1, the map of data payloads will always be instanced.
		 * The below checking is just for safety purpose to prevent NPE and ANR */
		if (remoteMessage.getData() != null) {
			Map<String, String> data = remoteMessage.getData();
			if (data.containsKey(KEY_URL)) {
				pendingIntent = createPendingIntentWithDeepLink(data.get(KEY_URL));
			} else if (data.containsKey(KEY_CODE)){
				pendingIntent = createPendingIntentWithCode(data.get(KEY_CODE));
			}
		}

		if (pendingIntent == null) {
			pendingIntent = createPendingIntent();
		}

		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle("FCM Message")
				.setContentText(remoteMessage.getData().get("message"))
				.setSound(defaultSoundUri)
				.setContentIntent(pendingIntent);

		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
	}

	private PendingIntent createPendingIntent() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private PendingIntent createPendingIntentWithCode(String code) {
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		Intent intent;

		switch (code) {
			case "1":
				intent = new Intent(this, SecondActivity.class);
				break;
			case "2":
				intent = new Intent(this, ThirdActivity.class);
				break;
			case "3":
				intent = new Intent(this, StandaloneActivity.class);
				break;
			default:
				intent = new Intent(this, MainActivity.class);
		}

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		stackBuilder.addNextIntentWithParentStack(intent);
		return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private PendingIntent createPendingIntentWithDeepLink(String deepLink) {
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		Intent intent = new Intent(Intent.ACTION_VIEW);

		intent.setData(Uri.parse(deepLink));
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		stackBuilder.addNextIntentWithParentStack(intent);
		return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
	}
}
