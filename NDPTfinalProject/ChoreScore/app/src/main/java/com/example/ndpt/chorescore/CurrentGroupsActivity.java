package com.example.ndpt.chorescore;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * CurrentGroupsActivity.java
 * Created by Nicole Dahlquist on 21/11/2015.
 *
 * This class provides functionality for the current groups activity
 */
public class CurrentGroupsActivity extends Activity
    implements CurrentGroupsButtonsFragment.OnFragmentInteractionListener,
    CurrentGroupsListFragment.OnFragmentInteractionListener,
    AdapterView.OnItemClickListener{

    //Class scope variables
    ArrayList<Group> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_current_groups);
            displayGroups();
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_groups, menu);
            return true;
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        try {
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement

            TransitionManager.MenuTransition(this, id);

            return super.onOptionsItemSelected(item);
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
            return false;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    /**
     * Click listener for current groups list view
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        try {
            //Get the selected group
            Group g = groups.get(position);

            //Set the users default group
            GroupManager.SetUserDefaultGroup(g.getGroupId(), this);

            //Message the user
            Toast toast = Toast.makeText(this, getString(R.string.success_group) + g.getName(), Toast.LENGTH_LONG);
            toast.show();
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }
    /**
     * Displays a list of groups in the current group list view
     */
    public void displayGroups()
    {
        try {
            ParseUser currentUser = UserManager.CheckCachedUser(this);
            if (currentUser != null) {
                //Get listview from the view
                ListView groupLv = (ListView) findViewById(R.id.lv_current_groups);

                //Get the users groups and default group from parse database
                groups = GroupManager.RetrieveUserGroups(currentUser.getObjectId(), this);
                String defaultGroup = currentUser.getString("defaultGroupId");
                Integer selected = 0;

                //Initialize the listview
                ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
                for (Group g : groups) {
                    //Set the selected item for the default group
                    if (g.getGroupId().equals(defaultGroup)) {
                        selected = data.size();
                    }

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("name", g.getName());
                    data.add(map);
                }

                int resource = R.layout.listview_group;
                String[] from = {"name"};
                int[] to = {R.id.tv_list_group_name};

                SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
                groupLv.setOnItemClickListener(this);
                groupLv.setAdapter(adapter);

                //Set the selector
                groupLv.setSelector(R.drawable.selector);
                groupLv.setItemChecked(selected, true);
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }
}
