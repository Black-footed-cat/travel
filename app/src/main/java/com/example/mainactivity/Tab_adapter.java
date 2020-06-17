package com.example.mainactivity;

import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Tab_adapter extends FragmentPagerAdapter {

    int mNumOfTabs; //tab의 갯수


    public Tab_adapter (FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;

    }
    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                Fragment_info tab1 = new Fragment_info();

                return tab1;
            case 1:
                Fragment_map tab2 = new Fragment_map();
                return tab2;
            case 2:
                Fragment_review tab3 = new Fragment_review();
                return tab3;
            default:
                return null;

        }
        //return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

