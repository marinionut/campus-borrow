package com.sac.campusborrow.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sac.campusborrow.R;

public class ObiectActivity extends AppCompatActivity {

    TextView titluObj;
    TextView descriereObj;
    DatabaseReference dref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obiect);

        Bundle bundle = getIntent().getExtras();
        dref = FirebaseDatabase.getInstance().getReference("/obiecte/"+bundle.getString("NumeObiect"));

        titluObj = (TextView) findViewById(R.id.titluObj);
        dref.child("nume").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                titluObj.setText((String)dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        descriereObj = (TextView) findViewById(R.id.descriereObj) ;
        dref.child("descriere").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                descriereObj.setText((String)dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
