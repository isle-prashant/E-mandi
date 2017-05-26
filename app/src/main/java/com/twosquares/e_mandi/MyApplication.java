package com.twosquares.e_mandi;

import android.app.Application;
import android.content.Intent;

import com.pushbots.push.Pushbots;

/**
 * Created by prkumar on 5/16/2017.
 */

public class MyApplication extends Application {

    public static boolean openSplash = true;
    @Override
    public void onCreate() {
        super.onCreate();
//        // Initialize Pushbots Library
        Pushbots.sharedInstance().init(this);
      /*  if (openSplash) {
            openSplash = false;
            startActivity(new Intent(this, SplashScreen.class));
        }*/
    }
}