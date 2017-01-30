package com.hbetz.taskmasterpro;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Hunter on 1/13/2017.
 */

public class Client extends
        AsyncTask<HashMap<String, String>, Void, ClientCommand> implements Serializable {

    private static final String TAG = "CLIENT";

    private Context context;
    private String urlString;
    private String clientFirstName;
    private String clientLastName;
    private String clientEmail;
    private String serverResponse;
    private ClientCommand command;

    /**
     * Constructor for a client object that allows user email and URL to be passed.
     * @param url
     * @param clientEmail
     */
    public Client(String url, String clientEmail) {
        this.urlString = url;
        this.clientEmail = clientEmail;
        this.command = ClientCommand.NONE;
    }

    /**
     * Constructor for a client object that allows user information to be passed as well as the URL.
     * The context can be specified by the receiving activity.
     * @param url
     * @param userInfo
     */
    public Client(String url, HashMap<String, String> userInfo) {
        this.urlString = url;
        this.clientEmail = userInfo.get("Email");
        this.clientFirstName = userInfo.get("FirstName");
        this.clientLastName = userInfo.get("LastName");
        this.command = ClientCommand.NONE;
    }

    /**
     * Constructor for this client. Specifies the context, URL, and user info
     * @param context
     * @param url
     * @param userInfo
     */
    public Client(Context context, String url, HashMap<String, String> userInfo) {
        this.context = context;
        this.urlString = url;
        this.clientEmail = userInfo.get("Email");
        this.clientFirstName = userInfo.get("FirstName");
        this.clientLastName = userInfo.get("LastName");
        this.command = ClientCommand.NONE;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }

    public String getClientFirstName() {
        return clientFirstName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getURL() {
        return urlString;
    }

    public void setServerResponse(String response) {
        this.serverResponse = response;
    }

    public String getServerResponse() {
        return this.serverResponse;
    }

    public void setCommand(ClientCommand command) {
        this.command = command;
    }

    /**
     * Calls method to insert user into database
     *
     * @param params
     */
    public void insertUser(HashMap<String, String> params) {
        this.command = ClientCommand.INSERT_USER;
        this.execute(params);
    }

    /**
     * Calls method to get groups associated with this user.
     */
    public void getGroups(HashMap<String, String> params) {
        this.command = ClientCommand.GET_GROUPS;
        this.execute(params);
    }

    /**
     * Calls method to get members associated with this group.
     * @param params
     */
    public void getMembers(HashMap<String, String> params) {
        this.command = ClientCommand.GET_MEMBERS;
        this.execute(params);
    }

    /**
     * Performs the actions necessary to create a new group.
     * @param params
     */
    public void createGroup(HashMap<String, String> params) {
        this.command = ClientCommand.CREATE_GROUP;
        this.execute(params);
    }

    @Override
    protected void onPreExecute() {
        Log.e(TAG, "1 - RequestVoteTask is about to start...");
    }

    @Override
    protected ClientCommand doInBackground(HashMap<String, String>... params) {
        boolean status = false;
        String response = "";

        Log.d(TAG, "2 - pre Request to response...");

        try {
            response = performPostCall(params[0]);
            this.setServerResponse(response.toString());
            Log.d(TAG, "3 - give Response...");
            Log.d(TAG, "4 " + response.toString());
        } catch (Exception e) {
            Log.e(TAG, "Error...");
        }

        Log.d(TAG, "5 - after Response...");

        if (!response.equalsIgnoreCase("")) {
            try {
                Log.d(TAG, "6 - response !empty...");
                JSONObject jRoot = new JSONObject(response);
                Log.d(TAG, jRoot.toString());
                JSONObject d = jRoot.getJSONObject("d");
                Log.d(TAG, jRoot.toString());

                //serverResponse = response;
                int ResultType = d.getInt("ResultType");
                Log.e("ResultType", ResultType + "");

                if (ResultType == 1) {
                    status = true;
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error " + e.getMessage());
            } finally {

            }
        } else {
            Log.e(TAG, "6 - response is empty...");
        }

        return this.command;
    }

    @Override
    protected void onPostExecute(ClientCommand command) {
        Log.d(TAG, "7 - onPostExecute...");
        Intent intent;
        Client client;
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("Email", this.clientEmail);
        userMap.put("FirstName", this.clientFirstName);
        userMap.put("LastName", this.clientLastName);
        switch (this.command) {
            case INSERT_USER:
                //Should be called only when a user first logs in or opens the app
                intent = new Intent(context, MainMenu.class);
                client = new Client(this.urlString, userMap);
                intent.putExtra("Client", client);
                context.startActivity(intent);
                break;
            case GET_GROUPS:
                intent = new Intent(context, GroupListPage.class);
                client = new Client(this.urlString, userMap);
                client.setServerResponse(this.serverResponse);
                intent.putExtra("Client", client);
                intent.putExtra("UserMap", userMap);
                context.startActivity(intent);
                break;
            case CREATE_GROUP:
                intent = new Intent(context, GroupListPage.class);
                client = new Client(this.urlString, userMap);
                Log.d(TAG, "Starting GROUPLISTPAGE");
                Log.d(TAG, "Server Response: " + this.getServerResponse());
                client.setServerResponse(this.serverResponse);
                intent.putExtra("Client", client);
                intent.putExtra("UserMap", userMap);
                context.startActivity(intent);
                break;
            case GET_MEMBERS:
                Log.d(TAG, "CLICK -- 2");
                intent = new Intent(context, GroupPageActivity.class);
                client = new Client(this.urlString, userMap);
                client.setServerResponse(this.serverResponse);
                intent.putExtra("Client", client);
                intent.putExtra("UserMap", userMap);
                Log.d(TAG, "STARTING ACTIVITY");
                context.startActivity(intent);
                break;
            default:
                throw new IllegalArgumentException("Invalid command");
        }

    }

    /**
     * Performs the actions necessary to send data to the server
     *
     * @param postDataParams
     * @return
     */
    public String performPostCall(HashMap<String, String> postDataParams) {
        URL url;
        String response = "";

        try {
            url = new URL(this.urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            Log.d(TAG, "11 - url : " + this.urlString);

            JSONObject root = createJSONObj(postDataParams);
            //Create JSON object based on the command.
/*            switch (this.command) {
                case INSERT_USER:
                    root = createJSONObj(postDataParams);
                    break;
                case GET_GROUPS:
                    root = createJSONObj(postDataParams);
                    break;
                case CREATE_GROUP:
                    root = createJSONObj(postDataParams);
                    break;
                case GET_MEMBERS:
                    break;
                default:
                    root = createJSONObj(postDataParams);
            }*/

            Log.d(TAG, "12 - root : " + root.toString());

            String str = root.toString();
            byte[] outputBytes = str.getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(outputBytes);
            int responseCode = conn.getResponseCode();
            Log.d(TAG, "13 - responseCode : " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "14 - HTTP_OK");
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                Log.d(TAG, response);
            } else {
                Log.d(TAG, "14 - False - HTTP_OK");
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Create a JSON object that represents a user's details and type of request which will be sent to the server.
     * @param postDataParams
     * @return
     */
    public JSONObject createJSONObj(HashMap<String, String> postDataParams) {
        JSONObject root = new JSONObject();
        try {
            root.put("Email", postDataParams.get("Email"));
            root.put("FirstName", postDataParams.get("FirstName"));
            root.put("LastName", postDataParams.get("LastName"));
            switch (this.command) {
                case INSERT_USER:
                    root.put("Command", "InsertUser");
                    break;
                case GET_GROUPS:
                    root.put("Command", "GetGroups");
                    break;
                case CREATE_GROUP:
                    root.put("GroupName", postDataParams.get("GroupName"));
                    root.put("GroupId", postDataParams.get("GroupId"));
                    root.put("Admin", postDataParams.get("Admin"));
                    root.put("Command", "CreateGroup");
                    break;
                case GET_MEMBERS:
                    Log.d(TAG, "CLICK -- 1");
                    root.put("GroupId", postDataParams.get("GroupId"));
                    root.put("Command", "GetMembers");
                    break;
                default:
                    root.put("Command", "None");
            }

        } catch (JSONException e) {
            Log.d(TAG, "Error creating user JSON object");
            e.printStackTrace();
        }
        return root;
    }



}
