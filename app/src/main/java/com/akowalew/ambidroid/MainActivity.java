package com.akowalew.ambidroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Intent mAmbilightService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Creating...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProjection();
            }
        });
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startSettingsActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startSettingsActivity() {
        Log.v(TAG, "Starting SettingsActivity...");
        Context context = this;
        Intent settingsActivity = new Intent(context, SettingsActivity.class);
        startActivity(settingsActivity);
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
            Log.v(TAG, "Stopping projection");

            stopService(mAmbilightService);
            mAmbilightService = null;
        }
    }
}
