package com.example.ndpt.chorescore;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * MainPageOptionsFragment.java
 * Created by Nicole Dahlquist on 11/11/2015.
 *
 * A simple {@link Fragment} subclass that displays the main page buttons
 */
public class MainPageOptionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnSignup;
    private Button btnLogin;
    private Button btnAbout;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPageOptionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainPageOptionsFragment newInstance(String param1, String param2) {
        try {
            MainPageOptionsFragment fragment = new MainPageOptionsFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
            return null;
        }
    }

    public MainPageOptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            // Inflate the layout for this fragment
            View v = inflater.inflate(R.layout.fragment_main_page_options, container, false);
            controlCreation(v);
            return v;
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
            return null;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        try {
            if (mListener != null) {
                mListener.onFragmentInteraction(uri);
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }
    // helper method for initializing controls and setting listeners
    private void controlCreation(View v){
        try {
            final Activity activity = getActivity();
            btnSignup = (Button) v.findViewById(R.id.btnMainSignup);
            btnLogin = (Button) v.findViewById(R.id.btnMainLogin);
            btnAbout = (Button) v.findViewById(R.id.btnMainAbout);

            Button[] buttons = {btnAbout, btnLogin, btnSignup};
            for (Button b : buttons) {
                final int id = b.getId();
                b.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (id == R.id.btnMainAbout) {
                            TransitionManager.ActivityTransition(activity, AboutActivity.class);
                        }

                        if (id == R.id.btnMainLogin) {
                            TransitionManager.ActivityTransition(activity, LoginActivity.class);
                        }
                        if (id == R.id.btnMainSignup) {
                            TransitionManager.ActivityTransition(activity, SignUpActivity.class);
                        }

                    }
                }));
            }
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
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
        try {
            super.onDetach();
            mListener = null;
        }
        catch(Exception e) {
            System.out.println("Error " + e.getMessage());
        }
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
