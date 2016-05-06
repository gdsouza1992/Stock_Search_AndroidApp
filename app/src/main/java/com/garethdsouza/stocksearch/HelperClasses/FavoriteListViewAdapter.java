package com.garethdsouza.stocksearch.HelperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.garethdsouza.stocksearch.R;

import java.util.ArrayList;

/**
 * Created by garethdsouza on 5/1/16.
 */
public class FavoriteListViewAdapter extends ArrayAdapter<FavoriteItemModel>{

    ArrayList<FavoriteItemModel> arrayAdapter;
    public FavoriteListViewAdapter(Context context,ArrayList<FavoriteItemModel> data){
        super(context,R.layout.favorite_listrow_item,data);
        arrayAdapter = data;
    }


    public void remove(int position){
        super.remove(arrayAdapter.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.favorite_listrow_item, parent, false);

        FavoriteItemModel favoriteItemModel = getItem(position);
        TextView favoriteSymbol = (TextView) customView.findViewById(R.id.favoriteSymbol);
        TextView favoriteCompanyName = (TextView) customView.findViewById(R.id.favoriteCompanyName);
        TextView favoriteStockPrice = (TextView) customView.findViewById(R.id.favoriteStockPrice);
        TextView favoriteChangePercent = (TextView) customView.findViewById(R.id.favoriteChangePercent);
        TextView favoriteMarketCap = (TextView) customView.findViewById(R.id.favoriteMarketCap);


        favoriteSymbol.setText(favoriteItemModel.symbol);
        favoriteCompanyName.setText(favoriteItemModel.companyName);
        favoriteStockPrice.setText(favoriteItemModel.price);
        favoriteChangePercent.setText(favoriteItemModel.changePercent);
        if(favoriteItemModel.color == -1){
            favoriteChangePercent.setBackgroundResource(R.color.colorRedDown);
        }else if(favoriteItemModel.color == 1){
            favoriteChangePercent.setBackgroundResource(R.color.colorGreenUp);
        }else{
            favoriteChangePercent.setBackgroundResource(android.R.color.transparent);
        }
        favoriteMarketCap.setText(favoriteItemModel.marketCap);

        return customView;


    }
}
