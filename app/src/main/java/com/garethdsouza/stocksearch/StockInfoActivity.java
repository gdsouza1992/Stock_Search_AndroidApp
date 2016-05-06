package com.garethdsouza.stocksearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.garethdsouza.stocksearch.HelperClasses.FBShareItemModel;
import com.garethdsouza.stocksearch.HelperClasses.Helper;
import com.garethdsouza.stocksearch.HelperClasses.TabPagerAdapter;

/**
 * Created by garethdsouza on 4/11/16.
 */
public class StockInfoActivity extends AppCompatActivity {
    private Menu menu;
    private boolean isFavorite;
    public FBShareItemModel fbShareItemModel;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockinfo);


        Intent intent = getIntent();
        String searchTerm = intent.getStringExtra("SearchTerm");
        String strIsFavorite = intent.getStringExtra("IsFavorite");
        if(strIsFavorite!=null)
        if(strIsFavorite.equals("true")){
            isFavorite = true;
        }

        //setFavorite(searchTerm);

        //Set up the ToolBar(ActionBar)
        Toolbar toolbar = (Toolbar) findViewById(R.id.stockinfo_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(searchTerm);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("CURRENT"));
        tabLayout.addTab(tabLayout.newTab().setText("HISTORICAL"));
        tabLayout.addTab(tabLayout.newTab().setText("NEWS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.stock_pager);
        final TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        adapter.setSearchTerm(searchTerm);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my, menu);
        this.menu = menu;
        if(isFavorite){
            menu.getItem(0).setIcon(R.drawable.star_filled);
            menu.getItem(0).setChecked(isFavorite);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;

            case R.id.action_share:
                // User chose the "Settings" item, show the app settings UI...
                onShareResult();

                Toast toast0 = Toast.makeText(getApplicationContext(), "FB Share Clicked", Toast.LENGTH_SHORT);
                toast0.show();
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                setFavorite();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void onShareResult(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        CallbackManager callbackManager = CallbackManager.Factory.create();
        ShareDialog shareDialog = new ShareDialog(this);

        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getApplicationContext(),"FB Share Posted",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"FB Share Failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"FB Share Failed",Toast.LENGTH_SHORT).show();
            }
        });


        if (shareDialog.canShow(ShareLinkContent.class)) {
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("stocksearch", Context.MODE_PRIVATE);
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(preferences.getString("FBTitle",""))
                    .setContentDescription(preferences.getString("FBDescription", ""))
                    .setContentUrl(Uri.parse(preferences.getString("FBURL", "")))
                    .setImageUrl(Uri.parse(preferences.getString("FBImage", "")))
                    .build();

            shareDialog.show(linkContent);
        }


    }


    public void setFavorite(){
        Intent intent = getIntent();
        String searchTerm = intent.getStringExtra("SearchTerm");
        String message;
        boolean isFav = menu.getItem(0).isChecked();
        //if start is checked, Uncheck it
        if(isFav)
        {
            message = "Removed "+ searchTerm+ " from favorites";
            menu.getItem(0).setIcon(R.drawable.star_empty);
            Helper.removeFavorite(searchTerm,this);
        }
        else{
            message = "Added "+ searchTerm+ " to favorites";
            menu.getItem(0).setIcon(R.drawable.star_filled);
            Helper.addFavorite(searchTerm,this);
        }
        menu.getItem(0).setChecked(!menu.getItem(0).isChecked());
        Toast toast1 = Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT);
        toast1.show();
    }




}
