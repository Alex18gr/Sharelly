package com.sharelly.alexc.sharelly.Share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sharelly.alexc.sharelly.JsonModels.Movie;
import com.sharelly.alexc.sharelly.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class NewPostDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "NewPostDialog";
    public static final int REQUEST_CODE = 2019;

    //widgets
    private EditText mTitle, mDescription;
    private TextView mCreate, mCancel;
    private Movie mMovie;

    private ShareMovieFragment mFragment;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        int style = DialogFragment.STYLE_NORMAL;
//        int theme = android.R.style.Theme_Holo_Light_Dialog;
//        setStyle(style, theme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_post, container, false);
        mTitle = view.findViewById(R.id.post_title);
        mDescription = view.findViewById(R.id.post_description);
        mCreate = view.findViewById(R.id.create);
        mCreate.setOnClickListener(this);
        mCancel = view.findViewById(R.id.cancel);
        mCancel.setOnClickListener(this);


        getDialog().setTitle("New Post Share");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(getString(R.string.share_movie));
            Log.d(TAG, "onViewCreated: moview to share: " + mMovie);
        } else {
            dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.create:{

                // insert the new note

                String title = mTitle.getText().toString();
                String description = mDescription.getText().toString();

                if(!title.equals("")){
                    Log.d(TAG, "onClick: got title: " + title + " and description: " + description );
                    //mIMainActivity.createNewNote(title, content);
                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.new_post_dialog_title), title);
                    intent.putExtra(getString(R.string.new_post_dialog_description), description);
                    Fragment fragment = getTargetFragment();
                    if (fragment == null) Log.d(TAG, "onClick: target fragment in NULL");
                    else Log.d(TAG, "onClick: " + fragment.toString());
                    fragment.onActivityResult(REQUEST_CODE, Activity.RESULT_OK, intent);
                    getDialog().dismiss();
                }
                else{
                    Toast.makeText(getActivity(), "Enter a title", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.cancel:{
                getDialog().dismiss();
                break;
            }
        }
    }
}
