package com.example.ndpt.chorescore;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * SignupButtons.java
 * Created by Nicole Dahlquist on 21/11/2015.
 *
 * A simple {@link Fragment} subclass for displaying the button portion of sign up form
 */
public class SignupButtons extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnSignupSignup;

    //Class scope variables
    private static final Integer PASS_LENGTH = 6;
    private static final Integer USER_LENGTH = 6;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupButtons.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupButtons newInstance(String param1, String param2) {
        SignupButtons fragment = new SignupButtons();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SignupButtons() {
        // Required empty public constructor
    }
    private  void controlCreation(View v)
    {
        btnSignupSignup = (Button)v.findViewById(R.id.btnSignupSignup);
        final Activity activity = getActivity();
        Button[] buttons = {btnSignupSignup};
        for (Button b : buttons)
        {
            final int id = b.getId();
            b.setOnClickListener((new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (id == R.id.btnSignupSignup)
                    {
                        //Get text from the signup personal fragment
                        EditText firstEv = (EditText)activity.findViewById(R.id.etFirstName);
                        EditText lastEv = (EditText)activity.findViewById(R.id.etLastName);
                        EditText emailEv = (EditText)activity.findViewById(R.id.etEmail);

                        EditText passwordEv = (EditText)activity.findViewById(R.id.etPassword);
                        EditText passwordConfirmEv = (EditText)activity.findViewById(R.id.etConfirmPassword);
                        EditText userEv = (EditText)activity.findViewById(R.id.etUsername);

                        //Initialize strings for user creation
                        String first = firstEv.getText().toString();
                        String last = lastEv.getText().toString();
                        String email = emailEv.getText().toString();
                        String password = passwordEv.getText().toString();
                        String passwordConfirm = passwordConfirmEv.getText().toString();
                        String user = userEv.getText().toString();
                        Boolean isSignupValid = true;

                        //Check user name length
                        if(user == null || user.length() < USER_LENGTH)
                        {
                            userEv.setError(getString(R.string.error_user_length) + USER_LENGTH + getString(R.string.error_characters_long));
                            isSignupValid = false;
                        }

                        //Check password length
                        if(password == null || password.length() < PASS_LENGTH)
                        {
                            passwordEv.setError(getString(R.string.error_password_length) + PASS_LENGTH + getString(R.string.error_characters_long));
                            isSignupValid = false;
                        }

                        //Ensure passwords match
                        else if(!password.equals(passwordConfirm))
                        {
                            passwordConfirmEv.setError(getString(R.string.error_password_match));
                            isSignupValid = false;
                        }

                        // if valid sign up form
                        if(isSignupValid)
                        {
                            UserManager.CreateUser(user, password, first, last, email, activity);
                        }

                    }
                }
            }));
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_signup_buttons, container, false);
        controlCreation(v);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
