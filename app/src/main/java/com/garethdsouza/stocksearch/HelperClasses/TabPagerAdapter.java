package com.garethdsouza.stocksearch.HelperClasses;

/**
 * Created by garethdsouza on 4/11/16.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;

import com.garethdsouza.stocksearch.R;
import com.garethdsouza.stocksearch.TabFragments.CurrentFragment;
import com.garethdsouza.stocksearch.TabFragments.HistoricalFragment;
import com.garethdsouza.stocksearch.TabFragments.NewsFragment;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    String searchTerm;

    public TabPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("SearchTerm",searchTerm);

        switch (position) {
            case 0:
                CurrentFragment currentFragment = new CurrentFragment();
                currentFragment.setArguments(bundle);
                return currentFragment;

            case 1:
                HistoricalFragment historicalFragment = new HistoricalFragment();
                historicalFragment.setArguments(bundle);
                return historicalFragment;
            case 2:
                NewsFragment newsFragment = new NewsFragment();
                newsFragment.setArguments(bundle);
                return newsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public void setSearchTerm(String searchTerm){
        this.searchTerm = searchTerm;
    }
}