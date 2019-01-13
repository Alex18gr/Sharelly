package com.sharelly.alexc.sharelly.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.sharelly.alexc.sharelly.Models.Post;
import com.sharelly.alexc.sharelly.R;
import com.sharelly.alexc.sharelly.Share.SearchMovieAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MoviesFragment extends Fragment {

    private static final String TAG = "MoviesFragment";

    private View view;
    private ListView mListView;
    private List<Post> posts;
    private MoviePostListAdapter adapter;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post, Post t1) {
                return t1.getTimestamp().compareTo(post.getTimestamp());
            }
        });

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new MoviePostListAdapter(getActivity(), posts);
            mListView.setAdapter(adapter);
        }
    }

    public MoviesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_movies, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //posts = getArguments().getParcelable("post_list");

        mListView = view.findViewById(R.id.moviesListView);
//        if (posts != null) {
//            adapter = new MoviePostListAdapter(getActivity(), posts);
//            mListView.setAdapter(adapter);
//        }
    }
}
