package com.ddtpt.android.yffa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.YahooApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit.http.GET;

/**
 * Created by e228596 on 10/8/2014.
 */
public class ImportantYahooStuff {

    private Team mTeam;

    public keyRetrievalCompleteListener delegate = null;
    public matchupRetrievalCompleteListener matchupDelegate = null;
    public statRetrievalCompleteListener statsDelegate = null;
    public rosterRetrievalCompleteListener rosterDelegate = null;

    private String mSession;
    Token mRequestToken, mAccessToken;
    private OAuthService mService;

    private String mLeagueKey, mGameKey, mTeamKey;

    private JSONParser mParser;

    private SharedPreferences mPrefs;
    private Context mContext;

    private static final String SECRET = "oauth_secret";
    private static final String TOKEN = "oauth_token";
    private static final String SESSION = "oauth_session_handle";

    private static final String BASE_URL = "http://fantasysports.yahooapis.com/fantasy/v2/";
    private static final String GET_TOKEN = "https://api.login.yahoo.com/oauth/v2/get_token";


    private static final String CONSUMER_KEY = "dj0yJmk9RjVUYUZNc1piMzVRJmQ9WVdrOVNGUnJkMFZFTldVbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD02YQ--";
    private static final String CONSUMER_SECRET = "7c5d77c23d2c2a4c2c117bc846e392c5fa7a4697";

    private static final String GET_REQUEST_TOKEN = "get_request_token";
    private static final String GET_ACCESS_TOKEN = "get_access_token";

    private static final String GAME = "game/";
    private static final String USER = "users;use_login=1";
    private static final String GAME_KEYS = "game_keys=";
    private static final String JSON_FORMAT = "?format=json";

    private static final String METADATA = "metadata";
    private static final String LEAGUES = "leagues";
    private static final String LEAGUE = "league";
    private static final String PLAYER = "player";
    private static final String PLAYERS = "players";
    private static final String GAME_WEEKS = "game_weeks";
    private static final String STAT_CATEGORIES = "stat_categories";
    private static final String POSITION_TYPES = "position_types";
    private static final String ROSTER_POSITIONS = "roster_positions";
    private static final String SETTINGS = "settings";
    private static final String STANDINGS = "standings";
    private static final String SCOREBOARD = "scoreboard";
    private static final String TEAMS = "teams";
    private static final String TRANSACTIONS = "transactions";
    private static final String GAMES = "games";
    private static final String TEAM = "team";
    private static final String ROSTER = "roster";
    private static final String STATS = "stats";
    private static final String USER_DATA = "user_data";
    private static final String USER_GAMES = "user_games";
    private static final String TEAM_KEY = "team_key";
    private static final String LEAGUE_KEY = "league_key";
    private static final String GAME_KEY = "game_key";
    private static final String USER_TEAMS = "user_teams";
    private static final String TEAM_STATS = "team_stats";
    private static final String PLAYER_STATS = "player_stats";
    private static final String TEST_ACCESS = "this_is_a_test";
    private static final String MATCHUPS = "matchups";

    private static ImportantYahooStuff sImportantYahooStuff;

    public static ImportantYahooStuff get(Context c) {
        if (sImportantYahooStuff == null) {
            sImportantYahooStuff = new ImportantYahooStuff(c.getApplicationContext());
        }
        return sImportantYahooStuff;
    }

    public Token getAccToken() {
        return mAccessToken;
    }

    public OAuthService getService() {
        return mService;
    }

    private ImportantYahooStuff(Context context) {
        mTeam = Team.get(context);
        mContext = context;
        mLeagueKey = "";
        mGameKey = "";
        mTeamKey = "";
        mParser = new JSONParser();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String accessToken, accessSecret, session, teamKey, leagueKey, gameKey;

        boolean tokenFlag, userDataFlag;
        tokenFlag = false;
        userDataFlag = false;

        accessToken = mPrefs.getString(TOKEN, "");
        accessSecret = mPrefs.getString(SECRET, "");
        session = mPrefs.getString(SESSION, "");
        teamKey = mPrefs.getString(TEAM_KEY, "");
        leagueKey = mPrefs.getString(LEAGUE_KEY, "");
        gameKey = mPrefs.getString(GAME_KEY, "");

        try {
            mService = new ServiceBuilder()
                    .provider(YahooApi.class)
                    .apiKey(CONSUMER_KEY)
                    .apiSecret(CONSUMER_SECRET)
                    .callback("yffa://www.ddtpt.com")
                    .build();


        } catch (Exception e) {
            Log.e("SERVICE SETUP", e.toString());
        }

        if (!accessToken.equals("") && !accessSecret.equals("") && !session.equals("")) {
            tokenFlag = true;
            mSession = session;
            mAccessToken = new Token(accessToken, accessSecret);


        } else {
            new getRequestToken().execute();
        }

        if (!teamKey.equals("") && !leagueKey.equals("") && !gameKey.equals("")) {
            userDataFlag = true;
            mLeagueKey = leagueKey;
            mGameKey = gameKey;
            mTeamKey = teamKey;

        } else {
            if (mAccessToken != null) {
                new getUserSpecificInformation().execute();
            }
        }

        if (tokenFlag && userDataFlag) {
            new refreshAccessToken().execute();
        }

    }

    public class getRequestToken extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            mRequestToken = mService.getRequestToken();
            String url = mService.getAuthorizationUrl(mRequestToken);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_FROM_BACKGROUND);
            Log.i("BALLS", "INTENT STARTING");
            mContext.startActivity(intent);
            Log.i("BALLS", "INTENT FINISHED");
            return null;
        }

    }

    public class getAccessToken extends AsyncTask<Uri, Void, Void> {
        @Override
        protected Void doInBackground(Uri... params) {
            String token, secret, session;
            Uri uri = params[0];
            String oauth_verifier = uri.getQueryParameter("oauth_verifier");
            Verifier v = new Verifier(oauth_verifier);

            try {
                Token accessToken = mService.getAccessToken(mRequestToken, v);

                session = ExtractSession(accessToken.getRawResponse());

                token = accessToken.getToken();
                secret = accessToken.getSecret();

                mAccessToken = new Token(token, secret);
                mSession = session;

                storeTokenData(token, secret, session);

                delegate.keyRetrievalComplete(true);

            } catch (Exception e) {
                Log.i("", "getAccessToken Exception: " + e.toString());
                delegate.keyRetrievalComplete(false);

            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            new getUserSpecificInformation().execute();
        }

    }

    public class refreshAccessToken extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Token newToken = null;

            OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.login.yahoo.com/oauth/v2/get_token");
            request.addOAuthParameter(SESSION, mSession);
            mService.signRequest(mAccessToken, request);
            Response response = request.send();

            try {
                newToken = YahooApi.class.newInstance().getAccessTokenExtractor().extract(response.getBody());
                mAccessToken = newToken;
                delegate.keyRetrievalComplete(true);
            } catch (Exception e) {
                Log.e("ERROR REFRESHING", e.toString());
                delegate.keyRetrievalComplete(false);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

            mSession = ExtractSession(mAccessToken.getRawResponse());
            storeTokenData(mAccessToken.getToken(), mAccessToken.getSecret(), ExtractSession(mAccessToken.getRawResponse()));
        }

    }

    public class getUserSpecificInformation extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            if (mGameKey.equals("")) {
                String userRequestString = leagueResourceRequestBuilder(USER_DATA);
                OAuthRequest request = new OAuthRequest(Verb.GET, userRequestString);
                mService.signRequest(mAccessToken, request);
                Response response = request.send();

                if (responseCodeHandler(response.getCode())) {
                    mGameKey = mParser.parseGameKey(response.getBody());
                    SharedPreferences.Editor edit = mPrefs.edit();
                    edit.putString(GAME_KEY, mGameKey);
                    edit.commit();
                } else {
                    delegate.keyRetrievalComplete(false);
                }
            }
            if (mLeagueKey.equals("")) {
                String leagueRequestString = leagueResourceRequestBuilder(USER_GAMES);
                OAuthRequest request = new OAuthRequest(Verb.GET, leagueRequestString);
                mService.signRequest(mAccessToken, request);
                Response response = request.send();

                if (responseCodeHandler(response.getCode())) {
                    mLeagueKey = mParser.parseLeagueKey(response.getBody());
                    SharedPreferences.Editor edit = mPrefs.edit();
                    edit.putString(LEAGUE_KEY, mLeagueKey);
                    edit.commit();
                } else {
                    delegate.keyRetrievalComplete(false);
                }
            }
            if (mTeamKey.equals("")) {
                String teamRequestString = leagueResourceRequestBuilder(USER_TEAMS);
                OAuthRequest request = new OAuthRequest(Verb.GET, teamRequestString);
                mService.signRequest(mAccessToken, request);
                Response response = request.send();

                if (responseCodeHandler(response.getCode())) {
                    mTeamKey = mParser.parseTeamKey(response.getBody());
                    SharedPreferences.Editor edit = mPrefs.edit();
                    edit.putString(TEAM_KEY, mTeamKey);
                    edit.commit();
                } else {
                    delegate.keyRetrievalComplete(false);
                }
            }
        return null;
        }

    }

    /*
    * All of the fetches are now individual AsyncTasks because of the
    * data required to be returned.  This way I dont have to make all of the
    * instances static and set them from this class.  They can be passed back
    * and handled by the Fragment.
     */

    //Fetches the JSON data that populates the ROSTER page

    public class fetchRoster extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.i("GENERATE ROSTER", "HIT");
            String query = leagueResourceRequestBuilder(ROSTER);
            //REMOVE THIS QUERY!!!!!!!!!!!!
            String not_query = BASE_URL + "players;player_keys=331.p.9265/stats;type=week;week=current" + JSON_FORMAT;
            OAuthRequest request = new OAuthRequest(Verb.GET, not_query);
            mService.signRequest(mAccessToken, request);
            Response response = request.send();

            if (responseCodeHandler(response.getCode())) {
                Log.i("GENERATE ROSTER", "COMPLETE");
                mTeam.setPlayers(mParser.parsePlayerList(response.getBody()));
                rosterDelegate.rosterRetrievalComplete(mTeam.getPlayers());
            }
            return null;
        }
    }

    public class fetchStats extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.i("GENERATE STATS", "HIT");
            String query = leagueResourceRequestBuilder(STATS);
            OAuthRequest request = new OAuthRequest(Verb.GET, query);
            mService.signRequest(mAccessToken, request);
            Response response = request.send();

            if (responseCodeHandler(response.getCode())) {
                Log.i("GENERATE STATS", "COMPLETE");
                mTeam.setStats(mParser.parseStatMap(response.getBody()));
                statsDelegate.statRetrievalComplete(mTeam.getStats());
            }
            return null;
        }
    }

    public class testAccessKey extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.i("TEST KEY", "HIT");
            String query = leagueResourceRequestBuilder(USER_DATA);
            OAuthRequest request = new OAuthRequest(Verb.GET, query);
            mService.signRequest(mAccessToken, request);
            Response response = request.send();

            if (responseCodeHandler(response.getCode())) {
                Log.i("TEST KEY", "COMPLETE");
                delegate.keyRetrievalComplete(true);
            } else {
                new refreshAccessToken().execute();
            }
            return null;
        }
    }

    public class fetchMatchups extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.i("MATCHUPS", "HIT");
            String query = leagueResourceRequestBuilder(MATCHUPS);
            OAuthRequest request = new OAuthRequest(Verb.GET, query);
            mService.signRequest(mAccessToken, request);
            Response response = request.send();

            if (responseCodeHandler(response.getCode())) {
                Log.i("MATCHUPS", "COMPLETE");
                matchupDelegate.matchupRetrievalComplete(mParser.parseMatchupData(response.getBody()));
            } else {
                new refreshAccessToken().execute();
            }
            return null;
        }
    }

    private boolean responseCodeHandler(int responseCode) {
        if (responseCode == 200) {
            return true;
        } else {
            new refreshAccessToken().execute();
            return false;
        }
    }

    public interface keyRetrievalCompleteListener {
        void keyRetrievalComplete(boolean result);
    }

    public interface matchupRetrievalCompleteListener {
        void matchupRetrievalComplete(ArrayList<MatchupObject> result);
    }

    public interface rosterRetrievalCompleteListener {
        void rosterRetrievalComplete(ArrayList<Player> result);
    }

    public interface statRetrievalCompleteListener {
        void statRetrievalComplete(ArrayList<Stats> result);
    }




    private String ExtractSession(String rawdata) {
        String temp = rawdata;
        temp = temp.substring(temp.indexOf("session_handle"));
        temp = temp.substring(temp.indexOf("=") + 1, temp.indexOf("&"));

        return temp;
    }

    private void storeTokenData(String access_token, String access_secret, String session) {
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putString(TOKEN, access_token);
        edit.putString(SECRET, access_secret);
        edit.putString(SESSION, session);

        edit.commit();
    }

    private String extractGUID(String responseBody) {
        String GUID = "";
        try {
            JSONObject ob = new JSONObject(responseBody);
            JSONObject users = ob.getJSONObject("fantasy_content").getJSONObject("users");
            if ((Integer)users.get("count") == 1) {
                GUID = users.getJSONObject("0").getJSONArray("user").getJSONObject(0).getString("guid");
            }
            Log.i("GUID", GUID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return GUID;
    }

    public String leagueResourceRequestBuilder(String command) {
        String query = "";
        //TeamKey Format:    {game_key}.l.{league_id}.t.{team_id}
        //LeagueKey Format:  {game_key}.l.{league_id}
        //GameKey Format:    {game_code} or {game_id} game_code = nfl
        //PlayerKey Format:  {game_key}.p.{player_id}

        if (command.equals(USER_DATA)) {
            query = BASE_URL + USER + "/" + GAMES;
        } else if (command.equals(USER_GAMES)) {
            //TeamKey Format:  {game_key}.l.{league_id}.t.{team_id}
            query = BASE_URL + USER + "/" + GAMES + ";" + GAME_KEYS + mGameKey + "/" + LEAGUES;
        } else if (command.equals(USER_TEAMS)) {
            query = BASE_URL + USER + "/" + GAMES + ";" + GAME_KEYS + mGameKey + "/" + TEAMS;
        } else if (command.equals(ROSTER)) {
            //String testString = BASE_URL + TEAM + "/" + "331.l.106320.t.1" + "/" + "roster/players/stats;type=week;week=6" + JSON_FORMAT;
            query = BASE_URL + TEAM + "/" + mTeamKey + "/" + ROSTER + "/" + PLAYERS + "/" + STATS + ";type=week;week=current";
        } else if (command.equals(TEAM_STATS)) {
            //String testString = BASE_URL + LEAGUE + "/331.l.106320/settings" + JSON_FORMAT;
            query = BASE_URL + TEAM + "/" + mTeamKey + "/" + STATS + ";" + "type=week;week=current";
        } else if (command.equals(STATS)) {
            query = BASE_URL + LEAGUE + "/" + mLeagueKey + "/" + SETTINGS;
        } else if (command.equals(MATCHUPS)) {
            query = BASE_URL + LEAGUE + "/" + mLeagueKey + "/" + SCOREBOARD;
        }
        query = query + JSON_FORMAT;

        return query;
    }


}
