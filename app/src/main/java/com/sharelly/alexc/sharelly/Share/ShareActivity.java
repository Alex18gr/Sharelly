package com.sharelly.alexc.sharelly.Share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sharelly.alexc.sharelly.Login.LoginActivity;
import com.sharelly.alexc.sharelly.R;
import com.sharelly.alexc.sharelly.Utils.BottomNavigationViewHelper;

import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ShareActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final int ACTIVITY_NUM = 2;

    private static final String TAG = "ShareActivity";

    private BottomNavigationView navigation;
    private Context mContext = ShareActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        setupFirebaseAuth();

        boolean addNewFragment = true;

        // If turn the screen orientation then the savedInstanceState is not null.
        // In this condition, do not need to add new fragment again.
        if(savedInstanceState!=null)
        {
            addNewFragment = false;
        }

        if (addNewFragment) {

            Intent intent = getIntent();

            String action = intent.getAction();
            Set<String> categories = intent.getCategories();
            Uri data = intent.getData();
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            String manipulatedText = null;
            if (data != null) {
                Log.d(TAG, "onCreate: data uri is: " + data);
            }
            if (sharedText != null) {
                Log.d(TAG, "onCreate: extra text from package >" + intent.getPackage() + "< data: " + sharedText);
                Log.d(TAG, "onCreate: " + intent.getAction() + ", "
                 + intent.getScheme() + ", " + intent.getDataString());
                manipulatedText = manipulateStringOmdb(sharedText);
            }

            ShareMovieFragment fragment = new ShareMovieFragment();

            Bundle bundle = new Bundle();
            bundle.putString("id", manipulatedText);
            fragment.setArguments(bundle);
            loadFragment(fragment);
        }

        setupBottomNavigationView();

    }

    private String manipulateStringOmdb(String txt) {
        String[] strings = txt.split("\n");
        Log.d(TAG, "manipulateStringOmdb: >" + strings[0] + "< and >" + strings[1] + "<.");
        String[] urlSplit = strings[1].split("/");
        String id = urlSplit[urlSplit.length - 1];
        Log.d(TAG, "manipulateStringOmdb: >" + urlSplit[urlSplit.length - 1] + "<.");
        return id;
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.enableNavigation(mContext, this, navigation);
        //navigation.setOnNavigationItemSelectedListener(this);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    /**
     * chscks if the user is logged in
     * @param user
     */
    private void checkCurrentUser(FirebaseUser user) {
        Log.d(TAG, "checkCurrentUser: chacking if user is logged in");

        if(user == null) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                checkCurrentUser(user);

                if(user!=null) {
                    Log.d(TAG, "onAuthStateChanged: signed in " + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signed out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }


}
