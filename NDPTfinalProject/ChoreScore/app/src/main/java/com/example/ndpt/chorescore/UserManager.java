package com.example.ndpt.chorescore;

import android.app.Activity;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by Peter Thomson on 24/11/2015.
 *
 * Class for handling user creations and logins, and any other functions specific to users
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
        if (email.equals(""))
        {
            email = userName + "@chorescore.com";
        }

        newUser.setEmail(email);

        //Additional custom fields
        if(firstName != null)
        {
            newUser.put("firstName", firstName);
        }
        if (lastName != null)
        {
            newUser.put("lastName", lastName);
        }

        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                //Redirect to current groups on successful login
                if (e == null) {
                    TransitionManager.ActivityTransition(activity, CurrentGroupsActivity.class);
                }

                //Display login error to user
                else {
                    Toast toast = Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG);
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

    /**
     * Check if the user has a valid default group assigned, redirect and display message if not
     * @param activity
     */
    static public boolean UserHasDefaultGroup(Activity activity)
    {
        ParseUser currentUser = ParseUser.getCurrentUser();
        boolean hasGroup = true;
        if(currentUser != null)
        {
            String defaultGroupId = currentUser.getString("defaultGroupId");

            //Group id is null
            if(defaultGroupId == null)
            {
                hasGroup = false;
            }

            else
            {
                try
                {
                    //Query groups table for group matching the users default group id
                    ParseQuery<ParseObject> groupQuery = ParseQuery.getQuery("Group");
                    groupQuery.whereContains("objectId", defaultGroupId);
                    List<ParseObject> result = groupQuery.find();

                    //Redirect to previous activity if group is invalid or does not exist
                    if(result.size() <= 0)
                    {
                        hasGroup = false;
                    }
                }

                catch (ParseException e)
                {
                    hasGroup = false;
                }
            }
        }

        if(!hasGroup)
        {
            TransitionManager.PreviousActivity(activity,true);

            //Display no group error
            Toast toast = Toast.makeText(activity,activity.getString(R.string.error_no_group),Toast.LENGTH_LONG);
            toast.show();
        }

        return hasGroup;
    }

    static public void LeaveCurrentGroup(Group group, final Activity activity)
    {
        try
        {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if(currentUser != null && currentUser.getString("defaultGroupId") != null)
            {
                //Check if the member is the only member of the group
                ArrayList<String> groupMembers = GroupManager.RetrieveGroupMemberIds(group.getGroupId(), activity);
                groupMembers.remove(currentUser.getObjectId());

                if(groupMembers.size() <= 0)
                {
                    //Delete the group if last member
                    GroupManager.DeleteGroup(group.getGroupId(), activity);
                }

                else if (currentUser.getObjectId().equals(group.getAdminId()))
                {
                    //Set new group admin if the admin is leaving and there are other members
                    GroupManager.UpdateGroupAdmin(group.getGroupId(),groupMembers.get(0),activity);
                }

                //Check if the user has other groups
                ArrayList<String> userGroups = GroupManager.RetrieveUserGroupIds(currentUser.getObjectId(),activity);
                userGroups.remove(group.getGroupId());

                if(userGroups.size() > 0)
                {
                    //Set new default group if user is in other groups
                    currentUser.put("defaultGroupId", userGroups.get(0));
                }

                else
                {
                    //Reset the current users default group
                    currentUser.remove("defaultGroupId");
                }

                //Remove the user from the group
                GroupManager.DeleteUserGroup(group.getGroupId(), currentUser.getObjectId(), activity);


                currentUser.saveInBackground(new SaveCallback()
                {
                    @Override
                    public void done(ParseException e)
                    {
                        if (e == null)
                        {
                            //Transition to current groups activity
                            TransitionManager.ActivityTransition(activity, CurrentGroupsActivity.class);
                        }
                        else
                        {
                            //Display parse error
                            Toast toast = Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
            }
        }

        catch (Exception e)
        {
            //Display error
            Toast toast = Toast.makeText(activity,activity.getString(R.string.error_no_group),Toast.LENGTH_LONG);
            toast.show();
        }
    }
}