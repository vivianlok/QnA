package com.qna.activities.categories;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.qna.R;

public class ScienceActivity extends AppCompatActivity {
    Intent receiveIntentFromWelcomeActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_science);
        receiveIntentFromWelcomeActivity = getIntent();

        if (receiveIntentFromWelcomeActivity.getExtras() != null){

            String title = receiveIntentFromWelcomeActivity.getStringExtra("title");
            TextView titleTextView = findViewById(R.id.toolbarTitleTextView);
            titleTextView.setText(title);
        } //End of if - receive Intent
    }//End of oncreate method
} //End of class