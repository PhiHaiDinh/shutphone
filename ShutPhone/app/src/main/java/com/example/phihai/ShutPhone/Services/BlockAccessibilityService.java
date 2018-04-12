package com.example.phihai.ShutPhone.Services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by Phi Hai on 02.11.2017.
 */

public class BlockAccessibilityService extends AccessibilityService {

    private static final String TAG = "BlockService";

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();

        Log.d(TAG, "service started");

        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

        config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        setServiceInfo(config);

       // blockServiceReceiver = new BlockServiceReceiver();
        //IntentFilter intentFilter = new IntentFilter();
        //intentFilter.addAction("com.example.phihai.shutapps20.BLOCK_ACCESSIBILITY_SERVICE");
      //  registerReceiver(blockServiceReceiver, intentFilter);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if(accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if(accessibilityEvent.getPackageName() != null) {
                String currentApp = accessibilityEvent.getPackageName().toString();
                Log.d(TAG, currentApp);
                Intent intent = new Intent("PACKAGE_NAME");
                intent.putExtra("packageName", currentApp);
                sendBroadcast(intent);
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
 //
    }
}
