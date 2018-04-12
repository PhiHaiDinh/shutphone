package com.example.phihai.ShutPhone.KioskMode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.phihai.ShutPhone.Activities.MainActivity;

/**
 * Created by Phi Hai on 10.10.2017.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }
}
