package com.ddtpt.android.yffa;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

/**
 * Created by e228596 on 10/16/2014.
 */
public class MainPagerActivity extends ActionBarActivity implements ScoreBoardFragment.Callbacks{
    private ViewPager mViewPager;

    @Override
    public void onMatchupSelected(String team1, String team2) {
        Intent intent = new Intent(this, MatchupActivity.class);
        intent.putExtra("team1", team1);
        intent.putExtra("team2", team2);
        this.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_pager_layout_with_toolbar);

        mViewPager = (ViewPager) findViewById(R.id.myViewPager);
        mViewPager.setOffscreenPageLimit(4);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0:
                        return RosterListFragment.newInstance();
                    case 1:
                        return ScoreBoardFragment.newInstance();
                    case 2:
                        return StatValueListFragment.newInstance();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

    }
}
