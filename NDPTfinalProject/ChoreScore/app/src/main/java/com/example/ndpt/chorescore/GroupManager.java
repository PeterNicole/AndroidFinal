package com.example.ndpt.chorescore;
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
    public static Group RetrieveGroup(String groupId)
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
        }

        return new Group(groupId, admin, name);
    }

    /**
     * Creates a new group in the parse database and returns that group
     * @param name
     * @param adminId
     * @return Group
     */
    public static void CreateGroup(String name, String adminId)
    {
        //Check for group with same name
        ArrayList<Group> sameNameGroups = RetrieveAll(name);

        //Name is unique
        if(sameNameGroups.size() == 0)
        {
            //Initialize the parse object
            ParseObject group = new ParseObject("Group");
            group.put("adminId", adminId);
            group.put("name", name);

            //Save the parse object
            group.saveInBackground();
        }

        else
        {
            //Fail to create group, name taken
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
    public static ArrayList<Group> RetrieveAll()
    {
        return RetrieveAll("");
    }

    /**
     * Returns a list of all groups with name matching the string
     * @param groupName search fragment for group name
     * @return ArrayList of all groups
     */
    public static ArrayList<Group> RetrieveAll(String groupName)
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
                groupId = p.getString("objectId");
                admin = p.getString("admin");
                name = p.getString("name");
                groups.add(new Group(groupId,admin,name));
            }
        }
        catch (ParseException e)
        {
            //Display parse exception
        }

        return groups;
    }
}
