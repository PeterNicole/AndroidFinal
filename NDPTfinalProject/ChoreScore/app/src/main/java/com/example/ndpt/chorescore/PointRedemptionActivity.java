package com.example.ndpt.chorescore;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * PointRedemptoinActivity.java
 * Created by Nicole Dahlquist on 03/12/2015.
 *
 * This class provides functionality for redeeming points earned in the default group
 */
public class PointRedemptionActivity extends Activity
    implements GoBackButtonFragment.OnFragmentInteractionListener,
    PointRedemptionDetailsFragment.OnFragmentInteractionListener,
    PointRedemptionFormFragment.OnFragmentInteractionListener {

    //Class scope variables
    private final Integer SEEK_FACTOR = 50;
    private SeekBar sbPointRedemption;
    private TextView tvGroupName;
    private TextView tvCurrentPoints;
    private TextView tvTotalPoints;
    private TextView tvSeekBar;
    private Button btnRedeemPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_redemption);
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

    /**
     * Populates the controls and their listeners for the point redemption activity
     */
    public void controlCreation()
    {
        final ParseUser currentUser = UserManager.CheckCachedUser(this);
        final Activity activity = this;
        if(currentUser != null)
        {
            Integer currentPoints = 0;
            Integer totalPoints = 0;
            ParseQuery<ParseObject> pointQuery = ParseQuery.getQuery("UserGroup");
            pointQuery.whereEqualTo("userId", currentUser.getObjectId());
            pointQuery.whereEqualTo("groupId", currentUser.getString("defaultGroupId"));

            pointQuery.findInBackground(new FindCallback<ParseObject>()
            {
                @Override
                public void done(List<ParseObject> objects, ParseException e)
                {
                    if(e == null)
                    {
                        final ParseObject object = objects.get(0);
                        final Group group = GroupManager.RetrieveGroup(object.getString("groupId"), activity);

                        //Initialize controls from the view
                        sbPointRedemption = (SeekBar)findViewById(R.id.sbPointRedeemption);
                        tvGroupName = (TextView)findViewById(R.id.tv_point_redemption_group_name);
                        tvSeekBar = (TextView)findViewById(R.id.tv_point_redemption_redemption_seekbar);
                        tvCurrentPoints = (TextView)findViewById(R.id.tv_point_redemption_current_points);
                        tvTotalPoints = (TextView)findViewById(R.id.tv_point_redemption_total_points);
                        btnRedeemPoints = (Button)findViewById(R.id.btnPointRedemptionSubmit);

                        //Seek bar initialization
                        sbPointRedemption.setMax(object.getInt("points")/SEEK_FACTOR);
                        sbPointRedemption.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                tvSeekBar.setText(Integer.toString(sbPointRedemption.getProgress() * SEEK_FACTOR));
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                                //Do nothing
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                //Do nothing
                            }
                        });

                        //Set points && group name
                        tvGroupName.setText(group.getName());
                        tvCurrentPoints.setText(Integer.toString(object.getInt("points")));
                        tvTotalPoints.setText(Integer.toString(object.getInt("cumulativePoints")));
                        tvSeekBar.setText("0");
                        sbPointRedemption.setProgress(0);

                        //Redeem points button click event
                        btnRedeemPoints.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                //Set point value to negative of the selected value
                                Integer points = -sbPointRedemption.getProgress() * SEEK_FACTOR;

                                if(points < 0 && object.getInt("points") > 0)
                                {
                                    //Remove the points from the user
                                    ChoreManager.UpdateUserPoints(currentUser.getObjectId(), group.getGroupId(), points, activity);

                                    //Send a notification to the admin
                                    NotificationPusher.PushMessageToUser(group.getAdminId(),currentUser.getUsername() + getString(R.string.notification_redeemed) + -points + getString(R.string.notification_points));

                                    //Re-create the controls
                                    controlCreation();
                                }
                            }
                        });

                    }

                    else
                    {
                        //Display the parse error
                        Toast toast = Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });




        }



    }

}
