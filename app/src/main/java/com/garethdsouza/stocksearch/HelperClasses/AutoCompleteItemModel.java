package com.garethdsouza.stocksearch.HelperClasses;

/**
 * Created by garethdsouza on 5/3/16.
 */
public class AutoCompleteItemModel {
    String StockSymbol;
    String StockName;
    String StockMarket;

    public AutoCompleteItemModel(String stockSymbol,String stockName,String stockMarket){
        this.StockSymbol = stockSymbol;
        this.StockName = stockName;
        this.StockMarket = stockMarket;
    }
}
