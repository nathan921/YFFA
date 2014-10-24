package com.ddtpt.android.yffa;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by e228596 on 10/17/2014.
 */
public class ScoreBoardFragment extends ListFragment implements ImportantYahooStuff.matchupRetrievalCompleteListener {
    ImportantYahooStuff mYahooStuff;
    ArrayList<HashMap<String, Object>> mMatchups;
    private Callbacks mCallbacks;
    League mLeague;
    MatchupListAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mYahooStuff = ImportantYahooStuff.get(getActivity());
        mYahooStuff.matchupDelegate = this;
        mLeague = mYahooStuff.getLeague();
        mMatchups = new ArrayList<HashMap<String, Object>>();

        adapter = new MatchupListAdapter();
        setListAdapter(adapter);

        mYahooStuff.new fetchMatchups().execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public interface Callbacks {
        void onMatchupSelected(String team1, String team2);
    }

     public static ScoreBoardFragment newInstance() {
        ScoreBoardFragment fragment = new ScoreBoardFragment();
        return fragment;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String team1 = mLeague.getTeamById(mLeague.getThisWeeksMatchups().get(2*position)).getTeamId();
        String team2 = mLeague.getTeamById(mLeague.getThisWeeksMatchups().get(2*position + 1)).getTeamId();

        mCallbacks.onMatchupSelected(team1, team2);
    }

    private class MatchupListAdapter extends ArrayAdapter<HashMap<String, Object>> {
        ImageLoader loader;
        public MatchupListAdapter()
        {
            super(getActivity(), 0, mMatchups);
            loader = ImageLoader.getInstance();
            loader.init(ImageLoaderConfiguration.createDefault(getContext()));
        }
        @Override
        public int getCount() {
            return mMatchups.size() / 2;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.matchup_list_item, null);
            }
            if(mMatchups.size() > 0) {
                ArrayList<Integer> mups = mLeague.getThisWeeksMatchups();
                Team home = mLeague.getTeam(mups.get(2 * position) - 1);
                Team away = mLeague.getTeam(mups.get(2 * position + 1) - 1);

                TextView homeTeamOwnerText = (TextView) convertView.findViewById(R.id.home_team_owner);
                TextView awayTeamOwnerText = (TextView) convertView.findViewById(R.id.away_team_owner);
                TextView homeTeamNameText = (TextView) convertView.findViewById(R.id.home_team_name);
                TextView awayTeamNameText = (TextView) convertView.findViewById(R.id.away_team_name);
                TextView homeTeamScoreText = (TextView) convertView.findViewById(R.id.home_team_score);
                TextView awayTeamScoreText = (TextView) convertView.findViewById(R.id.away_team_score);

                ImageView homeTeamIcon = (ImageView) convertView.findViewById(R.id.home_team_icon);
                ImageView awayTeamIcon = (ImageView) convertView.findViewById(R.id.away_team_icon);

                loader.displayImage(home.getTeamLogoUrl(), homeTeamIcon);
                loader.displayImage(away.getTeamLogoUrl(), awayTeamIcon);

                homeTeamOwnerText.setText(home.getOwner());
                awayTeamOwnerText.setText(away.getOwner());
                homeTeamNameText.setText(home.getTeamName());
                awayTeamNameText.setText(away.getTeamName());
                homeTeamScoreText.setText(String.valueOf(home.getPoints()));
                awayTeamScoreText.setText(String.valueOf(away.getPoints()));
            }

            return convertView;
        }

    }

    public void matchupRetrievalComplete(ArrayList<HashMap<String, Object>> matchups) {
        mMatchups.clear();
        mMatchups.addAll(matchups);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
        ArrayList<Integer> whosPlaying = new ArrayList<Integer>();
        if (mLeague.getNumberOfTeams() == 0) {
            for (HashMap<String, Object> m: matchups) {
                whosPlaying.add(Integer.valueOf(m.get("team_id").toString()));
                Team newTeam = new Team(getActivity());
                newTeam.populateTeam(m);
                mLeague.addNewTeam(newTeam);

            }
            mLeague.setThisWeeksMatchups(whosPlaying);
            mLeague.sortTeams();
        }
    }
}
