package com.qna.database;

public class LikesAndDislikeFirebaseItem {

    String userID;

    public  LikesAndDislikeFirebaseItem(){}

    public LikesAndDislikeFirebaseItem(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
