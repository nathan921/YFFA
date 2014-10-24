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

/**
 * Created by e228596 on 10/16/2014.
 */
public class MainPagerActivity extends Activity implements ScoreBoardFragment.Callbacks{
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
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(4);

        final ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        setContentView(mViewPager);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        for (int i = 0; i < 3; i++ ) {
            String tabText;
            switch (i) {
                case 0:
                    tabText = getString(R.string.roster);
                    break;
                case 1:
                    tabText = getString(R.string.matchups);
                    break;
                case 2:
                    tabText = getString(R.string.scoring);
                    break;
                default:
                    tabText = "";
            }
            actionBar.addTab(
                    actionBar.newTab()
                    .setText(tabText)
                    .setTabListener(tabListener)
            );
        }

        FragmentManager fm = getFragmentManager();
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                getActionBar().setSelectedNavigationItem(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
