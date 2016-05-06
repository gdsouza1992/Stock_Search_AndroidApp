package com.garethdsouza.stocksearch.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.garethdsouza.stocksearch.HelperClasses.Async.AsyncFetchData;

/**
 * Created by garethdsouza on 5/3/16.
 */
public class Helper {

    public static double roundTwo(double value){
        return Math.round(value * 100.0) / 100.0;
    }
    public static double roundTwo(String value){
        return Math.round(Double.parseDouble(value) * 100.0) / 100.0;
    }
    public static String getFavorites(Context context){
        SharedPreferences preferences = context.getSharedPreferences("stocksearch", Context.MODE_PRIVATE);
        String existingFavorites = preferences.getString("FavoriteStocks", "");
        return existingFavorites;
    }

    public static void removeFavorite(String stockSymbol,Context context){
        SharedPreferences preferences = context.getSharedPreferences("stocksearch", Context.MODE_PRIVATE);
        String existingFavorites = preferences.getString("FavoriteStocks", "");
        String updatedFavorites = existingFavorites.replace(stockSymbol + " ", "");
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FavoriteStocks",updatedFavorites);
        editor.apply();
    }
    public static void removeAllFavorite(Context context){
        SharedPreferences preferences = context.getSharedPreferences("stocksearch", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FavoriteStocks", "");
        editor.apply();
    }
    public static void addFavorite(String stockSymbol,Context context){
        SharedPreferences preferences = context.getSharedPreferences("stocksearch", Context.MODE_PRIVATE);
        String existingFavorites = preferences.getString("FavoriteStocks", "");
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FavoriteStocks",stockSymbol+" "+existingFavorites);
        editor.apply();
    }

    public static String removeFavoriteAt(int position,Context context){
        String Favorites = Helper.getFavorites(context);
        String FavoritesArray[] = Favorites.split(" ");
        String RemoveValue = FavoritesArray[position];
        Toast.makeText(context, "Removed" + RemoveValue, Toast.LENGTH_SHORT).show();
        return  RemoveValue;
    }


    public static void loadFavoriteList(Context context){
        String[] myFavorites = Helper.getFavorites(context).split(" ");
        for (int i =0 ;i<myFavorites.length;i++){
            AsyncFetchData taskData = new AsyncFetchData(context,myFavorites[i],"Favorites");
            taskData.execute();
        }
    }

    public static String BillionConvert(String marketCap){
        double marketCapDouble = Double.parseDouble(marketCap);
        if(marketCapDouble > 1000000000)
            return ("Market Cap : "+(Helper.roundTwo(marketCapDouble/1000000000))+ " Billion");
        else
            return ("Market Cap : "+(Helper.roundTwo(marketCapDouble/1000000))+ " Million");
    }

    public static String BillionConvertNoTitle(String marketCap){
        double marketCapDouble = Double.parseDouble(marketCap);
        if(marketCapDouble > 1000000000)
            return ((Helper.roundTwo(marketCapDouble/1000000000))+ " Billion");
        else
            return ((Helper.roundTwo(marketCapDouble/1000000))+ " Million");
    }

    public static String makeCurrentString(String recieved){
        String recievedArray[] = recieved.split(" ");
        String returnValue;
        if(Integer.parseInt(recievedArray[2])<10){
            returnValue = "0"+recievedArray[2];
        }else{
            returnValue = recievedArray[2];
        }
        returnValue +=" "+recievedArray[1]+" "+recievedArray[5]+", "+recievedArray[3];
        return returnValue;
    }
}
