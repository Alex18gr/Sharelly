package com.sharelly.alexc.sharelly.Models;

public class UserFollow {

    private String user_id;

    public UserFollow() {
    }

    public UserFollow(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    @Override
    public String toString() {
        return "UserFollow{" +
                "user_id='" + user_id + '\'' +
                '}';
    }
}
