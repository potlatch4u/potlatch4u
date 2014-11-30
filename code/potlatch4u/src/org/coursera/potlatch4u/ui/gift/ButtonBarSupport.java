package org.coursera.potlatch4u.ui.gift;

import static java.text.MessageFormat.format;
import static org.coursera.potlatch4u.ui.gift.GiftListActivity.LOG_TAG;
import static org.coursera.potlatch4u.ui.gift.Settings.getShowInappropriateGifts;
import static org.coursera.potlatch4u.ui.gift.Settings.getTouchCountUpdateIntervalMillis;

import org.coursera.potlatch4u.R;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ButtonBarSupport {
    private final Fragment fragment;

    public ButtonBarSupport(Fragment fragment) {
        super();
        this.fragment = fragment;
    }

    Button getGiftsButton() {
        return getAndRegisterButton(R.id.button_gifts_and_chains, new ShowGiftsClickListener());
    }

    Button getSettingsButton() {
        return getAndRegisterButton(R.id.button_settings, new SettingsClickListener());
    }

    Button getTopGiftButton() {
        return getAndRegisterButton(R.id.button_top_gifts, new TopGiftsClickListener());
    }

    Button getCreateButton() {
        return getAndRegisterButton(R.id.button_create_gift, new CreateGiftClickListener());
    }

    Button getAndRegisterButton(int resourceid, OnClickListener listener) {
        Button button = (Button) fragment.getView().findViewById(resourceid);
        button.setOnClickListener(listener);
        return button;
    }

    private void buttonCreateClicked(View v) {
        // TODO Auto-generated method stub
        debug("button CREATE-GIFT clicked from {0}.\nupdate-interval: {1}, show reported gifts: {2}", v,
                getTouchCountUpdateIntervalMillis(fragment.getActivity()),
                getShowInappropriateGifts(fragment.getActivity()));

    }

    private void buttonShowGiftsClicked(View v) {
        // TODO Auto-generated method stub
        debug("button SHOW-GIFTS clicked from " + v);

    }

    private void buttonSettingsClicked(View v) {
        debug("starting settings intent from " + v);
        fragment.startActivity(new Intent(fragment.getActivity(), SettingsActivity.class));
    }

    private void buttonTopGiftsClicked(View v) {
        // TODO Auto-generated method stub
        debug("button TOP-GIFTS clicked from " + v);

    }

    private final class CreateGiftClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            buttonCreateClicked(v);
        }
    }

    private final class ShowGiftsClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            buttonShowGiftsClicked(v);
        }
    }

    private final class SettingsClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            buttonSettingsClicked(v);
        }
    }

    private final class TopGiftsClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            buttonTopGiftsClicked(v);
        }
    }

    private void debug(String msg, Object... parameters) {
        String format = parameters != null && parameters.length > 0 ? format(msg, parameters) : msg;
        Log.d(LOG_TAG, this.getClass().getSimpleName() + ": " + format);
    }

}
