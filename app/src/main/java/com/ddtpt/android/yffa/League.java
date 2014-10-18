package com.ddtpt.android.yffa;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by e228596 on 10/13/2014.
 */
public class League {

    private ArrayList<Team> mTeams;
    private String mLeagueName;
    private int mNumberOfTeams;

    private static League sLeague;
    private Context mAppContext;

    public League(JSONObject leagueData) {
        try {
            mLeagueName = leagueData.getString("league_name");
        } catch (JSONException e) {
            Log.e("LEAGUE CREATION", e.toString());
        }

    }

    public static League get() {
        if (sLeague == null) {
            //sLeague = new League(c.getApplicationContext());
            return null;
        }
        return sLeague;
    }
}
