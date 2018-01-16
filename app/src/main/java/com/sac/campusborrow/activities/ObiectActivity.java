package com.sac.campusborrow.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sac.campusborrow.R;
import com.sac.campusborrow.model.Obiect;
import com.sac.campusborrow.model.User;

public class ObiectActivity extends AppCompatActivity {

    Obiect obiect;
    TextView titluObj, descriereObj, ownerObj, rUserName;
    Button delObj, rejTrade, accTrade, reqTrade, endTrade, fdbTrade;
    ImageView imagineObj;
    DatabaseReference oRef, uRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obiect);
        // get data from previous activity
        Bundle bundle = getIntent().getExtras();

        titluObj = (TextView) findViewById(R.id.titluObj);
        descriereObj = (TextView) findViewById(R.id.descriereObj);
        imagineObj = (ImageView) findViewById(R.id.imageView);
        // hide owner/request user
        ownerObj = (TextView) findViewById(R.id.ownerObj);
        ownerObj.setVisibility(View.INVISIBLE);
        rUserName = (TextView) findViewById(R.id.rUserName);
        rUserName.setVisibility(View.INVISIBLE);
        // hide all buttons
        delObj = (Button) findViewById(R.id.delObj);
        delObj.setVisibility(View.INVISIBLE);
        rejTrade = (Button) findViewById(R.id.rejTrade);
        rejTrade.setVisibility(View.INVISIBLE);
        accTrade = (Button) findViewById(R.id.accTrade);
        accTrade.setVisibility(View.INVISIBLE);
        reqTrade = (Button) findViewById(R.id.reqTrade);
        reqTrade.setVisibility(View.INVISIBLE);
        endTrade = (Button) findViewById(R.id.endTrade);
        endTrade.setVisibility(View.INVISIBLE);
        fdbTrade = (Button) findViewById(R.id.fdbTrade);
        fdbTrade.setVisibility(View.INVISIBLE);

        String previousView = bundle.getString("from");
        if(previousView.compareTo("listaToate") == 0) {
            ownerObj.setVisibility(View.VISIBLE);
            reqTrade.setVisibility(View.VISIBLE);
        } else if(previousView.compareTo("listaMea") == 0) {
            delObj.setVisibility(View.VISIBLE);
        }

        oRef = FirebaseDatabase.getInstance().getReference("/obiecte/");
        uRef = FirebaseDatabase.getInstance().getReference("/users");



        oRef.child(bundle.getString("numeObiect")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                obiect = dataSnapshot.getValue(Obiect.class);

                titluObj.setText(obiect.getNume());
                descriereObj.setText("Descriere: "+obiect.getDescriere());

                uRef.child(obiect.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        ownerObj.setText("Detinator: "+user.getDisplayName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
