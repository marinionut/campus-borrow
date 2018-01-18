package com.sac.campusborrow.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sac.campusborrow.R;
import com.sac.campusborrow.model.User;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ionut on 1/17/2018.
 */

public class FragmentFour extends Fragment {
    DatabaseReference dref;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    ArrayList<String> objectIdList;

    public FragmentFour() {
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
        View view = inflater.inflate(R.layout.fragment_four, container, false);

        list = new ArrayList<String>();
        objectIdList = new ArrayList<String>();
        listView = (ListView) view.findViewById(R.id.listAdapterUsers);
        adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, list);
        listView.setAdapter(adapter);

        dref = FirebaseDatabase.getInstance().getReference("/users");
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                list.add("Rating: " + String.format("%.1f", user.getRating())
                        + "  Borrowed: " + user.getObiecteLuate()
                        + "  Offered: " + user.getObiecteOferite()
                        + " | " + user.getDisplayName());
                Collections.sort(list, Collections.reverseOrder());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
}
