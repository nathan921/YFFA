package com.ddtpt.android.yffa;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by e228596 on 10/13/2014.
 */
public class Team {
    private String mOwner, mTeamName, mTeamId, mTeamKey, mTeamLogoUrl, mStreakType, mStreakValue;
    private int mWins, mLosses, mTies, mWaiverPriority, mMoves, mRank;
    private double mPoints, mProjectedPoint, mTotalPoints, mPointsAgainst;
    private League mLeague;
    private ArrayList<Player> mPlayers;
    private ImportantYahooStuff mYahooStuff;

    public onPlayerStatsRetrievedListener playerStatsDelegate = null;

    public Team(Context c) {
        mYahooStuff = ImportantYahooStuff.get(c);
        mLeague = mYahooStuff.getLeague();
    }

    public void populateTeam(HashMap<String, Object> teamData)  {
        mPlayers = (ArrayList<Player>)teamData.get("players");
        mOwner = teamData.get("team_nickname").toString();
        mTeamName = teamData.get("team_name").toString();
        mTeamId = teamData.get("team_id").toString();
        mTeamKey = teamData.get("team_key").toString();
        mTeamLogoUrl = teamData.get("team_logos").toString();
        mWaiverPriority = Integer.valueOf(teamData.get("team_waiver_priority").toString());
        mMoves = Integer.valueOf(teamData.get("team_number_of_moves").toString());
        mPoints = Double.valueOf(teamData.get("team_points").toString());
        mProjectedPoint = Double.valueOf(teamData.get("team_projected_points").toString());
        mRank = Integer.valueOf(teamData.get("team_rank").toString());
        mWins = Integer.valueOf(teamData.get("team_wins").toString());
        mLosses = Integer.valueOf(teamData.get("team_losses").toString());
        mTies = Integer.valueOf(teamData.get("team_ties").toString());
        mStreakType = teamData.get("team_streak_type").toString();
        mStreakValue = teamData.get("team_streak_value").toString();
        mTotalPoints = Double.valueOf(teamData.get("team_points_for_year").toString());
        mPointsAgainst = Double.valueOf(teamData.get("team_points_against_year").toString());
    }

    public interface onPlayerStatsRetrievedListener {
        void onPlayerStatsRetrieved();
    }

    public void setPlayers(ArrayList<Player> players) {
        mPlayers = players;
    }

    public ArrayList<Player> getPlayers() {
        return mPlayers;
    }

    public class updateTeamPointData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String playerKeys = generatePlayerKeysString();
            String fetchUri = "http://fantasysports.yahooapis.com/fantasy/v2/league/" + mYahooStuff.getLeagueKey() + "/players;player_keys=" + playerKeys + "/stats;type=week;week=current?format=json";

            return mYahooStuff.fetchData(fetchUri);
        }
        @Override
        protected void onPostExecute(String results) {
            //TODO: parse json for team data/pass data back to the fragment to be displayed
            ArrayList<HashMap<String, Object>> fullStats = mYahooStuff.getParser().parsePlayerScoring(results);
            for (HashMap<String, Object> s : fullStats) {
                getPlayerById(Integer.valueOf(s.get("player_id").toString())).setIndividualStats(s);
            }
            playerStatsDelegate.onPlayerStatsRetrieved();
        }
    }

    private String generatePlayerKeysString() {
        StringBuilder builder = new StringBuilder();
        int size = mPlayers.size();
        for (int i = 0; i < size; i++) {
            builder.append(mPlayers.get(i).getPlayerKey());
            if (i < size - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    public Player getPlayerById(int playerId) {
        for (int i = 0; i < mPlayers.size(); i++) {
            int id = Integer.valueOf(mPlayers.get(i).getPlayerId());
            if (id == playerId) {
                return mPlayers.get(i);
            }
        }
        return null;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        mOwner = owner;
    }

    public String getTeamName() {
        return mTeamName;
    }

    public void setTeamName(String teamName) {
        mTeamName = teamName;
    }

    public String getTeamId() {
        return mTeamId;
    }

    public void setTeamId(String teamId) {
        mTeamId = teamId;
    }

    public String getTeamKey() {
        return mTeamKey;
    }

    public void setTeamKey(String teamKey) {
        mTeamKey = teamKey;
    }

    public String getTeamLogoUrl() {
        return mTeamLogoUrl;
    }

    public void setTeamLogoUrl(String teamLogoUrl) {
        mTeamLogoUrl = teamLogoUrl;
    }

    public int getWins() {
        return mWins;
    }

    public void setWins(int wins) {
        mWins = wins;
    }

    public int getLosses() {
        return mLosses;
    }

    public void setLosses(int loses) {
        mLosses = loses;
    }

    public int getTies() {
        return mTies;
    }

    public void setTies(int ties) {
        mTies = ties;
    }

    public double getPoints() {
        return mPoints;
    }

    public void setPoints(double points) {
        mPoints = points;
    }

    public double getProjectedPoint() {
        return mProjectedPoint;
    }

    public void setProjectedPoint(double projectedPoint) {
        mProjectedPoint = projectedPoint;
    }

    public int getWaiverPriority() {
        return mWaiverPriority;
    }

    public void setWaiverPriority(int waiverPriority) {
        mWaiverPriority = waiverPriority;
    }
}
