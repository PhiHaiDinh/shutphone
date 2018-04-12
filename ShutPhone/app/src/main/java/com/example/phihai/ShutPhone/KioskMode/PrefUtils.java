package com.example.phihai.ShutPhone.KioskMode;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Phi Hai on 10.10.2017.
 */

public class PrefUtils {
    private static final String PREF_KIOSK_MODE = "pref_kiosk_mode";


    public static boolean isKioskModeActive(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        if (sp.getBoolean(PREF_KIOSK_MODE, false)) return true;
        else return false;
    }

    public static void setKioskModeActive(final boolean active, final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_KIOSK_MODE, active).commit();
    }
}
