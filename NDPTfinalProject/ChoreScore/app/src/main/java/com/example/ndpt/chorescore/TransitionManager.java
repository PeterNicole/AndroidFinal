package com.example.ndpt.chorescore;

import android.app.Activity;
import android.content.Intent;

import java.util.HashMap;

/**
 * TransitionManager.java
 * Created by Peter Thomson on 20/11/2015.
 *
 * Class for simplification of opening/closing activities and fragments
 */

public class TransitionManager
{

    private static Class previousActivity = MainActivity.class;
    private static HashMap<Integer,Class> menuMap;

    static private void populateMenuMap()
    {
        //Populates the menu map with menu button ids and their associated activity class
        menuMap = new HashMap<Integer,Class>();
        menuMap.put(R.id.mi_groups,CurrentGroups.class);
        menuMap.put(R.id.mi_chores,CurrentGroups.class);
        menuMap.put(R.id.mi_points,CurrentGroups.class);
        menuMap.put(R.id.mi_logout, CurrentGroups.class);

    }
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
        Intent intent = new Intent(currentActivity,previousActivity);
        currentActivity.startActivity(intent);
        if(closeCurrent){currentActivity.finish();}
    }

    /**
     * Handles transitions from the menu items specifically
     * @param currentActivity current activity
     * @param menuBtnId id of the selected menu button
     */
    static public void MenuTransition(Activity currentActivity, int menuBtnId)
    {
        if (menuMap == null)
        {
            populateMenuMap();
        }

        ActivityTransition(currentActivity, menuMap.get(menuBtnId));

    }

    /**
     * For opening fragments on current activity, work in progress
     */
    static public void FragmentTransition()
    {
        //Not sure yet
    }
}
