package com.akowalew.ambidroid;

import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.support.annotation.NonNull;

import java.io.IOException;

public interface ProjectionDevice {

    void open(final @NonNull UsbDeviceConnection usbDeviceConnection) throws IOException;

    void close();

    void fillWithColor(byte red, byte green, byte blue);

    void setPixelColor(int index, byte red, byte green, byte blue);

    void update() throws IOException;

    @NonNull UsbDevice getUsbDevice();
}
