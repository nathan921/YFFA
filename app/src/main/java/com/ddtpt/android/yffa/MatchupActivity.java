package com.ddtpt.android.yffa;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by e228596 on 10/20/2014.
 */
public class MatchupActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new MatchupFragment();
    }
}
