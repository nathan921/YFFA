package com.ddtpt.android.yffa;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


/**
 * Created by e228596 on 10/20/2014.
 */
public class MatchupFragment extends ListFragment {
    ImportantYahooStuff mYahooStuff;
    MatchupDetails mMatchupDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mYahooStuff = ImportantYahooStuff.get(getActivity());

        MatchupDetailsAdapter adapter = new MatchupDetailsAdapter();
        setListAdapter(adapter);

        mYahooStuff.new fetchMatchups().execute();
    }

    private class MatchupDetailsAdapter extends ArrayAdapter<MatchupDetails> {

        public MatchupDetailsAdapter() {
            super(getActivity(), 0, mMatchupDetails);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.matchup_detail_list_item, null);
            }

            return convertView;
        }
    }

}
