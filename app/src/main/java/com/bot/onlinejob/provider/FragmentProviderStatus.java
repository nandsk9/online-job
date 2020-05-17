package com.bot.onlinejob.provider;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.bot.onlinejob.R;
import com.bot.onlinejob.provider.adapters.StatusTabPagerAdapter;
import com.bot.onlinejob.provider.status.ProviderStatusActiveJobFragment;
import com.bot.onlinejob.provider.status.ProviderStatusJobAcceptedFragment;
import com.bot.onlinejob.provider.status.ProviderStatusJobFinishedFragment;
import com.google.android.material.tabs.TabLayout;

public class FragmentProviderStatus extends Fragment {
    Activity referenceActivity;
    View parentHolder;
    //for tablayout
    ViewPager tabLayoutStatusViewPager;
    TabLayout statusTabLayout;
    StatusTabPagerAdapter statusTabPagerAdapter;
   FragmentManager fmMainStatus;


    //required empty constructor
    public FragmentProviderStatus(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        referenceActivity=getActivity();
        parentHolder = inflater.inflate(R.layout.provider_status_fragment, container,
                false);
        Log.e("Frag : ","Status");
        //setup the inner tab layout fragments
        tabLayoutStatusViewPager =parentHolder.findViewById(R.id.seeker_viewpager_status_fragment);
        statusTabLayout = referenceActivity.findViewById(R.id.provider_tablayout_status_fragment);

        fmMainStatus = getActivity().getSupportFragmentManager();
        statusTabPagerAdapter = new StatusTabPagerAdapter(fmMainStatus);
        statusTabPagerAdapter.addFragment(new ProviderStatusActiveJobFragment(),getString(R.string.job_active));
        statusTabPagerAdapter.addFragment(new ProviderStatusJobAcceptedFragment(),getString(R.string.job_accepted));
        statusTabPagerAdapter.addFragment(new ProviderStatusJobFinishedFragment(),getString(R.string.finished));

        //to make the tabs swipe
        tabLayoutStatusViewPager.setAdapter(statusTabPagerAdapter);
        //to make the tabIndicator move according to swipe
        statusTabLayout.setupWithViewPager(tabLayoutStatusViewPager);
        //return parentHolder;
        return parentHolder;
    }
}
