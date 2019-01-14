package com.sharelly.alexc.sharelly.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Post implements Parcelable {

    private static final String TAG = "Post";

    private String name;
    private String type;
    private String contentId;
    private String description;
    private String user_id;
    private String user_full_name;
    private String post_image;
    private String content_title;
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

    protected Post(Parcel in) {
        name = in.readString();
        type = in.readString();
        contentId = in.readString();
        description = in.readString();
        user_id = in.readString();
        post_image = in.readString();
        content_title = in.readString();
        user_full_name = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getContent_title() {
        return content_title;
    }

    public void setContent_title(String content_title) {
        this.content_title = content_title;
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

    public String getUser_full_name() {
        return user_full_name;
    }

    public void setUser_full_name(String user_full_name) {
        this.user_full_name = user_full_name;
    }

    @Override
    public String toString() {
        return "Post{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", contentId='" + contentId + '\'' +
                ", description='" + description + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_full_name='" + user_full_name + '\'' +
                ", post_image='" + post_image + '\'' +
                ", content_title='" + content_title + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(contentId);
        parcel.writeString(description);
        parcel.writeString(user_id);
        parcel.writeString(post_image);
        parcel.writeString(content_title);
        parcel.writeString(user_full_name);
    }
}
