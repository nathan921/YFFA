package com.ddtpt.android.yffa;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by e228596 on 10/20/2014.
 */
public class MatchupFragment extends ListFragment implements Team.onPlayerStatsRetrievedListener{
    ImportantYahooStuff mYahooStuff;
    Team mHomeTeam, mAwayTeam;
    int mTeam1, mTeam2;
    MatchupDetailsAdapter adapter;

    private static final String TEAM_ID1 = "team1";
    private static final String TEAM_ID2 = "team2";

    public static MatchupFragment newInstance(String team_1, String team_2) {
        Bundle args = new Bundle();
        args.putString(TEAM_ID1, team_1);
        args.putString(TEAM_ID2, team_2);

        MatchupFragment fragment = new MatchupFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public void onPlayerStatsRetrieved() {
        //mHomeTeam = mYahooStuff.getLeague().getTeamById(mTeam1);
        //mAwayTeam = mYahooStuff.getLeague().getTeamById(mTeam2);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String homeTeam = getArguments().getString(TEAM_ID1);
        String awayTeam = getArguments().getString(TEAM_ID2);

        mTeam1 = Integer.valueOf(homeTeam);
        mTeam2 = Integer.valueOf(awayTeam);

        mYahooStuff = ImportantYahooStuff.get(getActivity());

        mHomeTeam = mYahooStuff.getLeague().getTeamById(mTeam1);
        mAwayTeam = mYahooStuff.getLeague().getTeamById(mTeam2);

        mHomeTeam.playerStatsDelegate = this;
        mAwayTeam.playerStatsDelegate = this;

        adapter = new MatchupDetailsAdapter();
        setListAdapter(adapter);

        mYahooStuff.getLeague().getTeam(Integer.valueOf(homeTeam)-1).new updateTeamPointData().execute();
        mYahooStuff.getLeague().getTeam(Integer.valueOf(awayTeam)-1).new updateTeamPointData().execute();
    }

    private class MatchupDetailsAdapter extends ArrayAdapter<Double> {

        public MatchupDetailsAdapter() {
            super(getActivity(), 0);
        }

        @Override
        public int getCount() {
            return mHomeTeam.getPlayers().size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.matchup_detail_list_item, null);
            }

            TextView leftPlayerNameText = (TextView) convertView.findViewById(R.id.left_player_name);
            TextView leftPlayerGameDataText = (TextView) convertView.findViewById(R.id.left_game_data);
            TextView leftPlayerPointsText = (TextView) convertView.findViewById(R.id.left_current_points);
            TextView leftPlayerProjectedPointsText = (TextView) convertView.findViewById(R.id.left_projected_points);

            TextView rightPlayerNameText = (TextView) convertView.findViewById(R.id.right_player_name);
            TextView rightPlayerGameDataText = (TextView) convertView.findViewById(R.id.right_game_data);
            TextView rightPlayerPointsText = (TextView) convertView.findViewById(R.id.right_current_points);
            TextView rightPlayerProjectedPointsText = (TextView) convertView.findViewById(R.id.right_projected_points);

            Player left_player = mHomeTeam.getPlayers().get(position);
            Player right_player = mAwayTeam.getPlayers().get(position);

            leftPlayerNameText.setText(left_player.getFullName());
            leftPlayerGameDataText.setText("11AM vs Cards");
            leftPlayerPointsText.setText(left_player.getPlayerTotal());
            leftPlayerProjectedPointsText.setText(left_player.getPlayerProjection());

            rightPlayerNameText.setText(right_player.getFullName());
            rightPlayerGameDataText.setText("2PM vs Giants");
            rightPlayerPointsText.setText(right_player.getPlayerTotal());
            rightPlayerProjectedPointsText.setText(right_player.getPlayerProjection());

            return convertView;
        }
    }

}
