package com.sharelly.alexc.sharelly.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharelly.alexc.sharelly.JsonModels.MoviesList;
import com.sharelly.alexc.sharelly.Models.User;
import com.sharelly.alexc.sharelly.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SearchUserAdapter extends ArrayAdapter<User> {

    private List<User> users;

    public SearchUserAdapter(@NonNull Context context,
                             @NonNull List<User> objects) {
        super(context, 0, objects);

        users = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;

        User user = users.get(position);

        if(rowView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(R.layout.layout_list_search_user, parent, false);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)rowView.getTag();
        }

        viewHolder.placeUsername.setText(user.getUsername());
        viewHolder.placeFullName.setText(user.getFull_name());
        // Picasso.get().load(movie.getPoster()).into(viewHolder.placePoster);

        return  rowView;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    static class ViewHolder {
        public final TextView placeUsername;
        public final TextView placeFullName;
        public final ImageView placeProfileImage;

        public ViewHolder(View view){
            placeUsername = view.findViewById(R.id.usernameTxt);
            placeFullName = view.findViewById(R.id.fullNameTxt);
            placeProfileImage = view.findViewById(R.id.profile_image);
        }
    }
}
