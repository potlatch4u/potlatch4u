

package org.coursera.potlatch4u.ui.gift;

import org.coursera.potlatch4u.R;
import org.coursera.potlatch4u.orm.FormerResolver;
import org.coursera.potlatch4u.orm.GiftData;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Fragments require a Container Activity, this is the one for the List
 * GiftData
 */
public class GiftListActivity extends GiftActivityBase {
	public static final String LOG_TAG = "potlatch4u";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        promptOnBackPressed = true;
        // Log.d(LOG_TAG, "initializing test data ...");
        // initTestData();
        // Log.d(LOG_TAG, "initializing test data ... done.");

        // set the Layout of main Activity.
        // (contains only the fragment holder)
        setContentView(R.layout.main);
        GiftListFragment fragment;
        String imageFragmentTag = "GiftListFragmentTag";
        if (savedInstanceState == null) {
            fragment = new GiftListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.potlatch4u_main, fragment, imageFragmentTag).commit();
        }
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            event.startTracking();
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    private void initTestData() {
        FormerResolver resolver = new FormerResolver(this);

        GiftData gift = null;
        try {
        for (int i = 1; i <= 11; i++) {
                gift = new GiftData(0, 100 + i, "Gift " + i, "Description " + i,
                        "http://icons.iconarchive.com/icons/iconleak/cerulean/128/link-icon.png");
                resolver.addGift(gift);
        }
            gift = new GiftData(0, 1, "Gift 1", "Description 1",
                    "http://icons.iconarchive.com/icons/iconleak/cerulean/96/chart-bar-chart-icon.png");
            resolver.addGift(gift);
        } catch (RemoteException e) {
            Log.wtf(LOG_TAG, "Failed add test gift data: " + gift + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}
