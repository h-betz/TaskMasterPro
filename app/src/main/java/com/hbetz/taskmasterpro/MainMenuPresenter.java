package com.hbetz.taskmasterpro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;

/**
 * Created by Hunter on 1/20/2017.
 */

public class MainMenuPresenter {

    private static final String TAG = "MainMenuPresenter";
    private Context context;
    private MainMenu mainMenu;
    private Client client;
    private User user;

    /**
     * Constructor for the presenter that will control the MainMenu view and act as the middle man
     * between the data and the display
     * @param context
     * @param mainMenu
     * @param extras
     */
    public MainMenuPresenter(Context context, MainMenu mainMenu, Bundle extras) {
        this.context = context;
        this.mainMenu = mainMenu;
        this.client = (Client) extras.get("Client");
        initialize();
    }

    /**
     * Set the client's context, create a user object, and update UI
     */
    private void initialize() {
        this.user = new User();
        this.user.setEmail(this.client.getClientEmail());
        this.user.setFirstName(this.client.getClientFirstName());
        this.user.setLastName(this.client.getClientLastName());
        this.client.setContext(this.context);
        this.mainMenu.setWelcomeText("Welcome, " + this.client.getClientFirstName());
    }

    /**
     * Perform the actions necessary to transition from main menu activity to the group list page.
     */
    public void goToGroupPage() {
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("Email", client.getClientEmail());
        userMap.put("FirstName", client.getClientFirstName());
        userMap.put("LastName", client.getClientLastName());
        this.client = new Client(this.context, this.client.getURL(), userMap);
        this.client.setCommand(ClientCommand.GET_GROUPS);
        this.client.getGroups(userMap);
    }

    /**
     * Performs the actions necessary to transition from main menu activity to the create group page.
     */
    public void goToCreateGroup() {
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("Email", client.getClientEmail());
        userMap.put("FirstName", client.getClientFirstName());
        userMap.put("LastName", client.getClientLastName());
        Intent intent = new Intent(context, CreateGroupActivity.class);
        Client tempClient = new Client(this.client.getURL(), userMap);
        intent.putExtra("Client", tempClient);
        intent.putExtra("UserMap", userMap);
        context.startActivity(intent);
    }

}
