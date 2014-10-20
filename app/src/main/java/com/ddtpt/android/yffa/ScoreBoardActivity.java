package com.ddtpt.android.yffa;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by e228596 on 10/17/2014.
 */
public class ScoreBoardActivity extends SingleFragmentActivity implements ScoreBoardFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new ScoreBoardFragment();
    }

    public void onMatchupSelected(MatchupObject matchup) {
        if (getResources().getBoolean(R.bool.has_two_panes)) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment oldDetail = fm.findFragmentById(R.id.detailsHolder);
            Fragment newDetail = MatchupFragment.newInstace(matchup);

            if (oldDetail != null) {
                ft.remove(oldDetail);
            }
            ft.add(R.id.detailsHolder, newDetail);
            ft.commit();
        } else {
            FragmentManager fm = getFragmentManager();
            MatchupFragment mFrag = (MatchupFragment)fm.findFragmentById(R.id.fragmentHolder);
            //Update MatchupFragments UI
            mFrag.updateUI();  //TODO: Update UI should call (MatchupAdapter)getListAdapter().notifyDataSetChanged();
        }
    }
}
