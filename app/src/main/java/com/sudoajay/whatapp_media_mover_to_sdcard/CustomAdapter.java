package com.sudoajay.whatapp_media_mover_to_sdcard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Olakunmi on 21/01/2017.
 */

public class CustomAdapter extends FragmentPagerAdapter {
    List<Fragment> mFragmentCollection = new ArrayList<>();
    List<String> mTitleCollection = new ArrayList<>();
    private After_MainTransferFIle after_main_transferFIle;

    public CustomAdapter(FragmentManager fm,After_MainTransferFIle after_main_transferFIle) {
        super(fm);
        this.after_main_transferFIle = after_main_transferFIle;
    }

    public void addFragment(String title, Fragment fragment)
    {
        mTitleCollection.add(title);
        mFragmentCollection.add(fragment);
    }
    //Needed for
    @Override
    public CharSequence getPageTitle(int position) {

        return mTitleCollection.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentCollection.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentCollection.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}