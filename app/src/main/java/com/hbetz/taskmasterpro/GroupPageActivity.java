package com.hbetz.taskmasterpro;
import com.hbetz.taskmasterpro.adapters.MembersAdapter;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class GroupPageActivity extends AppCompatActivity {

    private static final String TAG = "GroupPageActivity";
    private ListView mDrawerList;
    private ListView membersList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private GroupPagePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);
        Bundle extras = getIntent().getExtras();

        membersList = (ListView) findViewById(R.id.members_listview);

        mDrawerList = (ListView) findViewById(R.id.memberNavList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (this.presenter == null) {
            this.presenter = new GroupPagePresenter(this, this, extras);
        }

    }

    /**
     * Sets the adapter for the listview
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter) {
        this.membersList.setAdapter(adapter);
        ((BaseAdapter) this.membersList.getAdapter()).notifyDataSetChanged();
    }

    /**
     * Add items and configure our list
     */
    private void addDrawerItems() {
        final String[] menuOptions = {"Main Menu", "My Groups", "My Requests", "Not Started", "In Progress", "Completed", "New Request", "New Group"};
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
