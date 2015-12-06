package com.example.ndpt.chorescore;
import android.app.Activity;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
    public static void CreateGroup(String name, String adminId, Activity activity)
    {
        //Check for group with same name
        ArrayList<Group> sameNameGroups = RetrieveAll(name, activity);

        //Name is unique
        if(sameNameGroups.size() == 0)
        {
            //Initialize the parse object
            ParseObject group = new ParseObject("Group");
            group.put("admin", adminId);
            group.put("name", name);

            //Save the parse object
            group.saveInBackground();
        }

        else
        {
            //Fail to create group, name taken
            Toast toast = Toast.makeText(activity,activity.getString(R.string.error_group_name_taken),Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Deletes the specified group
     * @param groupId
     */
    public static void DeleteGroup(String groupId)
    {

    }

    /**
     * Returns a list of all groups
     * @return ArrayList of all groups
     */
    public static ArrayList<Group> RetrieveAll(Activity activity)
    {
        return RetrieveAll("", activity);
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
            groupQuery.whereContains("name", groupName );
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
            groupQuery.whereContains("userId", userId );
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
     * Adds the specified user to the specified group
     * @param groupId
     * @param userId
     */
    public static void JoinGroup( String userId,String groupId, Activity activity)
    {
        ArrayList<String> currentUserGroups = RetrieveUserGroupIds(userId, activity);

        //Check if the user is already a part of this group
        if(currentUserGroups.contains(groupId))
        {
            //User already part of this group
            Toast toast = Toast.makeText(activity,activity.getString(R.string.error_already_in_group),Toast.LENGTH_LONG);
            toast.show();
        }

        //Add the group
        else
        {
            //Initialize the parse object for UserGroup
            ParseObject userGroup = new ParseObject("UserGroup");
            userGroup.put("userId",userId);
            userGroup.put("groupId",groupId);
            userGroup.put("points",0);

            //Save the parse object
            userGroup.saveInBackground();
        }

    }
}
