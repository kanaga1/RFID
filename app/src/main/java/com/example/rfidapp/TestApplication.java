package com.example.rfidapp;

import android.app.Application;

public class TestApplication extends Application {

    private static TestApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized TestApplication getInstance() {
        return mInstance;
    }

    public void setConnectionListener(ConnectivityReceiver.ConnectionReceiverListener listener) {
        ConnectivityReceiver.connectionReceiverListener = listener;
    }
}