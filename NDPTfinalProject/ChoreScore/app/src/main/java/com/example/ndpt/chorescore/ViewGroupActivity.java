package com.example.ndpt.chorescore;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ViewGroupActivity.java
 * Created by Nicole Dahlquist on 27/11/2015.
 *
 * This class provides functionality for the view group activity for members
 */
public class ViewGroupActivity extends Activity
    implements ViewGroupDisplayFragment.OnFragmentInteractionListener,
    ViewGroupButtonsMemberFragment.OnFragmentInteractionListener,
    GoBackButtonFragment.OnFragmentInteractionListener {

    //class scope variables
    private TextView tvGroupName;
    private TextView tvAdminName;
    private ListView lvMembers;
    private Button btnLeaveGroup;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_member);
        if(UserManager.UserHasDefaultGroup(this))
        {
            DisplayGroupDetails();
            ControlCreation();
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
    public void onFragmentInteraction(Uri uri)
    {

    }

    /**
     * Displays the details for the current users default group
     */
    public void DisplayGroupDetails()
    {
        //Check for logged in user
        ParseUser currentUser = UserManager.CheckCachedUser(this);

        if(currentUser != null)
        {
            //Get the controls from the view
            tvGroupName = (TextView)findViewById(R.id.tv_view_group_name_display);
            tvAdminName = (TextView)findViewById(R.id.tv_view_group_admin_display);
            lvMembers = (ListView)findViewById(R.id.lv_group_members);
            ArrayList<String> groupMembers;

            //Get the current users group
            String groupId = currentUser.getString("defaultGroupId");
            group = GroupManager.RetrieveGroup(groupId,this);

            //Set group name
            tvGroupName.setText(group.getName());

            //Set group admin
            String adminName = UserManager.getUserName(group.getAdminId(),this);
            tvAdminName.setText(adminName);


            //Populate group members list view
            groupMembers = GroupManager.RetrieveGroupMemberNames(group.getGroupId(), this);
            ArrayList<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();
            for (String s: groupMembers)
            {
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("name",s);
                data.add(map);
            }

            int resource = R.layout.listview_members;
            String[] from = {"name"};
            int[] to = {R.id.tv_list_member_name};

            SimpleAdapter adapter = new SimpleAdapter(this,data,resource,from,to);
            lvMembers.setAdapter(adapter);
        }
    }

    public void ControlCreation()
    {
        final Activity activity = this;
        btnLeaveGroup = (Button)findViewById(R.id.btnLeaveGroup);
        btnLeaveGroup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Click event to leave the current group
                UserManager.LeaveCurrentGroup(group,activity);
            }
        });
    }
}
