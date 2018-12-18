package com.akowalew.ambidroid;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FloatingActionButton powerButton = findViewById(R.id.powerButton);
        powerButton.setScaleType(ImageView.ScaleType.CENTER);
        final FloatingActionButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setScaleType(ImageView.ScaleType.CENTER);
    }

    public void onPowerButtonClick(final View view) {
        final UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        if(usbManager == null) {
            throw new AssertionError();
        }

        final ProjectionDeviceProber projectionDeviceProber = ProjectionDeviceProber.getDefaultProber();
        final List<ProjectionDevice> projectionDevices = projectionDeviceProber.findAllDevices(usbManager);
        if(projectionDevices.size() == 0) {
            Toast.makeText(this, "No valid devices detected", Toast.LENGTH_LONG).show();
            return;
        } else if(projectionDevices.size() > 1) {
            throw new UnsupportedOperationException();
        }

        Toast.makeText(this, "Found device", Toast.LENGTH_LONG).show();
    }

    public void onSettingsButtonClick(final View view) {
        final Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onAboutButtonClick(final View view) {

    }

    public void onShareButtonClick(final View view) {

    }
}

