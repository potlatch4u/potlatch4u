package org.coursera.potlatch4u.ui.auth;

import static android.app.Activity.RESULT_OK;
import static java.text.MessageFormat.format;
import static org.coursera.potlatch4u.ui.gift.GiftListActivity.LOG_TAG;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class LoginRequest {
    private static final int LOGIN_REQUEST_CODE = 1967;

    public boolean wasSuccessfulonActivityResult(Activity caller, int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            return data.getBooleanExtra(LoginActivity.LOGIN_SUCCESS, false);
        } else {
            Log.e(LOG_TAG, format("onActivityResult(): unexptected: code {0}, result {1}!", requestCode, resultCode));
            return false;
        }
    }

    public void initiateLogin(Activity caller){
          Intent intent=new Intent(caller, LoginActivity.class);
        caller.startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

}
