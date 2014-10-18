package com.ddtpt.android.yffa;

import android.app.Fragment;

/**
 * Created by e228596 on 10/12/2014.
 */
public class RosterListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RosterListFragment();
    }
}
