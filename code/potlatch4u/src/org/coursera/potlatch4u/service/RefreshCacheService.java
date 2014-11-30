package org.coursera.potlatch4u.service;

import static java.lang.System.currentTimeMillis;
import static org.coursera.potlatch4u.service.RefreshResultMessageFactory.requestIgnoredMessage;
import static org.coursera.potlatch4u.ui.gift.GiftListFragment.debug;

import org.coursera.potlatch4u.ui.gift.Settings;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

public class RefreshCacheService extends Service implements RefreshContext {
    private long lastExecutionTime;
    private Thread refreshThread;
    private Messenger requestMessenger = null;

    private void startRefreshProcessIfNeeded(Messenger replyMessenger) {
        debug(this, "REFRESH? ...");
        if (isRefreshProcessRunning()) {
            debug(this, "         REFRESH? ignored, process running.");
            Toast.makeText(this, "Ignoring refresh request, another one is still running.", Toast.LENGTH_SHORT).show();
            send(replyMessenger, requestIgnoredMessage("Refresh already running."));
        } else if (isRefreshIntervalExceeded()) {
            debug(this, "         REFRESH? to be executed.");
            Toast.makeText(this,
                    "Starting cache refresh after at least " + toSeconds(minRefreshInterval()) + " seconds...",
                    Toast.LENGTH_LONG).show();
            startRefresh(replyMessenger);
        } else {
            debug(this, "         REFRESH? ignored, process was just run.");
            String reason = "Ignoring refresh request, last completion: " + toSeconds(millisSinceLastRefresh()) + " ago.";
            Toast.makeText(this,
                    reason,
                    Toast.LENGTH_SHORT).show();
            send(replyMessenger, requestIgnoredMessage(reason));
        }
        debug(this, "Handling REFRESH? done.");
    }

    private void send(Messenger messenger, Message message) {
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            debug(this, "RemoteException: " + e);
            e.printStackTrace();
        }
    }

    private synchronized boolean isRefreshProcessRunning() {
        return false;
        // return lastExecutionTime > 0;
    }

    private synchronized void startRefresh(Messenger replyMessenger) {
        debug(this, "+++ Starting REFRESH.");
        lastExecutionTime = currentTimeMillis();
        refreshThread = new Thread(new RefreshRunnable(replyMessenger));
        refreshThread.start();
    }

    private boolean isRefreshIntervalExceeded() {
        return millisSinceLastRefresh() > minRefreshInterval();
    }

    private synchronized long millisSinceLastRefresh() {
        return currentTimeMillis() - lastExecutionTime;
    }

    private long minRefreshInterval() {
        return Settings.getTouchCountUpdateIntervalMillis(this);
    }

    private String toSeconds(long millis) {
        return String.valueOf(millis / 1000L);
    }

    /**
     * called from the refresh thread
     * 
     * @see org.coursera.potlatch4u.service.RefreshContext#signalRefreshComplete()
     */
    @Override
    public void signalRefreshComplete() {
        debug(this, "+++ Finished REFRESH.");
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, RefreshCacheService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        debug(this, "onStartCommand: " + intent + ", id: " + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        debug(this, "RefreshCacheService created.");
        requestMessenger = new Messenger(new RefreshRequestHandler());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        debug(this, "RefreshCacheService destroyed.");
    }

    private class RefreshRequestHandler extends Handler {

        @Override
        public void handleMessage(Message request) {
            super.handleMessage(request);
            final Messenger replyMessenger = request.replyTo;
            startRefreshProcessIfNeeded(replyMessenger);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return requestMessenger.getBinder();
    }
}
