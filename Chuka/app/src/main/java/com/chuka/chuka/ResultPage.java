package com.chuka.chuka;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by cheng on 2016/11/9.
 */

public class ResultPage extends AppCompatActivity{

    public String searchName = "";
    private int resultNum=0;
    public String strResult = "";

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.result_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        searchName = intent.getStringExtra(TypePages.EXTRA_SEARCHING);


        TextView layout = (TextView) findViewById(R.id.title_search);
        layout.setText(searchName);

        ScrollView resultView = (ScrollView)findViewById(R.id.search_resultspage);



        getRequest(searchName);
        if(strResult=="")
            Log.d("Err",strResult);

        nextValue(strResult);

    }
    private void getRequest(String searchTag) {
        final Request request = new Request.Builder()
                .url("http://115.159.71.53:3000/tag?tag="+searchTag)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                strResult =  response.body().string();
            }
        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Response response = null;
//                try {
//                    response = client.newCall(request).execute();
//                    if (response.isSuccessful()) {
//                        Log.i("WY","打印GET响应的数据：" + response.body().string());
//                    } else {
//                        throw new IOException("Unexpected code " + response);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    private void nextValue(String JSON) {
        try{
            JSONArray jsonObjs = new JSONObject(JSON).getJSONArray("results");
            String s = "";
            resultNum = jsonObjs.length();
            for(int i=0;i<jsonObjs.length();i++){
                JSONObject jsonObj = ((JSONObject)jsonObjs.opt(i));
                int id = jsonObj.getInt("id");
                String name = jsonObj.getString("name");
                JSONArray material = jsonObj.getJSONArray("material");
                String imageUrl = jsonObj.getString("imageUrl");

                s += id+name+material+imageUrl+"...\n";
                Log.d("1",s);
            }

        }catch (JSONException ex){
            System.out.println("JSONS error\n");
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
