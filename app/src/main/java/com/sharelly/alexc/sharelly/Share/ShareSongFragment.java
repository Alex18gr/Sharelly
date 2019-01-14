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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.sharelly.alexc.sharelly.BuildConfig;
import com.sharelly.alexc.sharelly.JsonModels.Movie;
import com.sharelly.alexc.sharelly.JsonModels.Track;
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

public class ShareSongFragment extends Fragment {

    private static final String TAG = "ShareSongFragment";

    private View view;
    private String action;
    private String data;
    private Track receivedSong = null;

    // widgets
    private ImageView expandedImageView;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TextView artistTxt;
    private TextView albumTxt;
    private TextView listenersTxt;
    private TextView playcountTxt;
    private TextView summaryTxt;

    private Fragment fragment = this;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_share_song, container, false);
        data = getArguments().getString("id");
        if (data == null || data.equals("")) {
            receivedSong = getArguments().getParcelable("parcel_song");
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: data set");
        setupWidgets();
        //setupToolbar();

        if (data != null && !data.equals("")){
            Log.d(TAG, "onViewCreated: executing download async task");
            DownloadData downloadData = new DownloadData();
            downloadData.execute("http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key="
                    + BuildConfig.API_KEY_LAST_FM + "&format=json&mbid=" + data);
        } else if (receivedSong != null) {

            loadData();
        } else {
            receivedSong = getArguments().getParcelable("parcel_song");
            if (receivedSong != null) {
                Log.d(TAG, "onViewCreated: load recieved song from previous fragment. Not shareable.");
                loadData();
            } else {
                Log.d(TAG, "onViewCreated: error: data is NULL");
            }
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
        artistTxt.setText(receivedSong.getArtist().getName());
        albumTxt.setText(receivedSong.getAlbum().getTitle());
        if (receivedSong.getWiki() != null){
            summaryTxt.setText(receivedSong.getWiki().getSummary());
        } else {
            view.findViewById(R.id.cardView3).setVisibility(View.GONE);
        }

        playcountTxt.setText(receivedSong.getPlaycount());
        listenersTxt.setText(receivedSong.getListeners());
        Picasso.get().load(receivedSong.getAlbum().getImages().get(2).getText()).into(expandedImageView);
        //((ShareActivity)getActivity()).setActionBarTitle(receivedSong.getName());
        setupToolbar(receivedSong.getName());

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

        newPost.setContentId(receivedSong.getMbid());
        newPost.setType(getString(R.string.content_type_song_lastfm));
        newPost.setName(mTitle);
        newPost.setDescription(mDescription);
        newPost.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        newPost.setContent_title(receivedSong.getName());
        newPost.setUser_full_name(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        if (receivedSong.getAlbum().getImages().get(2).getText() != null) {
            newPost.setPost_image(receivedSong.getAlbum().getImages().get(2).getText());
        } else {
            newPost.setPost_image(null);
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
        artistTxt = view.findViewById(R.id.txtArtist);
        albumTxt = view.findViewById(R.id.txtAlbum);
        summaryTxt = view.findViewById(R.id.summaryTxt);
        listenersTxt = view.findViewById(R.id.listenersNumTxt);
        playcountTxt = view.findViewById(R.id.playcountNumTxt);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){

                    case R.id.fab:{
                        //create a new note
                        if (data == null || data.equals("")) {
                            Toast.makeText(getActivity(), "You cannot share this song.", Toast.LENGTH_SHORT).show();
                        } else {
                            NewPostDialog dialog = new NewPostDialog();
                            Bundle args = new Bundle();
                            args.putParcelable(getString(R.string.share_movie), receivedSong);
                            dialog.setArguments(args);
                            dialog.setTargetFragment(ShareSongFragment.this, NewPostDialog.REQUEST_CODE);
                            dialog.show(getActivity().getSupportFragmentManager(), getString(R.string.dialog_new_post));
                        }

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
            Track.TrackInfo info = gson.fromJson(s, Track.TrackInfo.class);
            receivedSong = info.getTrack();
            if (receivedSong != null) {
                Log.d(TAG, "onPostExecute: received track: " + receivedSong);

                loadData();

            } else {
                Log.d(TAG, "onPostExecute: error receiving the track, is NULL");
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
