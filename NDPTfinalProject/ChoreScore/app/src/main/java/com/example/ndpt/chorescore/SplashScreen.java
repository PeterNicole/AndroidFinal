package com.example.ndpt.chorescore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * SplashScreen.java
 * Created by Nicole Dahlquist on 11/11/2015.
 *
 * Class for displaying the splash screen
 */
public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            setContentView(R.layout.splash);

            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            };
            timerThread.start();
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        try {
            // TODO Auto-generated method stub
            super.onPause();
            finish();
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

}
