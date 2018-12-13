package com.akowalew.ambidroid;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class AmbilightService extends Service {
    public static final String START_ACTION = "com.akowalew.ambidroid.AmbilightService.START_ACTION";
    public static final String STOP_ACTION = "com.akowalew.ambidroid.AmbilightService.STOP_ACTION";
    public static final String CHANNEL_ID = "com.example.ambidroid.AmbilightService.CHANNEL_ID";

    private static final String TAG = "AmbilightService";
    private static final int IMPORTANCE = NotificationManagerCompat.IMPORTANCE_LOW;

    private NotificationManager mNotificationManager = null;
    private int mNotificationId = 1;
    private int mLastNotificationId = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Activity.NOTIFICATION_SERVICE);
        Log.v(TAG, "Created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String action = intent.getAction();
        switch (action != null ? action : "") {
            case START_ACTION:
                onStartAction();
                sendActionBroadcast(START_ACTION);
                break;
            case STOP_ACTION:
                onStopAction();
                sendActionBroadcast(STOP_ACTION);
                break;
            default:
                Log.v(TAG, "Received unsupported action: '" + action + "'");
                throw new IllegalArgumentException();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Bound services not used
    }

    private void onStartAction() {
        Log.v(TAG, "Starting...");
        final Context context = this;
        final Intent intent = new Intent(context, AmbilightService.class)
            .setAction(STOP_ACTION);
        final PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        final Notification notification = getNotificationBuilder(context, CHANNEL_ID, IMPORTANCE)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Projection in progress...")
            .setContentText("Tap to stop")
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .build();

        assert mNotificationId != 0;
        startForeground(mNotificationId, notification);
        if (mNotificationId != mLastNotificationId) {
            mNotificationManager.cancel(mLastNotificationId);
            mLastNotificationId = mNotificationId;
        }
    }

    private void onStopAction() {
        Log.v(TAG, "Stopping...");
        stopForeground(STOP_FOREGROUND_REMOVE);
        stopSelf();
    }

    private void sendActionBroadcast(String action) {
        final Intent broadcast = new Intent();
        broadcast.setAction(action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
    }

    private static NotificationCompat.Builder getNotificationBuilder(Context context, String channelId, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            prepareChannel(context, channelId, importance);
        }
        return new NotificationCompat.Builder(context, channelId);
    }

    @TargetApi(26)
    private static void prepareChannel(Context context, String id, int importance) {
        final String appName = context.getString(R.string.app_name);
        final String description = "Channel description";
        final NotificationManager nm = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
        if(nm != null) {
            NotificationChannel nChannel = nm.getNotificationChannel(id);
            if (nChannel == null) {
                nChannel = new NotificationChannel(id, appName, importance);
                nChannel.setDescription(description);
                nm.createNotificationChannel(nChannel);
            }
        }
    }

}
