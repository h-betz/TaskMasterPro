package com.hbetz.taskmasterpro;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hunter on 1/26/2017.
 *
 * Adapter class to fill in the listview items for the grouplist page activity
 */

public class GroupAdapter extends BaseAdapter {

    private static final String TAG = "GroupAdapter";
    private ArrayList<JSONObject> mData;
    private Context context;

    /**
     * Default constructor
     * @param context
     * @param list
     */
    public GroupAdapter(Context context, ArrayList<JSONObject> list) {
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
        View result = groupInflater.inflate(R.layout.group_adapter, parent, false);

        JSONObject object = getItem(position);

        Button groupBtn = (Button) result.findViewById(R.id.groupBtnId);
        try {
            groupBtn.setText(object.getString("group_name"));
            groupBtn.setTag(object.getString("groupId"));
            groupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = (String) v.getTag();
                    //Start activity for this group
                }
            });
            TextView textView = (TextView) result.findViewById(R.id.admin_member);
            String admin = object.getString("admin");
            if (admin.equals("1")) {
                textView.setText("Admin");
            } else {
                textView.setText("Member");
            }

        } catch (JSONException e) {
            Log.d(TAG, "FAILURE");
            e.printStackTrace();
        }

        return result;
    }

}
