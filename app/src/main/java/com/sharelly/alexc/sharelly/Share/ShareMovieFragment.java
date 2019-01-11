package com.sharelly.alexc.sharelly.Share;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.sharelly.alexc.sharelly.BuildConfig;
import com.sharelly.alexc.sharelly.JsonModels.Movie;
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
        setupToolbar();

        if (data != null){
            Log.d(TAG, "onViewCreated: executing down load async task");
            DownloadData downloadData = new DownloadData();
            downloadData.execute("http://www.omdbapi.com/?i=" + data + "&apikey=" + BuildConfig.API_KEY_OMDB);
        } else {
            Log.d(TAG, "onViewCreated: error: data is NULL");
        }



    }

    private void setupToolbar() {
        toolbar = view.findViewById(R.id.toolbar);
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
        toolbar.setTitle(receivedMovie.getTitle());
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
            view.findViewById(R.id.imdbRatingTxt).setVisibility(View.GONE);
        }
        if (rottenScoreTxt.getText().toString().equals("TextView")) {
            rottenScoreTxt.setVisibility(View.GONE);
            view.findViewById(R.id.rtRatingTxt).setVisibility(View.GONE);
        }
        if (metaScoreTxt.getText().toString().equals("TextView")) {
            metaScoreTxt.setVisibility(View.GONE);
            view.findViewById(R.id.mcRatingTxt).setVisibility(View.GONE);
        }

    }

    private void setupWidgets() {
        expandedImageView = view.findViewById(R.id.expandedImage);
        fab = view.findViewById(R.id.fab);
        imdbScoreTxt = view.findViewById(R.id.imdbRatingNumTxt);
        rottenScoreTxt = view.findViewById(R.id.rtRatingNumTxt2);
        metaScoreTxt = view.findViewById(R.id.mcRatingNumTxt);

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
