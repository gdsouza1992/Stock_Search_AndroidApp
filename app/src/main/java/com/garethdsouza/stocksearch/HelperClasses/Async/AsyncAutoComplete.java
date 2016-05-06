package com.garethdsouza.stocksearch.HelperClasses.Async;

import android.os.AsyncTask;

import com.garethdsouza.stocksearch.HelperClasses.AutoCompleteItemModel;
import com.garethdsouza.stocksearch.HomeActivity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by garethdsouza on 5/3/16.
 */
public class AsyncAutoComplete extends AsyncTask<String,String,String> {

    String searchTerm;
    Object callingClass;

    public AsyncAutoComplete(Object callingClass,String searchTerm){
        this.callingClass = callingClass;
        this.searchTerm = searchTerm;
    }

    @Override
    protected String doInBackground(String...params){
    OkHttpClient client = new OkHttpClient();
        String URL = "";
        URL = "http://csci571ss.appspot.com/php/stock.php?action=lookup&param="+searchTerm;

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

    public ArrayList<AutoCompleteItemModel> makeAutocompleteStrings(JSONArray suggestions){
        ArrayList<AutoCompleteItemModel> autocompleteSuggestionsList = new ArrayList<>();
        try {
            for (int i=0;i<suggestions.length();i++){
                autocompleteSuggestionsList.add(new AutoCompleteItemModel(suggestions.getJSONObject(i).getString("Symbol"),suggestions.getJSONObject(i).getString("Name"),suggestions.getJSONObject(i).getString("Exchange")));
                //autocompleteSuggestionsList.add(suggestions.getJSONObject(i).getString("Symbol"));
            }
        }
        catch (Exception e){
            //autocompleteSuggestionsList.add("Something went wrong");
        }
        return autocompleteSuggestionsList;
    }

    @Override
    protected void onPostExecute(String data) {
        if (data != null) {
            try {

                JSONArray object0 = new JSONArray(data);
                HomeActivity homeActivity = (HomeActivity)callingClass;
                homeActivity.onLoadAutoCompleteSuccess(makeAutocompleteStrings(object0));

            } catch (Exception e) {
            }
        }
    }



}
