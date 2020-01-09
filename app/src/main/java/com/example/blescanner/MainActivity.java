package com.example.blescanner;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;


import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_ENABLE_BT = 1;

    private HashMap<String, BTLE_Device> deviceHashMap;
    private ArrayList<BTLE_Device> deviceList;
    private BTLE_ListAdapter adapter;

    private  BTLE_Scan myScanner;

    private Button btn_Scan;

    private BroadcastReceiver_BTState BTonoffreceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Utils.toast(getApplicationContext(), "BLE not supported");
            finish();
        }

        BTonoffreceiver = new BroadcastReceiver_BTState(getApplicationContext());
        deviceHashMap = new HashMap<>();
        deviceList = new ArrayList<>();
        adapter = new BTLE_ListAdapter(this, R.layout.device_list_item, deviceList);
        myScanner=new BTLE_Scan(this,Integer.MAX_VALUE,-120);

        ListView listView = new ListView(getApplicationContext());
        listView.setAdapter(adapter);
        //listView.setOnItemClickListener(this);
        ((ScrollView) findViewById(R.id.scrollView)).addView(listView);

        registerReceiver(BTonoffreceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

        btn_Scan = (Button) findViewById(R.id.btn_scan);
        btn_Scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toast(getApplicationContext(), "Scan Button Pressed");
                if(myScanner.isScanning()==false)
                {
                    startScan();
                }
                else
                {
                    stopScan();
                }
            }
        });






    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Utils.toast(getApplicationContext(), "Bluetooth turned on");
            }
            else if (resultCode == RESULT_CANCELED) {
                Utils.toast(getApplicationContext(), "Failed to turn on Bluetooth");
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(BTonoffreceiver);
    }

    public void addDevice(BluetoothDevice device, int new_rssi, int[] distArray) {

        String address=device.getAddress();
        if(!deviceHashMap.containsKey(address))
        {
            BTLE_Device newDevice=new BTLE_Device(device);
            newDevice.setRSSI(new_rssi);
            newDevice.setDistArray(distArray);
            System.out.println(newDevice.getAddress());



            deviceHashMap.put(address,newDevice);
            deviceList.add(newDevice);
        }
        else
        {
            deviceHashMap.get(address).setRSSI(new_rssi);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScan();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopScan();
    }

    public void startScan()
    {

        btn_Scan.setText("Scanning...");
        deviceList.clear();
        deviceHashMap.clear();
        adapter.notifyDataSetChanged();
        myScanner.start();
    }

    public void stopScan() {

        btn_Scan.setText("Scan Again...");
        myScanner.stop();

    }
}
