package com.example.mainactivity;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
public class Main_Tab_adapter extends FragmentPagerAdapter {
    int mNumOfTabs; //tab의 갯수
    int currentPage;

    public Main_Tab_adapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;

    }




    @Override
    public Fragment getItem(int position) {


        switch (position) {
            case 0:
                Fragment_travel_main tab1 = new Fragment_travel_main();

                return tab1;
            case 1:
                Fragment_hotel_info tab2 = new Fragment_hotel_info();
                return tab2;
            case 2:
               Fragment_event_info tab3 = new Fragment_event_info();
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