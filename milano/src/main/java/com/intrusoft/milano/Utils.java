package com.intrusoft.milano;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Intruder Shanky on 7/31/2016.
 */

public class Utils {

    final static String NETWORK_MESSAGE = "Internet connection is unavailable";
    final static String NETWORK_TITLE = "Network Error";
    final static String FETCH_MESSAGE = "Loading..";
    final static String LOGTAG = "Milano";

    final static String PREFERENCE = "COOKIE_STORE";
    public final static String DEFAULT_COOKIE_TAG = "DEFAULT";
    static final String COOKIES_HEADER = "Set-Cookie";

    static void showMessage(Context context, String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

}
