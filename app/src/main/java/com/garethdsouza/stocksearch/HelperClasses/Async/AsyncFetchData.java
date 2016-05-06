package com.garethdsouza.stocksearch.HelperClasses.Async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.garethdsouza.stocksearch.HelperClasses.FavoriteItemModel;
import com.garethdsouza.stocksearch.HelperClasses.NewsItemModel;
import com.garethdsouza.stocksearch.HomeActivity;
import com.garethdsouza.stocksearch.TabFragments.CurrentFragment;
import com.garethdsouza.stocksearch.TabFragments.NewsFragment;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by garethdsouza on 4/15/16.
 */

public class AsyncFetchData extends AsyncTask<String,String,String> {
    String functionType;
    String stockSymbol;
    Object callingClass;
    public AsyncFetchData(Object callingClass,String stockSymbol,String functionType){
        this.callingClass = callingClass;
        this.stockSymbol =stockSymbol;
        this.functionType = functionType;
    }

    @Override
    protected String doInBackground(String...params) {
        OkHttpClient client = new OkHttpClient();
        String URL="";

        switch (functionType){
            case "Favorites":
                URL = "http://csci571ss.appspot.com/stock.php?action=stockInfo&param="+stockSymbol;
                break;
            case "Current":
                URL = "http://csci571ss.appspot.com/stock.php?action=stockInfo&param="+stockSymbol;
                break;
            case "News":
                //URL = "http://csci571ss.appspot.com/stocks.php?action=stockNews&param="+stockSymbol;
                URL = "http://stockportal-env.us-west-1.elasticbeanstalk.com/homework8.php?news="+stockSymbol;
                break;
            case "Hist":
                break;
            default:
                break;
        }

        Request request = new Request.Builder()
                .url(URL)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public FavoriteItemModel makeFavorites(JSONObject stockData){


        FavoriteItemModel myFav = null;
        try {
            if (stockData != null) {
                myFav = new FavoriteItemModel(stockData.getString("Symbol"),stockData.getString("Name"),stockData.getString("LastPrice"),stockData.getString("Change"),stockData.getString("MarketCap"));
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return myFav;

    }



    @Override
    protected void onPostExecute(String data) {
        if (data != null) {
            try {
                switch (functionType){

                    case "Favorites":
                        JSONObject object1 = new JSONObject(data);
                        HomeActivity homeActivityFavorites = (HomeActivity)callingClass;
                        homeActivityFavorites.onLoadFavoriteSucess(makeFavorites(object1));
                        break;
                    case "Current":
                        JSONObject object2 = new JSONObject(data);
                        CurrentFragment currentFragment =(CurrentFragment)callingClass;
                        currentFragment.DisplayData(object2);
                        break;
                    case "News":
                        JSONObject object3 = new JSONObject(data);
                        String newsHeaderTemp;
                        String newsUrlTemp;
                        String newsDescriptionTemp;
                        String newsPublisherTemp;
                        String newsPublishDateTemp;
                        ArrayList<NewsItemModel> newsItemModels = new ArrayList<>();

                        JSONObject temp = new JSONObject(object3.getJSONObject("d").toString());
                        JSONArray newsData = temp.getJSONArray("results");
                        if (newsData != null) {
                            //TODO
                            for(int i =0;i<=6;i++)
                            {
                                newsHeaderTemp = newsData.getJSONObject(i).getString("Title");
                                newsDescriptionTemp = newsData.getJSONObject(i).getString("Description");
                                newsPublisherTemp = newsData.getJSONObject(i).getString("Source");
                                newsPublishDateTemp = newsData.getJSONObject(i).getString("Date");
                                newsUrlTemp = newsData.getJSONObject(i).getString("Url");
                                newsItemModels.add(new NewsItemModel(newsHeaderTemp,newsDescriptionTemp,newsPublisherTemp,newsPublishDateTemp,newsUrlTemp));
                            }
                        }

                        NewsFragment newsFragment = (NewsFragment)callingClass;
                        newsFragment.DisplayData(newsItemModels);


                        break;
                    case "Hist":
                        break;
                    default:
                        break;
                }


            } catch (JSONException e) {
                Log.i("CurrentFragment", "JSON Failed");
            }
        }
    }
}
