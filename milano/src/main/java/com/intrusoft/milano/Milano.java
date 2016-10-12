package com.intrusoft.milano;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Handle Cookies in Request and Response. Basically an automated Cookie Handler
 * Use {@link #along(Context)}  for the global singleton instance
 */
public class Milano {

    private static Milano singleton = null;
    private RequestCreator requestCreator;
    private Connection connection;

    private Milano(RequestCreator requestCreator, Connection connection) {
        this.requestCreator = requestCreator;
        this.connection = connection;
    }

    /**
     * The global default {@link Milano} instance.
     * This instance is automatically initialized with defaults that are suitable to most implementations.
     *
     * @param context Reference to the current context
     */
    public static Milano along(@NonNull Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        if (singleton == null) {
            synchronized (Milano.class) {
                singleton = new Builder(context).build();
            }
        }
        return singleton;
    }

    /**
     * /**
     * Start a request using the specified URL.
     * <br>Passing null as a url will not trigger any request.
     *
     * @param url
     * @return
     */
    public Connection fromURL(@NonNull String url) {
        if (url == null)
            throw new IllegalArgumentException("URL must not be null");
        connection.requestCreator.fromURL(url);
        return connection;
    }

    /**
     * Clear all cookies from the MilanoCookieStore
     */
    public void clearAllCookies() {
        requestCreator.clearAllCookies();
    }

    /**
     * Clear cookies for specific cookieTag
     *
     * @param cookieTag
     */
    public void clearCookiesFor(String cookieTag) {
        requestCreator.clearCookiesFor(cookieTag);
    }

    /**
     * @return the set of all cookies stored in MilanoCookieStore.
     */
    public Map<String, ?> getAllCookies() {
        return requestCreator.getAllCookiesTag();
    }

    /**
     * Log cookies for specific cookieTag in logcat with logtag "Milano"
     *
     * @param cookieTag
     * @return cookies for specific cookieTag
     */
    public String printCookiesFor(String cookieTag) {
        return requestCreator.printCookiesFor(cookieTag);
    }

    /**
     * To Customize the request properties.
     */
    @SuppressWarnings("UnusedDeclaration") //public API
    public static class Builder {
        private Context context;
        private String cookieTag;
        private String networkErrorMessage;
        private String progressDialogMessage;
        private String progressDialogTitle;
        private boolean shouldDisplay;
        private boolean manageCookies;
        private String defaultURLPrefix;
        private int readTimeOut;
        private int connectTimeOut;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * To retrieve or store cookies for specific tag from or into MilanoCookieStore
         * @param cookieTag
         * @return
         */
        public Builder setCookieTag(String cookieTag) {
            this.cookieTag = cookieTag;
            manageCookies = false;
            shouldDisplay = false;
            return this;
        }

        /**
         * @param networkErrorMessage message to display when network is unavailable
         * @return
         */
        public Builder setNetworkErrorMessage(String networkErrorMessage) {
            this.networkErrorMessage = networkErrorMessage;
            return this;
        }

        /**
         * To display message on Progress Dialog
         *
         * @param dialogMessage This is your message
         */
        public Builder setDialogMessage(String dialogMessage) {
            this.progressDialogMessage = dialogMessage;
            return this;
        }

        /**
         * To display title on Progress Dialog
         *
         * @param dialogTitle This is your title
         */
        public Builder setDialogTitle(String dialogTitle) {
            this.progressDialogTitle = dialogTitle;
            return this;
        }

        /**
         * progress dialog should display on start request or not
         *
         * @param shouldDisplay
         * @return
         */
        public Builder shouldDisplayDialog(boolean shouldDisplay) {
            this.shouldDisplay = shouldDisplay;
            return this;
        }

        /**
         * To set the default prefix for url which will be same in all other request
         * <br>Example: "https:www.yourhost.com" may be same in all your future HTTP request.
         *
         * @param defaultURLPrefix
         * @return
         */
        public Builder setDefaultURLPrefix(String defaultURLPrefix) {
            this.defaultURLPrefix = defaultURLPrefix;
            return this;
        }

        /**
         * To manage cookies initially this set to false.
         * <br>If set true then {@link Milano} will set cookies to request and retrive cookies from response
         * <br>Automatically manage cookies.
         *
         * @param manageCookies boolean initially false
         */
        public Builder shouldManageCookies(boolean manageCookies) {
            this.manageCookies = manageCookies;
            return this;
        }

        /**
         * To set the ConnectionReadTimeOut
         * <br>Default Value: 5000 milliseconds.
         *
         * @param readTimeOut
         * @return
         */
        public Builder setReadTimeOut(int readTimeOut) {
            this.readTimeOut = readTimeOut;
            return this;
        }

        /**
         * To set the ConnectionConnectTimeOut
         * <br>Default Value: 5000 milliseconds.
         *
         * @param connectTimeOut
         * @return
         */
        public Builder setConnectTimeOut(int connectTimeOut) {
            this.connectTimeOut = connectTimeOut;
            return this;
        }

        /**
         * @return instance of {@link Milano} with the defined {@link Configuration}
         */
        public Milano build() {
            Context context = this.context;
            Configuration configuration = new Configuration();
            if (cookieTag == null || cookieTag.isEmpty())
                cookieTag = Utils.DEFAULT_COOKIE_TAG;
            if (networkErrorMessage == null || networkErrorMessage.isEmpty())
                networkErrorMessage = Utils.NETWORK_MESSAGE;
            if (progressDialogMessage == null || progressDialogMessage.isEmpty())
                progressDialogMessage = Utils.FETCH_MESSAGE;
            if (progressDialogTitle == null || progressDialogTitle.isEmpty())
                progressDialogTitle = "";
            if (defaultURLPrefix == null || defaultURLPrefix.isEmpty())
                defaultURLPrefix = "";
            if (connectTimeOut < 5000) connectTimeOut = 5000;
            if (readTimeOut < 5000) readTimeOut = 5000;

            configuration.setCookieTag(cookieTag)
                    .setDefaultURLPrefix(defaultURLPrefix)
                    .setNetworkErrorMessage(networkErrorMessage)
                    .setProgressDialogMessage(progressDialogMessage)
                    .setProgressDialogTitle(progressDialogTitle)
                    .setShouldDisplay(shouldDisplay)
                    .setShouldManageCookies(manageCookies)
                    .setReadTimeOut(readTimeOut)
                    .setConnectTimeOut(connectTimeOut);
            RequestCreator requestCreator = new RequestCreator(context, configuration);
            Connection connection = new Connection(requestCreator);
            return new Milano(requestCreator, connection);
        }
    }
}
