package com.example.phihai.ShutPhone.KioskMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.phihai.ShutPhone.Activities.MainActivity;

import java.util.concurrent.TimeUnit;

/**
 * Created by Phi Hai on 10.10.2017.
 */

public class KioskService extends Service {
    private static final long INTERVAL = TimeUnit.SECONDS.toMillis(2); // periodic interval to check in seconds -> 2 seconds
    private static final String TAG = KioskService.class.getSimpleName();

    private Thread t = null;
    private Context ctx = null;
    private boolean running = false;
    private boolean isInBackground = false;


    @Override
    public void onDestroy() {
        Log.i(TAG, "Stopping service 'KioskService'");
        running =false;
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Starting service 'KioskService'");
        running = true;
        ctx = this;

        PackageReceiver packageReceiver = new PackageReceiver();
        IntentFilter intentFilter = new IntentFilter("PACKAGE_NAME");
        registerReceiver(packageReceiver, intentFilter);

        // start a thread that periodically checks if your app is in the foreground
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                do {
                    handleKioskMode();
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        Log.i(TAG, "Thread interrupted: 'KioskService'");
                    }
                }while(running);
                stopSelf();
            }
        });

        t.start();
        return Service.START_STICKY;
    }

    private void handleKioskMode() {
        // is Kiosk Mode active?
        if(PrefUtils.isKioskModeActive(this)) {
            // is App in background?
            if(isInBackground) {
                restoreApp(); // restore!
                isInBackground = false;
            }
        }
    }

   // Restart activity
    private void restoreApp() {
        //PrefUtils.setKioskModeActive(false, getApplicationContext());
        Intent i = new Intent(ctx, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, i, 0);

        try {
            pendingIntent.send();
            ctx.startActivity(i);
        } catch (PendingIntent.CanceledException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public class PackageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getStringExtra("packageName") != null) {
                if(!ctx.getApplicationContext().getPackageName().equals(intent.getStringExtra("packageName"))) {
                    isInBackground = true;
                }
            }
        }
    }
}
