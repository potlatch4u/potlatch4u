package org.coursera.potlatch4u.ui.gift;

import org.coursera.potlatch4u.R;
import org.coursera.potlatch4u.orm.FormerResolver;
import org.coursera.potlatch4u.orm.GiftData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GiftViewFragment extends BaseGiftFragment {

    // private static final String LOG_TAG = GiftViewFragment.class
    // .getCanonicalName();
    private static final String LOG_TAG = GiftListActivity.LOG_TAG;

    private FormerResolver resolver;
    public final static String rowIdentifyerTAG = "index";

    private OnOpenWindowInterface mOpener;

    GiftData giftData;

    TextView titleTV;
    TextView descriptionTV;
    TextView touchedCountTV;
    ImageView imageView;

    // buttons for edit and delete
    Button touchedButton;
    Button reportButton;
    Button deleteButton;

    OnClickListener myOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
            case R.id.button_gift_view_to_delete:
                deleteButtonPressed();
                break;
            case R.id.button_gift_view_touched:
                touchedButtonPressed();
                break;
            case R.id.button_gift_view_report:
                reportInappropriateButtonPressed();
                break;
            default:
                break;
            }
        }

        private void touchedButtonPressed() {
            // TODO Auto-generated method stub

        }

        private void reportInappropriateButtonPressed() {
            // TODO Auto-generated method stub

        }
    };

    public static GiftViewFragment newInstance(long index) {
        GiftViewFragment f = new GiftViewFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putLong(rowIdentifyerTAG, index);
        f.setArguments(args);

        return f;
    }

    // this fragment was attached to an activity

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOpener = (OnOpenWindowInterface) activity;
            resolver = new FormerResolver(activity);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnOpenWindowListener");
        }
    }

    // this fragment is creating its view before it can be modified
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gift_view_fragment, container, false);
        container.setBackgroundColor(Color.GRAY);
        return view;
    }

    // this fragment is modifying its view before display
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        titleTV = (TextView) getView().findViewById(R.id.gift_view_value_title);
        descriptionTV = (TextView) getView().findViewById(R.id.gift_view_value_description);
        touchedCountTV = (TextView) getView().findViewById(R.id.gift_view_value_touched_count);
        imageView = (ImageView) getView().findViewById(R.id.gift_view_value_image);

        titleTV.setText("" + "");
        descriptionTV.setText("" + "");
        touchedCountTV.setText("");

        touchedButton = (Button) getView().findViewById(R.id.button_gift_view_touched);
        reportButton = (Button) getView().findViewById(R.id.button_gift_view_report);
        deleteButton = (Button) getView().findViewById(R.id.button_gift_view_to_delete);

        touchedButton.setOnClickListener(myOnClickListener);
        reportButton.setOnClickListener(myOnClickListener);
        deleteButton.setOnClickListener(myOnClickListener);

        try {
            setUiToGiftData(getUniqueKey());
        } catch (RemoteException e) {
            Toast.makeText(getActivity(), "Error retrieving information from local data store.", Toast.LENGTH_LONG)
                    .show();
            Log.e(LOG_TAG, "Error getting Gift data from C.P.");
            // e.printStackTrace();
        }
    }

    public void setUiToGiftData(long getUniqueKey) throws RemoteException {
        Log.d(LOG_TAG, "setUiToGiftData");
        giftData = resolver.getGift(getUniqueKey);
        if (giftData == null) {
            getView().setVisibility(View.GONE);
        } else { // else it just displays empty screen
            Log.d(LOG_TAG, "setUiToGiftData + giftData:" + giftData.toString());
            titleTV.setText(String.valueOf(giftData.title).toString());
            descriptionTV.setText(String.valueOf(giftData.description).toString());

            // Display the image data

            String imageUri = String.valueOf(giftData.imageUri).toString();

            if (imageUri != null) {
                showImage(imageUri);
            }

            touchedCountTV.setText(getTouchedCountDisplayValue(giftData.touchCount));
        }
    }

    private String getTouchedCountDisplayValue(long touchedCount) {
        String text;
        if (touchedCount == 0) {
            text = "Gift has not touched any users yet.";
        } else if (touchedCount == 1) {
            text = "Gift has not touched one user.";
        } else {
            text = "Gift has not touched " + touchedCount + " users.";
        }
        return text;
    }

    private void showImage(String imageMetaDataPath) {
        Uri imageUri = Uri.parse(imageMetaDataPath);
        // TDO - Set the URI of the ImageView to the image path stored in
        // the string
        // imageMetaDataPath, using the setImageURI function from the ImageView
        Log.d(LOG_TAG, "Loading image from " + imageUri);
        imageView.setImageURI(imageUri);
    }

    // action to be performed when the delete button is pressed
    private void deleteButtonPressed() {
        String message;

        message = getResources().getString(R.string.gift_view_deletion_dialog_message);

        new AlertDialog.Builder(getActivity()).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.gift_view_deletion_dialog_title).setMessage(message)
                .setPositiveButton(R.string.gift_view_deletion_dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            resolver.deleteGift(giftData.id);
                        } catch (RemoteException e) {
                            Log.e(LOG_TAG, "RemoteException Caught => " + e.getMessage());
                            e.printStackTrace();
                        }
                        mOpener.openListGiftFragment();
                        if (getResources().getBoolean(R.bool.isTablet) == true) {
                            mOpener.openViewGiftFragment(-1);
                        } else {
                            getActivity().finish();
                        }
                    }

                }).setNegativeButton(R.string.gift_view_deletion_dialog_no, null).show();
    }

    public long getUniqueKey() {
        return getArguments().getLong(rowIdentifyerTAG, 0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOpener = null;
        resolver = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            setUiToGiftData(getUniqueKey());
        } catch (RemoteException e) {
            Toast.makeText(getActivity(), "Error retrieving information from local data store.", Toast.LENGTH_LONG)
                    .show();
            Log.e(LOG_TAG, "Error getting Gift data from C.P.");
        }
    }
}
