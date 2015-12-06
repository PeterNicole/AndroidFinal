package com.example.ndpt.chorescore;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.ParseUser;

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
    ArrayList<Chore> chores;

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        //Chore click
    }
}
