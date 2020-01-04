package com.example.blescanner;

import android.bluetooth.BluetoothDevice;

import com.nexenio.bleindoorpositioning.ble.beacon.Beacon;

public class BTLE_Device {

    private BluetoothDevice bluetoothDevice;
    private Beacon beacon;
    private int rssi;
    private  int distance;

    public BTLE_Device(BluetoothDevice bluetoothDevice,Beacon beacon) {
        this.bluetoothDevice = bluetoothDevice;
        this.beacon=beacon;
    }

    public String getAddress() {
        return bluetoothDevice.getAddress();
    }

    public String getName() {
        return bluetoothDevice.getName();
    }

    public void setRSSI(int rssi) {
        this.rssi = rssi;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public  int getDistance()
  {
      return beacon.getCalibratedDistance();
  }

    public  void setDistance()
    {
        distance=beacon.getCalibratedDistance();
    }

    public int getRSSI() {
        return rssi;
    }


}
