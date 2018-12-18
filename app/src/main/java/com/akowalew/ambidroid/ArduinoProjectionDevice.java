package com.akowalew.ambidroid;

import android.support.annotation.NonNull;

import com.hoho.android.usbserial.driver.UsbSerialDriver;

public final class ArduinoProjectionDevice implements ProjectionDevice {
    private static final String TAG = "ArduinoProjectionDevice";

    private final @NonNull UsbSerialDriver mUsbSerialDriver;

    public ArduinoProjectionDevice(final @NonNull UsbSerialDriver usbSerialDriver) {
        mUsbSerialDriver = usbSerialDriver;
    }
}
