package com.example.ndpt.chorescore;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.Calendar;
import java.util.Date;

public class SelectChoresActivity extends Activity
    implements SelectChoresButtonsFragment.OnFragmentInteractionListener,
    SelectChoresCreateFormFragment.OnFragmentInteractionListener,
    GoBackButtonFragment.OnFragmentInteractionListener {

    //Class scope variables
    private TextView tvSeekbar;
    private EditText etDescription;
    private SeekBar seekBar;
    private DatePicker datePicker;
    private Button addChoreButton;

    private final Integer SEEKBAR_FACTOR = 50;
    private final Integer DESCRIPTION_MIN_LENGTH = 6;
    private final Integer DESCRIPTION_MAX_LENGTH = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_chores);
        if(UserManager.UserHasDefaultGroup(this))
        {
            controlCreation();
        }

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

    public void controlCreation()
    {
        //Get controls from view
        seekBar = (SeekBar)findViewById(R.id.sbChorePointSeekBar);
        tvSeekbar = (TextView)findViewById(R.id.tvChorePointSeekBar);
        datePicker = (DatePicker)findViewById(R.id.dpChoreDatePicker);
        addChoreButton = (Button)findViewById(R.id.btnSelectChoresAdd);
        etDescription = (EditText)findViewById(R.id.etAddChoreDescription);
        final Activity activity = this;

        //Set the minimum date to tomorrow
        Date today = new Date();
        Calendar minDate = Calendar.getInstance();
        minDate.setTime(today);
        minDate.add(Calendar.DATE, 1);

        datePicker.setMinDate(minDate.getTime().getTime());

        //Initialize the seekbar text
        tvSeekbar.setText(Integer.toString(seekBar.getProgress()));

        //Update seekbar text view on seek bar change
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                //Set the text view to the current value
                tvSeekbar.setText(Integer.toString(seekBar.getProgress()*SEEKBAR_FACTOR));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                //do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                //do nothing
            }
        });

        //Create new group on submit button click
        addChoreButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Initialize values from controls
                Boolean isValid = true;
                ParseUser currentUser = UserManager.CheckCachedUser(activity);
                String groupId = currentUser.getString("defaultGroupId");
                String description = etDescription.getText().toString();
                Calendar calender = Calendar.getInstance();
                calender.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                Date date = calender.getTime();

                Integer points = seekBar.getProgress()*SEEKBAR_FACTOR;

                //Check description is valid
                if(description.length() < DESCRIPTION_MIN_LENGTH || description.length() > DESCRIPTION_MAX_LENGTH)
                {
                    isValid = false;

                    //Show error message
                    etDescription.setError(getString(R.string.error_description_length) + DESCRIPTION_MIN_LENGTH + getString(R.string.and) + DESCRIPTION_MAX_LENGTH + getString(R.string.error_characters_long));
                }

                //Check date is valid
                if(date.before(new Date()))
                {
                    isValid = false;

                    //Show error message
                    Toast toast = Toast.makeText(activity,getString(R.string.error_date_before), Toast.LENGTH_LONG);
                    toast.show();
                }

                if(points <= 0)
                {
                    isValid = false;

                    //Show error message
                    Toast toast = Toast.makeText(activity,getString(R.string.error_points_zero), Toast.LENGTH_LONG);
                    toast.show();
                }

                //Add chore if valid
                if(isValid)
                {
                    ChoreManager.createChore(groupId,description,date,points,activity);
                }
            }
        });

    }



}
