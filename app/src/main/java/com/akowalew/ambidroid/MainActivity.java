package com.akowalew.ambidroid;

import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton powerButton = findViewById(R.id.powerButton);
        powerButton.setScaleType(ImageView.ScaleType.CENTER);
        FloatingActionButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setScaleType(ImageView.ScaleType.CENTER);
    }

    public void onPowerButtonClick(View view) {

    }

    public void onSettingsButtonClick(View view) {
        final Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onAboutButtonClick(View view) {

    }

    public void onShareButtonClick(View view) {

    }
}

