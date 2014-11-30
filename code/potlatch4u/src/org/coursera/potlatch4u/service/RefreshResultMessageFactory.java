package org.coursera.potlatch4u.service;

import java.text.MessageFormat;

import android.os.Bundle;
import android.os.Message;

public class RefreshResultMessageFactory {

    private static final String COMPLETED = "COMPLETED";
    private static final String REASON = "REASON";

    public static Message requestIgnoredMessage(String reason) {
        return createMessage(reason, false);
    }

    private static Message createMessage(String reason, boolean refreshCompleted) {
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putString(REASON, reason);
        data.putBoolean(COMPLETED, refreshCompleted);
        msg.setData(data);
        return msg;
    }

    public static Message refreshCompleted() {
        return createMessage("", true);
    }

    public static String msgToString(Message refreshResultMessage) {
        Bundle data = refreshResultMessage.getData();
        return MessageFormat.format("Refresh completed: {0}, reason: {1}.", data.getBoolean(COMPLETED),
                data.getString(REASON));
    }

}
