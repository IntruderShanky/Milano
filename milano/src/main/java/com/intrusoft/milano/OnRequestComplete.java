package com.intrusoft.milano;

/**
 * Created by Intruder Shanky on 7/31/2016.
 */

public interface OnRequestComplete {
    public void onSuccess(String response, int responseCode);

    public void onError(String error, int errorCode);
}
