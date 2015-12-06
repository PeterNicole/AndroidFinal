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
 * CurrentGroupsActivity.java
 * Created by Nicole Dahlquist on 23/11/2015.
 *
 * This class provides functionality for the join group activity
 */
public class JoinGroupActivity extends Activity
    implements JoinGroupDisplayResultsFragment.OnFragmentInteractionListener,
    JoinGroupSearchFragment.OnFragmentInteractionListener,
    GoBackButtonFragment.OnFragmentInteractionListener,
    AdapterView.OnItemClickListener{

    //Class scope variables
    ArrayList<Group> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        displayGroups("");
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
    public void onFragmentInteraction(Uri uri)
    {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        //Check if user is logged in
        ParseUser currentUser = UserManager.CheckCachedUser(this);

        //Get the group selected from the list view
        Group currentGroup = groups.get(position);

        //Join the selected group
        GroupManager.JoinGroup(currentUser.getObjectId(),currentGroup.getGroupId(),this);
    }

    /**
     * Displays a list of groups in the join group list view
     * @param groupName searches for groups containing this string
     */
    public void displayGroups(String groupName)
    {
        groups = GroupManager.RetrieveAll(groupName, this);
        ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();
        for (Group g: groups)
        {
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("name",g.getName());
            data.add(map);
        }

        int resource = R.layout.listview_group;
        String[] from = {"name"};
        int[] to = {R.id.tv_list_group_name};

        SimpleAdapter adapter = new SimpleAdapter(this,data,resource,from,to);
        ListView groupLv = (ListView) findViewById(R.id.lv_join_groups);
        groupLv.setOnItemClickListener(this);
        groupLv.setAdapter(adapter);
    }
}
