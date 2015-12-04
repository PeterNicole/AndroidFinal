package com.example.ndpt.chorescore;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * ChoreDetailsActivity.java
 * Created by Nicole Dahlquist on 02/12/2015.
 *
 * This class provides functionality for the chore details activity
 */
public class ChoreDetailsActivity extends Activity
    implements GoBackButtonFragment.OnFragmentInteractionListener,
    ChoreDescriptionFragment.OnFragmentInteractionListener,
    ChoreCameraFragment.OnFragmentInteractionListener,
    ChoreDetailsButtonsFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chore_details);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_groups, menu);
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