package com.sharelly.alexc.sharelly.Share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharelly.alexc.sharelly.JsonModels.MoviesList;
import com.sharelly.alexc.sharelly.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SearchMovieAdapter extends ArrayAdapter<MoviesList.SearchResultMovie> {

    public List<MoviesList.SearchResultMovie> getMovies() {
        return movies;
    }

    public void setMovies(List<MoviesList.SearchResultMovie> movies) {
        this.movies = movies;
    }

    private List<MoviesList.SearchResultMovie> movies;

    public SearchMovieAdapter(@NonNull Context context,
                              @NonNull List<MoviesList.SearchResultMovie> objects) {
        super(context, 0, objects);

        movies = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;

        MoviesList.SearchResultMovie movie = movies.get(position);

        if(rowView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(R.layout.layout_list_search_movie, parent, false);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)rowView.getTag();
        }

        viewHolder.placeTitle.setText(movie.getTitle());
        viewHolder.placeType.setText(movie.getType());
        viewHolder.placeYear.setText(movie.getYear());
        Picasso.get().load(movie.getPoster()).into(viewHolder.placePoster);

        return  rowView;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    static class ViewHolder {
        public final TextView placeTitle;
        public final TextView placeYear;
        public final TextView placeType;
        public final ImageView placePoster;

        public ViewHolder(View view){
            placeTitle = view.findViewById(R.id.titleTxt);
            placeYear = view.findViewById(R.id.yearTxt);
            placeType = view.findViewById(R.id.typeTxt);
            placePoster = view.findViewById(R.id.pictureCover);
        }
    }
}
