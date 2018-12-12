package com.akowalew.ambidroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private class ResponseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(AmbilightService.RESPONSE.equals(action)) {
                onProjectionFinish();
            }
        }
    }

    private static final String TAG = "MainActivity";
    private Intent mAmbilightService = null;
    private ResponseReceiver mResponseReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Creating...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerBroadcastReceiver();
        setupToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.v(TAG, "Started");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.v(TAG, "Resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.v(TAG, "Paused");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.v(TAG, "Stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterBroadcastReceiver();
        stopProjection();
        Log.v(TAG, "Destroyed");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_settings) {
            startSettingsActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onFloatingActionButtonClick(View view) {
        startProjection();
        view.setEnabled(false);
    }

    private void setupToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void startSettingsActivity() {
        Log.v(TAG, "Starting SettingsActivity...");
        final Context context = this;
        final Intent settings = new Intent(context, SettingsActivity.class);
        startActivity(settings);
    }

    private void startProjection() {
        if(mAmbilightService == null) {
            Log.v(TAG, "Starting projection...");

            mAmbilightService = new Intent(this, AmbilightService.class);
            startService(mAmbilightService);
        }
    }

    private void stopProjection() {
        if(mAmbilightService != null) {
            Log.v(TAG, "Stopping projection...");

            stopService(mAmbilightService);
            mAmbilightService = null;
        }
    }

    private void onProjectionFinish() {
        Log.v(TAG, "Projection finished");
        mAmbilightService = null;
        findViewById(R.id.fab).setEnabled(true);
    }

    private void registerBroadcastReceiver() {
        assert mResponseReceiver == null;

        Log.v(TAG, "Registering broadcast receiver...");
        final IntentFilter intentFilter = new IntentFilter(AmbilightService.RESPONSE);
        mResponseReceiver = new ResponseReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mResponseReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver() {
        if(mResponseReceiver != null)
        {
            Log.v(TAG, "Unregistering broadcast receiver...");
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mResponseReceiver);
            mResponseReceiver = null;
        }
    }
}
