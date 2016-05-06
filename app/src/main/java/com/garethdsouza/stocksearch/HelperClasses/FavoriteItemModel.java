package com.garethdsouza.stocksearch.HelperClasses;

/**
 * Created by garethdsouza on 5/1/16.
 */
public class FavoriteItemModel {
    public String symbol;
    public String companyName;
    public String price;
    public String changePercent;
    public String marketCap;
    public int color;

    public FavoriteItemModel(String symbol,String companyName,String price,String changePercent,String marketCap){
        this.symbol = symbol;
        this.companyName = companyName;

        if(Double.parseDouble(changePercent) > 0){
            this.changePercent = "+"+Helper.roundTwo(changePercent)+" %";
            this.color = 1;
        }
        else if(Double.parseDouble(changePercent) < 0){
            this.changePercent = Helper.roundTwo(changePercent)+" %";
            this.color = -1;
        } else{
            this.changePercent = Helper.roundTwo(changePercent)+" %";
            this.color =0;
        }

        this.price = "$ "+Helper.roundTwo(price);

        this.marketCap = Helper.BillionConvert(marketCap);


    }
}
