package com.example.barcodewidgetf10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        hd.postDelayed(new splashHandler(), 1500);
    }

    private class splashHandler implements Runnable {
        @Override
        public void run() {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            SplashActivity.this.finish();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }
}