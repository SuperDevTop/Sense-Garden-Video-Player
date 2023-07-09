package com.sensegarden.sensegardenplaydev.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefSingleton {

    private static PrefSingleton mInstance;
    private Context mContext;

    private SharedPreferences mMyPreferences;

    private PrefSingleton() {
    }

    public static PrefSingleton getInstance() {
        if (mInstance == null) mInstance = new PrefSingleton();
        return mInstance;
    }

    public void Initialize(Context ctxt) {
        mContext = ctxt;
        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putString(key, value);
        e.apply();
    }

    public String getString(String key) {
        return mMyPreferences.getString(key, "");
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putInt(key, value);
        e.apply();
    }

    public int getInt(String key) {
        return mMyPreferences.getInt(key, 0);
    }

    public void saveBool(String key, boolean value) {
        SharedPreferences.Editor e = mMyPreferences.edit();
        e.putBoolean(key, value);
        e.apply();
    }

    public Boolean getBool(String key) {
        return mMyPreferences.getBoolean(key, false);
    }

}
