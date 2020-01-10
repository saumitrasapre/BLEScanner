package com.example.blescanner;

import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import com.google.zxing.Result;

import android.content.Intent;
import android.os.Bundle;

public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    ZXingScannerView ScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView=new ZXingScannerView(this);

        setContentView(ScannerView);
    }

    @Override
    public void handleResult(Result result)
    {
        MainActivity.plan_link=result.getText().toString();
        startActivity(new Intent(getApplicationContext(),PositioningActivity.class));
        //onBackPressed();
    }

    @Override
    protected void onPause()
    {

        super.onPause();

        ScannerView.stopCamera();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }
}
