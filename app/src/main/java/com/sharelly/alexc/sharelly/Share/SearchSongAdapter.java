package com.sharelly.alexc.sharelly.Share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharelly.alexc.sharelly.JsonModels.TrackSearch;
import com.sharelly.alexc.sharelly.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SearchSongAdapter extends ArrayAdapter<TrackSearch> {

    private List<TrackSearch> tracks;

    public SearchSongAdapter(@NonNull Context context,
                             @NonNull List<TrackSearch> objects) {
        super(context, 0, objects);

        tracks = objects;

    }

    public List<TrackSearch> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackSearch> tracks) {
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;

        TrackSearch track = tracks.get(position);

        if(rowView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(R.layout.layout_list_search_song, parent, false);
            viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)rowView.getTag();
        }

        viewHolder.placeName.setText(track.getName());
        viewHolder.placeArtistAlbum.setText(track.getArtist() + " \u00B7 " +
                "last.fm listeners: " + track.getListeners());
        Picasso.get().load(track.getImage().get(3).getText()).into(viewHolder.placeCover);

        return  rowView;
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    static class ViewHolder {
        public final TextView placeName;
        public final TextView placeArtistAlbum;
        public final ImageView placeCover;

        public ViewHolder(View view){
            placeName = view.findViewById(R.id.nameTxt);
            placeArtistAlbum = view.findViewById(R.id.artistAlbumTxt);
            placeCover = view.findViewById(R.id.pictureCover);
        }
    }
}
