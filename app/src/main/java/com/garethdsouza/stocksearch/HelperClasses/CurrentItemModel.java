package com.garethdsouza.stocksearch.HelperClasses;

/**
 * Created by garethdsouza on 4/13/16.
 */
public class CurrentItemModel {
    String detailHeader;
    String detailValue;
    int detailImage;
    //String dataValue;

    public CurrentItemModel(String detailHeader,String detailValue,int detailImage){
        this.detailHeader = detailHeader;
        this.detailValue = detailValue;
        this.detailImage = detailImage;
    }
}
