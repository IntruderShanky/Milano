package com.intrusoft.milano;

import java.util.HashMap;

/**
 * Define properties for HTTP request
 */
public class Configuration {
    private String cookieTag;
    private String networkErrorMessage;
    private String progressDialogMessage;
    private String progressDialogTitle;
    private boolean shouldDisplay;
    private boolean shouldManageCookies;
    private String defaultURLPrefix;
    private int readTimeOut;
    private int connectTimeOut;
    private HashMap<String, String> requestProperty;

    /**
     * To retrieve or store cookies for specific tag from or into MilanoCookieStore
     * @param cookieTag
     * @return
     */
    public Configuration setCookieTag(String cookieTag) {
        this.cookieTag = cookieTag;
        return this;
    }

    /**
     * @param networkErrorMessage message to display when network is unavailable
     * @return
     */
    public Configuration setNetworkErrorMessage(String networkErrorMessage) {
        this.networkErrorMessage = networkErrorMessage;
        return this;
    }

    /**
     * To display message on Progress Dialog
     *
     * @param progressDialogMessage This is your message
     */
    public Configuration setProgressDialogMessage(String progressDialogMessage) {
        this.progressDialogMessage = progressDialogMessage;
        return this;
    }

    /**
     * To display title on Progress Dialog
     *
     * @param progressDialogTitle This is your title
     */
    public Configuration setProgressDialogTitle(String progressDialogTitle) {
        this.progressDialogTitle = progressDialogTitle;
        return this;
    }

    /**
     * progress dialog should display on start request or not
     *
     * @param shouldDisplay
     * @return
     */
    public Configuration setShouldDisplay(boolean shouldDisplay) {
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
    public Configuration setDefaultURLPrefix(String defaultURLPrefix) {
        this.defaultURLPrefix = defaultURLPrefix;
        return this;
    }

    /**
     * To manage cookies initially this set to false.
     * <br>If set true then {@link Milano} will set cookies to request and retrive cookies from response
     * <br>Automatically manage cookies.
     *
     * @param shouldManageCookies boolean initially false
     */
    public Configuration setShouldManageCookies(boolean shouldManageCookies) {
        this.shouldManageCookies = shouldManageCookies;
        return this;
    }

    /**
     * To set the ConnectionReadTimeOut
     * <br>Default Value: 5000 milliseconds.
     *
     * @param readTimeOut
     * @return
     */
    public Configuration setReadTimeOut(int readTimeOut) {
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
    public Configuration setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
        return this;
    }

    public String getCookieTag() {
        return cookieTag;
    }

    public String getNetworkErrorMessage() {
        return networkErrorMessage;
    }

    public String getProgressDialogMessage() {
        return progressDialogMessage;
    }

    public String getProgressDialogTitle() {
        return progressDialogTitle;
    }

    public boolean isShouldDisplay() {
        return shouldDisplay;
    }

    public String getDefaultURLPrefix() {
        return defaultURLPrefix;
    }

    public boolean isShouldManageCookies() {
        return shouldManageCookies;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public HashMap<String, String> getRequestProperty() {
        return requestProperty;
    }

    public void setRequestProperty(HashMap<String, String> requestProperty) {
        this.requestProperty = requestProperty;
    }
}