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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sac.campusborrow.R;
import com.sac.campusborrow.activities.ObiectActivity;
import com.sac.campusborrow.model.Obiect;
import com.sac.campusborrow.model.Status;

import java.util.ArrayList;

/**
 * Created by ionut on 1/15/2018.
 */

public class FragmentTwo extends Fragment {

    DatabaseReference dref;
    ListView listView;
    ArrayList<String> list;
    ArrayList<String> objectIdList;
    ArrayAdapter<String> adapter;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public FragmentTwo() {
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
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        final String email = user.getEmail();

        list = new ArrayList<String>();
        objectIdList = new ArrayList<String>();
        listView = (ListView) view.findViewById(R.id.lvObj3);
        adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(view.getContext(), ObiectActivity.class);
                myIntent.putExtra("numeObiect", objectIdList.get(i));
                myIntent.putExtra("from", "listaInchiriate");
                startActivity(myIntent);
            }
        });

        dref = FirebaseDatabase.getInstance().getReference("/obiecte");
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Obiect obiect = dataSnapshot.getValue(Obiect.class);
                String key = dataSnapshot.getKey();
                if(obiect.userId2.compareTo(firebaseUser.getUid()) == 0
                        && obiect.status.compareTo(Status.INCHIRIAT.name()) == 0) {
                    list.add(obiect.getNume());
                    objectIdList.add(key);
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
                    objectIdList.remove(value);
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
