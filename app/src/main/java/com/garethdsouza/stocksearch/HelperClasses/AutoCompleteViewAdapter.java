package com.garethdsouza.stocksearch.HelperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.garethdsouza.stocksearch.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by garethdsouza on 5/3/16.
 */
public class AutoCompleteViewAdapter extends BaseAdapter implements Filterable{
    Context context;
    ArrayList<AutoCompleteItemModel> data;
    int autocomplete_item;
    private static LayoutInflater inflater = null;

    public AutoCompleteViewAdapter(Context context,ArrayList<AutoCompleteItemModel> data){
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override public int getCount() { return data.size(); }
    @Override public AutoCompleteItemModel getItem(int index) { return data.get(index); }
    @Override public long getItemId(int position) { return position; }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.autocomplete_item, null);

        TextView textHeader = (TextView) vi.findViewById(R.id.autoCompleteTextLabel1);
        textHeader.setText(data.get(position).StockSymbol);
        TextView textDescription = (TextView) vi.findViewById(R.id.autoCompleteTextLabel2);
        textDescription.setText(data.get(position).StockName +"("+data.get(position).StockMarket+")");
        return vi;
    }
    @Override public Filter getFilter() {
        Filter filter = new Filter() {
            @Override protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = data;
                    filterResults.count = data.size();
                } return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

}