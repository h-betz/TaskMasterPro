package com.hbetz.taskmasterpro;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Hunter on 1/22/2017.
 */

public class CreateGroupPresenter {

    private static final String TAG = "CreateGroupPresenter";
    private Client client;
    private Context context;
    private CreateGroupActivity createGroupActivity;

    /**
     * Constructor that assigns context, activity, and extracts data from bundle
     * @param context
     * @param createGroupActivity
     * @param extras
     */
    public CreateGroupPresenter(Context context, CreateGroupActivity createGroupActivity, Bundle extras) {
        this.context = context;
        this.createGroupActivity = createGroupActivity;
        this.client = (Client) extras.get("Client");
        initialize();
    }

    /**
     * Perform actions necessary to set up client and presenter
     */
    public void initialize() {
        this.client.setContext(this.context);
    }

    /**
     * Generate a groupId by appending the timestamp to the end of the group name.
     * @param groupName
     * @return
     */
    public String generateGroupId(String groupName) {
        String groupId = groupName;
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        Log.d(TAG, timeStamp);
        timeStamp = timeStamp.replace(".","");
        groupId += timeStamp;
        Log.d(TAG, groupId);
        return groupId;
    }

    /**
     * Send new group information to the server when user submits.
     */
    public void onSubmit(String groupName) {
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("Email", client.getClientEmail());
        dataMap.put("FirstName", client.getClientFirstName());
        dataMap.put("LastName", client.getClientLastName());
        dataMap.put("Admin", "1");
        dataMap.put("GroupName", groupName);
        dataMap.put("GroupId", generateGroupId(groupName));
        client.setCommand(ClientCommand.CREATE_GROUP);
        client.createGroup(dataMap);
    }
}
