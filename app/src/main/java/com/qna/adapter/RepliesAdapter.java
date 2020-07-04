package com.qna.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.qna.database.LikesAndDislikeFirebaseItem;
import com.qna.database.RepliesFirebaseItem;

import java.util.List;


public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.ViewHolder> {

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
    List<RepliesFirebaseItem> repliesFirebaseItemList;
    // This is the View that is holding each questions
    // (for example the cardView holding a particular question)
    View view;


    /**
     * This is the QuestionAdapter Constructor
     */
    public RepliesAdapter(Activity activity, List<RepliesFirebaseItem> repliesFirebaseItem) {

        /**
         *  Inside this contructor, all other initializations were made
         *  such as the list, context and the firebase items (firebase references)
         */

        this.repliesFirebaseItemList = repliesFirebaseItem;
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

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_replies, parent, false);
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

        // Get the question position and then assign it to a variable called "repliesFirebaseItem"
        // So we are going to be using the variable called "repliesFirebaseItem" to reference each and
        // every question

        final RepliesFirebaseItem repliesFirebaseItem
                = repliesFirebaseItemList.get(position);


        // Assign values to the properties here
        viewHolder.replyUserNameTextView.setText(repliesFirebaseItem.getUserName());
        viewHolder.dateTV.setText(repliesFirebaseItem.getDate());
        //viewHolder.replyUserAvatarImageView.setBackgroundResource(repliesFirebaseItem.getAvatar());
        viewHolder.replyTV.setText(repliesFirebaseItem.getRepliedMessage());


        questionReference
                .child(RepliesActivity.qID)
                .child("replies")
                .child(repliesFirebaseItem.getReplyId())
                .child("likes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        long likesCount = dataSnapshot.getChildrenCount();
                        if (dataSnapshot.exists()){
                            viewHolder.likeCountTV.setText("" + likesCount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        questionReference
                .child(RepliesActivity.qID)
                .child("replies")
                .child(repliesFirebaseItem.getReplyId())
                .child("disLikes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        long disLikesCount = dataSnapshot.getChildrenCount();
                        if (dataSnapshot.exists()){
                            viewHolder.unlikeCountTV.setText("" + disLikesCount);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        //viewHolder.country_icon.setBackgroundResource(repliesFirebaseItem.getCountryIcon());
        //viewHolder.likeCountTV.setText(repliesFirebaseItem.getLikeCount());
        //viewHolder.unlikeCountTV.setText(repliesFirebaseItem.getUnlikeCount());

        viewHolder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                performLogicForLikes(repliesFirebaseItem, viewHolder);


            }
        }); //end of likeImage onclicklistener

        viewHolder.unlikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                performLogicFOrDIslike(repliesFirebaseItem, viewHolder);
            }
        });


        viewHolder.replyLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RepliesActivity.replyEditText.setText("@" + repliesFirebaseItem.getUserName());
                RepliesActivity.replyingText = repliesFirebaseItem.getRepliedMessage();
            }
        });
//if available
        if (repliesFirebaseItem.getOriginalReplyText() != null){

            // If there's a message been replied to

            viewHolder.replyingTextView.setVisibility(View.VISIBLE);
            viewHolder.replyingTextView.setText(repliesFirebaseItem.getOriginalReplyText());
        }



    }  // End of onBindViewHolder

    private void performLogicFOrDIslike(final RepliesFirebaseItem repliesFirebaseItem, ViewHolder viewHolder) {

        questionReference
                .child(RepliesActivity.qID)
                .child("replies")
                .child(repliesFirebaseItem.getReplyId())
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
                                    .child(RepliesActivity.qID)
                                    .child("replies")
                                    .child(repliesFirebaseItem.getReplyId())
                                    .child("disLikes")
                                    .child(userID) //.push()
                                    .setValue(likesAndDislikeFirebaseItem)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {


                                            //Toast.makeText(activity, "Disliked Reply", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else  {

                            // If user exist
                            questionReference
                                    .child(RepliesActivity.qID)
                                    .child("replies")
                                    .child(repliesFirebaseItem.getReplyId())
                                    .child("disLikes")
                                    .child(userID) //.push()
                                    .removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(activity, "Successfully removed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

//                            Toast.makeText(activity, "You already disliked this question", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void performLogicForLikes(final RepliesFirebaseItem repliesFirebaseItem, ViewHolder viewHolder) {

        questionReference
                .child(RepliesActivity.qID)
                .child("replies")
                .child(repliesFirebaseItem.getReplyId())
                .child("likes")
                .orderByChild("userID")
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
                                    .child(RepliesActivity.qID)
                                    .child("replies")
                                    .child(repliesFirebaseItem.getReplyId())
                                    .child("likes")
                                    .child(userID) //.push()
                                    .setValue(likesAndDislikeFirebaseItem)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(activity, "Liked Reply", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else  {
                            // If user exist
                            questionReference
                                    .child(RepliesActivity.qID)
                                    .child("replies")
                                    .child(repliesFirebaseItem.getReplyId())
                                    .child("disLikes")
                                    .child(userID) //.push()
                                    .removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(activity, "Successfully removed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            //Toast.makeText(activity, "You already liked this question", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    @Override
    public int getItemCount() {

        /**
         *  The getItemCount gets the total number of questions in the database (and the adapter)
         */

        return repliesFirebaseItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        /**
         * Declare properties for question items
         */
        public TextView replyUserNameTextView,dateTV,replyTV,likeCountTV,unlikeCountTV, replyingTextView;
        public ImageView replyUserAvatarImageView, likeImage, unlikeImage;
        public LinearLayout replyLinearLayout;


        public ViewHolder(View itemView) {

            super(itemView);


            /**
             *  Initialize all properties
             */

            replyUserNameTextView = itemView.findViewById(R.id.replyUserNameTextView);
            dateTV = itemView.findViewById(R.id.dateTV);
            replyTV = itemView.findViewById(R.id.replyTV);
            replyUserAvatarImageView = itemView.findViewById(R.id.replyUserAvatarImageView);
            likeCountTV = itemView.findViewById(R.id.likeCountTV);
            unlikeCountTV = itemView.findViewById(R.id.unlikeCountTV);
            likeImage = itemView.findViewById(R.id.likeImage);
            unlikeImage = itemView.findViewById(R.id.unlikeImage);
            replyLinearLayout = itemView.findViewById(R.id.replyLinearLayout);
            replyingTextView = itemView.findViewById(R.id.replyingTextView);

        }


    }

}