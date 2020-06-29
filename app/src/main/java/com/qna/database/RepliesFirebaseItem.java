package com.qna.database;

public class RepliesFirebaseItem {

    String replyId, userName, avatar, date, replyMessage;

    public RepliesFirebaseItem(){}


    public RepliesFirebaseItem(String replyId, String userName, String avatar, String date,
                               String replyMessage) {
        this.replyId = replyId;
        this.userName = userName;
        this.avatar = avatar;
        this.date = date;
        this.replyMessage = replyMessage;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }


}
