package com.akowalew.ambidroid;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class AmbilightService extends IntentService {
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
        Log.v(TAG, "Finished");
    }
}
