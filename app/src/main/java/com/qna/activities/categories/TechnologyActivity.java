package com.qna.activities.categories;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.qna.R;
import com.qna.adapter.QuestionAdapter;
import com.qna.database.QuestionFirebaseItems;

import java.util.ArrayList;
import java.util.List;

public class TechnologyActivity extends AppCompatActivity {
    Intent receiveIntentFromWelcomeActivity;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference QuestionDatabaseReference;

    String userId;
    // create and declare question replies ArrayList
    List<QuestionFirebaseItems> QuestionFirebaseItemsList = new ArrayList<>();

    RecyclerView QuestionRecyclerView;
    RecyclerView.Adapter questionAdapter;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technology);
        receiveIntentFromWelcomeActivity = getIntent();

        firebaseDatabase = FirebaseDatabase.getInstance();
        QuestionDatabaseReference = firebaseDatabase.getReference().child("Questions");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        QuestionRecyclerView = findViewById(R.id.QuestionRecyclerView);

        LinearLayoutManager allOrdersLinearLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        allOrdersLinearLayoutManager.setStackFromEnd(true);

        QuestionRecyclerView.setLayoutManager(allOrdersLinearLayoutManager);
        //Get Intent from Welcome Activity

        receiveIntentFromWelcomeActivity = getIntent();


        if (receiveIntentFromWelcomeActivity.getExtras() != null){

            String title = receiveIntentFromWelcomeActivity.getStringExtra("title");
            TextView titleTextView = findViewById(R.id.toolbarTitleTextView);
            titleTextView.setText(title);

            attachDatabaseReadListener();
        }
    } // End of onCreate method
    public void attachDatabaseReadListener() {

        QuestionDatabaseReference
                .orderByChild("category")
                .equalTo("Technology")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if (dataSnapshot.exists()) {


                            QuestionFirebaseItems QuestionFirebaseItems
                                    = dataSnapshot.getValue(QuestionFirebaseItems.class);

                            QuestionFirebaseItemsList.add(QuestionFirebaseItems);

                            // set adapter
                            questionAdapter = new QuestionAdapter(TechnologyActivity.this,
                                    QuestionFirebaseItemsList);
                            QuestionRecyclerView.setAdapter(questionAdapter);
                            questionAdapter.notifyDataSetChanged();


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


}