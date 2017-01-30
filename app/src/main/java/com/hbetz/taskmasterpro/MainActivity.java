package com.hbetz.taskmasterpro;

import android.content.Context;
import android.app.FragmentManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;


public class MainActivity extends FragmentActivity {

    //Boolean telling us whether a download is in progress, so we don't trigger overlapping
    //downloads with consecutive button clicks
    private boolean mDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}
