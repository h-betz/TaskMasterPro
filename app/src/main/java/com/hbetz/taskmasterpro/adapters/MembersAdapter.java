package com.hbetz.taskmasterpro.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hbetz.taskmasterpro.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hunter on 1/29/2017.
 */

public class MembersAdapter extends BaseAdapter {

    private static final String TAG = "MembersAdapter";
    private ArrayList<JSONObject> mData;
    private Context context;

    /**
     * Default constructor.
     * @param context
     * @param list
     */
    public MembersAdapter(Context context, ArrayList<JSONObject> list) {
        this.context = context;
        this.mData = list;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public JSONObject getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    /**
     * Constructs the view that is returned to the calling activity.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater groupInflater = LayoutInflater.from(this.context);
        View result = groupInflater.inflate(R.layout.members_adapter, parent, false);

        JSONObject object = getItem(position);

        try {
            TextView textView = (TextView) result.findViewById(R.id.staticMemberName);
            String name = object.getString("first_name") + " " + object.get("last_name");
            Log.d(TAG, name);
            textView.setText(name);
        } catch (JSONException e) {
            Log.d(TAG, "FAILURE");
            e.printStackTrace();
        }

        return result;
    }

}
