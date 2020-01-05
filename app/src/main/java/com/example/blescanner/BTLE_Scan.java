package com.example.blescanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.nexenio.bleindoorpositioning.IndoorPositioning;
import com.nexenio.bleindoorpositioning.ble.advertising.AdvertisingPacket;
import com.nexenio.bleindoorpositioning.ble.advertising.IndoorPositioningAdvertisingPacket;
import com.nexenio.bleindoorpositioning.ble.beacon.Beacon;
import com.nexenio.bleindoorpositioning.ble.beacon.BeaconManager;
import com.nexenio.bleindoorpositioning.ble.beacon.IBeacon;
import com.nexenio.bleindoorpositioning.location.Location;
import com.nexenio.bleindoorpositioning.location.provider.IBeaconLocationProvider;

import java.util.List;


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
        AdvertisingPacket advertisingPacket = BeaconManager.processAdvertisingData(macAddress, data, scanResult.getRssi());


        if (advertisingPacket != null) {
            AdvertisingPacket advertisingPacket1=new IndoorPositioningAdvertisingPacket(data);
            advertisingPacket1.setRssi(scanResult.getRssi());
            Beacon beacon = BeaconManager.getBeacon(macAddress,BeaconManager.processAdvertisingPacket(macAddress,advertisingPacket1));
            System.out.println("Distance: "+beacon.getMacAddress());


//            if (beacon instanceof IBeacon && !beacon.hasLocation()) {
//                beacon.setLocationProvider(createDebuggingLocationProvider((IBeacon) beacon));
//            }
        }


    }

  /*  private static IBeaconLocationProvider<IBeacon> createDebuggingLocationProvider(IBeacon iBeacon) {
        final Location beaconLocation = new Location();
        switch (iBeacon.getMinor()) {
            case 1: {
                beaconLocation.setLatitude(52.512437);
                beaconLocation.setLongitude(13.391124);
                beaconLocation.setAltitude(36);
                break;
            }
            case 2: {
                beaconLocation.setLatitude(52.512411788476356);
                beaconLocation.setLongitude(13.390875654442985);
                beaconLocation.setElevation(2.65);
                beaconLocation.setAltitude(36);
                break;
            }
            case 3: {
                beaconLocation.setLatitude(52.51240486636751);
                beaconLocation.setLongitude(13.390770270005437);
                beaconLocation.setElevation(2.65);
                beaconLocation.setAltitude(36);
                break;
            }
            case 4: {
                beaconLocation.setLatitude(52.512426);
                beaconLocation.setLongitude(13.390887);
                beaconLocation.setElevation(2);
                beaconLocation.setAltitude(36);
                break;
            }
            case 5: {
                beaconLocation.setLatitude(52.512347534813834);
                beaconLocation.setLongitude(13.390780437281524);
                beaconLocation.setElevation(2.9);
                beaconLocation.setAltitude(36);
                break;
            }
            case 12: {
                beaconLocation.setLatitude(52.51239708899507);
                beaconLocation.setLongitude(13.390878261276518);
                beaconLocation.setElevation(2.65);
                beaconLocation.setAltitude(36);
                break;
            }
            case 13: {
                beaconLocation.setLatitude(52.51242692608082);
                beaconLocation.setLongitude(13.390872969910035);
                beaconLocation.setElevation(2.65);
                beaconLocation.setAltitude(36);
                break;
            }
            case 14: {
                beaconLocation.setLatitude(52.51240825552749);
                beaconLocation.setLongitude(13.390821867681456);
                beaconLocation.setElevation(2.65);
                beaconLocation.setAltitude(36);
                break;
            }
            case 15: {
                beaconLocation.setLatitude(52.51240194910502);
                beaconLocation.setLongitude(13.390725856632926);
                beaconLocation.setElevation(2.65);
                beaconLocation.setAltitude(36);
                break;
            }
            case 16: {
                beaconLocation.setLatitude(52.512390301005595);
                beaconLocation.setLongitude(13.39077285305359);
                beaconLocation.setElevation(2.65);
                beaconLocation.setAltitude(36);
                break;
            }
            case 17: {
                beaconLocation.setLatitude(52.51241817994876);
                beaconLocation.setLongitude(13.390767908948872);
                beaconLocation.setElevation(2.65);
                beaconLocation.setAltitude(36);
                break;
            }
            case 18: {
                beaconLocation.setLatitude(52.51241494408066);
                beaconLocation.setLongitude(13.390923696709294);
                beaconLocation.setElevation(2.65);
                beaconLocation.setAltitude(36);
                break;
            }
        }
        return new IBeaconLocationProvider<IBeacon>(iBeacon) {
            @Override
            protected void updateLocation() {
                this.location = beaconLocation;
            }

            @Override
            protected boolean canUpdateLocation() {
                return true;
            }
        };
    }

   */
}
