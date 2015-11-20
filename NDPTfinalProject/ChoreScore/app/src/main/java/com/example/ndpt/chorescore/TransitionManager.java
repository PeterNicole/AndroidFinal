package com.example.ndpt.chorescore;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * TransitionManager.java
 * Created by Peter Thomson on 20/11/2015.
 *
 * Class for simplification of opening/closing activities and fragments
 */

public class TransitionManager
{

    private static Class previousActivity = MainActivity.class;

    /**
     * Overloaded method for default closeCurrent = true
     * @param currentActivity current activity
     * @param transitionActivity activity to be opened
     */
    static public void ActivityTransition(Activity currentActivity,  Class transitionActivity)
    {
        ActivityTransition(currentActivity, transitionActivity, true);
    }

    /**
     * Transitions to the specified activity
     * @param currentActivity current activity
     * @param transitionActivity activity to be opened
     * @param closeCurrent if true close the current activity
     */
    static public void ActivityTransition(Activity currentActivity, Class transitionActivity, boolean closeCurrent)
    {
        previousActivity = currentActivity.getClass();
        Intent intent = new Intent(currentActivity,transitionActivity);
        currentActivity.startActivity(intent);
        if(closeCurrent){currentActivity.finish();}
    }

    /**
     * Transitions to the last known activity which was opened
     * @param currentActivity current activity
     * @param closeCurrent if true close the current activity
     */
    static public void PreviousActivity(Activity currentActivity, boolean closeCurrent)
    {
        Intent intent = new Intent(currentActivity,previousActivity.getClass());
        currentActivity.startActivity(intent);
        if(closeCurrent){currentActivity.finish();}
    }

    /**
     * For opening fragments on current activity, work in progress
     */
    static public void FragmentTransition()
    {
        //Not sure yet
    }
}
