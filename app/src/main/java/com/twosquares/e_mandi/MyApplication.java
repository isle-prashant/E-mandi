package com.twosquares.e_mandi;

import android.app.Application;

import com.pushbots.push.Pushbots;
import com.twosquares.e_mandi.utils.UserLocalStore;

/**
 * Created by prkumar on 5/16/2017.
 */

public class MyApplication extends Application {
    public static UserLocalStore userLocalStore;

    @Override
    public void onCreate() {
        super.onCreate();
//        // Initialize Pushbots Library
        Pushbots.sharedInstance().init(this);
        userLocalStore = new UserLocalStore(this);

    }
}