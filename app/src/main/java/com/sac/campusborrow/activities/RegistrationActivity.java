package com.sac.campusborrow.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sac.campusborrow.R;
import com.sac.campusborrow.model.User;

public class RegistrationActivity extends AppCompatActivity {

    private EditText txtEmailAddress;
    private EditText txtPassword;
    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtPhoneNumber;
    private EditText txtAddress;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        txtEmailAddress = (EditText) findViewById(R.id.txtEmailRegistration);
        txtPassword = (EditText) findViewById(R.id.txtPasswordRegistration);
        txtFirstName = (EditText) findViewById(R.id.txtNumeRegistration);
        txtLastName = (EditText) findViewById(R.id.txtPrenumeRegistration);
        txtPhoneNumber = (EditText) findViewById(R.id.txtNumarTelefonRegistration);
        txtAddress = (EditText) findViewById(R.id.txtAdresaRegistration);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    public void btnRegistrationUser_Click(View v) {
        if (sanityChecks() == false) {
            Toast.makeText(RegistrationActivity.this, "Input Errors!",
                    Toast.LENGTH_LONG).show();

        } else {
            final ProgressDialog progressDialog = ProgressDialog.show(RegistrationActivity.this, "Please wait...", "Processing...", true);
            (firebaseAuth.createUserWithEmailAndPassword(txtEmailAddress.getText().toString(), txtPassword.getText().toString()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_LONG).show();

                                addUser();

                                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(i);
                            } else {
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void addUser() {
        User user = new User();
        user.setEmail(txtEmailAddress.getText().toString());
        user.setFirstName(txtFirstName.getText().toString());
        user.setLastName(txtLastName.getText().toString());
        user.setPhoneNumber(txtPhoneNumber.getText().toString());
        user.setAddress(txtAddress.getText().toString());
        user.setRating(0);
        user.setObiecteLuate(0);
        user.setObiecteOferite(0);
        user.setCounterRating(0);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();
        databaseReference.child("users").child(userId).setValue(user);
    }

    private boolean sanityChecks() {
        if ((txtEmailAddress != null && txtEmailAddress.getText().length() != 0) &&
                (txtPassword != null && txtPassword.getText().length() != 0) &&
                (txtFirstName != null && txtFirstName.getText().length() != 0) &&
                        (txtLastName != null && txtLastName.getText().length() != 0) &&
                        (txtPhoneNumber != null && txtPhoneNumber.getText().length() != 0) &&
                        (txtAddress != null && txtAddress.getText().length() != 0)) {
            return true;
        }
        return false;
    }
}
