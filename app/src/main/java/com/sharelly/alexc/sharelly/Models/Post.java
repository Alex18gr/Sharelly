package com.sharelly.alexc.sharelly.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Post {

    private static final String TAG = "Post";

    private String name;
    private String type;
    private String contentId;
    private String description;
    private String user_id;
    private @ServerTimestamp Date timestamp;

    public Post() {
    }

    public Post(String name, String type, String contentId, String description, String user_id, Date timestamp) {
        this.name = name;
        this.type = type;
        this.contentId = contentId;
        this.description = description;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public Post(String name, String type, String contentId, String description, String user_id) {
        this.name = name;
        this.type = type;
        this.contentId = contentId;
        this.description = description;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Post{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", contentId='" + contentId + '\'' +
                ", description='" + description + '\'' +
                ", user_id='" + user_id + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
