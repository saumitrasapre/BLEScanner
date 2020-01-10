package com.example.blescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PositioningActivity extends AppCompatActivity {

    static public ImageView b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positioning);

        ImageView floorimg = (ImageView) findViewById(R.id.floorimg);
        b2 = (ImageView)findViewById(R.id.b2);


        if (!MainActivity.plan_link.equals(null)) {
            System.out.println(MainActivity.plan_link);
            Picasso.get().load(MainActivity.plan_link).into(floorimg);

            Utils.toast(getApplicationContext(), "Scan Button Pressed");
            if(MainActivity.myScanner.isScanning()==false)
            {
                MainActivity.startScan();

            }
            else
            {
                MainActivity.stopScan();
            }



        }
    }


}
