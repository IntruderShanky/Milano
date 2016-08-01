package com.intrusoft.libraryapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.intrusoft.milano.Milano;
import com.intrusoft.milano.OnRequestComplete;
import com.intrusoft.milano.Utils;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    TextView view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (TextView) findViewById(R.id.stacktrace);
        view.setText("");


        Milano.along(MainActivity.this).execute(new OnRequestComplete() {
            @Override
            public void onComplete(String response) {

            }
        });
        Milano.along(MainActivity.this)
                .displayLoadingMessage("Finally")
                .displayLoadingTitle("THIs is Title")
                .manageCookies(true)
                .fromUrl("https://shipoya.com/auth_resources/api/user_login")
                .doPost("{\"remember_me\":\"true\",\"identifier\":\"7206820942\",\"password\":\"admin\"}")
                .execute(new OnRequestComplete() {
                    @Override
                    public void onComplete(String response) {
                        addText(response);
                        Milano.along(MainActivity.this)
                                .fromUrl("https://shipoya.com/auction_resources/api/loads/carrier_loads")
                                .manageCookies(true)
                                .displayLoadingTitle("Loading")
                                .displayLoadingMessage("User Data")
                                .setNetworkError("Internet connection is unavailable")
                                .execute(new OnRequestComplete() {
                                    @Override
                                    public void onComplete(String response) {
                                        addText(response);
                                    }
                                });
                    }
                });

        Picasso.with(MainActivity.this).load(R.mipmap.ic_launcher).into(new ImageView(MainActivity.this));

    }

    public void addText(String text) {
        view.setText(view.getText() + "\n\n\n" + text);
    }
}
