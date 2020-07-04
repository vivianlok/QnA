package com.qna.database;

public class RepliesFirebaseItem {

    String replyId, userName, avatar, date, repliedMessage, originalReplyText;

    public RepliesFirebaseItem(){}
    //replyingText- original comment - pizza hut   --- originalReplyText
    //replyMessage -  replied to comment - noted   --- repliedMessage


    public RepliesFirebaseItem(String replyId, String userName, String avatar, String date, String repliedMessage, String originalReplyText) {
        this.replyId = replyId;
        this.userName = userName;
        this.avatar = avatar;
        this.date = date;
        this.repliedMessage = repliedMessage;
        this.originalReplyText = originalReplyText;
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

    public String getRepliedMessage() {
        return repliedMessage;
    }

    public void setRepliedMessage(String repliedMessage) {
        this.repliedMessage = repliedMessage;
    }

    public String getOriginalReplyText() {
        return originalReplyText;
    }

    public void setOriginalReplyText(String originalReplyText) {
        this.originalReplyText = originalReplyText;
    }
}
