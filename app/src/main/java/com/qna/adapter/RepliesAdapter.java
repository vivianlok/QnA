package com.qna.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
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
import com.qna.database.RepliesFirebaseItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class RepliesAdapter extends RecyclerView.Adapter<RepliesAdapter.ViewHolder> {

    /**
     *  This is where the Firebase items were declared such as the Firebase User
     *  Firebase Auth and all the references
     */
    private FirebaseUser currentUser;
    private FirebaseAuth mFirebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDetailsReference, questionReference, repliesReference;
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
        repliesReference = firebaseDatabase.getReference().child("Questions").child("replies");
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
        viewHolder.replyTV.setText(repliesFirebaseItem.getReplyMessage());
        //viewHolder.country_icon.setBackgroundResource(repliesFirebaseItem.getCountryIcon());
        //viewHolder.likeCountTV.setText(repliesFirebaseItem.getLikeCount());
        //viewHolder.unlikeCountTV.setText(repliesFirebaseItem.getUnlikeCount());

        viewHolder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Liked Reply", Toast.LENGTH_SHORT).show();
                repliesReference.child("likeCount").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Integer likeCount 
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                repliesReference.child("likeCount").setValue("1");
                //
                //Fetching the views count from the Firebase
//                final Integer likeCount = dataSnapshot.child("viewsCount").getValue(Integer.class);
//                String attachment = dataSnapshot.child("attachment").getValue(String.class);
//                assert attachment != null;
            }
        }); //end of likeImage onclicklistener




    }  // End of onBindViewHolder




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
        public TextView replyUserNameTextView,dateTV,replyTV,likeCountTV,unlikeCountTV;
        public ImageView replyUserAvatarImageView, likeImage, unlikeImage;


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

        }


    }

}