package com.qna.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
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
import com.qna.activities.NewsFeedActivity;
import com.qna.activities.RepliesActivity;
import com.qna.activities.SplashScreenActivity;
import com.qna.database.LikesAndDislikeFirebaseItem;
import com.qna.database.QuestionFirebaseItems;
import com.qna.database.RepliesFirebaseItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.qna.activities.RepliesActivity.qID;


public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    /**
     *  This is where the Firebase items were declared such as the Firebase User
     *  Firebase Auth and all the references
     */
    private FirebaseUser currentUser;
    private FirebaseAuth mFirebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDetailsReference, questionReference;
    String userID;

    // Here is the declaration for the activity to get the context
    // (Kindly refer stack overflow to understand context)
    Activity activity;

    // Here is the declaration for the question list
    List<QuestionFirebaseItems> questionFirebaseItemsList;

    // This is the View that is holding each questions
    // (for example the cardView holding a particular question)
    View view;


    /**
     * This is the QuestionAdapter Constructor
     */
    public QuestionAdapter(Activity activity, List<QuestionFirebaseItems> questionFirebaseItems) {

        /**
         *  Inside this contructor, all other initializations were made
         *  such as the list, context and the firebase items (firebase references)
         */

        this.questionFirebaseItemsList = questionFirebaseItems;
        this.activity = activity;


        firebaseDatabase = FirebaseDatabase.getInstance();
        usersDetailsReference = firebaseDatabase.getReference().child("Users_Details");
        questionReference = firebaseDatabase.getReference().child("Questions");
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        if (currentUser != null) {

            userID = currentUser.getUid();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /**
         *  Here's the ViewHolder where we set the item to be displayed in the recyeclerVIew
         *  which in this case is the  "item_questions.xml"
         */

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_questions, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        /**
         *  Here's the onBideViewHolder, here is where we are performing our logics for each questions
         *  Logics such as fetching each details like the question title, setting onClick listeners and so on
         */

        // Get the question position and then assign it to a variable called "questionFirebaseItems"
        // So we are going to be using the variable called "questionFirebaseItems" to reference each and
        // every question

        final QuestionFirebaseItems questionFirebaseItems
                = questionFirebaseItemsList.get(position);

        questionFirebaseItems.getQuestionId();
        // Assign values to the properties here
        viewHolder.questionTextView.setText(questionFirebaseItems.getTitle());
        viewHolder.dateTextView.setText(questionFirebaseItems.getDate());
        viewHolder.userNameTextView.setText(questionFirebaseItems.getFullName());
        viewHolder.categoryTextView.setText(questionFirebaseItems.getCategory());

        //viewHolder.country_icon.setBackgroundResource(questionFirebaseItems.getCountryIcon());

        checkIfQuestionIsSolved(viewHolder, questionFirebaseItems);

        questionReference
                .child(questionFirebaseItems.getQuestionId())
                .child("likes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        long likesCount = dataSnapshot.getChildrenCount();
                        if (dataSnapshot.exists()){
                            viewHolder.likeCountTV.setText("" + likesCount);
                        } else {

                            viewHolder.likeCountTV.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        questionReference
                .child(questionFirebaseItems.getQuestionId())
                .child("disLikes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        long disLikesCount = dataSnapshot.getChildrenCount();
                        if (dataSnapshot.exists()){
                            viewHolder.unlikeCountTV.setText("" + disLikesCount);
                        } else {

                            viewHolder.unlikeCountTV.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        // checks if the questionFirebaseItems is not null
        if (questionFirebaseItems != null) {
            // fetch and set the value of view counts to
            viewHolder.viewTextView.setText("" + questionFirebaseItems.getViewsCount());
        } else {

            // if it is null, set the value to Zero
            viewHolder.viewTextView.setText("0");
        }

        // Method invocation to perform logic for the Flag imageView
        performLogicFOrFlagImageClick(viewHolder, questionFirebaseItems, questionReference);

        // Method invocation to perform logic for flaggedQuestions
        logicForFlaggedQuestions(viewHolder, questionFirebaseItems);


        // Checks to see if the value of authorId is not null (if it exits in firebase)
        if (questionFirebaseItems.getAuthorId() != null) {

            /**
             *  Performs logic for fetching the Author details
             */

            usersDetailsReference.child(questionFirebaseItems.getAuthorId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String authorAvatar = dataSnapshot.child("avatar").getValue(String.class);
                            String authorCountry = dataSnapshot.child("country").getValue(String.class);

                            assert authorCountry != null;

                            viewHolder.countryTV.setText(authorCountry);

                            if (authorAvatar != null) {

                                Picasso.get()
                                        .load(authorAvatar)
                                        .into(viewHolder.userAvatarImageView);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        /**
         *  Listen to click when the share imageView is clicked
         *  Meaning that onClick listener is added to the Share ImageView here
         */
        viewHolder.shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 *  Logic for explicit intent to share questions externally
                 */

                String textToShare = questionFirebaseItems.getTitle();
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);

                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_SUBJECT, "sample");
                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(textToShare));

                activity.startActivity(Intent.createChooser(intent, "Share Quetions with your friends"));
            }
        });

        /**
         *  Add onClick listener to answer button
         */
        viewHolder.answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // invoke method to perform the logic for answer button (going to the replies Activity)
                goToRepliesActivity(questionFirebaseItems);
            }
        });

        /**
         *  Add onClick listener to the entire question body
         */
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // invoke method to perform the logic for answer button (going to the replies Activity)
                goToRepliesActivity(questionFirebaseItems);
            }
        });

        viewHolder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                performLogicForLikes(questionFirebaseItems, viewHolder);


            }
        }); //end of unlikeImage onclicklistener

        viewHolder.unlikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                performLogicForDislike(questionFirebaseItems, viewHolder);


            }
        }); //end of unlikeImage onclicklistener
    }  // End of onBindViewHolder

    private void performLogicForLikes(final QuestionFirebaseItems questionFirebaseItems, QuestionAdapter.ViewHolder viewHolder) {

        questionReference
                .child(questionFirebaseItems.getQuestionId())
                .child("likes")
                //userId from the database
                .orderByChild("userID")
                //current userID
                .equalTo(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!dataSnapshot.exists()){

                            LikesAndDislikeFirebaseItem likesAndDislikeFirebaseItem
                                    = new LikesAndDislikeFirebaseItem(
                                    userID
                            );
                            questionReference
                                    .child(questionFirebaseItems.getQuestionId())
                                    .child("likes")
                                    .child(userID)
                                    .setValue(likesAndDislikeFirebaseItem)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(activity, "Liked Question", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else  {

                            questionReference
                                    .child(questionFirebaseItems.getQuestionId())
                                    .child("likes")
                                    .child(userID)
                                    .removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(activity, "Successfully removed", Toast.LENGTH_SHORT).show();
                                }
                            });

                           // Toast.makeText(activity, "You already liked this question", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void checkIfQuestionIsSolved(final ViewHolder viewHolder, QuestionFirebaseItems questionFirebaseItems) {

        questionReference
                .child(questionFirebaseItems.getQuestionId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Boolean solved = dataSnapshot.child("solved").getValue(Boolean.class);

                        if (solved != null){

                            if (solved){

                                // returns true
                                viewHolder.solvedImage.setBackgroundResource(R.drawable.icon_solved);
                            } else {

                                viewHolder.solvedImage.setBackgroundResource(R.drawable.icon_x);

                            }
                        } else {

                            viewHolder.solvedImage.setBackgroundResource(R.drawable.icon_x);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void performLogicForDislike(final QuestionFirebaseItems questionFirebaseItems, QuestionAdapter.ViewHolder viewHolder) {

        questionReference
                .child(questionFirebaseItems.getQuestionId())
                .child("disLikes")
                //userId from the database
                .orderByChild("userID")
                //current userID
                .equalTo(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (!dataSnapshot.exists()){

                            LikesAndDislikeFirebaseItem likesAndDislikeFirebaseItem
                                    = new LikesAndDislikeFirebaseItem(userID);
                            questionReference
                                    .child(questionFirebaseItems.getQuestionId())
                                    .child("disLikes")
                                    .child(userID) //push
                                    .setValue(likesAndDislikeFirebaseItem)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(activity, "Disliked Question", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else  {

                            // If user exist

                            questionReference
                                    .child(questionFirebaseItems.getQuestionId())
                                    .child("disLikes")
                                    .child(userID) //push
                                    .removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(activity, "Successfully removed", Toast.LENGTH_SHORT).show();
                                }
                            });


                            //  Toast.makeText(activity, "You already disliked this question", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    /**
     *  This is the method that performs logic for the FlaggedQuestions immediately when the app starts
     */
    private void logicForFlaggedQuestions(final ViewHolder viewHolder,
                                          final QuestionFirebaseItems questionFirebaseItems) {

        // Checks to see if the question is flagged
        if (questionFirebaseItems.isFlagged()) {
            // set the flag to red
            viewHolder.flagImage.setBackgroundResource(R.drawable.icon_red_flag);
        } else {

            // if it is not flagged (i.e false), then set the flag to the normal one
            viewHolder.flagImage.setBackgroundResource(R.drawable.flag_icon);
        }

    }

    /**
     *
     * This is the method that handles the click for
     * the Flag Image View.
     */
    private void performLogicFOrFlagImageClick(final ViewHolder viewHolder,
                                               final QuestionFirebaseItems questionFirebaseItems,
                                               final DatabaseReference questionReference) {

        viewHolder.flagImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 *  The Flag ImageView has been clicked
                 */

                // We use the question id from the Question Reference to get the current question that was clicked
                // and then finally add a LISTENER FOR SINGLE VALUE EVENT
                questionReference.child(questionFirebaseItems.getQuestionId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                /**
                                 *  The onDataChange method is called whenever the value for the flagged is changed in
                                 *  the database
                                 */


                                // Fetching the flagged value from firebase so we can know if
                                // the value is true or false (It is returning a boolean value)
                                final Boolean flagged = dataSnapshot.child("flagged").getValue(Boolean.class);

                                // Check to see if the flagged field is empty or not in the firebase
                                // hence checking if it is not equal to null
                                if (flagged != null) {

                                    /**
                                     *  This will return true if it is not equal to null, which means it exit
                                     *  and it is ready to update the value of flagged
                                     */

                                    // Use the question reference together with the questionId to update
                                    // the value of the flagged. The simple logic used here is to first check
                                    // if the value of flagged is true of false, if it is true, it will update
                                    // it to false, and if the value is false it will update is to true
                                    questionReference
                                            //qID questionFirebaseItems.getQuestionId()
                                            .child(qID)
                                            .child("flagged").setValue(flagged ? false : true)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    /**
                                                     *  This is the onSuccess listener which is called only when the
                                                     *  value of flagged is updated to either true or false
                                                     */

                                                    if (!flagged) {

                                                        // If the initial value of flagged was false when we clicked
                                                        // the flag button, then it means we are trying to update
                                                        // the value of flagged to true, hence it will show a toast
                                                        // messages saying that it is successfully Flagged and then
                                                        // change the icon to red flag
                                                        viewHolder.flagImage.setBackgroundResource(R.drawable.icon_red_flag);
                                                        Toast.makeText(activity, "Successfully Flagged", Toast.LENGTH_SHORT).show();
                                                    } else {

                                                        // If the initial value of flagged was true when we clicked
                                                        // the flag button, then it means we are trying to update
                                                        // the value of flagged to false, hence it will show a toast
                                                        // messages saying that it is "Unflagged" and then
                                                        // change the icon to normal flag
                                                        viewHolder.flagImage.setBackgroundResource(R.drawable.icon_flag);
                                                        Toast.makeText(activity, "Unflagged", Toast.LENGTH_SHORT).show();
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

    /**
     *  Metbod to take the users to the replies activity
     */
    private void goToRepliesActivity(QuestionFirebaseItems questionFirebaseItems) {

        /**
         *  Implicit intent to take the users to the replies activity
         */

        Intent intent = new Intent(activity, RepliesActivity.class);
        intent.putExtra("qID", questionFirebaseItems.getQuestionId());
        intent.putExtra("title", questionFirebaseItems.getTitle());
        activity.startActivity(intent);
    }


    @Override
    public int getItemCount() {

        /**
         *  The getItemCount gets the total number of questions in the database (and the adapter)
         */

        return questionFirebaseItemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        /**
         * Declare properties for question items
         */
        public TextView userNameTextView, questionTextView, dateTextView, categoryTextView,
                countryTV, viewTextView,likeCountTV,unlikeCountTV;
        public ImageView flagImage, shareImage, country_icon,likeImage,unlikeImage, solvedImage;
        public CircleImageView userAvatarImageView;
        public Button answerButton;


        public ViewHolder(View itemView) {

            super(itemView);


            /**
             *  Initialize all properties
             */

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
            country_icon = itemView.findViewById(R.id.country_icon);
            likeCountTV = itemView.findViewById(R.id.likeCountTV);
            unlikeCountTV = itemView.findViewById(R.id.unlikeCountTV);
            likeImage = itemView.findViewById(R.id.likeImage);
            unlikeImage = itemView.findViewById(R.id.unlikeImage);
            solvedImage = itemView.findViewById(R.id.solvedImage);


        }


    }

}