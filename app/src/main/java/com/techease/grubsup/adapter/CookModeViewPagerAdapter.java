package com.techease.grubsup.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.techease.grubsup.views.fragments.recipeFragments.CookModeDirectionFragment;
import com.techease.grubsup.views.fragments.recipeFragments.CookModeIngredientsFragment;

/**
 * Created by AttaUrRahman on 5/8/2018.
 */

public class CookModeViewPagerAdapter extends FragmentPagerAdapter {


    private int NUM_ITEMS = 2;

    public CookModeViewPagerAdapter(FragmentManager fm) {

        super(fm);

    }

    @Override
    public Fragment getItem(int position) {


        switch (position){
            case 0:
                return new CookModeIngredientsFragment();
            case 1:
                return new CookModeDirectionFragment();


        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
