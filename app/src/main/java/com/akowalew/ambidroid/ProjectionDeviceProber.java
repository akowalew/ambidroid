package com.akowalew.ambidroid;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hoho.android.usbserial.driver.FtdiSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ProjectionDeviceProber {
    private static final String TAG = "ProjectionDeviceProber";

    private @NonNull ProbeTable mProbeTable;

    public ProjectionDeviceProber(final @NonNull ProbeTable probeTable) {
        mProbeTable = probeTable;
    }

    public @NonNull static ProbeTable getDefaultProbeTable() {
        final ProbeTable probeTable = new ProbeTable();
        probeTable.addDevice(1027, 24577, ArduinoProjectionDevice.class);
        return probeTable;
    }

    @NonNull
    public static ProjectionDeviceProber getDefaultProber() {
        final @NonNull ProbeTable probeTable = getDefaultProbeTable();
        return new ProjectionDeviceProber(probeTable);
    }

    List<ProjectionDevice> findAllDevices(final @NonNull UsbManager usbManager) {
        Log.d(TAG, "Finding available usb serial drivers...");
        final List<ProjectionDevice> devices = new ArrayList<>();
        final UsbSerialProber usbSerialProber = getDefaultUsbSerialProber();
        final List<UsbSerialDriver> availableDrivers = usbSerialProber.findAllDrivers(usbManager);
        for(final UsbSerialDriver usbSerialDriver : availableDrivers) {
            final UsbDevice usbDevice = usbSerialDriver.getDevice();
            final int productId = usbDevice.getProductId();
            final int vendorId = usbDevice.getVendorId();
            Log.d(TAG, "Found driver: '" + usbDevice.getDeviceName() + "', '"
                    + usbDevice.getProductName() + "', '"
                    + usbDevice.getManufacturerName() + "', "
                    + vendorId + ":" + productId);

            final ProjectionDevice device = probeDevice(usbSerialDriver);
            if(device != null) {
                devices.add(device);
            }
        }

        return devices;
    }

    private ProjectionDevice probeDevice(final @NonNull UsbSerialDriver usbSerialDriver) {
        final UsbDevice usbDevice = usbSerialDriver.getDevice();
        final int vendorId = usbDevice.getVendorId();
        final int productId = usbDevice.getProductId();
        final Class<? extends ProjectionDevice> deviceClass = mProbeTable.findDevice(vendorId, productId);
        if(deviceClass == null) {
            return null;
        }

        try {
            final Constructor<? extends ProjectionDevice> constructor = deviceClass.getConstructor(UsbSerialDriver.class);
            return constructor.newInstance(usbSerialDriver);
        } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException
                | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    private static UsbSerialProber getDefaultUsbSerialProber() {
        final com.hoho.android.usbserial.driver.ProbeTable usbSerialProbeTable = getDefaultUsbSerialProbeTable();
        return new UsbSerialProber(usbSerialProbeTable);
    }

    @NonNull
    private static com.hoho.android.usbserial.driver.ProbeTable getDefaultUsbSerialProbeTable() {
        final com.hoho.android.usbserial.driver.ProbeTable usbSerialProbeTable = new com.hoho.android.usbserial.driver.ProbeTable();
        usbSerialProbeTable.addProduct(1027, 24577, FtdiSerialDriver.class);
        return usbSerialProbeTable;
    }
}
