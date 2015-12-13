package com.example.ndpt.chorescore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

/**
 * ReviewChoresActivity.java
 * Created by Peter Thomson on 06/12/2015.
 *
 * This class displays the chores that are awaiting review from a group admin
 */
public class ReviewChoresActivity extends Activity
    implements ReviewChoresListviewFragment.OnFragmentInteractionListener,
        GoBackButtonFragment.OnFragmentInteractionListener, AdapterView.OnItemClickListener {

    //Class scope variables
    private ArrayList<Chore> chores;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_review_chores);
            if (UserManager.UserHasDefaultGroup(this)) {
                ParseUser currentUser = UserManager.CheckCachedUser(this);
                if (currentUser != null) {
                    chores = ChoreManager.getSubmittedGroupChores(currentUser.getString("defaultGroupId"), this);
                    DisplayChores(chores);
                }
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_groups, menu);
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

    /**
     * Populates the list view with the current users default group chores
     *
     * @param chores list of chores
     */
    public void DisplayChores(ArrayList<Chore> chores)
    {
        try {
            ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
            for (Chore c : chores) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("desc", c.getDescription());
                map.put("points", Integer.toString(c.getPoints()));
                map.put("name", c.getCompleterName());
                data.add(map);
            }

            int resource = R.layout.listview_review_chores;
            String[] from = {"desc", "points", "name"};
            int[] to = {R.id.tv_chore_desc_listview, R.id.tv_chore_points_listview, R.id.tv_chore_user_name};

            SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
            ListView groupLv = (ListView) findViewById(R.id.lv_review_chores);
            groupLv.setOnItemClickListener(this);
            groupLv.setAdapter(adapter);
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    /**
     * Listview item click event for pending chore listview
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        try {
            choreApprovalPrompt(position, this);
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    /**
     * Displays a dialog box for approval of chore by an admin
     * @param position
     * @param activity
     */
    public void choreApprovalPrompt(final Integer position, final Activity activity)
    {
        try {
            ParseUser user = UserManager.CheckCachedUser(activity);
            final Chore chore = chores.get(position);
            Group choreGroup = GroupManager.RetrieveGroup(chore.getGroupId(), activity);
            final AlertDialog.Builder choreApprovalDialog = new AlertDialog.Builder(this);

            choreApprovalDialog.setTitle(getString(R.string.dialog_approve_chore_title))
                    .setMessage(getString(R.string.dialog_approve_chore_message))
                    .setPositiveButton(getString(R.string.dialog_approve_chore), new DialogInterface.OnClickListener() {
                        //Approve review button click
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeChore(position);
                            ChoreManager.UpdateUserPoints(chore.getCompleterId(), chore.getGroupId(), chore.getPoints(), activity);
                            ChoreManager.UpdateChoreState(chore.getChoreId(), chore.getCompleterId(), chore.getCompleterName(), true, null, activity);
                        }
                    })
                    .setNegativeButton(getString(R.string.dialog_deny_chore), new DialogInterface.OnClickListener() {
                        //Deny chore button click
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeChore(position);
                            ChoreManager.UpdateChoreState(chore.getChoreId(), null, null, false, null, activity);
                        }
                    });

            //Ensure current user is the admin of the group
            if (user != null && choreGroup.getAdminId().equals(user.getObjectId())) {
                ParseQuery<ParseObject> choreQuery = new ParseQuery<ParseObject>("Chore");
                choreQuery.getInBackground(chore.getChoreId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        //Get the parse file
                        ParseFile fileObject = (ParseFile) object.get("proofImage");

                        //Get the image from the parse file
                        fileObject.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null) {
                                    //Decode image from the image file derived byte array
                                    Bitmap imageBmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView proofImageView = new ImageView(activity);
                                    proofImageView.setImageBitmap(imageBmp);

                                    //Create the dialog box with the image
                                    choreApprovalDialog.setView(proofImageView);
                                    choreApprovalDialog.show();
                                }

                                //Display parse error message
                                else {
                                    Toast toast = Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        });
                    }
                });
            } else {
                //Display message to show current user is not authorized
                Toast toast = Toast.makeText(activity, activity.getString(R.string.error_review_chore), Toast.LENGTH_LONG);
                toast.show();
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    /**
     * Helper method to remove a chore from the chore list and display on chore update
     * @param position
     */
    public void removeChore(int position)
    {
        try {
            chores.remove(position);
            DisplayChores(chores);
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }


}
