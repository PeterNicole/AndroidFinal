package com.example.ndpt.chorescore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * PendingChoresActivity.java
 * Created by Nicole Dahlquist on 02/12/2015.
 *
 * This class displays the chores that are pending for the default group as well as admin
 * buttons if the user is the group's admin
 */
public class PendingChoresActivity extends Activity
    implements PendingChoresButtonsFragment.OnFragmentInteractionListener,
    PendingChoresListviewFragment.OnFragmentInteractionListener, AdapterView.OnItemClickListener {

    //Class scope variables
    ArrayList<Chore> chores;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_chores);
        DisplayChores();
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
     * Populates the list view with the current users default group chores
     */
    public void DisplayChores()
    {
        ParseUser currentUser =  UserManager.CheckCachedUser(this);
        if (currentUser!= null)
        {
            chores = ChoreManager.getPendingGroupChores(currentUser.getString("defaultGroupId"),this);
            ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();
            for (Chore c: chores)
            {
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("desc",c.getDescription());
                map.put("points",Integer.toString(c.getPoints()));
                map.put("date", c.getDueDate().toString()); //TODO format date
                data.add(map);
            }

            int resource = R.layout.listview_pending_chores;
            String[] from = {"desc","points","date"};
            int[] to = {R.id.tv_chore_desc_listview,R.id.tv_chore_points_listview, R.id.tv_chore_date_listview};

            SimpleAdapter adapter = new SimpleAdapter(this,data,resource,from,to);
            ListView groupLv = (ListView) findViewById(R.id.lv_pending_chores);
            groupLv.setOnItemClickListener(this);
            groupLv.setAdapter(adapter);
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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
    {
        //Pending chore click dialogue box
        final AlertDialog.Builder choreSubmissionDialog = new AlertDialog.Builder(this);

        //Initialize the dialog box
        choreSubmissionDialog.setTitle(getString(R.string.dialog_submit_chore_title))
            .setMessage(getString(R.string.dialog_submit_chore_message))
            .setPositiveButton(getString(R.string.dialog_okay), new DialogInterface.OnClickListener()
            {
                //Okay click event
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    setImageIntent(position);
                }
            })
            .setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                //Cancel click event
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Do nothing
                }
            })
            .show();
    }

    /**
     * Creates intent for choosing an image to customize the game token images
     */
    private void setImageIntent(int position)
    {
        try
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(Intent.createChooser(intent, "Select picture"), position);
        }
        catch (Exception e)
        {
            System.out.println("Error on setImage: " + e.getMessage());
        }
    }

    /**
     * onActivityResult used to set image variable after it has been selected in setImage
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        ParseUser currentUser = UserManager.CheckCachedUser(this);
        Activity activity = this;
        Chore chore = chores.get(requestCode);

        if(resultCode == RESULT_OK && currentUser != null)
        {
            try
            {
                Bundle extras = data.getExtras();
                image = (Bitmap)extras.get("data");

                if(image!= null)
                {
                    //Update the chore with the image and the users id
                    ChoreManager.UpdateChoreState(chore.getChoreId(), currentUser.getObjectId(), false, image, activity, new Runnable() {
                        @Override
                        public void run()
                        {
                            DisplayChores();
                        }
                    });
                }
            }

            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
}
