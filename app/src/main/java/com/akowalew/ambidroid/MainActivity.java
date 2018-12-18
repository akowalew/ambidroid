package com.akowalew.ambidroid;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
}

