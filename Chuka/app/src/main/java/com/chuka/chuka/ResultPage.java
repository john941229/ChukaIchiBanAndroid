package com.chuka.chuka;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by cheng on 2016/11/9.
 */

public class ResultPage extends AppCompatActivity{


    private LinearLayout resultView;
    public String searchName = "";
    private int resultNum=0;
    public String strResult = "";
    OkHttpClient client = new OkHttpClient();
    List<Map<String, Object>> mData;

    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);}

        Toolbar myToolbar = (Toolbar) findViewById(R.id.result_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        searchName = intent.getStringExtra(TypePages.EXTRA_SEARCHING);


        TextView layout = (TextView) findViewById(R.id.title_search);
        layout.setText(searchName);


        getRequest(searchName);
        while (strResult == ""){
        }
        System.out.println(strResult);

        ListView listview = (ListView)findViewById(R.id.search_resultspage);

        mData = getData(strResult);

        final SimpleAdapter adapter = new SimpleAdapter(this,mData,R.layout.search_result_list,
                new String[]{"imageUrl","name","material","id"},
                new int[]{R.id.search_img,R.id.search_name,R.id.s_material,R.id.s_id});
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Log.d("1",mData.get(arg2).get("id").toString());
            }
        });

    }


    private List<Map<String, Object>> getData(String JSON) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

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

                Map<String, Object> map = new HashMap<String, Object>();

                bitmap = getHttpBitmap(imageUrl);
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(bitmap);
                //imageView = R.drawable.type0pic0;


                map.put("imageUrl",imageView);//?????????????????????????

                map.put("name",name);
                map.put("material",material);
                map.put("id",id);

                list.add(map);

            }
            Log.d("1",s);
        }catch (JSONException ex){
            System.out.println("JSONS error\n");
            ex.printStackTrace();
        }

        return list;
    }

    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap = null;
        try{
            myFileURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)myFileURL.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }


//    protected void onListItemClick(AdapterView<?> arg0,View arg1, int arg2,long arg3){
//        Log.d("id:",(String)mData.get(arg2).get("id"));
//    }

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
        }
        );

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
