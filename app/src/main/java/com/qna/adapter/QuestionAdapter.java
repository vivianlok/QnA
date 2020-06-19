package com.qna.adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.qna.R;
import com.qna.database.QuestionFirebaseItems;

import java.util.List;


public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {



    Activity activity;
    List<QuestionFirebaseItems> questionFirebaseItemsList;
    View view;




    public QuestionAdapter(Activity activity, List<QuestionFirebaseItems> questionFirebaseItems) {

        this.questionFirebaseItemsList = questionFirebaseItems;
        this.activity = activity;


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_questions, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);


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
    }  // End of onBindViewHolder




    @Override
    public int getItemCount() {

        return questionFirebaseItemsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        // Declare properties for question items
        public TextView userNameTextView,questionTextView,dateTextView;
        public ImageView userAvatarImageView,flagImage,shareImage;



        public ViewHolder(View itemView) {

            super(itemView);


            // Initialize all properties

            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            userAvatarImageView = itemView.findViewById(R.id.userAvatarImageView);
            flagImage = itemView.findViewById(R.id.flagImage);
            shareImage = itemView.findViewById(R.id.shareImage);



        }
    }
}