package com.hbetz.taskmasterpro;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;

import com.hbetz.taskmasterpro.adapters.MembersAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hunter on 1/29/2017.
 */

public class GroupPagePresenter {

    private static final String TAG = "GroupPagePresenter";
    private Context context;
    private GroupPageActivity groupPageActivity;
    private Client client;

    /**
     * Constructor for the GroupPage Presenter
     * @param context
     * @param groupPageActivity
     * @param extras
     */
    public GroupPagePresenter(Context context,GroupPageActivity groupPageActivity, Bundle extras) {
        this.context = context;
        this.groupPageActivity = groupPageActivity;
        this.client = (Client) extras.get("Client");
        initialize((HashMap<String, String>)extras.get("UserMap"));
    }

    /**
     * Set the context and retrieve data
     * @param userMap
     */
    private void initialize(HashMap<String, String> userMap) {
        Log.d(TAG, "INITIALIZING GROUP PAGE");
        this.client.setContext(this.context);
        ArrayList<JSONObject> membersList = new ArrayList<>();
        try {
            if (this.client.getServerResponse() == null || this.client.getServerResponse().equals("")) {

            } else {
                JSONArray json = new JSONArray(this.client.getServerResponse());
                Log.d(TAG, this.client.getServerResponse());
                for (int i = 0; i < json.length(); i++) {
                    JSONObject obj = json.getJSONObject(i);
                    membersList.add(obj);
                }
                Log.d(TAG, "" + membersList.size());
                updateUI(membersList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the UI to show the data retrieved from the database.
     * @param membersList
     */
    private void updateUI(ArrayList<JSONObject> membersList) {
        ListAdapter memberListAdapter = new MembersAdapter(this.context, membersList);
        this.groupPageActivity.setAdapter(memberListAdapter);
    }

}
