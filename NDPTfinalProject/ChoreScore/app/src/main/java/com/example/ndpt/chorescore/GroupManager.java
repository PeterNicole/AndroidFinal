package com.example.ndpt.chorescore;
import android.app.Activity;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter Thomson on 01/12/2015.
 * Class for retrieving and creating chore groups in the parse database
 */
public class GroupManager
{
    /**
     * Retrieves a group from the parse database using its id
     * @param groupId
     * @return Group
     */
    public static Group RetrieveGroup(String groupId, Activity activity)
    {
        String name = "";
        String admin = "";

        try
        {
            //Query the parse database
            ParseQuery<ParseObject> groupQuery = ParseQuery.getQuery("Group");
            groupQuery.whereEqualTo("objectId", groupId );
            List<ParseObject> result = groupQuery.find();
            ParseObject groupObject = result.get(0);
            name = groupObject.getString("name");
            admin = groupObject.getString("admin");
        }

        catch (ParseException e)
        {
            //Display parse exception
            Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
            toast.show();
        }

        return new Group(groupId, admin, name);
    }

    /**
     * Creates a new group in the parse database and returns that group
     * @param name
     * @param adminId
     * @return Group
     */
    public static void CreateGroup(final String name, final String adminId, final Activity activity)
    {
        try {
            //Check for group with same name
            ArrayList<Group> sameNameGroups = RetrieveAll(name, activity);

            //Name is unique
            if (sameNameGroups.size() == 0) {
                //Initialize the parse object
                final ParseObject group = new ParseObject("Group");
                group.put("admin", adminId);
                group.put("name", name);

                //Save the parse object
                group.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            //Add admin to the group as a member
                            JoinGroup(adminId, group.getObjectId(), activity);
                        } else {
                            //Display parse error
                            Toast toast = Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
            } else {
                //Fail to create group, name taken
                Toast toast = Toast.makeText(activity, activity.getString(R.string.error_group_name_taken), Toast.LENGTH_LONG);
                toast.show();
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    /**
     * Updates the groups admin with the specified user id
     * @param groupId
     * @param userId
     * @param activity
     */
    public static void UpdateGroupAdmin(String groupId, final String userId, final Activity activity)
    {
        try {
            //Query the parse database for the group object
            ParseQuery<ParseObject> groupQuery = ParseQuery.getQuery("Group");
            groupQuery.whereEqualTo("objectId", groupId);

            //Retrieve the group object from the query result
            groupQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        ParseObject groupObject = objects.get(0);

                        //Update the groups admin
                        groupObject.put("admin", userId);

                        //Save object with updated information
                        groupObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    //Display parse error message
                                    Toast toast = Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        });
                    } else {
                        //Display parse error message
                        Toast toast = Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    /**
     * Deletes the specified group from the parse database
     * @param groupId
     * @param activity
     */
    public static void DeleteGroup(String groupId, final Activity activity)
    {
        //Delete the group object
        try
        {
            ParseObject.createWithoutData("Group",groupId).deleteEventually();
        }

        catch(Exception e)
        {
            //Display error message
            Toast toast = Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public static void DeleteUserGroup(String groupId, String userId, final Activity activity)
    {
        try {
            //Query the data base for the userGroup object
            ParseQuery<ParseObject> userGroupQuery = ParseQuery.getQuery("UserGroup");
            userGroupQuery.whereEqualTo("userId", userId);
            userGroupQuery.whereEqualTo("groupId", groupId);

            //Execute the querty
            userGroupQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        //Delete the object
                        ParseObject userGroupObject = objects.get(0);
                        userGroupObject.deleteEventually();
                    } else {
                        //Display parse error message
                        Toast toast = Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    /**
     * Returns a list of all groups with name matching the string
     * @param groupName search fragment for group name
     * @return ArrayList of all groups
     */
    public static ArrayList<Group> RetrieveAll(String groupName, Activity activity)
    {
        ArrayList<Group> groups = new ArrayList<Group>();
        String name = "";
        String admin = "";
        String groupId = "";

        try
        {
            //Query the parse database
            ParseQuery<ParseObject> groupQuery = ParseQuery.getQuery("Group");
            groupQuery.whereContains("name", groupName);
            List<ParseObject> result = groupQuery.find();

            //Add each group to the array list
            for (ParseObject p: result)
            {
                groupId = p.getObjectId();
                admin = p.getString("admin");
                name = p.getString("name");
                groups.add(new Group(groupId,admin,name));
            }
        }
        catch (ParseException e)
        {
            //Display parse exception
            Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
            toast.show();
        }

        return groups;
    }

    /**
     * Returns a list of all the groups a user belongs to
     * @param userId
     * @return
     */
    public static ArrayList<String> RetrieveUserGroupIds(String userId, Activity activity)
    {
        ArrayList<String> groupIds = new ArrayList<String>();

        try
        {
            //Query the parse database
            ParseQuery<ParseObject> groupQuery = ParseQuery.getQuery("UserGroup");
            groupQuery.whereContains("userId", userId);
            List<ParseObject> result = groupQuery.find();

            //Add each group to the array list
            for (ParseObject p: result)
            {
                groupIds.add(p.getString("groupId"));
            }
        }
        catch (ParseException e)
        {
            //Display parse exception
            Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
            toast.show();
        }

        return groupIds;
    }

    /**
     * Returns a list of all member names for a particular group
     * @param groupId
     * @return ArrayList<String>() containing member names
     */
    public static ArrayList<String> RetrieveGroupMemberNames(String groupId, Activity activity)
    {
        ArrayList<String> memberNames = new ArrayList<String>();
        ArrayList<String> memberIds = new ArrayList<String>();

        try
        {
            //Query the parse database for the member ids
            ParseQuery<ParseObject> memberIdQuery = ParseQuery.getQuery("UserGroup");
            memberIdQuery.whereContains("groupId", groupId);
            List<ParseObject> result = memberIdQuery.find();

            //Initialize member name query
            ParseQuery<ParseObject> memberNameQuery = ParseQuery.getQuery("_User");

            //Add each member id to the member name query
            for (ParseObject p: result)
            {
                memberIds.add(p.getString("userId"));
            }

            //Query the parse database for the member names
            memberNameQuery.whereContainedIn("objectId", memberIds);
            List<ParseObject> nameResult = memberNameQuery.find();

            for (ParseObject p: nameResult)
            {
                //Add each name to the array list
                memberNames.add(p.getString("username"));
            }
        }
        catch (ParseException e)
        {
            //Display parse exception
            Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
            toast.show();
        }

        return memberNames;
    }

    /**
     * Returns a list of all member ids for a particular group
     * @param groupId
     * @return ArrayList<String>() containing member ids
     */
    public static ArrayList<String> RetrieveGroupMemberIds(String groupId, Activity activity)
    {
        ArrayList<String> memberIds = new ArrayList<String>();

        try
        {
            //Query the parse database for the member ids
            ParseQuery<ParseObject> memberIdQuery = ParseQuery.getQuery("UserGroup");
            memberIdQuery.whereContains("groupId", groupId);
            List<ParseObject> result = memberIdQuery.find();

            //Add each member id to the member name query
            for (ParseObject p: result)
            {
                memberIds.add(p.getString("userId"));
            }

        }
        catch (ParseException e)
        {
            //Display parse exception
            Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
            toast.show();
        }

        return memberIds;
    }

    /**
     * Returns a list of groups for the specified user
     * @param userId
     * @param activity
     * @return list of user grooups
     */
    public static ArrayList<Group> RetrieveUserGroups(String userId, Activity activity)
    {
        ArrayList<String> groupIds = RetrieveUserGroupIds(userId,activity);
        ArrayList<Group> userGroups = new ArrayList<Group>();
        try
        {
            //Query the parse database
            ParseQuery<ParseObject> groupQuery = ParseQuery.getQuery("Group");
            groupQuery.whereContainedIn("objectId", groupIds);

            List<ParseObject> result = groupQuery.find();

            //Variables for creating group object from parse result
            String groupId ="";
            String admin ="";
            String name = "";

            //Add each group to the array list
            for (ParseObject p: result)
            {
                groupId = p.getObjectId();
                admin = p.getString("admin");
                name = p.getString("name");
                userGroups.add(new Group(groupId,admin,name));
            }
        }
        catch (ParseException e)
        {
            //Display parse exception
            Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
            toast.show();
        }
        return userGroups;
    }

    /**
     * Adds the specified user to the specified group
     * @param groupId
     * @param userId
     */
    public static void JoinGroup( String userId,String groupId, Activity activity)
    {
        try {
            ArrayList<String> currentUserGroups = RetrieveUserGroupIds(userId, activity);

            //Check if the user is already a part of this group
            if (currentUserGroups.contains(groupId)) {
                //User already part of this group
                Toast toast = Toast.makeText(activity, activity.getString(R.string.error_already_in_group), Toast.LENGTH_LONG);
                toast.show();
            }

            //Add the group
            else {
                //Initialize the parse object for UserGroup
                ParseObject userGroup = new ParseObject("UserGroup");
                userGroup.put("userId", userId);
                userGroup.put("groupId", groupId);
                userGroup.put("points", 0);
                userGroup.put("cumulativePoints", 0);

                //Save the parse object
                userGroup.saveInBackground();

                //Set the joined group to the users default group
                SetUserDefaultGroup(groupId, activity);

                //Display success message
                Toast toast = Toast.makeText(activity, activity.getString(R.string.success_group_joined), Toast.LENGTH_LONG);
                toast.show();
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    /**
     * Sets the default group for the current user and group
     * @param groupId
     * @param activity
     */
    public static void SetUserDefaultGroup( String groupId, Activity activity)
    {
        try {
            //Get the current user, redirect if noone logged in
            ParseUser currentUser = UserManager.CheckCachedUser(activity);

            if (currentUser != null) {
                //Set the current users group id
                currentUser.put("defaultGroupId", groupId);

                currentUser.saveInBackground();
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    /**
     * Gets the total count of groups from the parse database
     * @param activity
     * @return number of groups
     */
    static public Integer getGroupCount(Activity activity)
    {
        Integer groupCount = 0;
        try
        {
            //Query the groups table to get the count
            ParseQuery<ParseObject> groupQuery = ParseQuery.getQuery("Group");
            List<ParseObject> result = groupQuery.find();
            groupCount = result.size();
        }

        catch (ParseException e)
        {
            //Display parse exception
            Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
            toast.show();
        }

        return groupCount;

    }
}
