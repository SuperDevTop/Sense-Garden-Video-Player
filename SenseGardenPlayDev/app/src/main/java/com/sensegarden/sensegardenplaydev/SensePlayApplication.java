package com.sensegarden.sensegardenplaydev;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.sensegarden.sensegardenplaydev.utils.PrefSingleton;

public class SensePlayApplication extends Application {
    public static SensePlayApplication instance;

    public SensePlayApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Log.d("TAG_ANDREA", "INITI");
        PrefSingleton.getInstance().Initialize(this);
        FirebaseApp.initializeApp(this);
    }
}
