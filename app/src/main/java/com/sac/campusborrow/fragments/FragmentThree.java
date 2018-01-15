package com.sac.campusborrow.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sac.campusborrow.DashboardActivity;
import com.sac.campusborrow.R;

/**
 * Created by ionut on 1/15/2018.
 */

public class FragmentThree extends Fragment {
    private TextView tvEmail;

    public FragmentThree() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_three, container, false);

        DashboardActivity activity = (DashboardActivity) getActivity();
        String email = activity.getEmail();

        tvEmail = (TextView) view.findViewById(R.id.tvEmailProfile);
        tvEmail.setText(email);

        String[] obiecte = {"Ciocan", "Aspirator", "Fierbator", "Aspirina"};
        ListAdapter objAdapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_1, obiecte);
        ListView objListView = (ListView) view.findViewById(R.id.lvObj);
        objListView.setAdapter(objAdapter);


        return view;
    }
}
