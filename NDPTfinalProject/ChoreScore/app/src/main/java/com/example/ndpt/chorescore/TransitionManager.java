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
        try {
            //Populates the menu map with menu button ids and their associated activity class
            menuMap = new HashMap<Integer, Class>();
            menuMap.put(R.id.mi_groups, CurrentGroupsActivity.class);
            menuMap.put(R.id.mi_chores, PendingChoresActivity.class);
            menuMap.put(R.id.mi_points, PointRedemptionActivity.class);
            menuMap.put(R.id.mi_about, AboutActivity.class);
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }
    /**
     * Overloaded method for default closeCurrent = true
     * @param currentActivity current activity
     * @param transitionActivity activity to be opened
     */
    static public void ActivityTransition(Activity currentActivity,  Class transitionActivity)
    {
        try {
            ActivityTransition(currentActivity, transitionActivity, true);
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    /**
     * Transitions to the specified activity
     * @param currentActivity current activity
     * @param transitionActivity activity to be opened
     * @param closeCurrent if true close the current activity
     */
    static public void ActivityTransition(Activity currentActivity, Class transitionActivity, boolean closeCurrent)
    {
        try {
            previousActivity = currentActivity.getClass();
            Intent intent = new Intent(currentActivity, transitionActivity);
            currentActivity.startActivity(intent);
            if (closeCurrent) {
                currentActivity.finish();
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    /**
     * Transitions to the last known activity which was opened
     * @param currentActivity current activity
     * @param closeCurrent if true close the current activity
     */
    static public void PreviousActivity(Activity currentActivity, boolean closeCurrent)
    {
        try {
            Intent intent = new Intent(currentActivity, previousActivity);
            currentActivity.startActivity(intent);
            if (closeCurrent) {
                currentActivity.finish();
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    /**
     * Handles transitions from the menu items specifically
     * @param currentActivity current activity
     * @param menuBtnId id of the selected menu button
     */
    static public void MenuTransition(Activity currentActivity, int menuBtnId)
    {
        try {
            //Logout menu item
            if (menuBtnId == R.id.mi_logout) {
                UserManager.LogoutUser(currentActivity);
            }

            //Activity transition menu items
            else {
                if (menuMap == null) {
                    populateMenuMap();
                }

                ActivityTransition(currentActivity, menuMap.get(menuBtnId));
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }

    }

    /**
     * For opening fragments on current activity, work in progress
     */
    static public void FragmentTransition()
    {
        //Not sure yet
    }
}
