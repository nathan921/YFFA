package com.ddtpt.android.yffa;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by e228596 on 10/20/2014.
 */
public class MatchupActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);

        String team1 = getIntent().getStringExtra("team1");
        String team2 = getIntent().getStringExtra("team2");

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentHolder, MatchupFragment.newInstance(team1, team2))
                    .commit();

        }
    }
}
