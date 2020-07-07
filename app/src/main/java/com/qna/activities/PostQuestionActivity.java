package com.qna.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.qna.R;
import com.qna.database.QuestionFirebaseItems;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostQuestionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseUser currentUser;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDetailsReference, questionReference;

    private FirebaseApp app;
    private FirebaseStorage storage;

    private Spinner spinner;
    EditText questionTitleET,descriptionET;
    Button attachmentButton, askSubmitButton;
    TextView currentUserNameTextView,userProfileLink;
    ImageView currentUserAvatarImageView;

    String userID, currentDate, currentTime;
    Uri dataUri = null;

    String selectedCategory = "Food"; //setting variable
    private static final String[] category
            = {"Food", "Entrepreneurship", "Education", "Fashion", "Fitness", "Book", "Art", "Pet", "Music", "Economics", "Business", "Travel", "Technology", "Sports", "Science"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);

        firebaseDatabase  = FirebaseDatabase.getInstance();
        usersDetailsReference = firebaseDatabase.getReference().child("Users_Details");
        questionReference = firebaseDatabase.getReference().child("Questions");
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();


         currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
         currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        app = FirebaseApp.getInstance();
        storage =FirebaseStorage.getInstance(app);

        questionTitleET = findViewById(R.id.questionTitleET);
        descriptionET =  findViewById(R.id.descriptionET);
        attachmentButton = findViewById(R.id.attachmentButton);
        askSubmitButton =  findViewById(R.id.askSubmitButton);
        currentUserNameTextView = findViewById(R.id.currentUserNameTextView);
        userProfileLink =  findViewById(R.id.userProfileLink);
        currentUserAvatarImageView = findViewById(R.id.currentUserAvatarImageView);

        if (currentUser != null){
            userID = currentUser.getUid();
            setUserDetails();
        }

        spinner = (Spinner) findViewById(R.id.selectCategory);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PostQuestionActivity.this,android.R.layout.simple_spinner_item, category);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        attachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAttachment();
            }
        });

        askSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                performActionForAskQuestion();
            }
        });


    } // End of onCreate method



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                selectedCategory = category[0];
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                selectedCategory = category[1];
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                selectedCategory = category[2];
            case 3:
                // Whatever you want to happen when the first item gets selected
                selectedCategory = category[3];
                break;
            case 4:
                // Whatever you want to happen when the second item gets selected
                selectedCategory = category[4];
                break;
            case 5:
                // Whatever you want to happen when the thrid item gets selected
                selectedCategory = category[5];
                break;
            case 6:
                // Whatever you want to happen when the second item gets selected
                selectedCategory = category[6];
                break;
            case 7:
                // Whatever you want to happen when the thrid item gets selected
                selectedCategory = category[7];
                break;
            case 8:
                // Whatever you want to happen when the second item gets selected
                selectedCategory = category[8];
                break;
            case 9:
                // Whatever you want to happen when the thrid item gets selected
                selectedCategory = category[9];
                break;
            case 10:
                // Whatever you want to happen when the thrid item gets selected
                selectedCategory = category[10];
                break;
            case 11:
                // Whatever you want to happen when the second item gets selected
                selectedCategory = category[11];
                break;
            case 12:
                // Whatever you want to happen when the thrid item gets selected
                selectedCategory = category[12];
                break;
            case 13:
                // Whatever you want to happen when the second item gets selected
                selectedCategory = category[13];
                break;
            case 14:
                // Whatever you want to happen when the thrid item gets selected
                selectedCategory = category[14];
                break;
            case 15:
                // Whatever you want to happen when the thrid item gets selected
                selectedCategory = category[15];
                break;
        }
    }

    private void selectAttachment() {

        Intent attachment = new Intent(Intent.ACTION_GET_CONTENT);
        attachment.setType("*/*");
        startActivityForResult(Intent.createChooser(attachment, "Pick Attachment"),
                100);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 100 && resultCode == RESULT_OK) {


             dataUri = data.getData(); //getting path

            attachmentButton.setText("Selected");
            attachmentButton.setTextColor(Color.BLUE);


        }

    }

    private void performActionForAskQuestion() {



        if(TextUtils.isEmpty(questionTitleET.getText().toString())){
            Toast.makeText(PostQuestionActivity.this,"Error: Please post a question",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(descriptionET.getText().toString())){
            Toast.makeText(PostQuestionActivity.this,"Error: Please post description",Toast.LENGTH_SHORT).show();
        }
       else  {


            final String questionId = questionReference.push().getKey();

           if (dataUri != null){
               StorageReference storageReference = storage.getReference(selectedCategory).child(dataUri.getLastPathSegment()); //getting pic - last path of image
               storageReference.putFile(dataUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                       attachmentButton.setText("Attachment Added");
                       attachmentButton.setTextColor(Color.DKGRAY);
                   }
               });

               storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {

                    String   downloadedUriForAttachment = uri.toString();
                       saveQuestionToDatabase(questionId, downloadedUriForAttachment);
                   }
               });
           } else {

               saveQuestionToDatabase(questionId, "");

           }

        }
    }

    private void saveQuestionToDatabase(String questionId, String attachment) {

        QuestionFirebaseItems questionFirebaseItems
                = new QuestionFirebaseItems(
                questionId,
                currentUser.getDisplayName(),
                "",
                currentDate,
                currentTime,
                questionTitleET.getText().toString(),
                descriptionET.getText().toString(),
                attachment,
                selectedCategory,
                userID,
                0,
                false


        );

        questionReference.child(questionId)
                .setValue(questionFirebaseItems)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        questionTitleET.setText("");
                        descriptionET.setText("");
                        showToast("Question successfully submitted.");
                        finish();
                    }
                });
    }

    public void  showToast(String text){

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    private void setUserDetails() {

        currentUserNameTextView.setText(currentUser.getDisplayName());
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
// TODO Auto-generated method stub
    }
}