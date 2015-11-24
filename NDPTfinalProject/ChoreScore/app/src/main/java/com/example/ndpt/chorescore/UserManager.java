package com.example.ndpt.chorescore;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Peter Thomson on 24/11/2015.
 *
 * Class for handling user creations and logins, and any other functions specifc to users
 */
public class UserManager
{
    /**
     * Method for creating a new user in the parse database
     * @param userName user name
     * @param password user password
     */
    static public void CreateUser(String userName,String password)
    {
        ParseUser newUser = new ParseUser();
        newUser.setUsername(userName);
        newUser.setPassword(password);
        //Optional
        //user.setEmail("bla@bla.com");

        //For additional custom fields
        //newUser.put("fieldName", "value");

        newUser.signUpInBackground(new SignUpCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if (e == null)
                {
                    //Sign up success
                }

                else
                {
                    //Sign up fail
                }
            }
        });
    }

    /**
     * Method for logging in a parse user
     * @param userName user name
     * @param password user password
     */
    static public void LoginUser(String userName, String password)
    {
        ParseUser.logInInBackground(userName, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null)
                {
                    //Successful login
                }
                else
                {
                    //login fail
                }

            }
        });
    }

    static public void CheckCachedUser()
    {
        ParseUser currentUser = ParseUser.getCurrentUser().getCurrentUser();
        if(currentUser!= null)
        {
            //User currently cached on this device, no login required
        }
        else
        {
            //Prompt login
        }
    }
}
