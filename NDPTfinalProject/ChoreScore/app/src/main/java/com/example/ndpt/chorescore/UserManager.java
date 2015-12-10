package com.example.ndpt.chorescore;

import android.app.Activity;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

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
                    Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
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
    static public void LoginUser(String userName, String password, final Activity activity)
    {
        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                //Successful login
                if (user != null) {
                    //Add the user id to the installation on login for targetted notifications
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    installation.put("userId", user.getObjectId());
                    installation.saveInBackground();
                    TransitionManager.ActivityTransition(activity, CurrentGroupsActivity.class);
                }

                //Login fail
                else {
                    Toast toast = Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
    }

    /**
     * Checks if current user is logged in, returns a user if they are, redirects to login page if not
     * @param activity
     * @return ParseUser object for current user
     */
    static public ParseUser CheckCachedUser(Activity activity)
    {
        ParseUser currentUser = ParseUser.getCurrentUser();

        //If user is not currently logged in
        if(currentUser == null && activity.getClass() != MainActivity.class)
        {
            //Prompt login
            TransitionManager.ActivityTransition(activity,MainActivity.class);
        }

        return currentUser;
    }

    /**
     * Logs out the current user
     */
    static public void LogoutUser(Activity activity)
    {
        ParseUser.getCurrentUser().logOut();
        if(ParseUser.getCurrentUser() == null)
        {
            Toast toast = Toast.makeText(activity,R.string.success_logout, Toast.LENGTH_LONG);
            toast.show();
            TransitionManager.ActivityTransition(activity, MainActivity.class);
        }
    }

    /**
     * Gets the name of a user from the parse database
     * @param userId
     * @param activity
     * @return user name
     */
    static public String getUserName(String userId, Activity activity)
    {
        String name = "";
        try
        {
            ParseQuery<ParseObject> nameQuery = ParseQuery.getQuery("_User");
            nameQuery.whereEqualTo("objectId", userId);
            List<ParseObject> result = nameQuery.find();
            name = result.get(0).getString("username");

        }

        catch (ParseException e)
        {
            //Display parse exception
            Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
            toast.show();
        }

        return name;

    }

    /**
     * Gets the total count of registered users from the parse database
     * @param activity
     * @return number of users
     */
    static public Integer getUserCount(Activity activity)
    {
        Integer userCount = 0;
        try
        {
            ParseQuery<ParseObject> userQuery = ParseQuery.getQuery("_User");
            List<ParseObject> result = userQuery.find();
            userCount = result.size();

        }

        catch (ParseException e)
        {
            //Display parse exception
            Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
            toast.show();
        }

        return userCount;

    }

}