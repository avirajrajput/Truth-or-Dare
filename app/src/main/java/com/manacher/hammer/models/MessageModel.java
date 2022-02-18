package com.manacher.hammer.models;

public class MessageModel {
    private String text;
    private String userId;

    public MessageModel() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
