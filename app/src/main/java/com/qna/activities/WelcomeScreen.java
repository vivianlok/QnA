package com.qna.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qna.PostQuestionActivity;
import com.qna.R;

import java.util.Arrays;

public class WelcomeScreen extends AppCompatActivity implements View.OnClickListener {

    private FirebaseUser currentUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDetailsReference;


    // Declare CardViews

    CardView foodCV, entrepreneurshipCV,educationCV,fashionCV,fitnessCV,bookCV,artCV,petCV,musicCV,economicsCV,businessCV,travelCV,technologyCV,sportsCV,scienceCV;
    TextView logoutTV;
    String userId;
    public static final int RC_SIGN_IN = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);


        firebaseDatabase  = FirebaseDatabase.getInstance();
        usersDetailsReference = firebaseDatabase.getReference().child("Users_Details");
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        // Initialize CardViews

        foodCV = findViewById(R.id.foodCV);
        entrepreneurshipCV = findViewById(R.id.entrepreneurshipCV);
        educationCV = findViewById(R.id.educationCV);
        fashionCV = findViewById(R.id.fashionCV);
        fitnessCV = findViewById(R.id.fitnessCV);
        bookCV = findViewById(R.id.bookCV);
        artCV = findViewById(R.id.artCV);
        petCV = findViewById(R.id.petCV);
        musicCV = findViewById(R.id.musicCV);
        economicsCV = findViewById(R.id.economicsCV);
        businessCV = findViewById(R.id.businessCV);
        travelCV = findViewById(R.id.travelCV);
        technologyCV = findViewById(R.id.technologyCV);
        sportsCV = findViewById(R.id.sportsCV);
        scienceCV = findViewById(R.id.scienceCV);
        logoutTV = findViewById(R.id.logoutTV);

        foodCV.setOnClickListener(this);
        entrepreneurshipCV.setOnClickListener(this);
        educationCV.setOnClickListener(this);
        fashionCV.setOnClickListener(this);
        fitnessCV.setOnClickListener(this);
        bookCV.setOnClickListener(this);
        artCV.setOnClickListener(this);
        petCV.setOnClickListener(this);
        musicCV.setOnClickListener(this);
        economicsCV.setOnClickListener(this);
        businessCV.setOnClickListener(this);
        travelCV.setOnClickListener(this);
        technologyCV.setOnClickListener(this);
        sportsCV.setOnClickListener(this);
        scienceCV.setOnClickListener(this);
        logoutTV.setOnClickListener(this);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                currentUser = mFirebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    /// onSignedInInitialize(user.getDisplayName());
                    userId = currentUser.getUid();
                    //getUsersDetails(currentUser);
                    checkForNewUsers(userId);


                } else {



                    startActivityForResult(

                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            //  new AuthUI.IdpConfig.GoogleBuilder().build()
                                            //     new AuthUI.IdpConfig.FacebookBuilder().build(),
                                            //      new AuthUI.IdpConfig.TwitterBuilder().build(),
                                            //    new AuthUI.IdpConfig.GitHubBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            //      new AuthUI.IdpConfig.PhoneBuilder().build(),
                                                 new    AuthUI.IdpConfig.AnonymousBuilder().build()
                                    ))
                                    .build(),
                            RC_SIGN_IN);


                    mFirebaseAuth.setLanguageCode("en");
// To apply the default app language instead of explicitly setting it.
// auth.useAppLanguage();

                }
            }


        };

    }

    private void checkForNewUsers(String userId) {

        usersDetailsReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()){

                    Intent goToEditProfileActivity = new Intent(WelcomeScreen.this, EditProfileActivity.class);
                    startActivity(goToEditProfileActivity);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.foodCV){

            // Food CardView was clicked

            Intent goToFoodCvIntent = new Intent(WelcomeScreen.this, FoodActivity.class);
            goToFoodCvIntent.putExtra("title", "Food");
            startActivity(goToFoodCvIntent);
        }  else  if (v.getId() == R.id.entrepreneurshipCV){
            //onClick
            Intent goToEntrepreneurshipCvIntent = new Intent(WelcomeScreen.this, EntrepreneurshipActivity.class);
            goToEntrepreneurshipCvIntent.putExtra("title", "Entrepreneurship");
            startActivity(goToEntrepreneurshipCvIntent);
        } //End of else if
        else  if (v.getId() == R.id.educationCV){
            //onClick
            Intent goToEducationCvIntent = new Intent(WelcomeScreen.this, EducationActivity.class);
            goToEducationCvIntent.putExtra("title", "Education");
            startActivity(goToEducationCvIntent);
        } //End of else if
        else  if (v.getId() == R.id.fashionCV){
            //onClick
            Intent goToFashionCvIntent = new Intent(WelcomeScreen.this, FashionActivity.class);
            goToFashionCvIntent.putExtra("title", "Fashion");
            startActivity(goToFashionCvIntent);

        } //End of else if
        else  if (v.getId() == R.id.fitnessCV){
            //onClick
            Intent goToFitnessCvIntent = new Intent(WelcomeScreen.this, FitnessActivity.class);
            goToFitnessCvIntent.putExtra("title", "Fitness");
            startActivity(goToFitnessCvIntent);

        } //End of else if
        else  if (v.getId() == R.id.bookCV){
            //onClick
            Intent goToBookCvIntent = new Intent(WelcomeScreen.this, BookActivity.class);
            goToBookCvIntent.putExtra("title", "Book");
            startActivity(goToBookCvIntent);

        } //End of else if
        else  if (v.getId() == R.id.artCV){
            //onClick
            Intent goToArtCvIntent = new Intent(WelcomeScreen.this, ArtActivity.class);
            goToArtCvIntent.putExtra("title", "Art");
            startActivity(goToArtCvIntent);

        } //End of else if
        else  if (v.getId() == R.id.petCV){
            //onClick
            Intent goToPetCvIntent = new Intent(WelcomeScreen.this, PetActivity.class);
            goToPetCvIntent.putExtra("title", "Pet");
            startActivity(goToPetCvIntent);

        } //End of else if
        else  if (v.getId() == R.id.musicCV){
            //onClick
            Intent goToMusicCvIntent = new Intent(WelcomeScreen.this, MusicActivity.class);
            goToMusicCvIntent.putExtra("title", "Music");
            startActivity(goToMusicCvIntent);

        } //End of else if
        else  if (v.getId() == R.id.economicsCV){
            //onClick
            Intent goToEconomicsCvIntent = new Intent(WelcomeScreen.this, EconomicsActivity.class);
            goToEconomicsCvIntent.putExtra("title", "Economics");
            startActivity(goToEconomicsCvIntent);

        } //End of else if
        else  if (v.getId() == R.id.businessCV){
            //onClick
            Intent goToBusinessCvIntent = new Intent(WelcomeScreen.this, BusinessActivity.class);
            goToBusinessCvIntent.putExtra("title", "Business");
            startActivity(goToBusinessCvIntent);

        } //End of else if
        else  if (v.getId() == R.id.travelCV){
            //onClick
            Intent goToTravelCvIntent = new Intent(WelcomeScreen.this, TravelActivity.class);
            goToTravelCvIntent.putExtra("title", "Travel");
            startActivity(goToTravelCvIntent);

        } //End of else if
        else  if (v.getId() == R.id.technologyCV){
            //onClick
            Intent goToTechnologyCvIntent = new Intent(WelcomeScreen.this, TechnologyActivity.class);
            goToTechnologyCvIntent.putExtra("title", "Technology");
            startActivity(goToTechnologyCvIntent);

        } //End of else if
        else  if (v.getId() == R.id.sportsCV){
            //onClick
            Intent goToSportsCvIntent = new Intent(WelcomeScreen.this, SportsActivity.class);
            goToSportsCvIntent.putExtra("title", "Sports");
            startActivity(goToSportsCvIntent);

        } //End of else if
        else  if (v.getId() == R.id.scienceCV){
            //onClick
            Intent goToScienceCvIntent = new Intent(WelcomeScreen.this, ScienceActivity.class);
            goToScienceCvIntent.putExtra("title", "Science");
            startActivity(goToScienceCvIntent);

        } //End of else if
        else if (v.getId() == R.id.logoutTV){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);


    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);


        }


    }


    public void goToPostQuestionPage(View view) {
        Intent goToPostQuestionPage = new Intent(WelcomeScreen.this, PostQuestionActivity.class);
        startActivity(goToPostQuestionPage);
    }
}