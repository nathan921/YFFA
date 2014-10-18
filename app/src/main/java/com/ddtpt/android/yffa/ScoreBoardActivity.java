package com.ddtpt.android.yffa;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by e228596 on 10/17/2014.
 */
public class ScoreBoardActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ScoreBoardFragment();
    }
}
