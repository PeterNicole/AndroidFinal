package com.example.ndpt.chorescore;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * SignUp.java
 * Created by Nicole Dahlquist on 21/11/2015.
 *
 * This class provides functionality for the signup activity
 */
public class SignUp extends Activity
    implements SignupButtons.OnFragmentInteractionListener,
        SignupPersonal.OnFragmentInteractionListener,
        SignupSecurity.OnFragmentInteractionListener,
        GoBackButton.OnFragmentInteractionListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // No menu
       // getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        TransitionManager.MenuTransition(this, id);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
