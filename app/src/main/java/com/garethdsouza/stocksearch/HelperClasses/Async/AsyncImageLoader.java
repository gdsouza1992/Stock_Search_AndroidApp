package com.garethdsouza.stocksearch.HelperClasses.Async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.garethdsouza.stocksearch.TabFragments.CurrentFragment;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by garethdsouza on 4/16/16.
 * As implemented on https://www.learn2crack.com/2014/06/android-load-image-from-internet.html
 */

public class AsyncImageLoader extends AsyncTask<String, String, Bitmap> {
    Bitmap bitmap;
    ImageView img;
    String searchTerm;
    //Object callingClass;

    public AsyncImageLoader(String searchTerm,ImageView imgview){
        //this.callingClass = callingClass;
        this.searchTerm = searchTerm;
        this.img = imgview;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    protected Bitmap doInBackground(String... args) {
        try {
            String url = "http://chart.finance.yahoo.com/t?s="+searchTerm+"&amp;lang=en-US&amp;width=600&amp;height=400";
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap image) {

        if(image != null){
            img.setImageBitmap(image);
            img.getLayoutParams().width = 1200;
            img.getLayoutParams().height = 900;
        }else{
            String error = "Notfound";
        }
    }
}


