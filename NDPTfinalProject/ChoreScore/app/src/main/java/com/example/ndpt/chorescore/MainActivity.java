package com.example.ndpt.chorescore;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * MainActivity.java
 * Created by Nicole Dahlquist on 11/11/2015.
 *
 * This class provides functionality for the main activity
 */

public class MainActivity extends Activity
        implements MainUsageDisplayFragment.OnFragmentInteractionListener, MainPageOptionsFragment.OnFragmentInteractionListener{

    //Class scope variables
    private TextView tvNumberUsers;
    private TextView tvNumberGroups;
    private TextView tvNumberChores;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            CheckLogin();
            DisplayStats();
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }


    /**
     * Transitions to the current groups activity if the user is already logged in
     */
    public void CheckLogin()
    {
        ParseUser currentUser = UserManager.CheckCachedUser(this);
        if(currentUser != null)
        {
            TransitionManager.ActivityTransition(this, CurrentGroupsActivity.class);
        }
    }
    /**
     * Populates the textviews on the activity with the stats from the parse database
     */
    public void DisplayStats()
    {
        //Initialize the controls from the view
        tvNumberUsers = (TextView)findViewById(R.id.tv_main_usage_user_count);
        tvNumberGroups = (TextView)findViewById(R.id.tv_main_usage_groups_count);
        tvNumberChores = (TextView)findViewById(R.id.tv_main_usage_chore_count);

        //Call methods which query database for information
        String userCount = Integer.toString(UserManager.getUserCount(this));
        String groupCount = Integer.toString(GroupManager.getGroupCount(this));
        String choreCount = Integer.toString(ChoreManager.getChoreCount(this));

        //Display the information
        tvNumberUsers.setText(userCount);
        tvNumberGroups.setText(groupCount);
        tvNumberChores.setText(choreCount);
    }
}
