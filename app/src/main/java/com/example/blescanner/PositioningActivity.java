package com.example.blescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class PositioningActivity extends AppCompatActivity {

    public static ImageView b2,b4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positioning);


        ImageView floorimg = (ImageView) findViewById(R.id.floorimg);
        b2=(ImageView)findViewById(R.id.b2);
        b4=(ImageView)findViewById(R.id.b4);



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
           // Log.d("Dis Array : ", "[ " + String.valueOf(distArray[1]) + String.valueOf(distArray[2]) + String.valueOf(distArray[3]) + "]");

//            if(distArray[1]==1 && blinkflag==false) {
//                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim);
//                b2.startAnimation(animation);
//                blinkflag=true;
//            }
//            else if(distArray[1]==0 && blinkflag==true)
//            {
//                b2.clearAnimation();
//                blinkflag=false;
//            }




        }
    }



}
