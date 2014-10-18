package com.ddtpt.android.yffa;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by e228596 on 10/16/2014.
 */
public class StatValueListFragment extends ListFragment implements ImportantYahooStuff.statRetrievalCompleteListener{
    ImportantYahooStuff mYahooStuff;
    ArrayList<Stats> mStats;
    StatValueListAdapter mAdapter;

    public static StatValueListFragment newInstance() {
        StatValueListFragment fragment = new StatValueListFragment();
        return fragment;
    }

    @Override
    public void statRetrievalComplete(ArrayList<Stats> stats) {
        mStats.clear();
        mStats.addAll(stats);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStats = new ArrayList<Stats>();

        mYahooStuff = ImportantYahooStuff.get(getActivity());
        mYahooStuff.statsDelegate = this;


        mAdapter = new StatValueListAdapter();
        setListAdapter(mAdapter);

        mYahooStuff.new fetchStats().execute();

    }

    private class StatValueListAdapter extends ArrayAdapter<Stats> {
        public StatValueListAdapter()
        {
            super(getActivity(), 0, mStats);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.stat_list_item, null);
            }
            Stats s = getItem(position);

            TextView StatNameText = (TextView) convertView.findViewById(R.id.stat_list_stat_name);

            TextView StatValueText = (TextView) convertView.findViewById(R.id.stat_list_stat_value);


            StatNameText.setText(s.getName());
            StatValueText.setText(String.valueOf(s.getMod()));

            return convertView;
        }

    }
}
