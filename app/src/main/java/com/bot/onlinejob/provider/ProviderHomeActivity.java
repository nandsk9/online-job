package com.bot.onlinejob.provider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.bot.onlinejob.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class ProviderHomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationProvider;
    Toolbar toolbarStatus;

    //fragments
    final Fragment providerHomeFragment = new FragmentProviderHome();
    final Fragment providerPostFragment = new FragmentProviderPost();
    final Fragment providerStatusFragment = new FragmentProviderStatus();
    final Fragment providerAccountFragment = new FragmentProviderAccount();

    final FragmentManager fmMain = getSupportFragmentManager();

    //currently active fragment
    Fragment activeFragment = providerHomeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_activity_home);

        //status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        //bottom Navigation view
        bottomNavigationProvider = findViewById(R.id.bottom_navigation_provider);
        bottomNavigationProvider.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //show only home fragment first
        fmMain.beginTransaction().add(R.id.provider_main_container, providerAccountFragment, "providerAccountFragment").hide(providerAccountFragment).commit();
        fmMain.beginTransaction().add(R.id.provider_main_container, providerStatusFragment, "providerStatusFragment").hide(providerStatusFragment).commit();
        fmMain.beginTransaction().add(R.id.provider_main_container, providerPostFragment, "providerPostFragment").hide(providerPostFragment).commit();
        fmMain.beginTransaction().add(R.id.provider_main_container, providerHomeFragment, "providerHomeFragment").show(providerHomeFragment).commit();

        toolbarStatus = findViewById(R.id.provider_toolbartab_job_status);
    }

    //bottom navigation item clicker listeners
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem bottomNavItem) {
            switch (bottomNavItem.getItemId()) {
                case R.id.provider_navigation_home:
                    toolbarStatus.setVisibility(View.GONE);
                    fmMain.beginTransaction().hide(activeFragment).show(providerHomeFragment).commit();
                    activeFragment = providerHomeFragment;
                    Log.e("Frag : ","home");
                    return true;
                case R.id.provider_navigation_post:
                    toolbarStatus.setVisibility(View.GONE);
                    fmMain.beginTransaction().hide(activeFragment).show(providerPostFragment).commit();
                    activeFragment = providerPostFragment;
                    return true;
                case R.id.provider_navigation_status:
                    toolbarStatus.setVisibility(View.VISIBLE);
                    fmMain.beginTransaction().hide(activeFragment).show(providerStatusFragment).commit();
                    activeFragment = providerStatusFragment;
                    return true;
                case R.id.provider_navigation_account:
                    toolbarStatus.setVisibility(View.GONE);
                    fmMain.beginTransaction().hide(activeFragment).show(providerAccountFragment).commit();
                    activeFragment = providerAccountFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        if (activeFragment != providerHomeFragment) {
            fmMain.beginTransaction().hide(activeFragment).show(providerHomeFragment).commit();

            activeFragment = providerHomeFragment;
            bottomNavigationProvider.setSelectedItemId(R.id.provider_navigation_home);
        } else {
            super.onBackPressed();
        }

    }
}

