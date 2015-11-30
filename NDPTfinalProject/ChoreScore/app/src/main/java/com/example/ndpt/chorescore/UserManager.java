package com.example.ndpt.chorescore;

import android.app.Activity;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Peter Thomson on 24/11/2015.
 *
 * Class for handling user creations and logins, and any other functions specifc to users
 */
public class UserManager
{
    /**
     * Method for creating a new user in the parse database
     * @param userName user name
     * @param password user password
     * @return Boolean returns true if user created, false if not
     */
    static public void CreateUser(String userName,String password, String firstName, String lastName, String email, final Activity activity)
    {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(userName);
        newUser.setPassword(password);
        //Email
        if (email != null)
        {
            newUser.setEmail(email);
        }

        //Additional custom fields
        if(firstName != null)
        {
            newUser.put("firstName", firstName);
        }
        if (lastName != null)
        {
            newUser.put("lastName", lastName);
        }

        newUser.signUpInBackground(new SignUpCallback()
        {
            @Override
            public void done(ParseException e)
            {
                //Redirect to current groups on successful login
                if (e == null)
                {
                    TransitionManager.ActivityTransition(activity,CurrentGroupsActivity.class);
                }

                //Display login error to user
                else
                {
                    Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    /**
     * Method for logging in a parse user
     * @param userName user name
     * @param password user password
     */
    static public void LoginUser(String userName, String password)
    {
        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null)
                {
                    //Successful login
                }
                else
                {
                    //login fail
                }

            }
        });
    }

    static public void CheckCachedUser()
    {
        ParseUser currentUser = ParseUser.getCurrentUser().getCurrentUser();
        if(currentUser!= null)
        {
            //User currently cached on this device, no login required
        }
        else
        {
            //Prompt login
        }
    }
}