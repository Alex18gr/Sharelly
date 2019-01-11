package com.sharelly.alexc.sharelly.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sharelly.alexc.sharelly.Dashboard.DashboardActivity;
import com.sharelly.alexc.sharelly.MainActivity;
import com.sharelly.alexc.sharelly.Profile.ProfileActivity;
import com.sharelly.alexc.sharelly.R;
import com.sharelly.alexc.sharelly.Search.SearchActivity;
import com.sharelly.alexc.sharelly.Share.ShareActivity;

import androidx.annotation.NonNull;

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";
    public static final String FROM_MENU = "from_menu";

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(context, MainActivity.class);
                        context.startActivity(intent1);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.navigation_dashboard:
                        Intent intent2 = new Intent(context, DashboardActivity.class);
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.navigation_search:
                        Intent intent3 = new Intent(context, SearchActivity.class);
                        context.startActivity(intent3);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.navigation_profile:
                        Intent intent4 = new Intent(context, ProfileActivity.class);
                        context.startActivity(intent4);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.navigation_share:
                        Intent intent5 = new Intent(context, ShareActivity.class);
                        intent5.putExtra(FROM_MENU, 1);
                        context.startActivity(intent5);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }

                return false;
            }
        });
    }
}
