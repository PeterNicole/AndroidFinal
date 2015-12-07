package com.example.ndpt.chorescore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.Parse;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ReviewChoresActivity.java
 * Created by Peter Thomson on 06/12/2015.
 *
 * This class displays the chores that are awaiting review from a group admin
 */
public class ReviewChoresActivity extends Activity
    implements ReviewChoresListviewFragment.OnFragmentInteractionListener, AdapterView.OnItemClickListener {

    //Class scope variables
    ArrayList<Chore> chores;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_chores);
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
            chores = ChoreManager.getSubmittedGroupChores(currentUser.getString("defaultGroupId"),this);
            ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();
            for (Chore c: chores)
            {

                HashMap<String,String> map = new HashMap<String, String>();
                map.put("desc",c.getDescription());
                map.put("points",Integer.toString(c.getPoints()));
                map.put("name", "bobby");
                //map.put("name", c.getDueDate().toString());
                data.add(map);
            }

            int resource = R.layout.listview_review_chores;
            String[] from = {"desc","points","name"};
            int[] to = {R.id.tv_chore_desc_listview,R.id.tv_chore_points_listview, R.id.tv_chore_user_name};

            SimpleAdapter adapter = new SimpleAdapter(this,data,resource,from,to);
            ListView groupLv = (ListView) findViewById(R.id.lv_review_chores);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

    }


}