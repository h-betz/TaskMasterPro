package com.hbetz.taskmasterpro;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;

import com.hbetz.taskmasterpro.adapters.GroupAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hunter on 1/21/2017.
 */

public class GroupListPagePresenter {
    private static final String TAG = "GroupListPagePresenter";
    private Context context;
    private GroupListPage groupListPage;
    private Client client;
    private HashMap<String, String> userMap;
    private ArrayList<JSONObject> groupList;

    public GroupListPagePresenter(Context context, GroupListPage groupListPage, Bundle extras) {
        this.context = context;
        this.groupListPage = groupListPage;
        this.client = (Client) extras.get("Client");
        this.userMap = (HashMap<String, String>) extras.get("UserMap");
        initialize();
    }

    /**
     * Retrieve data from database (i.e. groups). Set up user info.
     */
    public void initialize() {
        this.client.setContext(this.context);
        this.groupList = new ArrayList<>();
        try {
            if (this.client.getServerResponse() == null || this.client.getServerResponse().equals("")) {

            } else {
                JSONArray json = new JSONArray(this.client.getServerResponse());
                Log.d(TAG, this.client.getServerResponse());
                for (int i = 0; i < json.length(); i++) {
                    JSONObject obj = json.getJSONObject(i);
                    this.groupList.add(obj);
                }
                Log.d(TAG, "" + this.groupList.size());
                updateUI();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method called after activity regains focus
     */
    public void onResume() {
        this.client = new Client(this.client.getURL(), this.userMap);
    }

    /**
     * Update the UI (populate the view with the groups and whether user is admin or not)
     */
    public void updateUI() {
        ListAdapter groupListAdapter = new GroupAdapter(this.context, groupList);
        this.groupListPage.setAdapter(groupListAdapter);
    }

    /**
     * Begin process of transferring activities
     * @param tag
     */
    public void goToGroupPage(String tag) {
        this.userMap.put("GroupId", tag);
        this.client = new Client(this.context, this.client.getURL(), this.userMap);
        this.client.setCommand(ClientCommand.GET_MEMBERS);
        this.client.getMembers(userMap);
    }

}
