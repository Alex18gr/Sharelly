package com.sharelly.alexc.sharelly.Share;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sharelly.alexc.sharelly.BuildConfig;
import com.sharelly.alexc.sharelly.JsonModels.MoviesList;
import com.sharelly.alexc.sharelly.JsonModels.TrackSearch;
import com.sharelly.alexc.sharelly.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private static final String LIST_INSTANCE_STATE = "searchListInstance";

    private View view;
    private TextView textView;
    private String action;
    private String data;
    private Spinner spinner;
    private EditText searchText;
    private ListView searchListView;
    private SearchMovieAdapter movieAdapter = null;
    private SearchSongAdapter songAdapter = null;
    private List<TrackSearch> mSongsData;
    private List<MoviesList.SearchResultMovie> mMoviesData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_INSTANCE_STATE, searchListView.onSaveInstanceState());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (movieAdapter != null) {
            searchListView.setAdapter(movieAdapter);
            movieAdapter.setMovies(mMoviesData);
            movieAdapter.notifyDataSetChanged();
        } else if (songAdapter != null) {
            searchListView.setAdapter(songAdapter);
            songAdapter.setTracks(mSongsData);
            songAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        this.view = view;
        super.onViewCreated(view, savedInstanceState);

        spinner = view.findViewById(R.id.search_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.search_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: spinner item selected: " + spinner.getItemAtPosition(i).toString());
                searchText.setText("");
                setEmptyListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchText = view.findViewById(R.id.search);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d(TAG, "onEditorAction: search button pressed");
                    String category = spinner.getSelectedItem().toString();
                    String searchQuerry = v.getText().toString();
                    if (!searchQuerry.equals("")) {
                        Log.d(TAG, "onEditorAction: performing search for: " + category +
                                ". The querry is: " + searchQuerry);
                        setEmptyListView();
                        performSearch(searchQuerry, category);
                        hideKeyboard(view);
                    }

                    return true;
                }
                return false;
            }
        });


        searchListView = view.findViewById(R.id.listViewSearchResults);
    }

    private void setEmptyListView() {
        ArrayAdapter<Integer> adapter1 = new ArrayAdapter<Integer>(getActivity(),
                0);
        searchListView.setAdapter(adapter1);
    }

    private void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void performSearch(String searchQuerry, String category) {
        if (category.equals(DownloadData.OMDB_CAT)) {
            Log.d(TAG, "onViewCreated: executing down load async task");
            DownloadData downloadData = new DownloadData();
            downloadData.execute("http://www.omdbapi.com/?s=" + searchQuerry + "&apikey=" + BuildConfig.API_KEY_OMDB, DownloadData.OMDB_CAT);
        } else if (category.equals(DownloadData.LASTFM_CAT)) {
            Log.d(TAG, "performSearch: executing download async task");
            DownloadData downloadData = new DownloadData();
            downloadData.execute("http://ws.audioscrobbler.com/2.0/?method=track.search&track=" +
                            searchQuerry + "&api_key=" +
                            BuildConfig.API_KEY_LAST_FM + "&format=json",
                    DownloadData.LASTFM_CAT);
        } else {
            Log.d(TAG, "performSearch: category not match, category=" + category + ".");
        }

    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        private static final String TAG = "DownloadData";
        private String dataType;

        public static final String OMDB_CAT = "Movie";
        public static final String LASTFM_CAT = "Song";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            Log.d(TAG, "onPostExecute: on post excecute, checking category...");
            if (dataType.equals(OMDB_CAT)) {
                MoviesList receivedMoviesList = gson.fromJson(s, MoviesList.class);
                if (receivedMoviesList != null && receivedMoviesList.getSearchResults() != null) {
                    Log.d(TAG, "onPostExecute: received movies: " + receivedMoviesList);
                    // here we place the records to the adapter

                    mMoviesData = receivedMoviesList.getSearchResults();
                    mSongsData = null;
                    movieAdapter = new SearchMovieAdapter(getActivity(),
                            receivedMoviesList.getSearchResults());

                    movieAdapter.notifyDataSetChanged();
                    songAdapter = null;
                    searchListView.setAdapter(movieAdapter);
                    searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            MoviesList.SearchResultMovie movie =
                                    (MoviesList.SearchResultMovie) searchListView.getItemAtPosition(i);
                            ShareMovieFragment fragment = new ShareMovieFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("id", movie.getImdbId());
                            fragment.setArguments(bundle);
                            getFragmentManager().beginTransaction()
                                    .replace(((ViewGroup) getView().getParent()).getId(), fragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });
                } else {
                    Log.d(TAG, "onPostExecute: error receiving the movies, list is NULL");
                }
            } else if (dataType.equals(LASTFM_CAT)) {
                TrackSearch.Results results = gson.fromJson(s,
                        TrackSearch.Results.class);
                Log.d(TAG, "onPostExecute: music results" + results);
                if (results != null && results.getTrackResults().getTrackmatches().getTrackList() != null) {
                    Log.d(TAG, "onPostExecute: music results" + results);
                    mSongsData = results.getTrackResults().getTrackmatches().getTrackList();
                    mMoviesData = null;
                    songAdapter = new SearchSongAdapter(getActivity(),
                            results.getTrackResults().getTrackmatches().getTrackList());
                    movieAdapter = null;
                    searchListView.setAdapter(songAdapter);
                    //songAdapter.notifyDataSetChanged();
                    searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            TrackSearch track = (TrackSearch) searchListView.getItemAtPosition(i);
                            Log.d(TAG, "onItemClick: selected song: " + track);
                        }
                    });

                }
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            Log.d(TAG, "doInBackground starts with: " + strings[0]);
            String jsonData = downloadJSON(strings[0]);
            dataType = strings[1];
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
