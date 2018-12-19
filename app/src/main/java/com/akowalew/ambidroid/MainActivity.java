package com.akowalew.ambidroid;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public static final String ACTION_USB_PERMISSION = "com.akowalew.ambidroid.USB_PERMISSION";

    private MainActivityController controller;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "Getting UsbManager service…");
        final UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        if(usbManager == null) { throw new AssertionError(); }

        controller = new MainActivityController(this, usbManager);

        final FloatingActionButton powerButton = findViewById(R.id.powerButton);
        if(powerButton == null) { throw new AssertionError(); }
        powerButton.setScaleType(ImageView.ScaleType.CENTER);

        final FloatingActionButton settingsButton = findViewById(R.id.settingsButton);
        if(settingsButton == null) { throw new AssertionError(); }
        settingsButton.setScaleType(ImageView.ScaleType.CENTER);

        final @NonNull IntentFilter intentFilter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        controller.onDestroy();
        unregisterReceiver(mUsbReceiver);
        super.onDestroy();
    }

    public void showMessage(final @NonNull String message) {
        final @NonNull Context context = this;
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public ProjectionDevice selectProjectionDevice(final @NonNull List<ProjectionDevice> projectionDevices) {
        Log.d(TAG, "Selecting the device…");
        final ProjectionDevice projectionDevice = projectionDevices.get(0);
        if(projectionDevice == null) { throw new AssertionError(); }
        return projectionDevice;
    }

    public void onPowerButtonClick(final View view) {
        controller.startProjection();
    }

    public void onSettingsButtonClick(final View view) {
        final Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onAboutButtonClick(final View view) {

    }

    public void onShareButtonClick(final View view) {

    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final @NonNull Context context, final @NonNull Intent intent) {
            controller.onPermissionResult(intent);
        }
    };
}

