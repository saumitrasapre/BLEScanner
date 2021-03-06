package com.example.blescanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;



import java.util.ArrayList;
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
   int [] distArray=new int[10];
   static int rssi1;
   static int rssi2;
   static int rssi3;
    static int rssi4;
    static  int rssi5;
    static  int rssi6;


    public BTLE_Scan(MainActivity main, long scanPeriod, int signalStrength) {
        this.main = main;
        this.signalStrength = signalStrength;
        this.scanPeriod = scanPeriod;
        myhandler = new Handler();

        distArray[0]=0;
        distArray[1]=0;
        distArray[2]=0;
        distArray[3]=0;
        distArray[4]=0;
        distArray[5]=0;
        distArray[6]=0;


        rssi1=-10;
        rssi2=-10;
        rssi3=-10;
        rssi4=-10;
        rssi5=-10;
        rssi6=-10;


        final BluetoothManager bluetoothManager = (BluetoothManager) main.getSystemService(Context.BLUETOOTH_SERVICE);

        myAdapter = bluetoothManager.getAdapter();
        myscanner = myAdapter.getBluetoothLeScanner();

    }


    ScanSettings settings = new ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(0)
            .build();
    List<ScanFilter> filters = new ArrayList<>();

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
            //myscanner.startScan(myLEScanCallback);
           myscanner.startScan(filters,settings,myLEScanCallback);

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

                    if(result.getDevice().getName()!=null) {
                        if (result.getDevice().getName().equals("Beacon1")) {
                            BTLE_Scan.rssi1 = new_rssi;
                        }
                        if (result.getDevice().getName().equals("Beacon2")) {
                            BTLE_Scan.rssi2 = new_rssi;
                        }
                        if (result.getDevice().getName().equals("Beacon3")) {
                            BTLE_Scan.rssi3 = new_rssi;
                        }
                        if (result.getDevice().getName().equals("Beacon4")) {
                            BTLE_Scan.rssi4 = new_rssi;
                        }
                        if (result.getDevice().getName().equals("Beacon5")) {
                            BTLE_Scan.rssi5 = new_rssi;
                        }
                        if (result.getDevice().getName().equals("Beacon6")) {
                            BTLE_Scan.rssi6 = new_rssi;
                        }
                    }
                int[]display=new int[10];
                   display= processScanResult(result);
                    System.out.println(result.getDevice().getAddress());
                    if (result.getDevice().getName()!=null)
                    {
                        if( result.getDevice().getName().equals("Beacon1") ||result.getDevice().getName().equals("Beacon2") ||result.getDevice().getName().equals("Beacon3")||result.getDevice().getName().equals("Beacon4")||result.getDevice().getName().equals("Beacon5")||result.getDevice().getName().equals("Beacon6")) {
                            //pos.addDevice(result.getDevice(),new_rssi,display);
                            main.addDevice(result.getDevice(), new_rssi, display);
                        }
                    }



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

    private int[] processScanResult(@NonNull ScanResult scanResult) {
        String macAddress = scanResult.getDevice().getAddress();
        byte[] data = scanResult.getScanRecord().getBytes();

        ScanRecord record=scanResult.getScanRecord();
        int txPowerlevel=record.getTxPowerLevel();

        double n = 2.25;
        //double distance;

        //distance=  Math.pow(10.0, ((double)txPowerlevel - rssi) / (10 * 2));
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
//       // if (ratio < 1.0)
//      //  {
//      //      y = Math.pow(ratio, 10);
//      //  }
//       // else
//        //{
//            y = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
            //distance= y;
      // }

//        myobj.calculateDistance(txPowerlevel,rssi);
        //if(scanResult.getDevice().getName()=="Adi") {

        // }
        int flag1=0;
        int flag2=0;
        int flag3 =0;
        int flag4=0;
        int flag5=0;
        int flag6=0;
        //String devName=scanResult.getDevice().getName();
      // if(scanResult.getDevice().getName().equals("Beacon1") ||scanResult.getDevice().getName().equals("Beacon2") ||scanResult.getDevice().getName().equals("Beacon3") ) {

            if(scanResult.getDevice().getName()!=null) {
                if (scanResult.getDevice().getName().equals("Beacon1") ) {//White Moto G4 Plus
                    if (rssi1 <= -69) {
                        flag1 = 0;//far
                    }

                    if (rssi1 > -69) {
                        flag1 = 1;
                    }

                    if (scanResult.getDevice().getName().equals("Beacon1")) {
                        distArray[1] = flag1;
                    }

                }

                if (scanResult.getDevice().getName().equals("Beacon2")) {//Black Moto G4 Plus
                    if (rssi2 <= -61) {
                        flag2 = 0;
                    }

                    if (rssi2 > -61) {
                        flag2 = 1;
                    }

                    if (scanResult.getDevice().getName().equals("Beacon2")) {
                        distArray[2] = flag2;
                    }

                }

                if (scanResult.getDevice().getName().equals("Beacon3")) {//Athang's Phone
                    if (rssi3 <= -65) {
                        flag3 = 0;
                    }

                    if (rssi3 > -65) {
                        flag3 = 1;
                    }
                    if (scanResult.getDevice().getName().equals("Beacon3")) {
                        distArray[3] = flag3;
                    }

                }

                if (scanResult.getDevice().getName().equals("Beacon4")) {//Moto G5 Plus #2
                    if (rssi4 <= -65) {
                        flag4 = 0;
                    }

                    if (rssi4 > -65) {
                        flag4 = 1;
                    }
                    if (scanResult.getDevice().getName().equals("Beacon4")) {
                        distArray[4] = flag4;
                    }

                }
                if (scanResult.getDevice().getName().equals("Beacon5")) {//Shubham's Phone
                    if (rssi5 <= -67) {
                        flag5 = 0;
                    }

                    if (rssi5 > -67) {
                        flag5 = 1;
                    }
                    if (scanResult.getDevice().getName().equals("Beacon5")) {
                        distArray[5] = flag5;
                    }

                }
                if (scanResult.getDevice().getName().equals("Beacon6")) {//Ganesh's Phone
                    if (rssi6 <= -67) {
                        flag6 = 0;
                    }

                    if (rssi6 > -67) {
                        flag6 = 1;
                    }
                    if (scanResult.getDevice().getName().equals("Beacon6")) {
                        distArray[6] = flag6;
                    }

                }
                Log.d("Dis Array : ", "[ " + String.valueOf(distArray[1]) + String.valueOf(distArray[2]) + String.valueOf(distArray[3]) + String.valueOf(distArray[4]) + String.valueOf(distArray[5])+ String.valueOf(distArray[6]) + "]");
            return distArray;
            }

             // Log.d("Dis Array : ","[ "+ rssi1 +rssi2 + rssi3 + "]");

        //}


        //   Log.d("Distance ", "ADDRESS  " + scanResult.getDevice().getName() + " distance is  " + String.valueOf(distance) + "TX Power "+String.valueOf(txPowerlevel));



        return new int[3];

    }

}