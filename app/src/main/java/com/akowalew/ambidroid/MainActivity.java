package com.akowalew.ambidroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Creating...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        registerBroadcastReceiver();
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
        stopProjection();
        unregisterBroadcastReceiver();
        super.onDestroy();
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

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch(action != null ? action : "") {
                case AmbilightService.START_ACTION:
                    onProjectionStart();
                    break;
                case AmbilightService.STOP_ACTION:
                    onProjectionStop();
                    break;
                default:
                    Log.v(TAG, "Received unsupported action: '" + action + "'");
                    break;
            }
        }
    };

    private void startSettingsActivity() {
        Log.v(TAG, "Starting SettingsActivity...");
        final Context context = this;
        final Intent settings = new Intent(context, SettingsActivity.class);
        startActivity(settings);
    }

    private void startProjection() {
        Log.v(TAG, "Starting projection...");
        final Intent startIntent = new Intent(this, AmbilightService.class);
        startIntent.setAction(AmbilightService.START_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(startIntent);
        } else {
            startService(startIntent);
        }
    }

    private void stopProjection() {
        Log.v(TAG, "Stopping projection...");
        final Intent stopIntent = new Intent(this, AmbilightService.class);
        stopIntent.setAction(AmbilightService.STOP_ACTION);
        startService(stopIntent);
    }

    private void onProjectionStart() {
        Log.v(TAG, "Projection started");
        Toast.makeText(this, "Projection started", Toast.LENGTH_LONG).show();
    }

    private void onProjectionStop() {
        Log.v(TAG, "Projection stopped");
        Toast.makeText(this, "Projection stopped", Toast.LENGTH_LONG).show();
        findViewById(R.id.fab).setEnabled(true);
    }

    private void registerBroadcastReceiver() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AmbilightService.START_ACTION);
        intentFilter.addAction(AmbilightService.STOP_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }
}
