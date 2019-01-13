package com.sharelly.alexc.sharelly.Dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharelly.alexc.sharelly.Models.Post;
import com.sharelly.alexc.sharelly.Models.User;
import com.sharelly.alexc.sharelly.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    private List<User> mFollowingUsers;
    private FirebaseFirestore db;
    private List<Post> mFollowingUsersPosts = new ArrayList<>();
    private List<String> mFollowingUsersRecodIds = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getFollowingUsers();
    }

    private void getFollowingUsers() {

        mFollowingUsers = new ArrayList<>();
        String currUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();

        // Getting the following users list
        db.collection("following").document(currUserId)
                .collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: following users received. User list: ");
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        User tempUser = documentSnapshot.toObject(User.class);
                        mFollowingUsersRecodIds.add(tempUser.getUser_id());
                        Log.d(TAG, "onComplete: " + tempUser);
                        mFollowingUsers.add(tempUser);
                    }

                    // here we get the users posts...
                    getUserPosts();
                }
            }
        });

    }

    private void getUserPosts() {

        for(int i = 0; i < mFollowingUsersRecodIds.size(); i++) {
            final int count = i;
            String tempUserId = mFollowingUsersRecodIds.get(i);
            db.collection("users").document(tempUserId)
                    .collection("posts")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Post tempPost = documentSnapshot.toObject(Post.class);
                            mFollowingUsersPosts.add(tempPost);
                        }
                        if(count >= mFollowingUsersRecodIds.size() - 1){
                            //display the photos
                            displayPosts();
                        }
                    }
                }
            });
        }

    }

    private void displayPosts() {
        Log.d(TAG, "displayPosts: posts received:");
        for (Post tempPost : mFollowingUsersPosts) {
            Log.d(TAG, "displayPosts: " + tempPost);
        }


    }


}
