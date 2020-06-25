package com.qna.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qna.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class RepliesActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mFirebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDetailsReference, questionReference;
    String userID;
    //ImageView flagImage;

Intent getInfoFromMainActivity;
String qID, title;
TextView questionTextView, viewsTV;
ImageView shareImage;
CircleImageView currentUserAvatarImageView;
Button attachmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replies);

        firebaseDatabase  = FirebaseDatabase.getInstance();
        usersDetailsReference = firebaseDatabase.getReference().child("Users_Details");
        questionReference = firebaseDatabase.getReference().child("Questions");
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        if (currentUser != null){

            userID = currentUser.getUid();
        }

        questionTextView  =findViewById(R.id.questionTextView);
        shareImage = findViewById(R.id.shareImage);
        viewsTV = findViewById(R.id.viewsTV);
        currentUserAvatarImageView = findViewById(R.id.currentUserAvatarImageView);

        getInfoFromMainActivity = getIntent(); // get intent

        if(getInfoFromMainActivity != null){
            qID = getInfoFromMainActivity.getStringExtra("qID");
            title = getInfoFromMainActivity.getStringExtra("title");
            questionTextView.setText(title);

            performLogicFOrViewsCount(qID);
            setProfilePicture();

        }

        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToShare = title;
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);

                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_SUBJECT, "sample");
                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(textToShare));

                startActivity(Intent.createChooser(intent, "Share Quetions with your friends"));
            }
        });

    } // End of onCreate method

    private void setProfilePicture() {

        usersDetailsReference
                .child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String avatar = dataSnapshot.child("avatar").getValue(String.class);

                        if (avatar != null) {
                            Picasso.get()
                                    .load(avatar)
                                    .into(currentUserAvatarImageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    // Listen to database qID child: viewsCount value when it is changed
    private void performLogicFOrViewsCount(final String qID) {
        //Listen to on change from the questionreference and gets the viewsCount
        questionReference
                .child(qID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Fetching the views count from the Firebase
                        final Integer viewsCount = dataSnapshot.child("viewsCount").getValue(Integer.class);

                        assert viewsCount != null;
                        viewsTV.setText("" + viewsCount);

                             //Handler delay add and method run which add views when it reaches 7 seconds - with onSuccessListener to show it has incremented
                        Handler handler = new Handler();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                int viewToAdd = viewsCount != null ?  viewsCount + 1 : 1;

                                questionReference
                                        .child(qID)
                                        .child("viewsCount")
                                        .setValue(viewToAdd)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                viewsTV.setText("" + viewsCount);
                                                //Toast.makeText(RepliesActivity.this, "Views added successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        }, 7000);





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    } //end of addToViewsCount

}