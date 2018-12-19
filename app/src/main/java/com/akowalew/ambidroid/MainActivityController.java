package com.akowalew.ambidroid;

import android.app.PendingIntent;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class MainActivityController {
    private static final String TAG = "MainActivityController";

    private final @NonNull MainActivity mView;
    private final @NonNull UsbManager mUsbManager;

    private ProjectionDevice mProjectionDevice = null;
    private UsbDeviceConnection mUsbDeviceConnection = null;

    public MainActivityController(final @NonNull MainActivity view, final @NonNull UsbManager usbManager) {
        mView = view;
        mUsbManager = usbManager;
    }

    public void onDestroy() {
        if(mProjectionDevice != null) {
            mProjectionDevice.close();
            mProjectionDevice = null;
        }

        if(mUsbDeviceConnection != null) {
            mUsbDeviceConnection.close();
            mUsbDeviceConnection = null;
        }
    }

    public void startProjection() {
        if(mProjectionDevice != null) { throw new AssertionError(); }

        final @NonNull List<ProjectionDevice> projectionDevices = probeForProjectionDevices();
        if(projectionDevices.size() == 0) {
            mView.showMessage("No valid projection devices detected");
            return;
        }

        final ProjectionDevice projectionDevice = mView.selectProjectionDevice(projectionDevices);
        if(projectionDevice == null) {
            mView.showMessage("Rejected to select projection device");
            return;
        }

        mProjectionDevice = projectionDevice;
        requestUsbDevicePermission(projectionDevice.getUsbDevice());
    }

    public void onPermissionResult(final @NonNull Intent intent) {
        final String action = intent.getAction();
        if(action == null || !action.equals(MainActivity.ACTION_USB_PERMISSION)) {
            Log.d(TAG, "Got unknown action");
            return;
        }

        onUsbPermissionResult(intent);
    }

    public void onUsbPermissionResult(final @NonNull Intent intent) {
        final @NonNull UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
        final boolean permissionGranted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);
        if(!permissionGranted) {
            mView.showMessage("You have not granted permission to open the projector device");
            return;
        }

        openUsbDevice(usbDevice);
    }

    private @NonNull List<ProjectionDevice> probeForProjectionDevices() {
        Log.d(TAG, "Probing for projection devices…");
        final @NonNull ProjectionDeviceProber projectionDeviceProber = ProjectionDeviceProber.getDefaultProber();
        final @NonNull List<ProjectionDevice> projectionDevices = projectionDeviceProber.findAllDevices(mUsbManager);
        return projectionDevices;
    }

    private void requestUsbDevicePermission(final @NonNull UsbDevice usbDevice) {
        Log.d(TAG, "Requesting permission for selected usb device…");
        final @NonNull Intent intent = new Intent(MainActivity.ACTION_USB_PERMISSION);
        final @NonNull PendingIntent pendingIntent = PendingIntent.getBroadcast(mView, 0, intent, 0);
        mUsbManager.requestPermission(usbDevice, pendingIntent);
    }

    private void openUsbDevice(final @NonNull UsbDevice usbDevice) {
        if(!mUsbManager.hasPermission(usbDevice)) { throw new AssertionError(); }

        Log.d(TAG, "Opening selected usb device…");
        final UsbDeviceConnection usbDeviceConnection = mUsbManager.openDevice(usbDevice);
        if(usbDeviceConnection == null) {
            mView.showMessage("Could not open usb device");
            return;
        }

        mUsbDeviceConnection = usbDeviceConnection;
        openProjectionDevice();
    }

    private void openProjectionDevice() {
        if(mProjectionDevice == null) { throw new AssertionError(); }
        if(mUsbDeviceConnection == null) { throw new AssertionError(); }

        try {
            mProjectionDevice.open(mUsbDeviceConnection);
        } catch (IOException e) {
            mView.showMessage("Could not open projection device");
            mUsbDeviceConnection.close();
            mUsbDeviceConnection = null;
            return;
        }

        mView.showMessage("Successfully opened projection device");
        fillWithColor();
    }

    private void fillWithColor() {
        Log.d(TAG, "Filling with color…");
        mProjectionDevice.setPixelColor(0, (byte)10, (byte)0, (byte)0);
        try {
            mProjectionDevice.update();
        } catch (IOException e) {
            e.printStackTrace();
            mView.showMessage("Projection device failed");
        }
    }
}
