package com.garethdsouza.stocksearch.TabFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.garethdsouza.stocksearch.HelperClasses.Async.AsyncFetchData;
import com.garethdsouza.stocksearch.HelperClasses.CurrentItemModel;
import com.garethdsouza.stocksearch.HelperClasses.CurrentListViewAdapter;
import com.garethdsouza.stocksearch.HelperClasses.FBShareItemModel;
import com.garethdsouza.stocksearch.HelperClasses.Helper;
import com.garethdsouza.stocksearch.HelperClasses.TouchImageView;
import com.garethdsouza.stocksearch.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.text.DecimalFormat;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by garethdsouza on 4/11/16.
 */
public class CurrentFragment extends Fragment {
    ListView listView;
    ImageView imageView;
    ImageView chartImageView;
    ScaleGestureDetector scaleGestureDetector;
    Matrix matrix=new Matrix();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_current, container, false);
        listView = (ListView) v.findViewById(R.id.list);

        String searchTerm = getArguments().getString("SearchTerm");
        AsyncFetchData taskData = new AsyncFetchData(this,searchTerm,"Current");
        taskData.execute();

        return v;
    }

    public ArrayList<CurrentItemModel> makeList(JSONObject stockData,Context context){
        String name,symbol,lastprice,change,changepercent;
        FBShareItemModel fbShareItemModel;
        ArrayList<CurrentItemModel> currentItemModels = new ArrayList<>();
        try {
            if (stockData != null) {
                name = stockData.getString("Name");
                currentItemModels.add(new CurrentItemModel("NAME",name,0));

                symbol = stockData.getString("Symbol");
                currentItemModels.add(new CurrentItemModel("SYMBOL",symbol,0));

                double lastpriceD = Helper.roundTwo(stockData.getString("LastPrice"));
                lastprice = String.valueOf(lastpriceD);
                currentItemModels.add(new CurrentItemModel("LAST PRICE",String.valueOf(lastpriceD),0));

                double changeD = Helper.roundTwo(stockData.getString("Change"));
                double changePercentD = Helper.roundTwo(stockData.getString("ChangePercent"));


                if(changeD > 0) currentItemModels.add(new CurrentItemModel("CHANGE",String.valueOf(changeD)+" (+"+String.valueOf(changePercentD)+"%) ",1));
                else if(changeD < 0) currentItemModels.add(new CurrentItemModel("CHANGE",String.valueOf(changeD)+" ("+String.valueOf(changePercentD)+"%) ",-1));
                else currentItemModels.add(new CurrentItemModel("CHANGE",String.valueOf(changeD)+" ("+String.valueOf(changePercentD)+"%) ",0));



                currentItemModels.add(new CurrentItemModel("TIMESTAMP", Helper.makeCurrentString(stockData.getString("Timestamp")),0));

                currentItemModels.add(new CurrentItemModel("MARKET CAP",Helper.BillionConvertNoTitle(stockData.getString("MarketCap")),0));

                currentItemModels.add(new CurrentItemModel("VOLUME",stockData.getString("Volume"),0));

                double changeYTDD = Helper.roundTwo(stockData.getString("ChangeYTD"));
                double changeYTDPercentD = Helper.roundTwo(stockData.getString("ChangePercentYTD"));
                if(changeYTDPercentD > 0) currentItemModels.add(new CurrentItemModel("CHANGE YTD(CHANGE YTD %)",String.valueOf(changeYTDD)+" (+"+String.valueOf(changeYTDPercentD)+"%)",1));
                else if(changeYTDPercentD < 0 )currentItemModels.add(new CurrentItemModel("CHANGE YTD(CHANGE YTD %)",String.valueOf(changeYTDD)+" ("+String.valueOf(changeYTDPercentD)+"%)",-1));
                else         currentItemModels.add(new CurrentItemModel("CHANGE YTD(CHANGE YTD %)",String.valueOf(changeYTDD)+" ("+String.valueOf(changeYTDPercentD)+"%)",0));


                //changepercent = stockData.getString("ChangePercent");

                double highD = Helper.roundTwo(stockData.getString("High"));
                double lowD = Helper.roundTwo(stockData.getString("Low"));
                double OpenD = Helper.roundTwo(stockData.getString("Open"));

                currentItemModels.add(new CurrentItemModel("High",String.valueOf(highD),0));
                currentItemModels.add(new CurrentItemModel("Low",String.valueOf(lowD),0));
                currentItemModels.add(new CurrentItemModel("Open",String.valueOf(OpenD),0));
                currentItemModels.add(new CurrentItemModel("Today's Stock Activity","",1));

                fbShareItemModel = new FBShareItemModel(name,symbol,lastprice);
                makeFBData(fbShareItemModel,context);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            currentItemModels.add(new CurrentItemModel("Error", "Something went wrong",0));
        }
        return currentItemModels;

    }


    public void makeFBData(FBShareItemModel fbShareItemModel,Context context){
        SharedPreferences preferences = context.getSharedPreferences("stocksearch", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("FBTitle",fbShareItemModel.Title);
        editor.putString("FBImage",fbShareItemModel.ImageUrl);
        editor.putString("FBURL",fbShareItemModel.Url);
        editor.putString("FBDescription",fbShareItemModel.Description);
        editor.apply();
    }


    public void DisplayImage(Bitmap image){
        imageView.setImageBitmap(image);
    }

    public void DisplayData(JSONObject object){
        String isError="";
        try {
            isError = object.getString("Status");
        }catch (Exception e){

        }
        if(!isError.equals("SUCCESS")){
            Toast.makeText(getActivity(),"ERROR in Stock Data",Toast.LENGTH_SHORT);
            TextView errorText = (TextView) getActivity().findViewById(R.id.errorMessage);
            errorText.setText("This is not a stock");
            errorText.setVisibility(View.VISIBLE);
        }else{
            TextView errorText = (TextView) getActivity().findViewById(R.id.errorMessage);
            errorText.setVisibility(View.GONE);
            listView.setAdapter(new CurrentListViewAdapter(getActivity(), makeList(object, getActivity())));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tempText = (TextView) view.findViewById(R.id.stockDetailData);
                    String tempTextStr = tempText.getText().toString();
                    if (tempTextStr.equals("")) {
                        ImageView selectedChart = (ImageView) view.findViewById(R.id.chartData);
                        showImageAlert(getActivity(), selectedChart.getDrawable());
                    }
                }
            });
        }

    }


    public void showImageAlert(Context context,Drawable imageData) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow()
                .setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setContentView(R.layout.imagepopup_layout);
        ImageView image = (ImageView) dialog.findViewById(R.id.imagePopupView);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);
        Bitmap bmpImageData = ((BitmapDrawable) imageData).getBitmap();
        image.setImageBitmap(bmpImageData);
        dialog.show();
        mAttacher.update();
    }
}
