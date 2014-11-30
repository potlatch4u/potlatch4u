package org.coursera.potlatch4u.ui.gift;

import static junit.framework.Assert.assertEquals;
import static org.coursera.potlatch4u.ui.gift.GiftListActivity.LOG_TAG;

import java.text.MessageFormat;

import org.coursera.potlatch4u.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    public static final String KEY_UPDATE_TOUCH_COUNT_INTERVAL = "pref_touch_count_update_interval";
    public static final String KEY_SHOW_REPORTED = "pref_key_show_bad_gifts";

    private String keyUpdateInterval;
    private String keyShowBadGifts;
    private Preference prefTouchedCountUpdateInterval;
    private Preference prefShowBadGifts;

    SharedPreferences sharedPreferences;

    public SettingsActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        keyUpdateInterval = getResources().getString(R.string.pref_key_touch_count_update_interval);
        keyShowBadGifts = getResources().getString(R.string.pref_key_show_bad_gifts);
        // sharedPreferences = getSharedPreferences("potlatch4u-prefs",
        // ContextWrapper.MODE_PRIVATE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // int[] values =
        // getResources().getIntArray(R.array.pref_touch_count_update_interval_value);
        // CharSequence[] display =
        // getResources().getStringArray(R.array.pref_touch_count_update_interval_value);
        // debug("list value items: ", Arrays.toString(values));
        // debug("list display items: ", Arrays.toString(display));

        assertEquals(KEY_UPDATE_TOUCH_COUNT_INTERVAL, keyUpdateInterval);
        assertEquals(KEY_SHOW_REPORTED, keyShowBadGifts);

        if (savedInstanceState == null) {
            // Load the preferences from an XML resource
            debug("Loading preference screen ...");
            addPreferencesFromResource(R.xml.user_prefs);
        }

        // Get the username Preference
        prefTouchedCountUpdateInterval = (Preference) getPreferenceManager().findPreference(keyUpdateInterval);
        prefShowBadGifts = (Preference) getPreferenceManager().findPreference(keyShowBadGifts);
        debug("Loaded preference: touch count interval: " + prefTouchedCountUpdateInterval.getKey());
        debug("Loaded preference: show reported gifts : " + prefShowBadGifts.getKey());

        // Invoke callback manually to display the current username
        onSharedPreferenceChanged(sharedPreferences, keyUpdateInterval);
        onSharedPreferenceChanged(sharedPreferences, keyShowBadGifts);
        debug("Manual invocation done.");
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_UPDATE_TOUCH_COUNT_INTERVAL)) {
            String newValue = sharedPreferences.getString(key, "none yet");
            prefTouchedCountUpdateInterval.setSummary(newValue + " seconds");
            debug("PREF {0} has changed, new value: {1}.", key, newValue);
        } else if (key.equals(KEY_SHOW_REPORTED)) {
            debug("PREF {0} has changed, new value: {1}.", key, sharedPreferences.getBoolean(key, true));
        }
    }

    private void debug(String msg, Object... parameters) {
        Log.d(LOG_TAG, this.getClass().getSimpleName() + ": " + MessageFormat.format(msg, parameters));
    }

}
