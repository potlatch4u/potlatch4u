package org.coursera.potlatch4u.service;

import static org.coursera.potlatch4u.ui.gift.GiftListFragment.debug;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class RefreshRunnable implements Runnable {
    final Messenger replyMessenger;

    public RefreshRunnable(Messenger replyMessenger) {
        super();
        this.replyMessenger = replyMessenger;
    }

    @Override
    public void run() {
        debug(this, "Starting REFRESH 6.");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            debug(this, "REFRESH sleep aborted.");
            e.printStackTrace();
        }
        debug(this, "Finished REFRESH.");
        sendResult(RefreshResultMessageFactory.refreshCompleted());
    }

    private void sendResult(Message message) {
        try {
            replyMessenger.send(message);
        } catch (RemoteException e) {
            debug(this, "RemoteException: " + e);
            e.printStackTrace();
        }
    }

}
