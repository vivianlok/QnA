package com.qna.database;

public class QuestionFirebaseItems {

    String questionId;
    String fullName;
    String avatar;
    String date;
    String time;
    String title;
    String description;
    String attachment;
    String category;
    String authorId;
    int viewsCount;

    public QuestionFirebaseItems(){

    }

    public QuestionFirebaseItems(String questionId, String fullName, String avatar, String date, String time, String title, String description, String attachment, String category, String authorId, int viewsCount) {
        this.questionId = questionId;
        this.fullName = fullName;
        this.avatar = avatar;
        this.date = date;
        this.time = time;
        this.title = title;
        this.description = description;
        this.attachment = attachment;
        this.category = category;
        this.authorId = authorId;
        this.viewsCount = viewsCount;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }
}
