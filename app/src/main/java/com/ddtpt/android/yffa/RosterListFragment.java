package com.ddtpt.android.yffa;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by e228596 on 10/12/2014.
 */
public class RosterListFragment extends ListFragment implements ImportantYahooStuff.rosterRetrievalCompleteListener {
    League mLeague;
    ImportantYahooStuff mYahooStuff;
    ArrayList<Player> mPlayers;
    RosterListAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlayers = new ArrayList<Player>();

        mYahooStuff = ImportantYahooStuff.get(getActivity());
        mYahooStuff.rosterDelegate = this;

        mAdapter = new RosterListAdapter();
        setListAdapter(mAdapter);

        mYahooStuff.new fetchRoster().execute();
    }

    public static RosterListFragment newInstance() {
        RosterListFragment fragment = new RosterListFragment();
        return fragment;
    }

    @Override
    public void rosterRetrievalComplete(ArrayList<Player> players) {
        mPlayers.clear();
        mPlayers.addAll(players);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private class RosterListAdapter extends ArrayAdapter<Player> {
        public RosterListAdapter() {
            super(getActivity(), 0, mPlayers);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.test_roster_player_list_item, null);
            }
            Player p = getItem(position);
            TextView PlayerNameTextView = (TextView) convertView.findViewById(R.id.playerName);
            //TextView PlayerNumberTextView = (TextView) convertView.findViewById(R.id.playerNumber);
            TextView PlayerPositionTextView = (TextView) convertView.findViewById(R.id.playerPosition);
            //TextView TeamScoreTextView = (TextView) convertView.findViewById(R.id.teamScore);
            TextView PlayerTeamTextView = (TextView) convertView.findViewById(R.id.playerTeam);
            //TextView PlayerProjectionTextView = (TextView) convertView.findViewById(R.id.playerProjection);
            //TextView PlayerAverageTextView = (TextView) convertView.findViewById(R.id.playerAverage);
            TextView PlayerScoreTextView = (TextView) convertView.findViewById(R.id.playerScore);

            PlayerNameTextView.setText(p.getFullName());
            //PlayerNumberTextView.setText(p.getUniformNumber());
            PlayerPositionTextView.setText(p.getDisplayPosition());
            //TeamScoreTextView.setText(p.getTeamScore());
            PlayerTeamTextView.setText(p.getEditorialTeamFullName());
            //PlayerProjectionTextView.setText(p.getProjPoints());
            //PlayerAverageTextView.setText(p.getAveragePoints());
            PlayerScoreTextView.setText(p.getPlayerTotal().toString());

            return convertView;
        }

    }
}
