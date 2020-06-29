package com.qna.database;

public class RepliesFirebaseItem {

    String replyId, userName, avatar, date, replyMessage;
    int likeCount,unlikeCount;

    public RepliesFirebaseItem(){}



    public RepliesFirebaseItem(String replyId, String userName, String avatar, String date, String replyMessage, int likeCount, int unlikeCount) {
        this.replyId = replyId;
        this.userName = userName;
        this.avatar = avatar;
        this.date = date;
        this.replyMessage = replyMessage;
        this.likeCount = likeCount;
        this.unlikeCount = unlikeCount;
    }

    public String getReplyId() {
        return replyId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDate() {
        return date;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public int getLikeCount() {
        return 0;
    }

    public int getUnlikeCount() {
        return 0;
    }
}
