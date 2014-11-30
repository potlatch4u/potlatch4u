package org.coursera.potlatch4u.ui.gift;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Button;

/**
 * BaseGiftFragment - common functions for all potlatch fragments
 * 
 * @author nbischof
 * @since Nov 28, 2014
 * 
 */
public class BaseGiftFragment extends Fragment {

    private Button buttonCreate;
    private Button buttonGifts;
    private Button buttonSettings;
    private Button buttonTopGifts;

    private final ButtonBarSupport buttonBar;

    public BaseGiftFragment() {
        super();
        buttonBar = new ButtonBarSupport(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        buttonCreate = buttonBar.getCreateButton();
        buttonGifts = buttonBar.getGiftsButton();
        buttonSettings = buttonBar.getSettingsButton();
        buttonTopGifts = buttonBar.getTopGiftButton();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

}
