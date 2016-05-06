package com.garethdsouza.stocksearch.HelperClasses;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.garethdsouza.stocksearch.HelperClasses.Async.AsyncImageLoader;
import com.garethdsouza.stocksearch.R;

import java.util.ArrayList;

/**
 * Created by garethdsouza on 4/12/16.
 */
public class CurrentListViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<CurrentItemModel> data;
    private static LayoutInflater inflater = null;
    String searchTerm;

    public CurrentListViewAdapter(Context context, ArrayList<CurrentItemModel> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            vi = inflater.inflate(R.layout.current_listrow_item, null);


        TextView textHeader = (TextView) vi.findViewById(R.id.stockDetailHeader);
        textHeader.setText(data.get(position).detailHeader);
        TextView textDescription = (TextView) vi.findViewById(R.id.stockDetailData);
        textDescription.setText(data.get(position).detailValue);
        ImageView imageView = (ImageView) vi.findViewById(R.id.stockDetailArrow);
        ImageView imageViewChart = (ImageView) vi.findViewById(R.id.chartData);
        imageViewChart.setImageResource(android.R.color.transparent);



        imageViewChart.setImageResource(android.R.color.transparent);
        imageViewChart.getLayoutParams().height = 0;
        imageViewChart.getLayoutParams().width = 0;

        imageView.setImageResource(android.R.color.transparent);
        imageView.getLayoutParams().height = 0;
        imageView.getLayoutParams().width = 0;



        if(data.get(position).detailHeader.equals("SYMBOL")){
            searchTerm = data.get(position).detailValue.toString();
        }

        if(data.get(position).detailHeader.equals("CHANGE")){
            if(data.get(position).detailImage !=0){

                if(data.get(position).detailImage > 0)
                {
                    imageView.setImageResource(R.drawable.up);
                }else if(data.get(position).detailImage < 0){
                    imageView.setImageResource(R.drawable.down);
                }
            }
            imageView.getLayoutParams().height = 100;
            imageView.getLayoutParams().width = 100;
        }

        if(data.get(position).detailHeader.equals("CHANGE YTD(CHANGE YTD %)")){
            if(data.get(position).detailImage !=0){

                if(data.get(position).detailImage > 0)
                {
                    imageView.setImageResource(R.drawable.up);
                }else if(data.get(position).detailImage < 0){
                    imageView.setImageResource(R.drawable.down);
                }
            }
            imageView.getLayoutParams().height = 100;
            imageView.getLayoutParams().width = 100;

        }

        if(data.get(position).detailValue.equals("")){
            AsyncImageLoader imagetask = new AsyncImageLoader(searchTerm,imageViewChart);
            imagetask.execute();
        }



        return vi;
    }

}
