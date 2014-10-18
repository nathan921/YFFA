package com.ddtpt.android.yffa;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


/**
 * Created by e228596 on 10/3/2014.
 */
public class LoadScreenActivity extends Activity implements ImportantYahooStuff.keyRetrievalCompleteListener {

    private ImportantYahooStuff mYahooStuff;
    private AsyncTask<Void, Void, Void> mTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_fragment_activity);

        mYahooStuff = ImportantYahooStuff.get(this);
        mYahooStuff.delegate = this;

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentHolder, new LoadScreenFragment())
                    .commit();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("BALLS", "onNewIntent Start");
        final Uri uri = intent.getData();
        Log.i("BALLS", "getting uri from intent");
        if (uri != null && uri.getScheme().equals("yffa")) {
            Log.i("BALLS", "calling get access token with uri");
            mYahooStuff.new getAccessToken().execute(uri);
        }
    }

    public void keyRetrievalComplete(boolean result) {
        if (result == false) {
            if (mTask == null) {
                mTask = mYahooStuff.new refreshAccessToken().execute();
            }
        } else if (result == true) {
            Intent intent = new Intent(this, MainPagerActivity.class);
            startActivity(intent);
            //mYahooStuff.new fetchStats().execute();
        }
    }



}
