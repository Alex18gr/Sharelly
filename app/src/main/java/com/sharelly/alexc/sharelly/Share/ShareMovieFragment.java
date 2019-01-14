package com.sharelly.alexc.sharelly.Share;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.sharelly.alexc.sharelly.BuildConfig;
import com.sharelly.alexc.sharelly.JsonModels.Movie;
import com.sharelly.alexc.sharelly.Models.Post;
import com.sharelly.alexc.sharelly.Models.User;
import com.sharelly.alexc.sharelly.R;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class ShareMovieFragment extends Fragment {

    private static final String TAG = "ShareMovieFragment";

    private View view;
    private String action;
    private String data;
    private Movie receivedMovie = null;

    // widgets
    private ImageView expandedImageView;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TextView textView;
    private TextView imdbScoreTxt;
    private TextView rottenScoreTxt;
    private TextView metaScoreTxt;

    private Fragment fragment = this;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_share_movie, container, false);
        data = getArguments().getString("id");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.txtShare);


        //textView.setText(data);
        Log.d(TAG, "onViewCreated: data set");
        setupWidgets();
        //setupToolbar(null);

        if (data != null){
            Log.d(TAG, "onViewCreated: executing down load async task");
            DownloadData downloadData = new DownloadData();
            downloadData.execute("http://www.omdbapi.com/?i=" + data + "&apikey=" + BuildConfig.API_KEY_OMDB);
        } else {
            Log.d(TAG, "onViewCreated: error: data is NULL");
        }



    }

    private void setupToolbar(String title) {
        toolbar = view.findViewById(R.id.toolbar);
        if (title != null) toolbar.setTitle(title);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    private void loadData() {
        Picasso.get().load(receivedMovie.getPoster()).into(expandedImageView);
        //((ShareActivity)getActivity()).setActionBarTitle(receivedMovie.getTitle());
        //((ShareActivity) getActivity()).getSupportActionBar().setTitle(receivedMovie.getTitle());
        setupToolbar(receivedMovie.getTitle());
        textView.setText(receivedMovie.getYear() + " \u00B7 "+
        receivedMovie.getGenre());
        for (Movie.Rating rating : receivedMovie.getRatings()) {
            if (rating.getSource().equals("Internet Movie Database"))  {
                imdbScoreTxt.setText(rating.getValue());
            } else if (rating.getSource().equals("Rotten Tomatoes")) {
                rottenScoreTxt.setText(receivedMovie.getRatings().get(1).getValue());
            } else if (rating.getSource().equals("Metacritic")) {
                metaScoreTxt.setText(receivedMovie.getRatings().get(2).getValue());
            }
        }

        if (imdbScoreTxt.getText().toString().equals("TextView")) {
            imdbScoreTxt.setVisibility(View.GONE);
            view.findViewById(R.id.postsTxt).setVisibility(View.GONE);
        }
        if (rottenScoreTxt.getText().toString().equals("TextView")) {
            rottenScoreTxt.setVisibility(View.GONE);
            view.findViewById(R.id.followersTxt).setVisibility(View.GONE);
        }
        if (metaScoreTxt.getText().toString().equals("TextView")) {
            metaScoreTxt.setVisibility(View.GONE);
            view.findViewById(R.id.followingTxt).setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == NewPostDialog.REQUEST_CODE) {
                String mTitle = data.getStringExtra(getString(R.string.new_post_dialog_title));
                String mDescription = data.getStringExtra(getString(R.string.new_post_dialog_description));
                Log.d(TAG, "onActivityResult: recieved title: " + mTitle
                        + " and description: " + mDescription);
                createNewPost(mTitle, mDescription);
            }
        }


    }

    private void createNewPost(String mTitle, String mDescription) {

        final Post newPost = new Post();

        newPost.setContentId(receivedMovie.getImdbId());
        newPost.setType(getString(R.string.content_type_movie_omdb));
        newPost.setName(mTitle);
        newPost.setDescription(mDescription);
        newPost.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        newPost.setContent_title(receivedMovie.getTitle());
        newPost.setUser_full_name(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        if (receivedMovie.getPoster() != null) {
            newPost.setPost_image(receivedMovie.getPoster());
        }

        Log.d(TAG, "createNewPost: creating new post :" + newPost);

        // Add the post to the database...

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("posts").add(newPost)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: post added with id " + task.getResult().getId());
                    addPostToUserPosts(task.getResult().getId(), newPost);

                } else {
                    Log.d(TAG, "onComplete: could not add the post. Check logs.");
                }
            }
        });

    }

    private void addPostToUserPosts(final String id, final Post newPost) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").whereEqualTo("user_id", FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    User mUser = null;
                    String mId = null;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        mUser = document.toObject(User.class);
                        mId = document.getId();
                        Log.d(TAG, "onComplete: the user document id: " + mId);
                    }
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users")
                            .document(mId)
                            .collection("posts")
                            .document(id).set(newPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: post successfully added to users posts");
                                Toast.makeText(getActivity(), "Post added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "onComplete: Could not add post. Check logs.");
                            }
                        }
                    });

                } else {
                    Log.d(TAG, "onComplete: could not find user. Check logs.");
                }



            }
        });

    }

    private void setupWidgets() {
        expandedImageView = view.findViewById(R.id.expandedImage);
        fab = view.findViewById(R.id.fab);
        imdbScoreTxt = view.findViewById(R.id.postsNumTxt);
        rottenScoreTxt = view.findViewById(R.id.followersNumTxt);
        metaScoreTxt = view.findViewById(R.id.followingNumTxt);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){

                    case R.id.fab:{
                        //create a new note
                        NewPostDialog dialog = new NewPostDialog();
                        Bundle args = new Bundle();
                        args.putParcelable(getString(R.string.share_movie), receivedMovie);
                        dialog.setArguments(args);
                        dialog.setTargetFragment(ShareMovieFragment.this, NewPostDialog.REQUEST_CODE);
                        dialog.show(getActivity().getSupportFragmentManager(), getString(R.string.dialog_new_post));
                        break;
                    }
                }
            }
        });

    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            receivedMovie = gson.fromJson(s, Movie.class);
            if (receivedMovie != null) {
                Log.d(TAG, "onPostExecute: received movie: " + receivedMovie);
                textView.append("\n" + receivedMovie);

                loadData();

            } else {
                Log.d(TAG, "onPostExecute: error receiving the movie, is NULL");
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            Log.d(TAG, "doInBackground starts with: " + strings[0]);
            String jsonData = downloadJSON(strings[0]);
            if(jsonData == null){
                Log.e(TAG, "doInBackground: Error downloading from url " + strings[0] );
            }
            return jsonData;
        }

        private String downloadJSON(String urlPath) {
            StringBuilder sb = new StringBuilder();

            try{
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                int responseCode = connection.getResponseCode();
                Log.d(TAG, "downloadJSON: Response code was " + responseCode);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = reader.readLine();
                while(line != null){
                    sb.append(line).append("\n");
                    line = reader.readLine();
                }

                reader.close();

            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadJSON: not correct URL: "+urlPath , e);
            } catch (IOException e) {
                Log.e(TAG, "downloadJSON: io error ",e);
            }

            return sb.toString();
        }
    }


}
