package com.example.ndpt.chorescore;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Peter Thomson on 06/12/2015.
 * Class for handling events related to chores
 */
public class ChoreManager
{
    /**
     * Adds a new chore to the parse database
     * @param groupId
     * @param description
     * @param dueDate
     * @param points
     * @param activity
     */
    public static void createChore(String groupId, String description, Date dueDate, int points, Activity activity)
    {
        Group currentGroup = GroupManager.RetrieveGroup(groupId,activity);
        ParseUser currentUser = UserManager.CheckCachedUser(activity);

        //Ensure current user is the group admin
        if(currentUser.getObjectId().equals(currentGroup.getAdminId()))
        {
            //Initialize the parse object
            ParseObject chore = new ParseObject("Chore");
            chore.put("groupId", groupId);
            chore.put("description", description);
            chore.put("dueDate", dueDate);
            chore.put("points", points);

            //Save the parse object
            chore.saveInBackground();

            //Show success message
            Toast toast = Toast.makeText(activity, activity.getString(R.string.success_add_chore), Toast.LENGTH_LONG);
            toast.show();
        }

        else
        {
            //Show error message
            Toast toast = Toast.makeText(activity, activity.getString(R.string.error_add_chore), Toast.LENGTH_LONG);
            toast.show();
        }

    }

    public static ArrayList<Chore> getPendingGroupChores(String groupId, Activity activity)
    {
        ArrayList<Chore> chores = new ArrayList<Chore>();

        try
        {
            //Get today's date
            Date currentDate = new Date();

            //Query the parse database
            ParseQuery<ParseObject> choreQuery  = ParseQuery.getQuery("Chore");
            choreQuery.whereContains("groupId", groupId);
            choreQuery.whereGreaterThanOrEqualTo("dueDate", currentDate);

            List<ParseObject> result = choreQuery.find();

            //Initialize variables for creating pending chore objects
            String choreId;
            String description;
            Date dueDate;
            int points;
            String completerId = "";
            Boolean isApproved = false;
            Bitmap proofImage = null;

            for (ParseObject p:result)
            {
                choreId = p.getObjectId();
                description = p.getString("description");
                dueDate = p.getDate("dueDate");
                points = p.getInt("points");
                chores.add(new Chore(choreId,groupId,description,dueDate,points,completerId,isApproved,proofImage));
            }
        }

        catch (com.parse.ParseException e)
        {
            //Display parse exception
            Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
            toast.show();
        }

        return chores;
    }
}
