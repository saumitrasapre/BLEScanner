package com.example.blescanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;

import com.nexenio.bleindoorpositioning.ble.advertising.AdvertisingPacket;
import com.nexenio.bleindoorpositioning.ble.advertising.AdvertisingPacketFactory;
import com.nexenio.bleindoorpositioning.ble.advertising.AdvertisingPacketFactoryManager;
import com.nexenio.bleindoorpositioning.ble.beacon.Beacon;
import com.nexenio.bleindoorpositioning.ble.beacon.BeaconManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.telephony.SignalStrength;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.nexenio.bleindoorpositioning.ble.beacon.BeaconManager;
import com.nexenio.bleindoorpositioning.ble.beacon.BeaconUpdateListener;
import com.nexenio.bleindoorpositioning.location.provider.BeaconLocationProvider;

import java.util.List;


public class BTLE_Scan {

    private  MainActivity main;
    private BluetoothAdapter myAdapter;

    BluetoothLeScanner bluetoothScanner;
    private boolean isScanning;
    private Handler handler;
    private long scanPeriod;
    private  int signalStrength;


    public BTLE_Scan(MainActivity main, long scanPeriod, int signalStrength) {
        this.main = main;
        this.scanPeriod = scanPeriod;
        this.signalStrength = signalStrength;

        handler=new Handler();
        BluetoothManager bluetoothManager= (BluetoothManager) main.getSystemService(Context.BLUETOOTH_SERVICE);
        myAdapter=bluetoothManager.getAdapter();
        bluetoothScanner=myAdapter.getBluetoothLeScanner();
    }

    public  boolean scanState()
    {
        return isScanning;
    }

    public void startScanning()
    {
        if(!Utils.checkBluetooth(myAdapter))
        {
            Utils.requestUserBluetooth(main);
            main.stopScan();
        }
        else if(Utils.checkBluetooth(myAdapter))
        {
            scanLEDevice(true);
        }
    }

    public void stop()
    {
        scanLEDevice(false);
    }
    private void  scanLEDevice(final boolean enable)
    {
        if(enable==true && isScanning==false) {
            Utils.toast(main.getApplicationContext(), "Starting BLE scan..");

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.toast(main.getApplicationContext(),"Stopping BLE scan...");
                    isScanning=false;
                    bluetoothScanner.startScan(myLeScanCallback);
                    main.stopScan();
                }
            },scanPeriod);


            //Staring the scanning

            isScanning=true;


            bluetoothScanner.startScan(myLeScanCallback);
            //myAdapter.startLeScan(myLeScanCallback);
        }
    }




//    private BluetoothAdapter.LeScanCallback myLeScanCallback= new BluetoothAdapter.LeScanCallback() {
//      @Override
//      public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
//
//            final int new_rssi=rssi;
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        main.addDevice(device,new_rssi);
//                    }
//                });
//       }

    private ScanCallback myLeScanCallback=new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);
            final int new_rssi=result.getRssi();
            handler.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    Beacon dummyBeacon = processScanResult(result);
                    if(dummyBeacon != null)
                        main.addDevice(result.getDevice(),dummyBeacon,new_rssi);

                }
            });




            BeaconManager.registerBeaconUpdateListener(new BeaconUpdateListener() {
                @Override
                public void onBeaconUpdated(final Beacon beacon) {
                    // have fun with your beacon!
                    //Log.d("Beacon Update",String.valueOf(beacon.getCalibratedDistance()));
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



    private Beacon processScanResult(ScanResult scanResult) {
        String macAddress = scanResult.getDevice().getAddress();
        byte[] advertisingData = scanResult.getScanRecord().getBytes();

        int rssi = scanResult.getRssi();
        if(BeaconManager.getInstance().getAdvertisingPacketFactoryManager().createAdvertisingPacket(advertisingData) == null)
            return null;
        return BeaconManager.getBeacon(macAddress ,BeaconManager.processAdvertisingData(macAddress, advertisingData, rssi));
    }




}