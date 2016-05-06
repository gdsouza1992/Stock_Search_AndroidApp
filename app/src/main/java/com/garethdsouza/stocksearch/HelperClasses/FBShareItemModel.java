package com.garethdsouza.stocksearch.HelperClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by garethdsouza on 5/4/16.
 */
public class FBShareItemModel {
    public String Title;
    public String Description;
    public String Url;
    public String ImageUrl;

    public FBShareItemModel(String name,String symbol,String lastprice){
        this.Title = "Current Stock Price of "+name+" is $"+lastprice;
        this.Description = "Stock Information of "+name+" ("+symbol+")";
        this.Url = "http://dev.markitondemand.com/MODApis/";
        this.ImageUrl = "http://chart.finance.yahoo.com/t?s="+symbol+"&lang=en-US&width=500&height=400";
    }
}
