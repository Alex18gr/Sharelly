package com.sharelly.alexc.sharelly.Models;



import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class User implements Parcelable {

    private @ServerTimestamp
    Date date_created;
    private String email;
    private String full_name;
    private String user_id;
    private String username;

    public User() {
    }

    public User(Date date_created, String email, String full_name, String user_id, String username) {
        this.date_created = date_created;
        this.email = email;
        this.full_name = full_name;
        this.user_id = user_id;
        this.username = username;
    }

    protected User(Parcel in) {
        email = in.readString();
        full_name = in.readString();
        user_id = in.readString();
        username = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "date_created=" + date_created +
                ", email='" + email + '\'' +
                ", full_name='" + full_name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(email);
        parcel.writeString(full_name);
        parcel.writeString(user_id);
        parcel.writeString(username);
    }
}
