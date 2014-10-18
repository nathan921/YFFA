package com.ddtpt.android.yffa;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by e228596 on 10/7/2014.
 */
public class FootballDataActivity extends Activity {

    public FootballDataActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentHolder, new FootballDataFragment())
                    .commit();
        }

    }

}
