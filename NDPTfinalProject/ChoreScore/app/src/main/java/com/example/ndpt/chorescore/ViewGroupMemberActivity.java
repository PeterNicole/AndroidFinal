package com.example.ndpt.chorescore;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
/**
 * ViewGroupMemberActivity.java
 * Created by Nicole Dahlquist on 27/11/2015.
 *
 * This class provides functionality for the view group activity for members
 */
public class ViewGroupMemberActivity extends Activity
    implements ViewGroupDisplayFragment.OnFragmentInteractionListener,
    ViewGroupButtonsMemberFragment.OnFragmentInteractionListener,
    GoBackButtonFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_member);
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