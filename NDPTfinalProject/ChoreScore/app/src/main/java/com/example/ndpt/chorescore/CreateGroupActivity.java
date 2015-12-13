package com.example.ndpt.chorescore;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;

/**
 * CreateGroupActivity.java
 * Created by Nicole Dahlquist on 21/11/2015.
 *
 * This class provides functionality for group creation
 */
public class CreateGroupActivity extends Activity
    implements CreateGroupFormFragment.OnFragmentInteractionListener,
    SubmitResetButtonsFragment.OnFragmentInteractionListener,
    GoBackButtonFragment.OnFragmentInteractionListener{

    //Class scope variables
    private Button btnReset;
    private Button btnSubmit;
    private final int GROUP_NAME_LENGTH = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_group);
            controlCreation();
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

    private void controlCreation(){
        try {
            btnReset = (Button) findViewById(R.id.btnReset);
            btnSubmit = (Button) findViewById(R.id.btnSubmit);
            final Activity activity = this;
            Button[] buttons = {btnSubmit, btnReset};
            for (Button b : buttons) {
                final int id = b.getId();
                b.setOnClickListener((new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //Get text from the signup personal fragment
                        EditText groupNameEv = (EditText) activity.findViewById(R.id.etCreateGroupName);

                        //Initialize strings for user creation
                        String groupName = groupNameEv.getText().toString();

                        if (id == R.id.btnSubmit) {
                            //Check if user is logged in, if not redirect to login page
                            UserManager.CheckCachedUser(activity);
                            if (groupName.length() < GROUP_NAME_LENGTH) {
                                groupNameEv.setError(getString(R.string.error_group_name_length) + GROUP_NAME_LENGTH + getString(R.string.error_characters_long));
                            } else {
                                GroupManager.CreateGroup(groupName, ParseUser.getCurrentUser().getObjectId(), activity);
                            }

                        } else if (id == R.id.btnReset) {
                            //clear
                            groupNameEv.getText().clear();
                        }
                    }
                }));
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }
}
