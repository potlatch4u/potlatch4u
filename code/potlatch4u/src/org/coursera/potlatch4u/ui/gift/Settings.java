package org.coursera.potlatch4u.ui.gift;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Settings - static helpers to access settings for this projects
 * 
 * @author nbischof
 * @since Nov 28, 2014
 * 
 */
public class Settings {
    private static final long TEN_MINUTES = 10 * 60 * 1000;

    private Settings() {
    }

    public static long getTouchCountUpdateIntervalMillis(Context ctx) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String updateIntervalInMilliseconds = sharedPref.getString(SettingsActivity.KEY_UPDATE_TOUCH_COUNT_INTERVAL,
                String.valueOf(TEN_MINUTES));
        return Long.valueOf(updateIntervalInMilliseconds);
    }

    public static boolean getShowInappropriateGifts(Context ctx) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String showInappropriateGifts = sharedPref.getString(SettingsActivity.KEY_UPDATE_TOUCH_COUNT_INTERVAL, "false");
        return Boolean.valueOf(showInappropriateGifts);
    }

}
