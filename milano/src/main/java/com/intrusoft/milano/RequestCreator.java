package com.intrusoft.milano;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLException;

import static com.intrusoft.milano.Connection.RequestType;
import static com.intrusoft.milano.Utils.COOKIES_HEADER;
import static com.intrusoft.milano.Utils.LOGTAG;
import static com.intrusoft.milano.Utils.PREFERENCE;

/**
 * Fluent API for building a request.
 */
public class RequestCreator {

    private String url;
    private Context context;
    private SharedPreferences cookies;
    private Configuration configuration;
    private String request;
    private int responseCode;
    private RequestType requestType;
    private HashMap<String,String> headers;

    RequestCreator(Context context, Configuration configuration) {
        this.context = context;
        this.configuration = configuration;
        cookies = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }


    public RequestCreator addHeader(String key, String value){
        if(headers == null)
            headers = new HashMap<>();
        headers.put(key, value);
        return this;
    }

    /**
     * Executes the request with the specified values and return to {@link OnRequestComplete}
     *
     * @param onRequestComplete Interface definition for a callback to be invoked when request is complete <br>{@link OnRequestComplete}
     */
    public void execute(final OnRequestComplete onRequestComplete) {
        //Checking Internet Connection
        if (!NetworkConnection.isConnected(context))
            Utils.showMessage(context, configuration.getNetworkErrorMessage(), Utils.NETWORK_TITLE);
        else {
            new AsyncTask<String, String, String>() {
                ProgressDialog dialog = new ProgressDialog(context);

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (configuration.isShouldDisplay()) {
                        if (configuration.getProgressDialogTitle() != null)
                            dialog.setTitle(configuration.getProgressDialogTitle());
                        dialog.setMessage(configuration.getProgressDialogMessage());
                        try {
                            dialog.show();
                        } catch (Exception e) {
                            Log.e(LOGTAG, e.getMessage());
                        }
                    }
                }

                @Override
                protected String doInBackground(String... params) {
                    URL url;
                    HttpURLConnection connection;
                    OutputStream outputStream;
                    try {
                        url = new URL(params[0]);
                        Log.d(LOGTAG, "Request URL: " + url.toString());

                        //setting Connection Properties
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(configuration.getConnectTimeOut());
                        connection.setReadTimeout(configuration.getReadTimeOut());
                        connection.setRequestMethod(requestType.value);

                        //Setting cookies to request
                        if (configuration.isShouldManageCookies() && cookies.getStringSet(configuration.getCookieTag(), null) != null)
                            connection.setRequestProperty("Cookie", TextUtils.join(";", cookies.getStringSet(configuration.getCookieTag(), null)));

                        //Preparing Connection for HTTP requests other than GET.
                        //adding headers
                        if(headers != null){
                            for (String s : headers.keySet()) {
                                connection.setRequestProperty(s, headers.get(s));
                            }
                        }
                        if (requestType != RequestType.GET) {
                            connection.setDoInput(true);
                            connection.setDoOutput(true);
                            HashMap<String, String> requestProperty = configuration.getRequestProperty();
                            if(requestProperty==null){
                                connection.setRequestProperty("Content-Type", "application/json");
                            }
                            outputStream = connection.getOutputStream();
                            if (requestProperty != null) {
                                Uri.Builder builder = new Uri.Builder();
                                for (String s : requestProperty.keySet()) {
                                    builder.appendQueryParameter(s, requestProperty.get(s));
                                }
                                String queryParams = builder.build().getEncodedQuery();
                                System.out.println(queryParams);
                                OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                                writer.write(queryParams);
                                writer.flush();
                                writer.close();
                            } else
                                outputStream.write(request.getBytes());
                            outputStream.close();
                        }


                        //Connecting the Connection
                        connection.connect();

                        //Storing cookies from response
                        if (configuration.isShouldManageCookies()) {
                            Map<String, List<String>> headerFields = connection.getHeaderFields();
                            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
                            if (cookiesHeader != null) {
                                Set<String> set = new HashSet<>(cookiesHeader);
                                cookies.edit().putStringSet(configuration.getCookieTag(), set).apply();
                            }
                        }

                        //Logging Connection Properties.
                        Log.d(LOGTAG, "Request Type: " + requestType.value);
                        Log.d(LOGTAG, "Connect Timeout: " + connection.getConnectTimeout() + " milliseconds");
                        Log.d(LOGTAG, "Read Timeout: " + connection.getReadTimeout() + " milliseconds");
                        Log.d(LOGTAG, "Response Code: " + connection.getResponseCode());

                        responseCode = connection.getResponseCode();
                        if (responseCode / 100 == 2)
                            return Convert.toString(connection.getInputStream());
                        else return Convert.toString(connection.getErrorStream());

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return e.getLocalizedMessage();
                    } catch (SSLException e) {
                        e.printStackTrace();
                        return e.getLocalizedMessage();
                    } catch (SocketTimeoutException e) {
                        e.printStackTrace();
                        return e.getLocalizedMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return e.getLocalizedMessage();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return e.getLocalizedMessage();
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    System.out.println(s);
                    if (responseCode / 100 == 2)
                        onRequestComplete.onSuccess(s, responseCode);
                    else
                        onRequestComplete.onError(s, responseCode);
                    if (configuration.isShouldDisplay() && dialog != null && dialog.isShowing())
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                            Log.e(LOGTAG, e.getMessage());
                        }
                }
            }.execute(configuration.getDefaultURLPrefix() + url);
        }
    }

    /**
     * To manage cookies initially this set to false.
     * <br>If set true then {@link Milano} will set cookies to request and retrive cookies from response
     * <br>Automatically manage cookies.
     *
     * @param manageCookies boolean initially false
     */
    public RequestCreator shouldManageCookies(boolean manageCookies) {
        this.configuration.setShouldManageCookies(manageCookies);
        return this;
    }

    /**
     * To manage cookies initially this set to false.
     * <br>If set true then {@link Milano} will set cookies for specific cookieTag to request and retrive cookies from response
     * <br>Automatically manage cookies.
     *
     * @param manageCookies
     * @param cookieTag
     */
    public RequestCreator shouldManageCookies(boolean manageCookies, String cookieTag) {
        this.configuration.setShouldManageCookies(manageCookies);
        this.configuration.setCookieTag(cookieTag);
        return this;
    }

    /**
     * To set the {@link RequestType}
     *
     * @param requestType
     */
    void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    void clearAllCookies() {
        cookies.edit().clear().apply();
    }

    void clearCookiesFor(String cookieTag) {
        if (cookies.contains(cookieTag)) {
            cookies.edit().remove(cookieTag).apply();
        }
    }

    void fromURL(String url) {
        this.url = url;
    }

    void setRequest(String request) {
        this.request = request;
    }

    Map<String, ?> getAllCookiesTag() {
        return cookies.getAll();
    }

    String printCookiesFor(String cookieTag) {
        if (cookies.getStringSet(cookieTag, null) != null) {
            Log.d(LOGTAG, "Cookies: " + TextUtils.join(";", cookies.getStringSet(configuration.getCookieTag(), null)));
            return TextUtils.join(";", cookies.getStringSet(configuration.getCookieTag(), null));
        } else {
            Log.d(LOGTAG, "Cookies: Not Found");
            return "Cookies: Not Found";
        }
    }
}
