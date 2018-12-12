package com.akowalew.ambidroid;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class AmbilightService extends IntentService {
    public static final String RESPONSE = "com.akowalew.ambidroid.AmbilightService.PROJECTION_FINISHED";

    private static final String TAG = "AmbilightService";

    public AmbilightService() {
        super("AmbilightService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        Log.v(TAG, "Running...");
        SystemClock.sleep(5000);

        Intent broadcast = new Intent();
        broadcast.setAction(RESPONSE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);

        Log.v(TAG, "Finished");
    }
}
