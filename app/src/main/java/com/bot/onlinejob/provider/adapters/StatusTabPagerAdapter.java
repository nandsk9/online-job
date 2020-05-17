package com.bot.onlinejob.provider.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;

public class StatusTabPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> mFragmentListTab;
    ArrayList<String> mFragmentTitleListTab;
    public StatusTabPagerAdapter(FragmentManager fm)
    {
        super(fm);
        mFragmentListTab=new ArrayList<>();
        mFragmentTitleListTab=new ArrayList<>();
    }

    @Nullable
    @Override
    public Fragment getItem(int position){
        return mFragmentListTab.get(position);
    }

    @Override
    public int getCount(){
        return mFragmentListTab.size();
    }

    public void addFragment(Fragment fragment,String title)
    {
        mFragmentListTab.add(fragment);
        mFragmentTitleListTab.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return mFragmentTitleListTab.get(position);
    }
}
