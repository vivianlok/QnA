package com.qna.activities.categories;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.qna.R;
import com.qna.database.UserDetailsFirebaseItem;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser currentUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDetailsReference;

    EditText nameET, emailET;
    Button countryButton,updateButton;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firebaseDatabase  = FirebaseDatabase.getInstance();
        usersDetailsReference = firebaseDatabase.getReference().child("Users_Details");
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();



        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        countryButton = findViewById(R.id.countryButton);
        updateButton = findViewById(R.id.updateButton);

        if (currentUser != null){
            userID = currentUser.getUid();
            setUserDetails();
    }

        updateButton.setOnClickListener(this);
countryButton.setOnClickListener(this);

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
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.updateButton){
            updateUSersDetails();
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