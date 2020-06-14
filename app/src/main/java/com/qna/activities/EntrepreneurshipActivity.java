package com.qna.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.qna.R;

public class EntrepreneurshipActivity extends AppCompatActivity {


    Intent receiveIntentFromWelcomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrepreneurship);

        receiveIntentFromWelcomeActivity = getIntent();


        if (receiveIntentFromWelcomeActivity.getExtras() != null){

            String title = receiveIntentFromWelcomeActivity.getStringExtra("title");
            TextView titleTextView = findViewById(R.id.toolbarTitleTextView);
            titleTextView.setText(title);
        }
    }

}