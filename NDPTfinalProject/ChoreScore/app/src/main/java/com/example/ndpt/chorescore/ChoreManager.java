package com.example.ndpt.chorescore;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.Image;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
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

        if(currentUser != null)
        {
            //Ensure current user is the group admin
            if(currentUser.getObjectId().equals(currentGroup.getAdminId()))
            {
                //Initialize the parse object
                ParseObject chore = new ParseObject("Chore");
                chore.put("groupId", groupId);
                chore.put("description", description);
                chore.put("dueDate", dueDate);
                chore.put("points", points);
                chore.put("isApproved",false);

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
    }

    /**
     * Returns a list of all chores in the group that are still available for completion
     * @param groupId
     * @param activity
     * @return ArrayList of Chore objects
     */
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
            choreQuery.whereDoesNotExist("completerId");

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

    /**
     * Gets a list of all chores which have been submitted as complete, to be approved by the group admin
     * @param groupId
     * @param activity
     * @return ArrayList of Chore objects
     */
    public static ArrayList<Chore> getSubmittedGroupChores(final String groupId, final Activity activity)
    {
        final ArrayList<Chore> chores = new ArrayList<Chore>();

        //Query the parse database
        ParseQuery<ParseObject> choreQuery  = ParseQuery.getQuery("Chore");
        choreQuery.whereContains("groupId", groupId);
        choreQuery.whereExists("completerId");
        choreQuery.whereEqualTo("isApproved", false);

        choreQuery.findInBackground(new FindCallback<ParseObject>()
        {
            @Override
            public void done(List<ParseObject> objects, ParseException e)
            {
                if(e == null)
                {
                    //Initialize variables for creating pending chore objects
                    String choreId;
                    String description;
                    Date dueDate;
                    int points;
                    String completerId;
                    Boolean isApproved;
                    Bitmap proofImage = null;

                    for (ParseObject p:objects)
                    {
                        choreId = p.getObjectId();
                        description = p.getString("description");
                        dueDate = p.getDate("dueDate");
                        points = p.getInt("points");
                        completerId = p.getString("completerId");
                        isApproved = p.getBoolean("isApproved");
                        chores.add(new Chore(choreId,groupId,description,dueDate,points,completerId,isApproved,proofImage));
                    }
                }

                else
                {
                    //Display parse exception
                    Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        return chores;
    }

    /**
     * Updates the state of am existing chore for chore submissions and approvals
     * @param choreId
     * @param completerId setting to null will delete
     * @param isApproved
     * @param proofImage
     */
    public static void UpdateChoreState(final String choreId, final String completerId, final Boolean isApproved, final Bitmap proofImage, final Activity activity, final Runnable callback)
    {
        //Query the parse database for the chore object
        ParseQuery<ParseObject> choreQuery = ParseQuery.getQuery("Chore");
        choreQuery.whereEqualTo("objectId", choreId);

        //Retrieve the chore object from the query result
       choreQuery.findInBackground(new FindCallback<ParseObject>()
       {
           @Override
           public void done(List<ParseObject> objects, ParseException e)
           {
               if(e == null)
               {
                   ParseObject choreObject = objects.get(0);


                   //Update the chore properties
                   choreObject.put("isApproved", isApproved);

                   //Set completer id if not null
                   if(completerId == null)
                   {
                       choreObject.remove("completerId");
                   }

                   //Delete completer id if null
                   else
                   {
                       choreObject.put("completerId", completerId);
                   }

                   //Set image if not null
                   if (proofImage != null)
                   {
                       //Prepare the image file for upload
                       ByteArrayOutputStream stream = new ByteArrayOutputStream();
                       proofImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                       byte[] image = stream.toByteArray();

                       ParseFile imageFile = new ParseFile(choreId + "-proof.png", image);
                       imageFile.saveInBackground();

                       //Upload the image
                       choreObject.put("proofImage", imageFile);
                   }

                   //Save object with updated information
                   choreObject.saveInBackground(new SaveCallback()
                   {
                       @Override
                       public void done(ParseException e)
                       {
                           if(e == null)
                           {
                               //Run the callback method after updating
                               callback.run();
                           }

                       }
                   });
               } else {
                   Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
                   toast.show();
               }
           }
       });
    }

    /**
     * Updates the users points for a specific userGroup combination
     * @param userId
     * @param groupId
     * @param points amount of points to change (can be negative)
     */
    public static void UpdateUserPoints(String userId, String groupId, Integer points, Activity activity)
    {
        try
        {
            //Query the parse database for the userGroup object
            ParseQuery<ParseObject> userGroupQuery = ParseQuery.getQuery("UserGroup");
            userGroupQuery.whereEqualTo("userId", userId);
            userGroupQuery.whereEqualTo("groupId", groupId);

            //Retrieve the userGroup object from the query result
            List<ParseObject> result = userGroupQuery.find();
            ParseObject userGroupObject = result.get(0);

            //Update the chore properties
            userGroupObject.increment("points", points);

            //Update cumulative points if adding points
            if(points > 0)
            {
                userGroupObject.increment("cumulativePoints", points);
            }

            //Save object with updated information
            userGroupObject.saveInBackground();
        }

        catch (ParseException e)
        {
            //Display parse exception
            Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
            toast.show();
        }

    }
}