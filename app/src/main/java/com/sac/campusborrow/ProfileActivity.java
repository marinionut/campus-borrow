package com.sac.campusborrow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvEmail;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        tvEmail = (TextView) findViewById(R.id.tvEmailProfile);
        tvEmail.setText(getIntent().getExtras().getString("Email"));

        String[] obiecte = {"Ciocan", "Aspirator", "Fierbator", "Aspirina"};
        ListAdapter objAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, obiecte);
        ListView objListView = (ListView) findViewById(R.id.lvObj);
        objListView.setAdapter(objAdapter);
    }

    public void btnAdd_Click(View v) {

    }

}
