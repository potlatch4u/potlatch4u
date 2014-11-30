

package org.coursera.potlatch4u.ui.gift;

import android.os.Bundle;

/**
 * Fragments require a Container Activity, this is the one for the View
 * GiftData
 * 
 */
public class GiftViewActivity extends GiftActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			// During initial setup, plug in the details fragment.
			long index = getIntent().getExtras().getLong(
					GiftViewFragment.rowIdentifyerTAG);

			GiftViewFragment details = GiftViewFragment.newInstance(index);

			details.setArguments(getIntent().getExtras());

			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, details).commit();
		}
	}
}
