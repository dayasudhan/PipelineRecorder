package com.kuruvatech.pipelinerecorder.fragments;

import android.os.Bundle;
//import android.support.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kuruvatech.pipelinerecorder.R;
import com.kuruvatech.pipelinerecorder.utils.SessionManager;



public class LogoutFragment extends Fragment {

    View rootview;

    // Session Manager Class
    SessionManager session;
    // Button Logout
    Button btnLogout;

   // @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.logout,container,false);
        // Session class instance
        session = new SessionManager(getActivity().getApplicationContext());

//        ((MainActivity) getActivity())
//                .setActionBarTitle("About Me");
        // Button logout
        btnLogout = (Button)rootview.findViewById(R.id.btnLogout);
        // get user data from session
        /**
         * Logout button click event
         * */
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Clear the session data
                // This will clear all session data and
                // redirect user to LoginActivity

                session.logoutUser();
            }
        });

        return rootview;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.menu_refresh).setVisible(false);
//        super.onPrepareOptionsMenu(menu);
    }
}
