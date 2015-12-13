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
 * PendingChoresButtonsFragment.java
 * Created by Nicole Dahlquist on 02/12/2015.
 *
 * A simple {@link Fragment} subclass for displaying the group admin buttons
 */
public class PendingChoresButtonsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnAddToList;
    private Button btnReviewChores;
    private Button btnGroups;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PendingChoresButtonsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PendingChoresButtonsFragment newInstance(String param1, String param2) {
        try {
            PendingChoresButtonsFragment fragment = new PendingChoresButtonsFragment();
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

    public PendingChoresButtonsFragment() {
        // Required empty public constructor
    }

    /**
     * Creates controls for the pending chores button fragment
     * @param v
     */
    private void controlCreation(View v)
    {
        try {
            //Get buttons from fragments view
            btnAddToList = (Button) v.findViewById(R.id.btnPendingChoresAdd);
            btnReviewChores = (Button) v.findViewById(R.id.btnPendingChoresReview);
            btnGroups = (Button) v.findViewById(R.id.btnPendingChoresGroups);

            final Activity activity = getActivity();
            Button[] buttons = {btnAddToList, btnReviewChores, btnGroups};
            for (Button b : buttons) {
                final int id = b.getId();

                //Set button click listeners for navigation
                b.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (id == R.id.btnPendingChoresAdd) {
                            TransitionManager.ActivityTransition(activity, SelectChoresActivity.class);
                        }

                        if (id == R.id.btnPendingChoresReview) {
                            TransitionManager.ActivityTransition(activity, ReviewChoresActivity.class);
                        }

                        if (id == R.id.btnPendingChoresGroups) {
                            TransitionManager.ActivityTransition(activity, CurrentGroupsActivity.class);
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
            View v = inflater.inflate(R.layout.fragment_pending_chores_buttons, container, false);
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
