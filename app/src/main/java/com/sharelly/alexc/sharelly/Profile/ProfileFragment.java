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
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharelly.alexc.sharelly.Login.LoginActivity;
import com.sharelly.alexc.sharelly.MainActivity;
import com.sharelly.alexc.sharelly.Models.User;
import com.sharelly.alexc.sharelly.R;
import com.sharelly.alexc.sharelly.Utils.ExpandableHeightGridView;
import com.sharelly.alexc.sharelly.Utils.GridImageAdapter;
import com.sharelly.alexc.sharelly.ViewModels.UserModel;

import java.util.ArrayList;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class ProfileFragment extends Fragment {

    private TextView textView;
    private View view;
    private Toolbar toolbar;
    private UserModel mModel;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;


    private static final String TAG = "ProfileFragment";
    private Fragment mContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.textView1);
//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < 100;i++) stringBuilder.append("PAOK thriskeia gamietai h super 3!!!!");
//        textView.setText(stringBuilder.toString());

        setupFirebaseAuth();
        setupToolbar();
        tempGridSetup();
        if (savedInstanceState == null) {
            Log.d(TAG, "onViewCreated: savedInstanceState is null !!!");
            getUserDetails();
        } else {
            Log.d(TAG, "onViewCreated: savedInstanceState NOT null.");
        }

    }

    private void getUserDetails() {
        if (textView.getText().toString().equals(getString(R.string.profile))) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .whereEqualTo("user_id", mAuth.getCurrentUser().getUid()).limit(1)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        User user = null;
                        for(QueryDocumentSnapshot document: task.getResult()){
                            user = document.toObject(User.class);
                            Log.d(TAG, "onComplete: user details obtained " + user);
                        }
                        mModel.getUserLiveData().setValue(user);
                    } else {
                        Log.d(TAG, "onComplete: could not obtain the user details");
                    }
                }
            });
        }



    }

    private void setupToolbar() {

        toolbar = view.findViewById(R.id.home_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "onMenuItemClick: clicked menu item");

                switch(item.getItemId()) {
                    case R.id.miLogout:
                        Log.d(TAG, "onMenuItemClick: Navigating to Profile Preferences");
//                        Intent intent = new Intent(getActivity(), AccountSettingActivity.class);
//                        startActivity(intent);
                        break;
                }

                return false;
            }
        });

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
        inflater.inflate(R.menu.menu_profile, menu);
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
            }
        };

        mModel.getUserLiveData().observe(getActivity(), userObserver);
    }

    private void setupImageGrid(ArrayList<String> imgURLs) {
        ExpandableHeightGridView gridView = view.findViewById(R.id.gridView);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/3;
        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview,
                "", imgURLs);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(3);
        gridView.setExpanded(true);
    }

    private void tempGridSetup() {
        ArrayList<String> imgURLs = new ArrayList<>();
        imgURLs.add("https://cdn.shopify.com/s/files/1/0825/2951/files/Android-under-maintenance_1600x1600.png?v=1509371840");
        imgURLs.add("https://2.bp.blogspot.com/-2ZMkSo7CnUs/WvMvSK0u9RI/AAAAAAAAFZA/zJOCZ8LUM8ol3hcHYHwVyOpc3iiYaxquACLcBGAs/s1600/Jetpack_logo.png");
        imgURLs.add("https://tr2.cbsistatic.com/hub/i/r/2016/04/14/957569ba-49e0-4762-8a9b-032594d44404/resize/770x/e151fc109f851df34cf29116cdaddb9b/android-security-1.jpg");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT-Nx02HfBq_GRXRD_OoNW_pNy4YYlCKySAAd9MT68lwwcnaSR1-g");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfqnbN--CdFXrLgPoqruoN__5Xcf7uX9D-jMRhSWC-SoQ6ZBWr");
        imgURLs.add("https://cdn.vox-cdn.com/thumbor/eVoUeqwkKQ7MFjDCgrPrqJP5ztc=/0x0:2040x1360/1200x800/filters:focal(860x1034:1186x1360)/cdn.vox-cdn.com/uploads/chorus_image/image/59377089/wjoel_180413_1777_android_001.1523625143.jpg");
        imgURLs.add("https://prod-discovery.edx-cdn.org/media/course/image/019dd154-cb89-4a24-aed5-f0b8db33d705-1becb8880fa8.small.jpg");
        imgURLs.add("https://cdn.wccftech.com/wp-content/uploads/2017/08/download-android-8.jpg");
        imgURLs.add("https://i.redd.it/9dbo7puxpy521.jpg");
        imgURLs.add("https://external-preview.redd.it/62rvPqVG-hWEi9q5M_bPy878XUAEuSl7H8ejsuWEBpE.jpg?auto=webp&s=6420851201a561c6d948d327912a8e257b39c9b7");
        imgURLs.add("https://i.redd.it/st8gdu6gcy521.jpg");
        imgURLs.add("https://i.redd.it/b20fxfimou521.jpg");



        setupImageGrid(imgURLs);
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
