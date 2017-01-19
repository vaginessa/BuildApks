package com.example.f.buildapk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String channel = WalleChannelReader.getChannel(this);
        Log.d("MainActivity", "channel----" + channel);

        String province = WalleChannelReader.get(this, "province");
        Log.d("MainActivity", "channel----province--" + province);

        String isTest = WalleChannelReader.get(this, "isTest");
        Log.d("MainActivity", "channel----isTest--" + isTest);

    }
}
