package com.chuka.chuka;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cheng on 2016/12/21.
 */

public class SpPage extends AppCompatActivity{
    int sid;
    OkHttpClient spClient = new OkHttpClient();
    String strResult = "";
    List<Map<String, Object>> mData;
    int resultNum;
    public Context context;
    public LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = SpPage.this;
        inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);


        setContentView(R.layout.sp_page);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        TextView spTitle = (TextView)findViewById(R.id.title_sp_name);
        spTitle.setText(intent.getStringExtra("sname"));
        ImageView spImage = (ImageView)findViewById(R.id.sp_pic);
        spImage.setImageDrawable(getResources().getDrawable(intent.getIntExtra("SpPic",0)));
        sid = intent.getIntExtra("sid",0);

        //Log.d("1","12313"+sid);
        getRequest(sid);
    }

    Handler handler = new Handler();

    public void solveData()
    {
        getData(strResult);
    }
    void getData(String JSON){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        Log.d("d",JSON);

        try{
            JSONArray jsonObjs = new JSONObject(JSON).getJSONArray("item");
            String s = "";
            LinearLayout spLayout = (LinearLayout) findViewById(R.id.sp_layout);
            resultNum = jsonObjs.length();
            for(int i=0;i<resultNum;i++){
                JSONObject jsonObj = ((JSONObject)jsonObjs.opt(i));

               // Map<String, Object> map = new HashMap<String, Object>();
                View spItemView = inflater.inflate(R.layout.sp_item,null);
                TextView spItemTitle = (TextView)spItemView.findViewById(R.id.sp_item_title);
                spItemTitle.setText(jsonObj.getString("itemname"));

                TextView spItemText = (TextView)spItemView.findViewById(R.id.sp_item_text);
                spItemText.setText(jsonObj.getString("descripe"));

                spLayout.addView(spItemView);
            }
        }catch (JSONException ex){
            System.out.println("JSONS error\n");
            ex.printStackTrace();
        }

    }


    private void getRequest(int spid){
        final Request request = new Request.Builder()
                .url("http://115.159.71.53:3000/sp?sp="+spid)
                .build();

        spClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                strResult = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        solveData();
                    }
                });
            }
        });
    }

}
