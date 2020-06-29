package com.qna.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qna.R;
import com.qna.adapter.QuestionAdapter;
import com.qna.adapter.RepliesAdapter;
import com.qna.database.QuestionFirebaseItems;
import com.qna.database.RepliesFirebaseItem;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RepliesActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mFirebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDetailsReference, questionReference,repliesDatabaseReference;
    String userID, currentDate, currentTime;
    //ImageView flagImage;
    String userId;
    // create and declare question replies ArrayList
    List<RepliesFirebaseItem> repliesFirebaseItemList = new ArrayList<>();
    RecyclerView repliesRecyclerView;
    RecyclerView.Adapter repliesAdapter;

    Intent getInfoFromMainActivity;
   public static String qID, title;
    TextView questionTextView, viewsTV;
    ImageView shareImage;
    CircleImageView currentUserAvatarImageView;
    Button attachmentButton;

    TextView postTextView;
    EditText replyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replies);

        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDetailsReference = firebaseDatabase.getReference().child("Users_Details");
        questionReference = firebaseDatabase.getReference().child("Questions");
        //repliesDatabaseReference = firebaseDatabase.getReference().child("Questions");
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        if (currentUser != null) {

            userID = currentUser.getUid();
        }

        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        questionTextView = findViewById(R.id.questionTextView);
        shareImage = findViewById(R.id.shareImage);
        viewsTV = findViewById(R.id.viewsTV);
        currentUserAvatarImageView = findViewById(R.id.currentUserAvatarImageView);
        attachmentButton = findViewById(R.id.attachmentButton);
        postTextView = findViewById(R.id.postTextView);
        replyEditText = findViewById(R.id.replyEditText);

        repliesRecyclerView = findViewById(R.id.repliesRecyclerView);

        LinearLayoutManager allOrdersLinearLayoutManager;
        allOrdersLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        allOrdersLinearLayoutManager.setStackFromEnd(true);

        repliesRecyclerView.setLayoutManager(allOrdersLinearLayoutManager);

        getInfoFromMainActivity = getIntent(); // get intent

        if (getInfoFromMainActivity != null) {
            qID = getInfoFromMainActivity.getStringExtra("qID");
            title = getInfoFromMainActivity.getStringExtra("title");
            questionTextView.setText(title);

            performLogicFOrViewsCount(qID);
            setProfilePicture();
            attachDatabaseReadListener();

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


        postTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if (TextUtils.isEmpty(replyEditText.getText().toString())){

                  Toast.makeText(RepliesActivity.this, "Please enter a reply", Toast.LENGTH_SHORT).show();
              } else {

                  String replyId = questionReference.child(qID).child("replies").push().getKey();

                  RepliesFirebaseItem repliesFirebaseItem
                          = new RepliesFirebaseItem(
                          replyId,
                          currentUser.getDisplayName(),
                          "",
                          currentDate + " at " + currentTime,
                          replyEditText.getText().toString()
                  );

                  questionReference.child(qID).child("replies")
                          .child(replyId).setValue(repliesFirebaseItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {

                          replyEditText.setText("");
                      }
                  });
              }
            }
        });

    } // End of onCreate method


    public void attachDatabaseReadListener() {



        questionReference
              .child(qID)
                .child("replies")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if (dataSnapshot.exists()) {


                            RepliesFirebaseItem repliesFirebaseItem
                                    = dataSnapshot.getValue(RepliesFirebaseItem.class);

                            repliesFirebaseItemList.add(repliesFirebaseItem);

                            // set adapter
                            repliesAdapter = new RepliesAdapter(RepliesActivity.this,
                                    repliesFirebaseItemList);
                            repliesRecyclerView.setAdapter(repliesAdapter);
                            repliesAdapter.notifyDataSetChanged();


                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

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
                        //final Integer likeCount = dataSnapshot.child(replyId).child("likeCount").getValue(Integer.class);
                        String attachment = dataSnapshot.child("attachment").getValue(String.class);
                        assert attachment != null;

                        if (!attachment.equalsIgnoreCase("")) {

                            attachmentButton.setText("Download Attachment");
                            attachmentButton.setTextColor(Color.WHITE);
                            attachmentButton.setBackgroundResource(R.drawable.round_btn);
                        }

                        assert viewsCount != null;
                        viewsTV.setText("" + viewsCount);

                        //Handler delay add and method run which add views when it reaches 7 seconds - with onSuccessListener to show it has incremented
                        Handler handler = new Handler();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                int viewToAdd = viewsCount != null ? viewsCount + 1 : 1;

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