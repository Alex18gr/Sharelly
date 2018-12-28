package com.sharelly.alexc.sharelly.ViewModels;

import android.util.Log;

import com.sharelly.alexc.sharelly.Models.User;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class UserModel extends ViewModel {

    private static final String TAG = "UserModel";

    private MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public MutableLiveData<User> getUserLiveData() {
        Log.d(TAG, "getUserLiveData: getting user live data...");
        if (userLiveData == null) {
            return new MutableLiveData<User>();
        }
        return userLiveData;
    }



}
