package com.garethdsouza.stocksearch.HelperClasses;

import java.util.Date;

/**
 * Created by garethdsouza on 4/15/16.
 */
public class NewsItemModel {
    String newsHeader;
    String newsDescription;
    String newsPublisher;
    String newsPublishDate;
    String newsUrl;

    public NewsItemModel(String newsHeader,String newsDescription,String newsPublisher,String newsPublishDate,String url){
        this.newsHeader = "<a href=&quot" +url+ "&quot>" +newsHeader+ "</a>";
        this.newsDescription=newsDescription;
        this.newsPublisher = "Publisher : "+newsPublisher;
        String months[] = {"","January", "February", "March", "April" ,"May", "June", "July", "August", "September", "October", "November","December"};

        String year,day,month,articleTime;
        year = newsPublishDate.substring(0,4);
        month = newsPublishDate.substring(5,7);
        day = newsPublishDate.substring(8, 10);
        articleTime = newsPublishDate.substring(11,19);
        int monthInt = Integer.parseInt(month);
        String monthName = months[monthInt];



        this.newsPublishDate ="Date : "+day+" "+monthName+" "+year+", "+articleTime;
        this.newsUrl = url;
    }
}
