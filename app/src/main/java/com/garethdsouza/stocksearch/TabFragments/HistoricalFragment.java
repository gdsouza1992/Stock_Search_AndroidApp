package com.garethdsouza.stocksearch.TabFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.garethdsouza.stocksearch.R;

/**
 * Created by garethdsouza on 4/11/16.
 */
public class HistoricalFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstance){

        View v = inflater.inflate(R.layout.fragment_historical, container, false);
        WebView wv = (WebView) v.findViewById(R.id.hist_webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new HistWebClient());
        String searchTerm = getArguments().getString("SearchTerm");

        wv.loadUrl("http://csci571ss.appspot.com/histindex.html?symbol="+searchTerm);


        return v;
        //return inflater.inflate(R.layout.fragment_historical, container, false);
    }


    private class HistWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

    }

}

