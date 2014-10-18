package com.ddtpt.android.yffa;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by e228596 on 10/13/2014.
 */
public class Team {
    private String mOwner, mTeamName;
    private int mWins, mLoses, mTies, mPoints;
    private ArrayList<Player> mPlayers;
    private ArrayList<Stats> mStats;
    private Context mAppContext;

    private static Team sTeam;

    public Team(Context context) {
        mAppContext = context;

    }

    public void setStats(ArrayList<Stats> stats) {
        mStats = stats;
    }

    public ArrayList<Stats> getStats() {
        return mStats;
    }

    public void setPlayers(ArrayList<Player> players) {
        mPlayers = players;
    }

    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }

    public static Team get(Context c) {
        if (sTeam == null) {
            sTeam = new Team(c.getApplicationContext());
        }
        return sTeam;
    }

}
