package com.sharelly.alexc.sharelly.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharelly.alexc.sharelly.JsonModels.MoviesList;
import com.sharelly.alexc.sharelly.Models.Post;
import com.sharelly.alexc.sharelly.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MoviePostListAdapter extends ArrayAdapter<Post> {

    private List<Post> posts;

    public MoviePostListAdapter(@NonNull Context context,
                                @NonNull List<Post> objects) {
        super(context, 0, objects);

        posts = objects;

    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;

        Post post = posts.get(position);

        if(rowView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(R.layout.layout_post_movie_item, parent, false);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)rowView.getTag();
        }

        viewHolder.placeTitle.setText(post.getName());
        viewHolder.placeTimestamp.setText(viewHolder.spf.format(post.getTimestamp()));
        viewHolder.placeDescription.setText(post.getDescription());
        if (post.getContent_title() != null) {
            viewHolder.placeContentTitleContentType.setText(post.getType());
        } else {
            viewHolder.placeContentTitleContentType.setText(post.getContent_title() +
             " - " + post.getType());
        }
        if (post.getPost_image() != null) {
            Picasso.get().load(post.getPost_image()).into(viewHolder.placeContentImage);
        }

//        viewHolder.placeType.setText(movie.getType());
//        viewHolder.placeYear.setText(movie.getYear());
//        Picasso.get().load(movie.getPoster()).into(viewHolder.placePoster);

        return  rowView;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    static class ViewHolder {
        public final TextView placeTitle;
        public final TextView placeContentTitleContentType;
        public final TextView placeTimestamp;
        public final TextView placeDescription;
        public final ImageView placeContentImage;
        public final SimpleDateFormat spf;

        public ViewHolder(View view){
            placeTitle = view.findViewById(R.id.titleTxt);
            placeContentTitleContentType = view.findViewById(R.id.contentTitleContentTypeTxt);
            placeTimestamp = view.findViewById(R.id.timestampTxt);
            placeDescription = view.findViewById(R.id.descriptionTxt);
            placeContentImage = view.findViewById(R.id.contentImage);
            spf = new SimpleDateFormat("MMM dd, yyyy");
        }
    }
}
