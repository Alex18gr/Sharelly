package com.sharelly.alexc.sharelly.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.sharelly.alexc.sharelly.Dashboard.DashboardActivity;
import com.sharelly.alexc.sharelly.R;
import com.sharelly.alexc.sharelly.Share.ShareActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private int mNum;
    private View view;
    private CardView cardViewMainFeedLink;
    private CardView cardViewSearchContentLink;
    private TextView helloUserTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardViewMainFeedLink = view.findViewById(R.id.cardView1);
        cardViewSearchContentLink = view.findViewById(R.id.cardView2);
        helloUserTxt = view.findViewById(R.id.helloUserTxt);

        cardViewMainFeedLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DashboardActivity.class);
                getContext().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        cardViewSearchContentLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShareActivity.class);
                getContext().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        helloUserTxt.setText("Hello " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }
}
