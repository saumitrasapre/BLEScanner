package com.example.blescanner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nexenio.bleindoorpositioning.ble.beacon.Beacon;

import java.util.ArrayList;


public class BTLE_ListAdapter extends ArrayAdapter<BTLE_Device> {

    Activity activity;
    int layoutResourceID;
    ArrayList<BTLE_Device> devices;

    public BTLE_ListAdapter(Activity activity, int resource, ArrayList<BTLE_Device> objects) {
        super(activity.getApplicationContext(), resource, objects);

        this.activity = activity;
        layoutResourceID = resource;
        devices = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResourceID, parent, false);
        }

        BTLE_Device device=devices.get(position);
        String name=device.getName();
        String address=device.getAddress();
        int rssi=device.getRSSI();
        int distance=device.getDistance();


        TextView tv_name=convertView.findViewById(R.id.tv_name);
        TextView tv_distance=convertView.findViewById(R.id.tv_distance);

        System.out.println("Device name: " +device.getName());
        System.out.println("Device distance: " +distance);

        if (name != null) {

           // System.out.println("Device name: " +device.getName());
            tv_name.setText(device.getName());
        }
        tv_distance.setText(distance);
        TextView tv_rssi=convertView.findViewById(R.id.tv_rssi);
        tv_rssi.setText("RSSI: "+Integer.toString(rssi));

        TextView tv_macaddr=convertView.findViewById(R.id.tv_macaddr);
        if(address!=null && address.length()>0)
        {
            tv_macaddr.setText(device.getAddress());
        }
        else
        {
            tv_macaddr.setText("No Address...");
        }


        return convertView;
    }
}

