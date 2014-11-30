

package org.coursera.potlatch4u.ui.gift;

import org.coursera.potlatch4u.R;
import org.coursera.potlatch4u.service.RefreshCacheService;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * Base class for all GiftData activities
 * 
 * @author Michael A. Walker
 * 
 */
public class GiftActivityBase extends FragmentActivity implements
		OnOpenWindowInterface {

	boolean promptOnBackPressed = false;
	GiftListFragment fragment;
	private static final String LOG_TAG = GiftActivityBase.class
			.getCanonicalName();
	boolean mDualPane;

	@Override
	/**
	 * Handle when the back button is pressed. Overridden to require
	 * confirmation of wanting to exit via back button. 
	 * This functionality can easily be removed.
	 */
	public void onBackPressed() {
		if (promptOnBackPressed == true) {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Closing Activity")
					.setMessage("Are you sure you want to close this activity?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}

							}).setNegativeButton("No", null).show();
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * Determine if the device this app is running on is a tablet or a phone.
	 * 
	 * <p>
	 * phones generally have less than 600dp (this is the threshold we use, this
	 * can be adjusted)
	 * 
	 * @return boolean truth
	 */
	private boolean determineDualPane() {
		if (getResources().getBoolean(R.bool.isTablet) == true) {
			mDualPane = true;
			return true;
		} else {
			mDualPane = false;
			return false;
		}
	}

	/**
	 * Logic required to open the appropriate View GiftData Fragment/Activity
	 * combination to display properly on the phone or tablet.
	 */
	public void openViewGiftFragment(long index) {
		Log.d(LOG_TAG, "openGiftViewFragment(" + index + ")");
        initiateCacheRefresh();
		if (determineDualPane()) {

			Fragment test = getSupportFragmentManager().findFragmentById(
					R.id.details);

			// Log.d(LOG_TAG, "open view class:" + test.getClass());
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			if (test != null && test.getClass() != GiftViewFragment.class) {
				GiftViewFragment details = GiftViewFragment
						.newInstance(index);

				// Execute a transaction, replacing any existing
				// fragment with this one inside the frame.
				ft.replace(R.id.details, details);

			} else {
				// Check what fragment is shown, replace if needed.
				GiftViewFragment details = (GiftViewFragment) getSupportFragmentManager()
						.findFragmentById(R.id.details);
				if (details == null || details.getUniqueKey() != index) {
					// Make new fragment to show this selection.
					details = GiftViewFragment.newInstance(index);

				}
				// Execute a transaction, replacing any existing
				// fragment with this one inside the frame.

				ft.replace(R.id.details, details);

			}
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();

		} else {
			// Otherwise we need to launch a new activity to display
			// the dialog fragment with selected text.
			Intent intent = newGiftViewIntent(this, index);
			startActivity(intent);
		}
	}

	/**
	 * Logic required to open the appropriate Create GiftData Fragment/Activity
	 * combination to display properly on the phone or tablet.
	 */
	public void openCreateGiftFragment() {
		Log.d(LOG_TAG, "openCreateGiftFragment");
		if (determineDualPane()) {

			Fragment test = getSupportFragmentManager().findFragmentById(
					R.id.details);

			// Log.d(LOG_TAG, "open view class:" + test.getClass());
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			if (test != null && test.getClass() != CreateGiftFragment.class) {
				CreateGiftFragment details = CreateGiftFragment.newInstance();

				// Execute a transaction, replacing any existing
				// fragment with this one inside the frame.

				ft.replace(R.id.details, details);

			} else {
				// Check what fragment is shown, replace if needed.
				CreateGiftFragment details = (CreateGiftFragment) getSupportFragmentManager()
						.findFragmentById(R.id.details);
				if (details == null) {
					// Make new fragment to show this selection.
					details = CreateGiftFragment.newInstance();

				}
				// Execute a transaction, replacing any existing
				// fragment with this one inside the frame.

				ft.replace(R.id.details, details);

			}
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();

		} else {
			// Otherwise we need to launch a new activity to display
			// the dialog fragment with selected text.
			Intent intent = newCreateGiftIntent(this);
			startActivity(intent);
		}
	}

	@Override
	public void openListGiftFragment() {
		Log.d(LOG_TAG, "openCreateGiftFragment");
		if (determineDualPane()) {
			// already displayed
			Fragment test = getSupportFragmentManager().findFragmentByTag(
					"imageFragmentTag");
			if (test != null) {
				GiftListFragment t = (GiftListFragment) test;
				t.updateGiftData();
			}

		} else {
			// Otherwise we need to launch a new activity to display
			// the dialog fragment with selected text.
			Intent intent = newListGiftIntent(this);
			startActivity(intent);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (determineDualPane()) {
			getSupportFragmentManager().findFragmentById(R.id.details)
					.onActivityResult(requestCode, resultCode, data);
		} else {
			getSupportFragmentManager().findFragmentById(android.R.id.content)
					.onActivityResult(requestCode, resultCode, data);
		}

	}

	/*************************************************************************/
	/*
	 * Create Intents for Intents
	 */
	/*************************************************************************/
	// TODO: for extensibility: replace by register-approach, sub-classes not to
	// be mentioned here

	public static Intent newGiftViewIntent(Activity activity, long index) {
		Intent intent = new Intent();
		intent.setClass(activity, GiftViewActivity.class);
		intent.putExtra(GiftViewFragment.rowIdentifyerTAG, index);
		return intent;
	}

	public static Intent newListGiftIntent(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, GiftListActivity.class);
		return intent;
	}

	public static Intent newCreateGiftIntent(Activity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, CreateGiftActivity.class);
		return intent;
	}

    private void initiateCacheRefresh() {
        startService(RefreshCacheService.makeIntent(this));
    }
}
