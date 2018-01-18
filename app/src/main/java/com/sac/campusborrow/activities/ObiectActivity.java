package com.sac.campusborrow.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sac.campusborrow.R;
import com.sac.campusborrow.model.Obiect;
import com.sac.campusborrow.model.Status;
import com.sac.campusborrow.model.User;
import com.squareup.picasso.Picasso;

public class ObiectActivity extends AppCompatActivity {

    Obiect obiect;
    TextView titluObj, descriereObj, userDisplayName, userPhoneNumber;
    Button delObj, reqTrade, endTrade, cancelTrade;
    ImageView imagineObj;
    RatingBar ratingBar;
    DatabaseReference oRef, uRef;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    Bundle bundle;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obiect);
        // get data from previous activity
        bundle = getIntent().getExtras();

        titluObj = (TextView) findViewById(R.id.titluObj);
        descriereObj = (TextView) findViewById(R.id.descriereObj);
        imagineObj = (ImageView) findViewById(R.id.imagineObj);
        // hide userDisplayName
        userDisplayName = (TextView) findViewById(R.id.ownerObj);
        userDisplayName.setVisibility(View.INVISIBLE);
        userPhoneNumber = (TextView) findViewById(R.id.phoneOwnerObj);
        userPhoneNumber.setVisibility(View.INVISIBLE);
        // hide ratingBar
        ratingBar = (RatingBar) findViewById(R.id.rating);
        ratingBar.setVisibility(View.INVISIBLE);
        ratingBar.setClickable(Boolean.FALSE);
        // hide all buttons
        delObj = (Button) findViewById(R.id.delObj);
        delObj.setVisibility(View.INVISIBLE);
        reqTrade = (Button) findViewById(R.id.reqTrade);
        reqTrade.setVisibility(View.INVISIBLE);
        endTrade = (Button) findViewById(R.id.endTrade);
        endTrade.setVisibility(View.INVISIBLE);
        cancelTrade = (Button) findViewById(R.id.cancelTrade);
        cancelTrade.setVisibility(View.INVISIBLE);

        String previousView = bundle.getString("from");
        oRef = FirebaseDatabase.getInstance().getReference("/obiecte/");
        uRef = FirebaseDatabase.getInstance().getReference("/users");

        if(previousView.compareTo("listaToate") == 0) {
            userDisplayName.setVisibility(View.VISIBLE);
            userPhoneNumber.setVisibility(View.VISIBLE);
            reqTrade.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
        } else if(previousView.compareTo("listaMea") == 0) {
            delObj.setVisibility(View.VISIBLE);
            DatabaseReference myListObjectReference = oRef.child(bundle.getString("numeObiect"));
            myListObjectReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    obiect = snapshot.getValue(Obiect.class);
                    if(obiect != null) {
                        if (obiect.getStatus().equals(Status.INCHIRIAT.name())) {
                            cancelTrade.setVisibility(View.VISIBLE);
                            userDisplayName.setVisibility(View.VISIBLE);
                            userPhoneNumber.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if(previousView.compareTo("listaInchiriate") == 0) {
            endTrade.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
            ratingBar.setClickable(Boolean.TRUE);
            userDisplayName.setVisibility(View.VISIBLE);
            userPhoneNumber.setVisibility(View.VISIBLE);

            ratingBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        float touchPositionX = event.getX();
                        float width = ratingBar.getWidth();
                        float starsf = (touchPositionX / width) * 5.0f;
                        int stars = (int)starsf + 1;
                        ratingBar.setRating(stars);

                        v.setPressed(false);
                    }
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setPressed(true);
                    }

                    if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        v.setPressed(false);
                    }
                    return true;
                }
            });

            endTrade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    obiect.setUserId("0");
                    obiect.setStatus(Status.DISPONIBIL.name());

                    DatabaseReference currentObjectDatabaseReference = oRef.child(bundle.getString("numeObiect"));
                    currentObjectDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Obiect obiect = dataSnapshot.getValue(Obiect.class);

                            DatabaseReference userOwnerOfObject = uRef.child(obiect.getUserId());
                            userOwnerOfObject.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        User userOwner = dataSnapshot.getValue(User.class);
                                        int counterRating = userOwner.getCounterRating();
                                        double rating = userOwner.getRating();
                                        double newRating = rating * counterRating;
                                        double ratingBarValue = ratingBar.getRating();
                                        newRating = (newRating + ratingBarValue) / (counterRating + 1);
                                        int obiecteOferite = userOwner.getObiecteOferite();

                                        uRef.child(dataSnapshot.getKey()).child("counterRating").setValue(counterRating + 1);
                                        uRef.child(dataSnapshot.getKey()).child("rating").setValue(newRating);
                                        uRef.child(dataSnapshot.getKey()).child("obiecteOferite").setValue(obiecteOferite + 1);
                                    } catch (Error e) {
                                        Toast.makeText(getApplicationContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            DatabaseReference currentUser = uRef.child(obiect.getUserId2());
                            currentUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        User userCurrent = dataSnapshot.getValue(User.class);
                                        int obiecteLuate = userCurrent.getObiecteOferite();
                                        uRef.child(dataSnapshot.getKey()).child("obiecteLuate").setValue(obiecteLuate + 1);
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
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

                    oRef.child(bundle.getString("numeObiect")).child("userId2").setValue("0");
                    oRef.child(bundle.getString("numeObiect")).child("status").setValue(Status.DISPONIBIL.name());

                    Intent i = new Intent(ObiectActivity.this, DashboardActivity.class);
                    startActivity(i);
                }
            });
        }

        delObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oRef.child(bundle.getString("numeObiect")).removeValue();
                Intent i = new Intent(ObiectActivity.this, DashboardActivity.class);
                startActivity(i);
            }
        });

        reqTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obiect.setUserId(firebaseUser.getUid());
                obiect.setStatus(Status.INCHIRIAT.name());
                oRef.child(bundle.getString("numeObiect")).child("userId2").setValue(firebaseUser.getUid());
                oRef.child(bundle.getString("numeObiect")).child("status").setValue(Status.INCHIRIAT.name());

                Intent i = new Intent(ObiectActivity.this, DashboardActivity.class);
                startActivity(i);
            }
        });

        cancelTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obiect.setUserId2("0");
                obiect.setStatus(Status.DISPONIBIL.name());
                oRef.child(bundle.getString("numeObiect")).child("userId2").setValue("0");
                oRef.child(bundle.getString("numeObiect")).child("status").setValue(Status.DISPONIBIL.name());

                Intent i = new Intent(ObiectActivity.this, DashboardActivity.class);
                startActivity(i);
            }
        });

        oRef.child(bundle.getString("numeObiect")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                obiect = dataSnapshot.getValue(Obiect.class);
                titluObj.setText(obiect.getNume());
                descriereObj.setText("Description: "+obiect.getDescriere());

                storageReference.child("images/"+ obiect.getImageId()).getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'
                            if(uri == null)
                                Toast.makeText(getApplicationContext(), "Error un getting object image", Toast.LENGTH_LONG).show();
                            else {
                                Picasso.with(ObiectActivity.this).load(uri).into(imagineObj);

                            }
                        }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                Toast.makeText(getApplicationContext(), "Error un getting object image", Toast.LENGTH_LONG).show();
                            }
                        });

                uRef.child(obiect.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        userDisplayName.setText(user.getDisplayName());
                        userPhoneNumber.setText(user.getPhoneNumber());
                        ratingBar.setRating((float) user.getRating());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                uRef.child(obiect.getUserId2()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!obiect.getUserId2().equals("0")) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user != null && bundle.get("from").equals("listaMea") && obiect.status.equals(Status.INCHIRIAT.name())) {
                                userDisplayName.setText(user.getDisplayName());
                                userPhoneNumber.setText(user.getPhoneNumber());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                titluObj.setText("");
                descriereObj.setText("");
                imagineObj.setImageURI(null);
            }
        });
    }
}
