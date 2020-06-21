package com.qna.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.qna.R;
import com.qna.activities.categories.EditProfileActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements  View.OnClickListener {

    private FirebaseUser currentUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDetailsReference;
    private FirebaseApp app;
    private FirebaseStorage storage;

    String userID;

    Button editProfileButton;
    CircleImageView profileCircleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        firebaseDatabase  = FirebaseDatabase.getInstance();
        usersDetailsReference = firebaseDatabase.getReference().child("Users_Details");
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        //Get storage instance
        app = FirebaseApp.getInstance();
        storage =FirebaseStorage.getInstance(app);

        editProfileButton = findViewById(R.id.editProfileButton);
        profileCircleImageView = findViewById(R.id.profile_picture);

        editProfileButton.setOnClickListener(this);

        if (currentUser != null){

            userID = currentUser.getUid();
            setUserDetails();
        }


    } // End of onCreate Method

    private void setUserDetails() {

        usersDetailsReference
               .child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String avatar = dataSnapshot.child("avatar").getValue(String.class);

                        if (avatar != null) {
                            Picasso.get()
                                    .load(avatar)
                                    .into(profileCircleImageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.editProfileButton){
            Intent goToEditProfileActivity = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(goToEditProfileActivity);
        }
    }
}