package org.coursera.potlatch4u.ui.gift;

import static java.text.MessageFormat.format;
import static org.coursera.potlatch4u.service.RefreshCacheService.makeIntent;
import static org.coursera.potlatch4u.service.RefreshResultMessageFactory.msgToString;
import static org.coursera.potlatch4u.ui.gift.GiftListActivity.LOG_TAG;

import java.util.ArrayList;

import org.coursera.potlatch4u.R;
import org.coursera.potlatch4u.orm.FormerResolver;
import org.coursera.potlatch4u.orm.GiftData;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Fragment to hold all the UI components and related Logic for Listing
 * GiftData.
 * 
 * @author Michael A. Walker
 * 
 */
public class GiftListFragment extends ListFragment {

    private Button buttonCreate;
    private Button buttonGifts;
    private Button buttonSettings;
    private Button buttonTopGifts;
    private final ButtonBarSupport buttonBar;

    // static final String LOG_TAG = GiftListFragment.class.getCanonicalName();

    OnOpenWindowInterface mOpener;
    FormerResolver resolver;
    ArrayList<GiftData> GiftData;
    private GiftDataArrayAdaptor aa;

    EditText filterET;

    public GiftListFragment() {
        super();
        buttonBar = new ButtonBarSupport(this);
    }

    /**
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        debug(this, "onAttach start");
        super.onAttach(activity);
        try {
            mOpener = (OnOpenWindowInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnOpenWindowListener" + e.getMessage());
        }
        bindRefreshService();

        debug(this, "onAttach end");
    }

    public static void debug(Object caller, String msg, Object... parameters) {
        String formattedMsg = msg;
        if (parameters.length == 1) {
            formattedMsg = format(msg, parameters[0]);
        } else if (parameters.length == 2) {
            formattedMsg = format(msg, parameters[0], parameters[1]);
        } else if (parameters.length == 3) {
            formattedMsg = format(msg, parameters[0], parameters[1], parameters[2]);
        } else if (parameters.length > 0) {
            throw new IllegalArgumentException("Too many parameters: " + parameters.length);
        }
        Log.d(LOG_TAG, caller.getClass().getSimpleName() + " [" + threadId() + "]: " + formattedMsg);
    }

    private static String threadId() {
        return Thread.currentThread().getName() + "-" + Thread.currentThread().getId();
    }

    @Override
    /**
     * @see android.support.v4.app.Fragment#onDetach()
     */
    public void onDetach() {
        super.onDetach();
        mOpener = null;
    }

    /**
     * The system calls this when creating the fragment. Within your
     * implementation, you should initialize essential components of the
     * fragment that you want to retain when the fragment is paused or stopped,
     * then resumed.
     */
    @Override
    /**
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    public void onCreate(Bundle savedInstanceState) {
        debug(this, "onCreate");
        super.onCreate(savedInstanceState);
        resolver = new FormerResolver(getActivity());
        GiftData = new ArrayList<GiftData>();
        setRetainInstance(true);
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater
     * , android.view.ViewGroup, android.os.Bundle)
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gift_listview, container, false);
        // get the ListView that will be displayed
        ListView lv = (ListView) view.findViewById(android.R.id.list);

        filterET = (EditText) view.findViewById(R.id.gift_listview_tags_filter);

        filterET.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateGiftData();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // customize the ListView in whatever desired ways.
        lv.setBackgroundColor(Color.GRAY);
        // return the parent view
        return view;
    }

    //
    // This function is called every time the filter EditText is changed
    // This function should update the ListView to match the specified
    // filter text.
    //

    public void updateGiftData() {
        debug(this, "updateGiftData");
        initiateCacheRefresh();

        try {
            GiftData.clear();

            String filterWord = filterET.getText().toString();

            ArrayList<GiftData> currentList2 = resolver.findByTitlePart(filterWord);

            GiftData.addAll(currentList2);
            aa.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error connecting to Content Provider" + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    public void onActivityCreated(Bundle savedInstanceState) {
        buttonCreate = buttonBar.getCreateButton();
        buttonGifts = buttonBar.getGiftsButton();
        buttonSettings = buttonBar.getSettingsButton();
        buttonTopGifts = buttonBar.getTopGiftButton();

        // create the custom array adapter that will make the custom row
        // layouts
        super.onActivityCreated(savedInstanceState);
        debug(this, "onActivityCreated");
        initiateCacheRefresh();
        aa = new GiftDataArrayAdaptor(getActivity(), R.layout.gift_listview_custom_row, GiftData);

        // update the back end data.
        updateGiftData();

        setListAdapter(aa);

        Button createNewButton = (Button) getView().findViewById(R.id.gift_listview_create);
        createNewButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mOpener.openCreateGiftFragment();
            }
        });
    }

    /*
     * Refresh gift list on fragment resume (rather than having to manually
     * click a refresh button) (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        updateGiftData();
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView
     * , android.view.View, int, long)
     */
    public void onListItemClick(ListView l, View v, int position, long id) {
        debug(this, "onListItemClick");
        debug(this, "position: " + position + "id = " + (GiftData.get(position)).id);
        mOpener.openViewGiftFragment((GiftData.get(position)).id);
    }

    private void initiateCacheRefresh() {
        // getActivity().startService(RefreshCacheService.makeIntent(getActivity()));

        Message request = Message.obtain();
        request.replyTo = mReplyMessenger;
        // Log.d(TAG, "mReplyMessenger = " + mReplyMessenger.hashCode());
        try {
            if (mReqMessengerRef != null) {
                debug(this, "sending message");
                // Send the request Message to the
                // UniqueIDGeneratorService.
                mReqMessengerRef.send(request);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    class ReplyHandler extends Handler {
        /**
         * Callback to handle the reply from the UniqueIDGeneratorService.
         */
        public void handleMessage(Message reply) {
            debug(this, "Fragment recevied Refresh-Result: " + getResultMsg(reply));
        }

        private String getResultMsg(Message reply) {
            return msgToString(reply);
        }
    }

    // **********************************************************************************************************
    // *** Refresh-Service connection handling
    // **********************************************************************************************************

    private Messenger mReplyMessenger = new Messenger(new ReplyHandler());
    private Messenger mReqMessengerRef = null;

    private ServiceConnection mSvcConn = new ServiceConnection() {
        /**
         * Called after the UniqueIDGeneratorService is connected to convey the
         * result returned from onBind().
         */
        public void onServiceConnected(ComponentName className, IBinder binder) {
            debug(this, "onServiceConnected:" + className);
            mReqMessengerRef = new Messenger(binder);
        }

        /**
         * Called if the Service crashes and is no longer available. The
         * ServiceConnection will remain bound, but the Service will not respond
         * to any requests.
         */
        public void onServiceDisconnected(ComponentName className) {
            mReqMessengerRef = null;
        }
    };

    private void bindRefreshService() {
        debug(this, "calling bindService()");
        if (mReqMessengerRef == null) {
            getActivity().bindService(makeIntent(getActivity()), mSvcConn, Context.BIND_AUTO_CREATE);
        }
    }

}
