package com.example.ndpt.chorescore;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * LoginActivity.java
 * Created by Nicole Dahlquist on 20/11/2015.
 *
 * This class provides functionality for the login activity
 */
public class LoginActivity extends Activity
    implements LoginButtonsFragment.OnFragmentInteractionListener,
    LoginFormFragment.OnFragmentInteractionListener,
    GoBackButtonFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // No menu
        //getMenuInflater().inflate(R.menu.menu_login, menu);
        try {
            return true;
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        try {
            int id = item.getItemId();

            TransitionManager.MenuTransition(this, id);

            return super.onOptionsItemSelected(item);
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
            return false;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
