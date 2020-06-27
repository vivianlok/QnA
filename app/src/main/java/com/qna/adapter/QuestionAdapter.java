package com.qna.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qna.R;
import com.qna.activities.RepliesActivity;
import com.qna.database.QuestionFirebaseItems;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


p                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        ublic class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {


    private FirebaseUser currentUser;
    private FirebaseAuth mFirebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDetailsReference, questionReference;
    String userID;
    //ImageView flagImage;


    Activity activity;
    List<QuestionFirebaseItems> questionFirebaseItemsList;
    View view;
    Boolean flaggedValue;



    public QuestionAdapter(Activity activity, List<QuestionFirebaseItems> questionFirebaseItems) {

        this.questionFirebaseItemsList = questionFirebaseItems;
        this.activity = activity;

        firebaseDatabase  = FirebaseDatabase.getInstance();
        usersDetailsReference = firebaseDatabase.getReference().child("Users_Details");
        questionReference = firebaseDatabase.getReference().child("Questions");
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        if (currentUser != null){

            userID = currentUser.getUid();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_questions, parent, false);
        ViewHolder viewHolder;
        viewHolder = new ViewHolder(view);


        return viewHolder;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final QuestionFirebaseItems questionFirebaseItems
                = questionFirebaseItemsList.get(position);

        // Assign values to the properties here
        holder.questionTextView.setText(questionFirebaseItems.getTitle());
        holder.dateTextView.setText(questionFirebaseItems.getDate());
        holder.userNameTextView.setText(questionFirebaseItems.getFullName());
        holder.categoryTextView.setText(questionFirebaseItems.getCategory());

        if (questionFirebaseItems != null){
            holder.viewTextView.setText("" + questionFirebaseItems.getViewsCount());
        } else {

            holder.viewTextView.setText("0");
        }




        performLogicFOrFlagImageClick(holder, questionFirebaseItems, questionReference);
        logicForFlaggedQuestions(holder, questionFirebaseItems, questionReference);


        questionReference.child(questionFirebaseItems.getQuestionId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Boolean flagged = dataSnapshot.child("flagged").getValue(Boolean.class);

                        if (flagged != null){
                            if (flagged){


                                holder.flagImage.setBackgroundResource(R.drawable.icon_red_flag);
                            } else
                            {
                                holder.flagImage.setBackgroundResource(R.drawable.flag_icon);
                            }
                        } else {

                            holder.flagImage.setBackgroundResource(R.drawable.flag_icon);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        usersDetailsReference.child(questionFirebaseItems.getAuthorId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String authorAvatar = dataSnapshot.child("avatar").getValue(String.class);
                        String authorCountry = dataSnapshot.child("country").getValue(String.class);

                        assert  authorCountry != null;

                        holder.countryTV.setText(authorCountry);

                        if (authorAvatar != null){

                            Picasso.get()
                                    .load(authorAvatar)
                                    .into(holder.userAvatarImageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        holder.shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToShare = questionFirebaseItems.getTitle();
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);

                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_SUBJECT, "sample");
                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(textToShare));

                activity.startActivity(Intent.createChooser(intent, "Share Quetions with your friends"));
            }
        });

        holder.answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToRepliesActivity(questionFirebaseItems);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToRepliesActivity(questionFirebaseItems);
            }
        });

    }  // End of onBindViewHolder

    private void logicForFlaggedQuestions(final ViewHolder holder, final QuestionFirebaseItems questionFirebaseItems, final DatabaseReference questionReference) {

       // if (questionFirebaseItems.flagged() != null){

            if (questionFirebaseItems.isFlagged()){

                holder.flagImage.setBackgroundResource(R.drawable.icon_red_flag);
            } else {

                holder.flagImage.setBackgroundResource(R.drawable.flag_icon);
            }

    }

    private void performLogicFOrFlagImageClick(final ViewHolder holder, final QuestionFirebaseItems questionFirebaseItems, final DatabaseReference questionReference) {

        holder.flagImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                questionReference.child(questionFirebaseItems.getQuestionId())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Boolean flagged = dataSnapshot.child("flagged").getValue(Boolean.class);

                                if (flagged != null){


                                    questionReference.child(questionFirebaseItems.getQuestionId())
                                            .child("flagged").setValue(flagged ? false : true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            if (questionFirebaseItems.isFlagged() == true){
                                                Toast.makeText(activity, "Successfully Flagged", Toast.LENGTH_SHORT).show();
                                                holder.flagImage.setBackgroundResource(R.drawable.icon_red_flag);
                                            } else if (questionFirebaseItems.isFlagged() == false){

                                                Toast.makeText(activity, "Unflagged", Toast.LENGTH_SHORT).show();
                                                holder.flagImage.setBackgroundResource(R.drawable.flag_icon);
                                            }
                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });







            }
        });
    }

    private void goToRepliesActivity(QuestionFirebaseItems questionFirebaseItems) {

        Intent intent = new Intent(activity, RepliesActivity.class);
        intent.putExtra("qID", questionFirebaseItems.getQuestionId());
        intent.putExtra("title", questionFirebaseItems.getTitle());
        activity.startActivity(intent);
    }


    @Override
    public int getItemCount() {

        return questionFirebaseItemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        // Declare properties for question items
        public TextView userNameTextView,questionTextView,dateTextView, categoryTextView,
                countryTV,viewTextView;
        public ImageView flagImage,shareImage;
        public CircleImageView userAvatarImageView;
        public Button answerButton;



        public ViewHolder(View itemView) {

            super(itemView);


            // Initialize all properties

            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            viewTextView = itemView.findViewById(R.id.viewTextView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            userAvatarImageView = itemView.findViewById(R.id.userAvatarImageView);
            flagImage = itemView.findViewById(R.id.flagImage);
            shareImage = itemView.findViewById(R.id.shareImage);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            countryTV = itemView.findViewById(R.id.countryTV);
            answerButton = itemView.findViewById(R.id.answerButton);


        }
    }
}