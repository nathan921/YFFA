package com.ddtpt.android.yffa;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;


/**
 * Created by e228596 on 10/20/2014.
 */
public class MatchupFragment extends ListFragment {
    ImportantYahooStuff mYahooStuff;
    MatchupDetails mMatchupDetails;

    private static final String TEAM_ID1 = "ID1";
    private static final String TEAM_ID2 = "ID2";

    public MatchupFragment() {

    }

    public static MatchupFragment newInstance(MatchupObject object) {
        Bundle args = new Bundle();
        args.putString(TEAM_ID1, object.getHomeTeamId());
        args.putString(TEAM_ID2, object.getAwayTeamId());

        MatchupFragment fragment = new MatchupFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String homeTeam = getArguments().getString(TEAM_ID1);
        String awayTeam = getArguments().getString(TEAM_ID2);

        mYahooStuff = ImportantYahooStuff.get(getActivity());

        MatchupDetailsAdapter adapter = new MatchupDetailsAdapter();
        setListAdapter(adapter);

        mYahooStuff.new fetchMatchups().execute();
    }

    private class MatchupDetailsAdapter extends ArrayAdapter<MatchupDetails> {

        public MatchupDetailsAdapter() {
            super(getActivity(), 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.matchup_detail_list_item, null);
            }

            return convertView;
        }
    }

    private class getMatchupDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Token accessToken = mYahooStuff.getAccToken();
            OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.login.yahoo.com/oauth/v2/get_token");  //Update with data specific to the teams requested.
            mYahooStuff.getService().signRequest(accessToken, request);
            Response response = request.send();

            if (response.getCode() == 200) {
                return response.getBody();
            } else {
                return "";
            }
        }
        @Override
        protected void onPostExecute(String json) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode root = mapper.readTree(json);
            } catch (Exception e) {
                Log.e("MATCHUPFRAGMENT", e.toString());
            }
        }
    }

}
