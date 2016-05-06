package com.garethdsouza.stocksearch.TabFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.garethdsouza.stocksearch.HelperClasses.Async.AsyncFetchData;
import com.garethdsouza.stocksearch.HelperClasses.NewsItemModel;
import com.garethdsouza.stocksearch.HelperClasses.NewsListViewAdapter;
import com.garethdsouza.stocksearch.R;

import java.util.ArrayList;

/**
 * Created by garethdsouza on 4/11/16.
 */
public class NewsFragment extends Fragment {
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_news, container, false);

        listView = (ListView) v.findViewById(R.id.list);
        String searchTerm = getArguments().getString("SearchTerm");

        AsyncFetchData task = new AsyncFetchData(this,searchTerm,"News");
        task.execute();


        return v;
    }

    public void DisplayData(ArrayList<NewsItemModel> object){
        listView.setAdapter(new NewsListViewAdapter(getActivity(), object));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tempText = (TextView) view.findViewById(R.id.newsURL);
                Log.v("NEWS", tempText.getText().toString());
                //startActivity(browserIntent);
                Uri uri = Uri.parse(tempText.getText().toString()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}


