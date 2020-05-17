package com.bot.onlinejob.seeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.bot.onlinejob.R;
import com.bot.onlinejob.auth.EnterMobileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SeekerHomeActivity extends AppCompatActivity {
    //to check user logged in or not
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    //bottom navigation view
    BottomNavigationView bottomNavigationSeeker;

    //fragments
    final Fragment seekerHomeFragment = new FragmentSeekerHome();
    final Fragment seekerPostFragment = new FragmentSeekerPost();
    final Fragment seekerStatusFragment = new FragmentSeekerStatus();
    final Fragment seekerAccountFragment = new FragmentSeekerAccount();

    final FragmentManager fmMain = getSupportFragmentManager();

    //currently active fragment
    Fragment activeFragment = seekerHomeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker_activity_home);
        //initialize auth and current user
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        //bottom Navigation view
        bottomNavigationSeeker = findViewById(R.id.bottom_navigation_seeker);
        bottomNavigationSeeker.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //show only home fragment first
        fmMain.beginTransaction().add(R.id.seeker_main_container, seekerAccountFragment, "seekerAccountFragment").hide(seekerAccountFragment).commit();
        fmMain.beginTransaction().add(R.id.seeker_main_container, seekerStatusFragment, "seekerStatusFragment").hide(seekerStatusFragment).commit();
        fmMain.beginTransaction().add(R.id.seeker_main_container, seekerPostFragment, "seekerPostFragment").hide(seekerPostFragment).commit();
        fmMain.beginTransaction().add(R.id.seeker_main_container, seekerHomeFragment, "seekerHomeFragment").show(seekerHomeFragment).commit();

    }

    @Override
    protected void onStart(){
        super.onStart();
        //ensures the user is logged in
        if (currentUser == null){
            Intent intentToVerifyMob = new Intent(SeekerHomeActivity.this, EnterMobileActivity.class);
            intentToVerifyMob.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentToVerifyMob.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentToVerifyMob);
            finish();
        }
    }

    //bottom navigation item clicker listeners
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem bottomNavItem) {
            switch (bottomNavItem.getItemId()) {
                case R.id.seeker_navigation_home:

                    fmMain.beginTransaction().hide(activeFragment).show(seekerHomeFragment).commit();
                    activeFragment = seekerHomeFragment;
                    Log.e("Frag : ","home");
                    return true;
                case R.id.seeker_navigation_post:

                    fmMain.beginTransaction().hide(activeFragment).show(seekerPostFragment).commit();
                    activeFragment = seekerPostFragment;
                    return true;
                case R.id.seeker_navigation_status:

                    fmMain.beginTransaction().hide(activeFragment).show(seekerStatusFragment).commit();
                    activeFragment = seekerStatusFragment;
                    return true;
                case R.id.seeker_navigation_account:

                    fmMain.beginTransaction().hide(activeFragment).show(seekerAccountFragment).commit();
                    activeFragment = seekerAccountFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        if (activeFragment != seekerHomeFragment) {
            fmMain.beginTransaction().hide(activeFragment).show(seekerHomeFragment).commit();

            activeFragment = seekerHomeFragment;
            bottomNavigationSeeker.setSelectedItemId(R.id.seeker_navigation_home);
        } else {
            super.onBackPressed();
        }

    }
}

