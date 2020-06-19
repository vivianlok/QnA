package com.qna.activities.categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.qna.R;
import com.qna.database.UserDetailsFirebaseItem;
import com.squareup.picasso.Picasso;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser currentUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDetailsReference;
    private FirebaseApp app;
    private FirebaseStorage storage;

    EditText nameET, emailET;
    Button countryButton,updateButton;
    CircleImageView editProfilePictureImage;

    String userID;
    Uri dataUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firebaseDatabase  = FirebaseDatabase.getInstance();
        usersDetailsReference = firebaseDatabase.getReference().child("Users_Details");
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        //Get storage instance
        app = FirebaseApp.getInstance();
        storage =FirebaseStorage.getInstance(app);

        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        countryButton = findViewById(R.id.countryButton);
        updateButton = findViewById(R.id.updateButton);
        editProfilePictureImage = findViewById(R.id.editProfilePictureImage);

        if (currentUser != null){
            userID = currentUser.getUid();
            setUserDetails();
    }


        updateButton.setOnClickListener(this);
        countryButton.setOnClickListener(this);
        editProfilePictureImage.setOnClickListener(this);

        countryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String countryName, String code, String dialCode, int flagDrawableResID) {
                        // Implement your code here
                        countryButton.setText(countryName);
                        picker.dismiss();
                    }
                });
                picker.show(getSupportFragmentManager(), "CHOOSE COUNTRY");
            }
        });
    }



    private void setUserDetails() {

        nameET.setText(currentUser.getDisplayName());
        emailET.setText(currentUser.getEmail());

        usersDetailsReference.
                child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String avatar = dataSnapshot.child("avatar").getValue(String.class);

                if (avatar != null) {
                    Picasso.get()
                            .load(avatar)
                            .into(editProfilePictureImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.updateButton){
            updateUSersDetails();
        }
        else if (v.getId() == R.id.editProfilePictureImage){
            slectAttachment();
        }
    }


    private void slectAttachment() {

        Intent attachment = new Intent(Intent.ACTION_GET_CONTENT);
        attachment.setType("*/*");
        startActivityForResult(Intent.createChooser(attachment, "Pick Attachment"),
                100);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 100 && resultCode == RESULT_OK) {


            dataUri = data.getData(); //getting path
            final StorageReference storageReference
                    = storage.getReference("Profile_Pictures").child(dataUri.getLastPathSegment()); //getting pic - last path of image
            storageReference.putFile(dataUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    // Start here
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String downloadedUri = uri.toString();

                            usersDetailsReference.child(userID).child("avatar").setValue(downloadedUri);
                            Picasso.get()
                                    .load(downloadedUri)
                                    .into(editProfilePictureImage);
                        }
                    });

                    // Ends here
                }
            });

        }

    }

    private void updateUSersDetails() {

        if (TextUtils.isEmpty(nameET.getText().toString())){
            Toast.makeText(EditProfileActivity.this, "Error: Please fill out Name",Toast.LENGTH_SHORT).show();
        }
        else if  (TextUtils.isEmpty(countryButton.getText().toString())){
            Toast.makeText(EditProfileActivity.this, "Error: Please fill out Country",Toast.LENGTH_SHORT).show();
        }
        else if  (TextUtils.isEmpty(emailET.getText().toString())){
            Toast.makeText(EditProfileActivity.this, "Error: Please fill out Email",Toast.LENGTH_SHORT).show();
        }
        else{
            UserDetailsFirebaseItem userDetailsFirebaseItem = new UserDetailsFirebaseItem(
                    nameET.getText().toString(),
                    emailET.getText().toString(),
                    countryButton.getText().toString()
            );

            usersDetailsReference.child(userID).setValue(userDetailsFirebaseItem)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(EditProfileActivity.this, "Details Successfully Updated", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}