package com.example.blescanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.nexenio.bleindoorpositioning.IndoorPositioning;
import com.nexenio.bleindoorpositioning.ble.advertising.AdvertisingPacket;
import com.nexenio.bleindoorpositioning.ble.advertising.IndoorPositioningAdvertisingPacket;
import com.nexenio.bleindoorpositioning.ble.beacon.Beacon;
import com.nexenio.bleindoorpositioning.ble.beacon.BeaconManager;
import com.nexenio.bleindoorpositioning.ble.beacon.IBeacon;
import com.nexenio.bleindoorpositioning.location.Location;
import com.nexenio.bleindoorpositioning.location.provider.IBeaconLocationProvider;

import org.altbeacon.beacon.distance.AndroidModel;
import org.altbeacon.beacon.distance.DistanceCalculator;
import org.altbeacon.beacon.distance.ModelSpecificDistanceCalculator;

import java.util.List;

import static java.lang.Math.pow;


public class BTLE_Scan {

    MainActivity main;
    private BluetoothAdapter myAdapter;
    private BluetoothLeScanner myscanner;
    private boolean scanState;
    private Handler myhandler;
    private int signalStrength;
    private long scanPeriod;

    public BTLE_Scan(MainActivity main, long scanPeriod, int signalStrength) {
        this.main = main;
        this.signalStrength = signalStrength;
        this.scanPeriod = scanPeriod;
        myhandler = new Handler();

        final BluetoothManager bluetoothManager = (BluetoothManager) main.getSystemService(Context.BLUETOOTH_SERVICE);

        myAdapter = bluetoothManager.getAdapter();
        myscanner = myAdapter.getBluetoothLeScanner();

    }

    public boolean isScanning() {
        return scanState;
    }

    public void start() {
        if (!Utils.checkBluetooth(myAdapter)) {
            Utils.requestUserBluetooth(main);
            main.stopScan();
        } else {
            scanLEDevice(true);
        }
    }

    public void stop() {
        scanLEDevice(false);
    }

    private void scanLEDevice(final boolean enable) {
        if (enable == true && scanState == false) {
            Utils.toast(main.getApplicationContext(), "Starting BLE Scan...");

            myhandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Utils.toast(main.getApplicationContext(), "Stopping BLE Scan...");
                    scanState = false;
                    //myAdapter.stopLeScan(myLEScanCallback);
                    myscanner.stopScan(myLEScanCallback);
                    main.stopScan();

                }
            }, scanPeriod);

            scanState = true;
            myscanner.startScan(myLEScanCallback);

        }
    }

    private ScanCallback myLEScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);
            final int new_rssi = result.getRssi();
            myhandler.post(new Runnable() {
                @Override
                public void run() {

                    processScanResult(result);
                    System.out.println(result.getDevice().getAddress());
                    main.addDevice(result.getDevice(), new_rssi);


                }
            });

        }


        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };

    private void processScanResult(@NonNull ScanResult scanResult) {
        String macAddress = scanResult.getDevice().getAddress();
        byte[] data = scanResult.getScanRecord().getBytes();
        ScanRecord record=scanResult.getScanRecord();
        int txPowerlevel=record.getTxPowerLevel();
        int rssi=scanResult.getRssi();
        AndroidModel model=AndroidModel.forThisDevice();
        //ModelSpecificDistanceCalculator myobj=new ModelSpecificDistanceCalculator(main.getApplicationContext(),null,model);
     double n = 2.25;
     double distance;

     distance=  Math.pow(10.0, ((double)txPowerlevel - rssi) / (10 * 2));
//        double distance=0;
//        if (rssi == 0 || txPowerlevel == 0) {
//            distance = -1;
//        }
//        double ratio2 = txPowerlevel - rssi;
//        double ratio2_linear = Math.pow(10, ratio2 / 10);
//        double y = 0;
//         double r = Math.sqrt(ratio2_linear);
//
//        double ratio = rssi * 1.0 / txPowerlevel;
//        if (ratio < 1.0)
//        {
//            y = Math.pow(ratio, 10);
//        }
//        else
//        {
//            y = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
//            distance= y*10;
//       }

//        myobj.calculateDistance(txPowerlevel,rssi);
        //if(scanResult.getDevice().getName()=="Adi") {
            Log.d("Distance ", "ADDRESS  " + scanResult.getDevice().getName() + " distance is  " + String.valueOf(distance));
       // }





    }

  }