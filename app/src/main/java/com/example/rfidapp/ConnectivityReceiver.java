package com.example.rfidapp;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectionReceiverListener connectionReceiverListener;

    ConnectivityManager connectivityManager;

    public ConnectivityReceiver() {
        super();
    }
boolean isConnected;
    NetworkInfo networkInfo;
    AlertDialog.Builder builder;
    private AlertDialog alertDialog;



    @Override
    public void onReceive(final Context context, Intent intent) {

        connectivityManager =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null)
        {
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null &&     networkInfo.isConnected())
            {

                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }

                 isConnected = networkInfo != null
                        && networkInfo.isConnectedOrConnecting();
                if (connectionReceiverListener != null) {

                    connectionReceiverListener.onNetworkConnectionChanged(isConnected);
                }

            }
            else {
                builder = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.internet_screen,null);
                builder.setView(view);
                builder.setCancelable(false);
                builder.create();
                alertDialog =  builder.show();

                view.findViewById(R.id.internetsetting).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(Settings.ACTION_SETTINGS));

                    }
                });



            }
        }
    }
    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) TestApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null
                && activeNetwork.isConnected();
    }
    public interface ConnectionReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

}