package com.linmama.dinning.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by jingkang on 2017/3/13
 */

public class BTDeviceBean {
    private BluetoothDevice device;
    private boolean bonded;

    public BTDeviceBean(BluetoothDevice device, boolean bonded) {
        this.device = device;
        this.bonded = bonded;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public boolean isBonded() {
        return bonded;
    }

    public void setBonded(boolean bonded) {
        this.bonded = bonded;
    }
}
