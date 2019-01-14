package com.sharelly.alexc.sharelly.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharelly.alexc.sharelly.CustomViews.WrapContentHeightViewPager;
import com.sharelly.alexc.sharelly.Login.LoginActivity;
import com.sharelly.alexc.sharelly.Models.Post;
import com.sharelly.alexc.sharelly.Models.User;
import com.sharelly.alexc.sharelly.Models.UserFollow;
import com.sharelly.alexc.sharelly.R;
import com.sharelly.alexc.sharelly.Utils.ExpandableHeightGridView;
import com.sharelly.alexc.sharelly.Utils.GridImageAdapter;
import com.sharelly.alexc.sharelly.ViewModels.UserModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class ProfileViewFragment extends Fragment {

    private TextView textView;
    private View view;
    private Toolbar toolbar;
    private UserModel mModel;

    private TabLayout tabLayout;
    private WrapContentHeightViewPager viewPager;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;


    private static final String TAG = "ProfileViewFragment";
    private Fragment mContent;
    private TextView fullNameTxt;
    private TextView emailTxt;
    private NestedScrollView scrollView;
    private Button followBtn;
    private Button unfollowBtn;
    private User currentUser;
    private SongsFragment songsFragment;
    private MoviesFragment moviesFragment;
    private List<Post> moviesPosts = new ArrayList<>();
    private List<Post> songsPosts = new ArrayList<>();
    private TextView followersNumTxt;
    private TextView followingNumTxt;
    private TextView postsNumTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_view_profile, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        super.onViewCreated(view, savedInstanceState);
        //scrollView.setFillViewport(true);

        setupWidgets();
        setupTabLayoutAndViewPager();

//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < 100;i++) stringBuilder.append("PAOK thriskeia gamietai h super 3!!!!");
//        textView.setText(stringBuilder.toString());

        setupFirebaseAuth();
        setupToolbar();
        if (savedInstanceState == null) {
            Log.d(TAG, "onViewCreated: savedInstanceState is null !!!");
            currentUser = getArguments().getParcelable(getString(R.string.intent_user));
            getUserDetails();
            isFollowing();
        } else {
            Log.d(TAG, "onViewCreated: savedInstanceState NOT null.");
        }

    }

    private void setupWidgets() {
        scrollView = view.findViewById(R.id.profileScrollView);
        textView = view.findViewById(R.id.textView4);
        fullNameTxt = view.findViewById(R.id.fullNameTxt);
        emailTxt = view.findViewById(R.id.emailTxt);
        followBtn = view.findViewById(R.id.buttonFollow);
        unfollowBtn = view.findViewById(R.id.buttonUnfollow);
        followersNumTxt = view.findViewById(R.id.followersNumTxt);
        followingNumTxt = view.findViewById(R.id.followingNumTxt);
        postsNumTxt = view.findViewById(R.id.postsNumTxt);

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: now following " + currentUser.getUser_id());

                // Map<String, Object> userData = new HashMap<>();
                // userData.put("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                UserFollow userData = new UserFollow(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid());

                FirebaseFirestore.getInstance()
                        .collection("followers")
                        .document(currentUser.getUser_id())
                        .collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Map<String, Object> userData = new HashMap<>();
                            // userData.put("user_id", currentUser.getUser_id());
                            UserFollow userData = new UserFollow(currentUser.getUser_id());

                            FirebaseFirestore.getInstance()
                                    .collection(getString(R.string.db_collection_following))
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .collection(getString(R.string.db_collection_follow_users))
                                    .document(currentUser.getUser_id())
                                    .set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "onComplete: following successful");
                                        setFollowing();
                                        getUserFollowersAndFollowing();
                                    } else {
                                        Log.d(TAG, "onComplete: could not add following. Check logs.");
                                    }
                                }
                            });
                        } else{
                            Log.d(TAG, "onComplete: could not add follower. Check logs.");
                        }
                    }
                });

            }
        });

        unfollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: now unfollowing");

                FirebaseFirestore.getInstance()
                        .collection(getString(R.string.db_collection_followers))
                        .document(currentUser.getUser_id())
                        .collection(getString(R.string.db_collection_follow_users))
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: unfolllowing follower deletion successful");

                            FirebaseFirestore.getInstance()
                                    .collection("following")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .collection("users")
                                    .document(currentUser.getUser_id())
                                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "onComplete: unfollowing following deletion successful.");
                                        Log.d(TAG, "onComplete: Unfollow successful");
                                        setUnfollowing();
                                        getUserFollowersAndFollowing();
                                    } else {
                                        Log.d(TAG, "onComplete: unfollowing follwoing deletion failed. Check logs.");
                                    }
                                }
                            });
                        } else {
                            Log.d(TAG, "onComplete: unfollowing follower deletion failed. Check logs.");
                        }
                    }
                });
            }
        });
    }

    private void isFollowing() {
        setUnfollowing();

        FirebaseFirestore.getInstance()
                .collection(getString(R.string.db_collection_following))
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection(getString(R.string.db_collection_follow_users))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot document = task.getResult();
                    Log.d(TAG, "onComplete: following users:");
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        UserFollow tempUser = documentSnapshot.toObject(UserFollow.class);
                        Log.d(TAG, "onComplete: " + tempUser);
                        if (tempUser.getUser_id().equals(currentUser.getUser_id()))
                            setFollowing();
                    }

                } else {
                    Log.d(TAG, "onComplete: could not obtain following. Check logs.");
                }
            }
        });
    }

    private void setupTabLayoutAndViewPager() {
        tabLayout = view.findViewById(R.id.tabLayoutProfile);
        viewPager = view.findViewById(R.id.viewPager);
        ProfileViewPagerAdapter pagerAdapter =
                new ProfileViewPagerAdapter(getActivity()
                        .getSupportFragmentManager());
        if (pagerAdapter != null) Log.d(TAG, "onViewCreated: pager adapter NOT null");
        moviesFragment = new MoviesFragment();
        songsFragment = new SongsFragment();
        pagerAdapter.addFragment(moviesFragment, "Movies");
        pagerAdapter.addFragment(songsFragment, "Songs");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void getUserDetails() {
       // if (textView.getText().toString().equals(getString(R.string.profile))) {
        if (true) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("user_id", currentUser.getUser_id()).limit(1)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        User user = null;
                        String userRecordId = null;
                        for(QueryDocumentSnapshot document: task.getResult()){
                            user = document.toObject(User.class);
                            Log.d(TAG, "onComplete: user details obtained " + user);
                            userRecordId = document.getId();
                        }
                        mModel.getUserLiveData().setValue(user);
                        ((ProfileActivity)getActivity()).setActionBarTitle(user.getUsername());
                        final String finalUserRecordId = userRecordId;
                        FirebaseFirestore.getInstance()
                                .collection("users").document(userRecordId)
                                .collection("posts").get().addOnCompleteListener(
                                new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        Log.d(TAG, "onComplete: user " + currentUser.getUser_id()
                                                + " with user record id " + finalUserRecordId + " posts: ");
                                        int totalPosts = 0;
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                            Post tempPost = documentSnapshot.toObject(Post.class);
                                            Log.d(TAG, "onComplete: " + tempPost);
                                            totalPosts++;
                                            if (tempPost.getType().equals(getString(R.string.content_type_movie_omdb))) {
                                                moviesPosts.add(tempPost);
                                            } else if (tempPost.getType().equals(getString(R.string.content_type_song_lastfm))) {
                                                songsPosts.add(tempPost);
                                            }
                                        }
                                        postsNumTxt.setText("" + totalPosts);
                                        moviesFragment.setPosts(moviesPosts);
                                        songsFragment.setPosts(songsPosts);
                                        getUserFollowersAndFollowing();
                                    }
                                });
                    } else {
                        Log.d(TAG, "onComplete: could not obtain the user details");
                    }
                }
            });
        }



    }

    private void getUserFollowersAndFollowing() {
        FirebaseFirestore.getInstance().collection("followers")
                .document(currentUser.getUser_id())
                .collection("users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int followers = 0;
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        followers++;
                    }
                    Log.d(TAG, "onComplete: total followers: " + followers);
                    followersNumTxt.setText("" + followers);
                } else {
                    Log.d(TAG, "onComplete: could not obtain followers from db. Check logs.");
                }

            }
        });

        FirebaseFirestore.getInstance().collection("following")
                .document(currentUser.getUser_id())
                .collection("users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int following = 0;
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        following++;
                    }
                    Log.d(TAG, "onComplete: total following: " + following);
                    followingNumTxt.setText("" + following);
                } else {
                    Log.d(TAG, "onComplete: could not obtain following from db. Check logs.");
                }

            }
        });

    }

    private void setFollowing() {
        Log.d(TAG, "setFollowing: setting up UI for following this user");
        followBtn.setVisibility(View.GONE);
        unfollowBtn.setVisibility(View.VISIBLE);
    }

    private void setUnfollowing() {
        Log.d(TAG, "setUnfollowing: setting up UI for unfollowing this user");
        followBtn.setVisibility(View.VISIBLE);
        unfollowBtn.setVisibility(View.GONE);
    }

    private void setupToolbar() {

        toolbar = view.findViewById(R.id.home_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
            Log.d(TAG, "onActivityCreated: resrore the user data from saved instance state");
            textView.setText(savedInstanceState.getString("user"));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the fragment's state here
        outState.putString("user", textView.getText().toString());

        // Save the fragment's instance
        // getFragmentManager().putFragment(outState, "ProfileFragment", mContent);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState!=null) {
            // Retrieve the values...
            
            // Do not need below code, because android os will automatically save and restore view objects value that has id attribute.
            // EditText userEmailInputBox = getActivity().findViewById(R.id.fragment_instance_state_user_email_edit_box);
            //userEmailInputBox.setText(userEmail);
            String restoredUserText = savedInstanceState.getString("user");
            textView.setText(restoredUserText);
            Log.d(TAG, "onViewStateRestored: Fragment onViewStateRestored called");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(UserModel.class);

        Observer<User> userObserver = new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d(TAG, "onChanged: observer update text view");
                textView.setText(user.getEmail() + "\n" + user.getFull_name() + "\n" + user.getDate_created());
                fullNameTxt.setText(user.getFull_name());
                emailTxt.setText(user.getEmail());
            }
        };

        mModel.getUserLiveData().observe(getActivity(), userObserver);
    }

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: started.");
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}
