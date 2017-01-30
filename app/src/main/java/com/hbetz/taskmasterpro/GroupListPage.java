package com.hbetz.taskmasterpro;

import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class GroupListPage extends AppCompatActivity {

    private static final String TAG = "GroupListPage";
    private GroupListPagePresenter presenter;
    private ListView mDrawerList;
    private ListView groupsList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list_page);
        Bundle extras = getIntent().getExtras();

        groupsList = (ListView) findViewById(R.id.groups_listview);

        mDrawerList = (ListView) findViewById(R.id.groupListNavList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (this.presenter == null) {
            this.presenter = new GroupListPagePresenter(this, this, extras);
        }
    }

    /*@Override
    public void onResume() {
        super.onResume();
        this.presenter.onResume();
    }*/

    /**
     * Sets the adapter for the listview
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter) {
        this.groupsList.setAdapter(adapter);
        ((BaseAdapter) this.groupsList.getAdapter()).notifyDataSetChanged();
        //this.mDrawerList.deferNotifyDataSetChanged();
    }

    /**
     * Calls the presenter to go to the group page
     */
    public void goToGroupPage(String tag) {
        this.presenter.goToGroupPage(tag);
    }

    /**
     * Add items and configure our list
     */
    private void addDrawerItems() {
        final String[] menuOptions = {"Main Menu", "My Requests", "Not Started", "In Progress", "Completed", "New Request", "New Group"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuOptions);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainMenu.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
                String option = menuOptions[position];
                switch (option) {
                    case "Main Menu":
                        //presenter.goToGroupPage();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid choice");
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Where to?");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /**
     * Handle changes of going from portrait to landscape or vice versa
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
