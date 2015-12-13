package com.example.ndpt.chorescore;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

/**
 * AboutActivity.java
 * Created by Nicole Dahlquist on 21/11/2015.
 *
 * This class provides functionality for the About activity
 */
public class AboutActivity extends Activity
        implements AboutOneFragment.OnFragmentInteractionListener,
        AboutTwoFragment.OnFragmentInteractionListener,
        GoBackButtonFragment.OnFragmentInteractionListener{

    //Class scope variables
    private Button manualButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about);
            CreateControls();
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        try {
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
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

    /**
     * Initializes controls on the about activity
     */
    public void CreateControls()
    {
        try
        {
            //Set on click event to download the user manual
            manualButton = (Button)findViewById(R.id.about_manual_btn);
            manualButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ShowManual();
                }
            });
        }

        catch(Exception e)
        {
            System.out.println("Error " + e.getMessage());
        }
    }

    /**
     * Opens browser for the user manual
     */
    public void ShowManual()
    {
        try
        {
            Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://drive.google.com/file/d/0B7vtDsy6XVruVFY5NFd6V09QMzg/view?usp=sharing"));
            startActivity(intent);
        }
        catch(Exception e)
        {
            System.out.println("Error " + e.getMessage());
        }

    }
}
