package com.ddtpt.android.yffa;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by e228596 on 10/13/2014.
 */
public class League {

    private ArrayList<Team> mTeams;
    private ArrayList<Stats> mStats;

    private String mLeagueKey, mLeagueName, mLeagueChatId;

    private int mCurrentWeek, mNumberOfTeams;

    private ArrayList<Integer> mThisWeeksMatchups;

    private static League sLeague;
    private Context mContext;

    protected League(Context c) {
        mContext = c;
        mTeams = new ArrayList<Team>();
        mNumberOfTeams = mTeams.size();
        mThisWeeksMatchups = new ArrayList<Integer>();
    }

    public static League get(Context c) {
        if (sLeague == null) {
            sLeague = new League(c.getApplicationContext());
        }
        return sLeague;
    }

    public void sortTeams() {
        Collections.sort(mTeams, new Comparator<Team>() {
            @Override
            public int compare(Team lhs, Team rhs) {
                return Integer.valueOf(lhs.getTeamId()).compareTo(Integer.valueOf(rhs.getTeamId()));
            }
        });
    }

    public Team getTeamById(int teamId) {
        for (int i = 0; i < mNumberOfTeams; i++) {
            int id = Integer.valueOf(mTeams.get(i).getTeamId());
            if (id == teamId) {
                return mTeams.get(i);
            }
        }
        return null;
    }

    public ArrayList<Integer> getThisWeeksMatchups() {
        return mThisWeeksMatchups;
    }

    public void setThisWeeksMatchups(ArrayList<Integer> mups) {
        mThisWeeksMatchups = mups;
    }

    public ArrayList<Stats> getStats() {
        return mStats;
    }

    public void setStats(ArrayList<Stats> stats) {
        mStats = stats;
    }

    public void addNewTeam(Team team) {
        mTeams.add(team);
        mNumberOfTeams = mTeams.size();
    }

    public Team getTeam(int teamId) {
        return mTeams.get(teamId);
    }

    public String getLeagueKey() {
        return mLeagueKey;
    }

    public void setLeagueKey(String leagueKey) {
        mLeagueKey = leagueKey;
    }

    public String getLeagueName() {
        return mLeagueName;
    }

    public void setLeagueName(String leagueName) {
        mLeagueName = leagueName;
    }

    public String getLeagueChatId() {
        return mLeagueChatId;
    }

    public void setLeagueChatId(String leagueChatId) {
        mLeagueChatId = leagueChatId;
    }

    public int getCurrentWeek() {
        return mCurrentWeek;
    }

    public void setCurrentWeek(int currentWeek) {
        mCurrentWeek = currentWeek;
    }

    public int getNumberOfTeams() {
        return mNumberOfTeams;
    }

    public void setNumberOfTeams(int numberOfTeam) {
        mNumberOfTeams = numberOfTeam;
    }
}
