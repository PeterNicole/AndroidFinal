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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckLogin();
        DisplayStats();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // No menu
        getMenuInflater().inflate(R.menu.menu_groups, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        TransitionManager.MenuTransition(this, id);


        return super.onOptionsItemSelected(item);
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
