package com.intrusoft.milano;

import android.content.Context;

/**
 * Handle Cookies in Request and Response. Basically an automated Cookie Handler
 * Use {@link #along(Context)}  for the global singleton instance
 */

public class Milano {

    private static RequestCreator requestCreator;

    /**
     * The global default {@link Milano} instance.
     * This instance is automatically initialized with defaults that are suitable to most implementations.
     *
     * @param context
     */
    public static RequestCreator along(Context context) {
        requestCreator = new RequestCreator(context);
        requestCreator.setSingletonInstance(requestCreator);
        return requestCreator;
    }

}
