package com.garethdsouza.stocksearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.garethdsouza.stocksearch.HelperClasses.Async.AsyncAutoComplete;
import com.garethdsouza.stocksearch.HelperClasses.AutoCompleteItemModel;
import com.garethdsouza.stocksearch.HelperClasses.AutoCompleteViewAdapter;
import com.garethdsouza.stocksearch.HelperClasses.FavoriteItemModel;
import com.garethdsouza.stocksearch.HelperClasses.FavoriteListViewAdapter;
import com.garethdsouza.stocksearch.HelperClasses.Helper;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class HomeActivity extends AppCompatActivity {

    ArrayList<FavoriteItemModel> myFavoritesArrayList;
    ArrayList<String> suggestionList;
    AutoCompleteTextView autoComplete;

    TimerTask mTimerTask;
    final Handler handler = new Handler();

    Timer timer;


    boolean ispaused;
    boolean needNew;
    private Context mContext;
    private static final int TIME_TO_AUTOMATICALLY_DISMISS_ITEM = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        //Helper.removeAllFavorite(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        timer = new Timer();
        myFavoritesArrayList = new ArrayList<>();

        suggestionList = new ArrayList<String>();
        //SharedPreferencesStub();
        autoComplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteText);
        autoComplete.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                AsyncAutoComplete autoFetchTask = new AsyncAutoComplete(HomeActivity.this, newText);
                autoFetchTask.execute(newText);
            }

        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.v("HELPER FAVS",Helper.getFavorites(this));

        ListView favoriteList = (ListView)findViewById(R.id.favoritelist);
        favoriteList.setAdapter(null);
        Helper.loadFavoriteList(this);
    }


    //Clear The Search field functionality
    public void clearSearchTerm(){
        //Get the Text box
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteText);
        //Set the text box value to empty
        autoCompleteTextView.setText("");
    }

    //Return value present in the search box

    public String getSearchTerm(){
        //Get the TextBox
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteText);
        //Get text in TextBox
        String searchTerm = autoCompleteTextView.getText().toString();
        if(searchTerm.equals("")){
            return "Error";
        }
        else{
            return searchTerm;
        }
    }

    public void setSearchTermText(String term){
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteText);
        autoCompleteTextView.setText(term.toString());
    }



    public void onButtonClick(View v){

        switch (v.getId()){
            case R.id.getQuoteButton:
                Intent I = new Intent(HomeActivity.this,StockInfoActivity.class);
                String searchTerm = getSearchTerm();
                if(searchTerm != "Error"){
                    I.putExtra("SearchTerm",searchTerm);
                    startActivity(I);
                }else{
                    Toast.makeText(getBaseContext(), "Please enter a valid Stock", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.clearButton:
                clearSearchTerm();
                break;
            case R.id.refreshFavorites:
                Toast.makeText(getBaseContext(), "Refreshing List", Toast.LENGTH_SHORT).show();
                Helper.loadFavoriteList(this);
                break;
            case R.id.refreshFavoritesSwitch:
                Switch toggle = (Switch)findViewById(R.id.refreshFavoritesSwitch);

                if(toggle.isChecked()){
                    doTimerTask(this);
                }else{
                    stopTask(this);
                }


//                //timer = new Timer();
//                ispaused = toggle.isChecked();
//                if(needNew){
//                    timer = new Timer();
//                }
//
//                timer.scheduleAtFixedRate(new TimerTask() {
//                    @Override
//                    public void run() {
//                        if (ispaused) {
//                            Log.v("TESTING", "123");
//                        } else {
//                            needNew = true;
//                            Log.v("STOP TESTING","123");
//                            timer.purge();
//                            timer.cancel();
//                        }
//
//                    }
//                }, 0, 1000);







                break;
            default:
                break;
        }
    }


    public void onLoadAutoCompleteSuccess(ArrayList<AutoCompleteItemModel> autoSuggestionsList){

        AutoCompleteViewAdapter adapter = new AutoCompleteViewAdapter(HomeActivity.this,autoSuggestionsList);
        adapter.notifyDataSetChanged();
        autoComplete.setThreshold(3);
        autoComplete.setDropDownHeight(150 * 5);
        autoComplete.setAdapter(adapter);

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedStockTV = (TextView) view.findViewById(R.id.autoCompleteTextLabel1);
                setSearchTermText(selectedStockTV.getText().toString());
            }
        });
    }


    public void onLoadFavoriteSucess(FavoriteItemModel asyncFavoriteListItem){
        myFavoritesArrayList.add(asyncFavoriteListItem);
        ArrayList<FavoriteItemModel> myFavoritesArrayListSorted = new ArrayList<>();
        String [] favoriteListString = Helper.getFavorites(this).split(" ");

        //Log.v("FAVORITES",String.valueOf(favoriteListSize));

        if(myFavoritesArrayList.size() == favoriteListString.length && (!favoriteListString[0].equals(""))){
            String[] myFavorites = Helper.getFavorites(this).split(" ");
            for (int i=myFavorites.length-1;i>=0;i--){
                for (int j=0;j<myFavoritesArrayList.size();j++){
                    FavoriteItemModel temp = (FavoriteItemModel)myFavoritesArrayList.get(j);
                    if(temp.symbol.equals(myFavorites[i])){
                        myFavoritesArrayListSorted.add(temp);
                        myFavoritesArrayList.remove(j);
                    }
                }

            }

            final ListAdapter favoriteAdapter = new FavoriteListViewAdapter (this,myFavoritesArrayListSorted);
            final ListView favoriteListView = (ListView) findViewById(R.id.favoritelist);


            favoriteListView.setAdapter(favoriteAdapter);

            final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                    new SwipeToDismissTouchListener<>(new ListViewAdapter(favoriteListView),
                            new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                                @Override
                                public boolean canDismiss(int position) {
                                    return true;
                                }
                                @Override
                                public void onDismiss(ListViewAdapter view, int position) {
                                    ListView swipedItem = (ListView)findViewById(R.id.favoritelist);
                                    TextView deleteTextItem = (TextView)swipedItem.findViewById(R.id.favoriteSymbol);
                                    Helper.removeFavorite(deleteTextItem.getText().toString(), getApplicationContext());
                                    ((FavoriteListViewAdapter)favoriteAdapter).remove(position);
                                }
                            });

            favoriteListView.setOnTouchListener(touchListener);
            favoriteListView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
            favoriteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (touchListener.existPendingDismisses()) {
                        touchListener.undoPendingDismiss();
                    } else {
                        //Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_SHORT).show();
                        TextView selectedItem = (TextView)view.findViewById(R.id.favoriteSymbol);
                        String selectedSymbol = selectedItem.getText().toString();
                        Intent I = new Intent(HomeActivity.this,StockInfoActivity.class);
                        I.putExtra("SearchTerm",selectedSymbol);
                        I.putExtra("IsFavorite", "true");
                        startActivity(I);
                    }
                }
            });

//                favoriteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                }
//                });
        }

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void doTimerTask(final Context appContext){

        mTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(appContext, "Refreshing List ever 10 seconds", Toast.LENGTH_SHORT).show();

                        Helper.loadFavoriteList(appContext);
                    }
                });
            }};

        // public void schedule (TimerTask task, long delay, long period)
        timer.schedule(mTimerTask,0, 10000);  //

    }

    public void stopTask(Context appContext){

        if(mTimerTask!=null){
            Toast.makeText(appContext, "Auto Refresh Stopped", Toast.LENGTH_SHORT).show();
            mTimerTask.cancel();
        }

    }
}
