package com.sac.campusborrow.activities;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sac.campusborrow.R;
import com.sac.campusborrow.model.Obiect;
import com.sac.campusborrow.model.Status;

import java.util.UUID;

/**
 * Created by ionut on 1/16/2018.
 */

public class AddObjectActivity extends AppCompatActivity {
    private EditText txtObjectName;
    private EditText txtObjectDescription;
    private ImageView imageView;
    private Button btnChoose, btnAddObject, btnClearObject;
    static final int PICK_IMAGE_REQUEST = 1;
    private String filePath = "";
    private Uri filePathUri;
    //Firebase
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addobject);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        txtObjectName = (EditText) findViewById(R.id.txtNumeObiect);
        txtObjectDescription = (EditText) findViewById(R.id.txtDescriereObiect);
        imageView = (ImageView) findViewById(R.id.imageView);
        btnChoose = (Button) findViewById(R.id.button_choose);
        btnAddObject = (Button) findViewById(R.id.btnAddObject);
        btnClearObject = (Button) findViewById(R.id.btnClearObject);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBrowse();
            }
        });

        btnAddObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath != null) {
                    uploadImage();
                } else {
                    Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnClearObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageURI(null);
                txtObjectName.setText("");
                txtObjectDescription.setText("");
            }
        });
    }

    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if(requestCode == PICK_IMAGE_REQUEST){
                filePathUri = data.getData();
                filePath = getPath(filePathUri);

                Log.d("picUri", filePathUri.toString());
                Log.d("filePath", filePath);

                imageView.setImageURI(filePathUri);
            }
        }
    }

    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void uploadImage() {

        if(filePath != null && sanityChecks())
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final String uuid = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/"+ uuid);
            ref.putFile(filePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            try {
                                addObject(uuid);
                            } catch (Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddObjectActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                            Toast.makeText(AddObjectActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddObjectActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private void addObject(String uuid) {
        Obiect obiect = new Obiect();
        obiect.setNume(txtObjectName.getText().toString());
        obiect.setDescriere(txtObjectDescription.getText().toString());
        obiect.setImageId(uuid);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        obiect.setUserId(firebaseUser.getUid());
        obiect.setUserId2("0");
        obiect.setStatus(Status.DISPONIBIL.name());

        databaseReference.child("obiecte").child(uuid).setValue(obiect);
        Intent i = new Intent(AddObjectActivity.this, DashboardActivity.class);
        startActivity(i);
    }

    private boolean sanityChecks() {
        if ((imageView != null) &&
                (txtObjectName != null && txtObjectName.getText().length() != 0) &&
                (txtObjectDescription != null && txtObjectDescription.getText().length() != 0)) {
            return true;
        }
        return false;
    }
}
