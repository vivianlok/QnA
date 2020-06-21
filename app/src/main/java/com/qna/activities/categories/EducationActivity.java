package com.qna.activities.categories;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.qna.R;
import com.qna.database.QuestionFirebaseItems;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EducationActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_education);

        receiveIntentFromWelcomeActivity = getIntent();


        if (receiveIntentFromWelcomeActivity.getExtras() != null){

            String title = receiveIntentFromWelcomeActivity.getStringExtra("title");
            TextView titleTextView = findViewById(R.id.toolbarTitleTextView);
            titleTextView.setText(title);
        }
    }

}