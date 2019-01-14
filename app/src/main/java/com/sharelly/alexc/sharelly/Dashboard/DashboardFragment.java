package com.sharelly.alexc.sharelly.Dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharelly.alexc.sharelly.JsonModels.MoviesList;
import com.sharelly.alexc.sharelly.Models.Post;
import com.sharelly.alexc.sharelly.Models.User;
import com.sharelly.alexc.sharelly.R;
import com.sharelly.alexc.sharelly.Share.ShareMovieFragment;
import com.sharelly.alexc.sharelly.Share.ShareSongFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";

    private List<User> mFollowingUsers;
    private FirebaseFirestore db;
    private List<Post> mFollowingUsersPosts = new ArrayList<>();
    private List<String> mFollowingUsersRecodIds = new ArrayList<>();
    private ListView mMainFeedListView;
    private SwipeRefreshLayout mMainFeedSwipeRefresh;
    private MainFeedPostListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainFeedListView = view.findViewById(R.id.mainFeedList);
        mFollowingUsersRecodIds = new ArrayList<>();
        mFollowingUsersPosts = new ArrayList<>();
        mMainFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Post post = (Post) mMainFeedListView.getItemAtPosition(i);
                if (post.getType().equals(getString(R.string.content_type_movie_omdb))) {
                    showMovieSharePage(post.getContentId());
                } else if (post.getType().equals(getString(R.string.content_type_song_lastfm))) {
                    showSongSharePage(post.getContentId());
                }
            }
        });
        mMainFeedSwipeRefresh = view.findViewById(R.id.swipeToRefreshMainFeed);
        mMainFeedSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFollowingUsersPosts = new ArrayList<>();
                mFollowingUsersRecodIds = new ArrayList<>();
                getFollowingUsers();
            }
        });
        getFollowingUsers();
    }

    private void showSongSharePage(String contentId) {
        ShareSongFragment fragment = new ShareSongFragment();
        Bundle bundle = new Bundle();
        Log.d(TAG, "onItemClick: share song with id: >" + contentId + "<");
        bundle.putString("id", contentId);

        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(((ViewGroup) getView().getParent()).getId(), fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showMovieSharePage(String contentId) {
        ShareMovieFragment fragment = new ShareMovieFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", contentId);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(((ViewGroup) getView().getParent()).getId(), fragment)
                .addToBackStack(null)
                .commit();
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

    private void setEmptyListView() {
        ArrayAdapter<Integer> adapter1 = new ArrayAdapter<Integer>(getActivity(),
                0);
        mMainFeedListView.setAdapter(adapter1);
    }

    private void displayPosts() {
        Log.d(TAG, "displayPosts: posts received:");
        for (Post tempPost : mFollowingUsersPosts) {
            Log.d(TAG, "displayPosts: " + tempPost);
        }

        Collections.sort(mFollowingUsersPosts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                return t1.getTimestamp().compareTo(post.getTimestamp());
            }
        });

        mAdapter = new MainFeedPostListAdapter(getActivity(), mFollowingUsersPosts);

        mMainFeedListView.setAdapter(mAdapter);
        mMainFeedSwipeRefresh.setRefreshing(false);
    }
}
