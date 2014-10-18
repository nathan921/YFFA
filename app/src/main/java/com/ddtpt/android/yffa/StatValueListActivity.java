package com.ddtpt.android.yffa;

import android.app.Fragment;

/**
 * Created by e228596 on 10/16/2014.
 */
public class StatValueListActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new StatValueListFragment();
    }
}
