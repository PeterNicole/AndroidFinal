package com.example.ndpt.chorescore;

/**
 * Created by Peter Thomson on 08/12/2015.
 */

import android.app.Activity;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Class for handling client notification pushes
 */
public class NotificationPusher
{
    static public void PushMessageToUser(String userId, String message)
    {
        try {
            ParsePush push = new ParsePush();
            ParseQuery<ParseInstallation> parseQuery = ParseQuery.getQuery(ParseInstallation.class);
            parseQuery.whereEqualTo("userId", userId);
            push.setQuery(parseQuery);
            push.setMessage(message);
            push.sendInBackground();
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

}
