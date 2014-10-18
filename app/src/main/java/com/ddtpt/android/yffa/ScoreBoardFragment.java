package com.ddtpt.android.yffa;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by e228596 on 10/17/2014.
 */
public class ScoreBoardFragment extends ListFragment implements ImportantYahooStuff.matchupRetrievalCompleteListener {
    ImportantYahooStuff mYahooStuff;
    ArrayList<MatchupObject> mMatchups;
    MatchupListAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMatchups = new ArrayList<MatchupObject>();
        mYahooStuff = ImportantYahooStuff.get(getActivity());
        mYahooStuff.matchupDelegate = this;

        adapter = new MatchupListAdapter();
        setListAdapter(adapter);

        mYahooStuff.new fetchMatchups().execute();
    }

    public static ScoreBoardFragment newInstance() {
        ScoreBoardFragment fragment = new ScoreBoardFragment();
        return fragment;
    }

    private class MatchupListAdapter extends ArrayAdapter<MatchupObject> {
        ImageLoader loader;
        public MatchupListAdapter()
        {
            super(getActivity(), 0, mMatchups);
            loader = ImageLoader.getInstance();
            loader.init(ImageLoaderConfiguration.createDefault(getContext()));
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.matchup_list_item, null);
            }
            MatchupObject m = getItem(position);




            TextView homeTeamOwnerText = (TextView) convertView.findViewById(R.id.home_team_owner);
            TextView awayTeamOwnerText = (TextView) convertView.findViewById(R.id.away_team_owner);
            TextView homeTeamNameText = (TextView) convertView.findViewById(R.id.home_team_name);
            TextView awayTeamNameText = (TextView) convertView.findViewById(R.id.away_team_name);
            TextView homeTeamScoreText = (TextView) convertView.findViewById(R.id.home_team_score);
            TextView awayTeamScoreText = (TextView) convertView.findViewById(R.id.away_team_score);

            ImageView homeTeamIcon = (ImageView) convertView.findViewById(R.id.home_team_icon);
            ImageView awayTeamIcon = (ImageView) convertView.findViewById(R.id.away_team_icon);

            loader.displayImage(m.getHomeIcon(), homeTeamIcon);
            loader.displayImage(m.getAwayIcon(), awayTeamIcon);

            homeTeamOwnerText.setText(m.getHomeOwner());
            awayTeamOwnerText.setText(m.getAwayOwner());
            homeTeamNameText.setText(m.getHomeTeam());
            awayTeamNameText.setText(m.getAwayTeam());
            homeTeamScoreText.setText(m.getHomeScore().toString());
            awayTeamScoreText.setText(m.getAwayScore().toString());

            return convertView;
        }

    }

    public void matchupRetrievalComplete(ArrayList<MatchupObject> matchups) {
        mMatchups.clear();
        mMatchups.addAll(matchups);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}
