package com.sharelly.alexc.sharelly.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sharelly.alexc.sharelly.Dashboard.DashboardActivity;
import com.sharelly.alexc.sharelly.MainActivity;
import com.sharelly.alexc.sharelly.Profile.ProfileActivity;
import com.sharelly.alexc.sharelly.R;

import androidx.annotation.NonNull;

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";

    public static void enableNavigation(final Context context, BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(context, MainActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent1);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent2 = new Intent(context, DashboardActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent2);
                        break;
                    case R.id.navigation_notifications:
                        Intent intent3 = new Intent(context, MainActivity.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent3);
                        break;
                    case R.id.navigation_profile:
                        Intent intent4 = new Intent(context, ProfileActivity.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(intent4);
                        break;
                }

                return false;
            }
        });
    }
}
