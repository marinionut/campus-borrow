package com.sac.campusborrow.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sac.campusborrow.model.Obiect;
import com.sac.campusborrow.activities.ObiectActivity;
import com.sac.campusborrow.R;

import java.util.ArrayList;

/**
 * Created by ionut on 1/15/2018.
 */

public class FragmentThree extends Fragment {

    TextView tvEmail;
    DatabaseReference dref;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

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
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        final String email = user.getEmail();

        tvEmail = (TextView) view.findViewById(R.id.tvEmailProfile);
        tvEmail.setText(email);

        list = new ArrayList<String>();
        listView = (ListView) view.findViewById(R.id.lvObj3);
        adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(view.getContext(), ObiectActivity.class);
                myIntent.putExtra("numeObiect", listView.getItemAtPosition(i).toString());
                myIntent.putExtra("from", "listaMea");
                startActivity(myIntent);
            }
        });

        dref = FirebaseDatabase.getInstance().getReference("/obiecte");
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Obiect obiect = dataSnapshot.getValue(Obiect.class);
                if(obiect.userId.compareTo(userId) == 0 && obiect.status.compareTo("disponibil") == 0) {
                    list.add(obiect.getNume());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getKey();
                if(list.indexOf(value) != -1) {
                    list.remove(value);
                    adapter.notifyDataSetChanged();
                }
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
