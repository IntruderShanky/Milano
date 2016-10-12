package com.intrusoft.libraryapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.intrusoft.milano.Milano;
import com.intrusoft.milano.OnRequestComplete;

public class MainActivity extends AppCompatActivity {
    TextView view;
    String request = "{\"username\": \"IntruderShanky\",\"password\": \"asd54fbg\"}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (TextView) findViewById(R.id.stacktrace);
        view.setText("");

        //To Make fluent request use Singleton instance of Milano
        Milano.along(MainActivity.this)
                .fromURL("https://your_api.com/data")
                .doGet()
                .shouldManageCookies(true)
                .execute(new OnRequestComplete() {
                    @Override
                    public void onSuccess(String response, int responseCode) {
                        //Do whatever you want to do
                        addText("\nResponse: " + response);
                    }

                    @Override
                    public void onError(String error, int errorCode) {
                        //Do whatever you want to do
                        addText("\nError: " + error);
                    }
                });

        //To make customized HTTP request use Milano.Builder
        Milano.Builder builder = new Milano.Builder(MainActivity.this);

        //This is the part of url which will remain same in future request
        String defaultURLPrefix = "https://www.yourdomain.com";
        String request = "{\"username\": \"IntruderShanky\",\"password\": \"asd54fbg\"}";

        builder.setCookieTag("Login Cookies");
        builder.shouldDisplayDialog(true);
        builder.setDialogMessage("Fetching Info");
        builder.setDialogTitle("Loging In");
        builder.shouldManageCookies(true);
        builder.setConnectTimeOut(6000);
        builder.setReadTimeOut(5500);
        builder.setNetworkErrorMessage("Internet not connected");
        builder.setDefaultURLPrefix(defaultURLPrefix);
        //Create an instance of Milano
        Milano milano = builder.build();

        //Make HTTP request with customized request properties
        milano.fromURL("/user_login")
                .doPost(request)
                .execute(new OnRequestComplete() {
                    @Override
                    public void onSuccess(String response, int responseCode) {
                        //Do whatever you want to do
                        addText("\nResponse: " + response);
                    }

                    @Override
                    public void onError(String error, int errorCode) {
                        //Do whatever you want to do
                        addText("\nError: " + error);
                    }
                });

    }

    public void addText(String text) {
        view.setText(view.getText() + "\n\n\n" + text);
    }
}
