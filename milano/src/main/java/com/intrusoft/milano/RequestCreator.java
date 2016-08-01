package com.intrusoft.milano;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.intrusoft.milano.Utils.COOKIES_HEADER;
import static com.intrusoft.milano.Utils.PREFERENCE;

/**
 * Fluent API for building a request.
 */

public class RequestCreator {

    private RequestCreator requestCreator;
    private String message;
    private String url;
    private Context context;
    private boolean display;
    private String title;
    private String networkErrorMessage;
    private String request;
    private SharedPreferences cookies;
    private String loadingTitle;
    private boolean manageCookies;

    public RequestCreator(Context context) {
        this.context = context;
        message = "Loading..";
        display = true;
        url = "";
        request = null;
        cookies = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        title = "Network Connection Error";
        manageCookies = false;
        networkErrorMessage = "This app requires an internet requestCreator. Make sure you are connected to a wifi network or have switched to your network data.";
    }

    /**
     * Set the global instance returned from {@link Milano#along(Context)}.
     * This method must be called before any calls to with and may only be called once.
     */
    protected void setSingletonInstance(RequestCreator requestCreator) {
        this.requestCreator = requestCreator;
    }

    /**
     * To make get request
     * This is initially default method to make request
     */
    public RequestCreator doGet() {
        requestCreator.request = null;
        return requestCreator;
    }

    /**
     * Executes the request with the specified values and return to {@link OnRequestComplete}
     *
     * @param onRequestComplete Interface definition for a callback to be invoked when request is complete <br>{@link OnRequestComplete}
     */
    public void execute(final OnRequestComplete onRequestComplete) {
        if (!NetworkConnection.isConnected(context))
            showNetworkError(context);
        else {
            new AsyncTask<String, String, String>() {
                ProgressDialog dialog = new ProgressDialog(context);

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (display) {
                        if (loadingTitle != null)
                            dialog.setTitle(loadingTitle);
                        dialog.setMessage(message);

                        try {
                            dialog.show();
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                protected String doInBackground(String... params) {

                    URL url = null;
                    HttpURLConnection connection = null;
                    OutputStream outputStream;
                    try {
                        url = new URL(params[0]);
                        System.out.println(params[0]);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(5000);

                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("Content-Type", "application/json");

                        if (manageCookies && cookies.getStringSet("cookies", null) != null) {
                            connection.setRequestProperty("Cookie", TextUtils.join(";", cookies.getStringSet("cookies", null)));
                        }

                        if (request != null) {
                            connection.setDoInput(true);
                            connection.setDoOutput(true);
                            outputStream = connection.getOutputStream();
                            outputStream.write(request.getBytes());
                        }

                        connection.connect();
                        if (manageCookies) {
                            Map<String, List<String>> headerFields = connection.getHeaderFields();
                            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

                            if (cookiesHeader != null) {
                                Set<String> set = new HashSet<>(cookiesHeader);
                                cookies.edit().putStringSet("cookies", set).apply();
                            }
                        }

                        Log.d("CODE", connection.getResponseCode() + "");
                        return Convert.toString(connection.getInputStream());
                    } catch (SocketTimeoutException t) {
                        return t.getMessage();
                    } catch (Exception e) {
                        return "failed :: " + e.toString();
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    System.out.println(s);
                    onRequestComplete.onComplete(s);
                    if (display && dialog != null && dialog.isShowing())
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                        }

                }
            }.execute(url);
        }
    }


    /**
     * To make post request
     *
     * @param request set to the connection before connection is made
     * @return
     */
    public RequestCreator doPost(String request) {
        requestCreator.request = request;
        return requestCreator;
    }

    /**
     * To display message on Progress Dialog
     *
     * @param message This is your message
     */
    public RequestCreator displayLoadingMessage(String message) {
        requestCreator.message = message;
        return requestCreator;
    }

    /**
     * To display title on Progress Dialog
     *
     * @param title This is your title
     */
    public RequestCreator displayLoadingTitle(String title) {
        requestCreator.loadingTitle = title;
        return requestCreator;
    }

    /**
     * Start a request using the specified URL.
     * Passing null as a url will not trigger any request.
     *
     * @return
     */
    public RequestCreator fromUrl(String url) {
        requestCreator.url = url;
        return requestCreator;
    }

    /**
     * @param networkErrorMessage message to display when network is unavailable
     * @return
     */
    public RequestCreator setNetworkError(String networkErrorMessage) {
        requestCreator.networkErrorMessage = networkErrorMessage;
        return requestCreator;
    }

    public void showNetworkError(Context context) {
        Utils.showMessage(context, networkErrorMessage, title);
    }

    /**
     * To manage cookies initially this set to false.
     * <br>If set true then {@link Milano} will set cookies to request and retrive cookies from response
     * <br>Automatically manage cookies.
     *
     * @param manageCookies boolean initially false
     */
    public RequestCreator manageCookies(boolean manageCookies) {
        requestCreator.manageCookies = manageCookies;
        return requestCreator;
    }
}
