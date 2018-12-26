package com.sharelly.alexc.sharelly.Models;



import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

@IgnoreExtraProperties
public class User {

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
}
