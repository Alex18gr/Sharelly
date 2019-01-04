package com.sharelly.alexc.sharelly.Share;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sharelly.alexc.sharelly.BuildConfig;
import com.sharelly.alexc.sharelly.JsonModels.Movie;
import com.sharelly.alexc.sharelly.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ShareMovieFragment extends Fragment {

    private static final String TAG = "ShareMovieFragment";

    private View view;
    private TextView textView;
    private String action;
    private String data;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_share, container, false);
        data = getArguments().getString("id");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        super.onViewCreated(view, savedInstanceState);
        textView = view.findViewById(R.id.txtShare);


        textView.setText(data);
        Log.d(TAG, "onViewCreated: data set");


        if (data != null){
            Log.d(TAG, "onViewCreated: executing down load async task");
            DownloadData downloadData = new DownloadData();
            downloadData.execute("http://www.omdbapi.com/?i=" + data + "&apikey=" + BuildConfig.API_KEY_OMDB);
        } else {
            Log.d(TAG, "onViewCreated: error: data is NULL");
        }

    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            Movie receivedMovie = gson.fromJson(s, Movie.class);
            if (receivedMovie != null) {
                Log.d(TAG, "onPostExecute: received movie: " + receivedMovie);
                textView.append("\n" + receivedMovie);
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
