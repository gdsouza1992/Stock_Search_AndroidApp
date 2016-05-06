package com.garethdsouza.stocksearch.HelperClasses;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.garethdsouza.stocksearch.R;

import java.util.ArrayList;

/**
 * Created by garethdsouza on 4/15/16.
 */
public class NewsListViewAdapter extends BaseAdapter{
    Context context;
    ArrayList<NewsItemModel> data;
    private static LayoutInflater inflater = null;

    public NewsListViewAdapter(Context context, ArrayList<NewsItemModel> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.news_listrow_item, null);


        TextView textHeader = (TextView) vi.findViewById(R.id.newsHeader);
        textHeader.setText(Html.fromHtml(data.get(position).newsHeader));
        TextView textDescription = (TextView) vi.findViewById(R.id.newsDescription);
        textDescription.setText(data.get(position).newsDescription);
        TextView textPublisher = (TextView) vi.findViewById(R.id.newsPublisher);
        textPublisher.setText(data.get(position).newsPublisher);
        TextView textDatePublished = (TextView) vi.findViewById(R.id.newsPublishDate);
        textDatePublished.setText(data.get(position).newsPublishDate);
        TextView textURL= (TextView) vi.findViewById(R.id.newsURL);
        textURL.setText(data.get(position).newsUrl);

        return vi;
    }
}
