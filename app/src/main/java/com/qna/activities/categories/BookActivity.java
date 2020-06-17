package com.qna.activities.categories;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.qna.R;

import org.w3c.dom.Text;

public class BookActivity extends AppCompatActivity {

    Intent receiveIntentFromWelcomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        receiveIntentFromWelcomeActivity = getIntent();


        if (receiveIntentFromWelcomeActivity.getExtras() != null){

            String title = receiveIntentFromWelcomeActivity.getStringExtra("title");
            TextView titleTextView = findViewById(R.id.toolbarTitleTextView);
            titleTextView.setText(title);
        }
    }

}