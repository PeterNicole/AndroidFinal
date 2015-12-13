package com.example.ndpt.chorescore;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * SignUpActivity.java
 * Created by Nicole Dahlquist on 21/11/2015.
 *
 * This class provides functionality for the signup activity
 */
public class SignUpActivity extends Activity
    implements SubmitResetButtonsFragment.OnFragmentInteractionListener,
        SignupPersonalFragment.OnFragmentInteractionListener,
        SignupSecurityFragment.OnFragmentInteractionListener,
        GoBackButtonFragment.OnFragmentInteractionListener{

    //Class scope variables
    private Button btnReset;
    private Button btnSubmit;
    private static final Integer PASS_LENGTH = 6;
    private static final Integer USER_LENGTH = 6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);
            controlCreation();
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // No menu
       // getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        try {
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

    private void controlCreation(){
        try {
            btnReset = (Button) findViewById(R.id.btnReset);
            btnSubmit = (Button) findViewById(R.id.btnSubmit);
            final Activity activity = this;
            Button[] buttons = {btnSubmit, btnReset};
            for (Button b : buttons) {
                final int id = b.getId();
                b.setOnClickListener((new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //Get text from the signup personal fragment
                        EditText firstEv = (EditText) activity.findViewById(R.id.etFirstName);
                        EditText lastEv = (EditText) activity.findViewById(R.id.etLastName);
                        EditText emailEv = (EditText) activity.findViewById(R.id.etEmail);

                        EditText passwordEv = (EditText) activity.findViewById(R.id.etPassword);
                        EditText passwordConfirmEv = (EditText) activity.findViewById(R.id.etConfirmPassword);
                        EditText userEv = (EditText) activity.findViewById(R.id.etUsername);

                        //Initialize strings for user creation
                        String first = firstEv.getText().toString();
                        String last = lastEv.getText().toString();
                        String email = emailEv.getText().toString();
                        String password = passwordEv.getText().toString();
                        String passwordConfirm = passwordConfirmEv.getText().toString();
                        String user = userEv.getText().toString();
                        Boolean isSignupValid = true;

                        if (id == R.id.btnSubmit) {

                            //Check user name length
                            if (user == null || user.length() < USER_LENGTH) {
                                userEv.setError(getString(R.string.error_user_length) + USER_LENGTH + getString(R.string.error_characters_long));
                                isSignupValid = false;
                            }

                            //Check password length
                            if (password == null || password.length() < PASS_LENGTH) {
                                passwordEv.setError(getString(R.string.error_password_length) + PASS_LENGTH + getString(R.string.error_characters_long));
                                isSignupValid = false;
                            }

                            //Ensure passwords match
                            else if (!password.equals(passwordConfirm)) {
                                passwordConfirmEv.setError(getString(R.string.error_password_match));
                                isSignupValid = false;
                            }

                            // if valid sign up form
                            if (isSignupValid) {
                                UserManager.CreateUser(user, password, first, last, email, activity);
                            }
                        } else if (id == R.id.btnReset) {
                            //clear
                            firstEv.getText().clear();
                            lastEv.getText().clear();
                            emailEv.getText().clear();
                            passwordEv.getText().clear();
                            passwordConfirmEv.getText().clear();
                            userEv.getText().clear();
                        }
                    }
                }));
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }
}
