package com.example.ndpt.chorescore;

/**
 * Created by Peter Thomson on 24/11/2015.
 *
 * This class extends the application class to override the oncreate method and initialize Parse
 */
import android.app.Application;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class App extends Application
{

    @Override public void onCreate()
    {
        try {
            super.onCreate();

            // Enable Local Datastore.
            Parse.enableLocalDatastore(this);

            Parse.initialize(this, "eeFeLikeQiwpaLAtdDuboSkFYpNEQZgYzlSWvlFU", "ol43zjzNsEiCtRqVHD1i0z3V7BLLXOc4DyD4Mhgy");

            ParseUser user = ParseUser.getCurrentUser();

            ParseInstallation installation = ParseInstallation.getCurrentInstallation();

            if (user != null) {
                installation.put("userId", user.getObjectId());
            }

            installation.saveInBackground();
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }
}
