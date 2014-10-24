package com.ddtpt.android.yffa;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Created by e228596 on 10/17/2014.
 */
public class ScoreBoardActivity extends SingleFragmentActivity implements ScoreBoardFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        ScoreBoardFragment sbFrag = new ScoreBoardFragment();
        return sbFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
   public void onMatchupSelected(String team1, String team2) {

        if (getResources().getBoolean(R.bool.has_two_panes)) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment oldDetail = fm.findFragmentById(R.id.detailsHolder);
            Fragment newDetail = MatchupFragment.newInstance(team1, team2);

            if (oldDetail != null) {
                ft.remove(oldDetail);
            }
            ft.add(R.id.detailsHolder, newDetail);
            ft.commit();
        } else {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ScoreBoardFragment mFrag = (ScoreBoardFragment)fm.findFragmentById(R.id.fragmentHolder);
            Fragment newDetailFrag = MatchupFragment.newInstance(team1, team2);
            ft.add(R.id.fragmentHolder, newDetailFrag);
            ft.commit();

            //Update MatchupFragments UI
            //mFrag.updateUI();  //TODO: Update UI should call (MatchupAdapter)getListAdapter().notifyDataSetChanged();
        }
    }
}
