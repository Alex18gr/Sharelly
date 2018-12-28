package com.sharelly.alexc.sharelly.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.sharelly.alexc.sharelly.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GridImageAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private LayoutInflater mInflater;
    private int layoutResource;
    private String mAppend;
    private ArrayList<String> imgURLs;

    public GridImageAdapter(@NonNull Context context, int layoutResource,
                            String mAppend, @NonNull ArrayList<String> imgURLs) {
        super(context, layoutResource, imgURLs);
        this.mContext = context;
        this.layoutResource = layoutResource;
        this.mAppend = mAppend;
        this.imgURLs = imgURLs;
        this.mInflater = LayoutInflater.from(context);
        this.mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null) {
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.image = convertView.findViewById(R.id.gridImageView);
            holder.mProgressBar = convertView.findViewById(R.id.gridImageProgressbar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String imgURL = getItem(position);
        Picasso.get().load(imgURL).into(holder.image);
        holder.mProgressBar.setVisibility(View.GONE);

        return convertView;
    }

    private static class ViewHolder {
        SquareImageView image;
        ProgressBar mProgressBar;

    }
}
