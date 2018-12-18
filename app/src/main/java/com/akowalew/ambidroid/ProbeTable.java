package com.akowalew.ambidroid;

import android.util.Pair;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProbeTable {
    private final Map<Pair<Integer, Integer>, Class<? extends ProjectionDevice>> mDeviceMap = new LinkedHashMap<>();

    public ProbeTable addDevice(final int vendorId, final int productId, final Class<? extends  ProjectionDevice> device) {
        final Pair<Integer, Integer> key = new Pair<>(vendorId, productId);
        mDeviceMap.put(key, device);
        return this;
    }

    public Class<? extends ProjectionDevice> findDevice(final int vendorId, final int productId) {
        final Pair<Integer, Integer> key = new Pair<>(vendorId, productId);
        return mDeviceMap.get(key);
    }
}
