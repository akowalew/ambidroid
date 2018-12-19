package com.akowalew.ambidroid;

import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;

import java.io.IOException;

public final class ArduinoProjectionDevice implements ProjectionDevice {
    private static final String TAG = "ArduinoProjectionDevice";

    private static final int BAUD_RATE = 57600;
    private static final int DATA_BITS = 8;
    private static final int STOP_BITS = UsbSerialPort.STOPBITS_1;
    private static final int PARITY = UsbSerialPort.PARITY_EVEN;

    private static final int PIXELS_HORIZONTAL = 8;
    private static final int PIXELS_VERTICAL = 16;
    private static final int PIXEL_COUNT = (PIXELS_VERTICAL + PIXELS_HORIZONTAL) * 2;
    private static final int COMPONENT_COUNT = 3; // R-G-B
    private static final int BUFFER_LENGTH = (PIXEL_COUNT * COMPONENT_COUNT);

    private final @NonNull UsbSerialDriver mUsbSerialDriver;
    private final @NonNull UsbSerialPort mUsbSerialPort;
    private byte[] mBuffer = null;

    public ArduinoProjectionDevice(final @NonNull UsbSerialDriver usbSerialDriver) {
        mUsbSerialDriver = usbSerialDriver;

        final UsbSerialPort port = mUsbSerialDriver.getPorts().get(0);
        if(port == null) {
            throw new RuntimeException("Could not open serial port 0");
        }

        mUsbSerialPort = port;
    }

    @Override
    public void open(@NonNull UsbDeviceConnection usbDeviceConnection) throws IOException {
        try {
            mUsbSerialPort.open(usbDeviceConnection);
            mUsbSerialPort.setParameters(BAUD_RATE, DATA_BITS, STOP_BITS, PARITY);
        } catch (IOException e) {
            Log.d(TAG, "Could not configure device: " + e.getMessage());
            throw e;
        }

        mBuffer = new byte[BUFFER_LENGTH];
    }

    @Override
    public void close() {
        try {
            mUsbSerialPort.close();
        } catch (IOException e) {
            Log.d(TAG, "Error occurred during closing port: " + e.getMessage());
        }

        mBuffer = null;
    }

    @Override
    public void fillWithColor(byte red, byte green, byte blue) {
        if(mBuffer == null) { throw new AssertionError(); }
        if(COMPONENT_COUNT != 3) { throw new AssertionError(); }

        for(int i = 0; i < BUFFER_LENGTH; i += COMPONENT_COUNT) {
            mBuffer[i] = green;
            mBuffer[i+1] = red;
            mBuffer[i+2] = blue;
        }
    }

    public void setPixelColor(int index, byte red, byte green, byte blue) {
        if(mBuffer == null) { throw new AssertionError(); }
        if(COMPONENT_COUNT != 3) { throw new AssertionError(); }

        final int offset = (index * COMPONENT_COUNT);
        mBuffer[offset] = green;
        mBuffer[offset+1] = red;
        mBuffer[offset+2] = blue;
    }

    @Override
    public void update() throws IOException {
        if(mBuffer == null) { throw new AssertionError(); }

        mUsbSerialPort.write(mBuffer, BUFFER_LENGTH);
    }

    @NonNull @Override
    public UsbDevice getUsbDevice() {
        final UsbDevice usbDevice = mUsbSerialDriver.getDevice();
        if(usbDevice == null) {
            throw new AssertionError();
        }
        return usbDevice;
    }
}
