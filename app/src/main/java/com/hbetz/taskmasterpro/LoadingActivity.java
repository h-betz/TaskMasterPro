package com.hbetz.taskmasterpro;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.HashMap;

public class LoadingActivity extends FragmentActivity  {

    private static final String TAG = "LoadingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Bundle extras = getIntent().getExtras();
        Log.d(TAG, "Initializing connections");
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("Email", extras.getString("Email"));
        userMap.put("FirstName", extras.getString("FirstName"));
        userMap.put("LastName", extras.getString("LastName"));
        Client client = new Client(this, "http://54.214.81.143:8080", userMap);
        client.insertUser(userMap);
    }

}
