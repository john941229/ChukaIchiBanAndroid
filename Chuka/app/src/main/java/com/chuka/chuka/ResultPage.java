package com.chuka.chuka;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
    private Context mContext = null;
    OkHttpClient client = new OkHttpClient();
    List<Map<String, Object>> mData;

    public Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);

        mContext = this;//定义弹出窗口的parent为本view

        //强制设置主线程进行网络请求
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);}

        Toolbar myToolbar = (Toolbar) findViewById(R.id.result_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        searchName = intent.getStringExtra(TypePages.EXTRA_SEARCHING);

        //标题
        TextView layout = (TextView) findViewById(R.id.title_search);
        layout.setText(searchName);


        getRequest(searchName);//请求数据

        while (strResult == ""){
        }//等待数据
        System.out.println(strResult);

        ListView listview = (ListView)findViewById(R.id.search_resultspage);

        mData = getData(strResult);
        //设置adapter数据格式内容
        final SimpleAdapter adapter = new SimpleAdapter(this,mData,R.layout.search_result_list,
                new String[]{"imgUrl","name","material","idNumber"},
                new int[]{R.id.search_img,R.id.search_name,R.id.s_material,R.id.s_id});
        //为adapter重写图片显示
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if(view instanceof ImageView  && data instanceof  Bitmap){
                    ImageView iv = (ImageView) view;
                    iv.setImageBitmap((Bitmap) data);
                    return true;
                }else
                return false;
            }
        });

        listview.setAdapter(adapter);
        //list item单击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long arg3) {
                Log.d("1",mData.get(position).get("materialList").toString());

            }
        });

        //item 长按事件
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //popupWindow调用
                showPopupWindow(view,
                        mData.get(position).get("name").toString(),
                        (Bitmap)mData.get(position).get("imgUrl"),
                        (List)mData.get(position).get("materialList"));
                return false;
            }
        });

    }

    private void showPopupWindow(View view,String name,Bitmap bitmap,List mList){
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.pop_window,null);

        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        //设置弹框文字图片
        TextView tV = (TextView)contentView.findViewById(R.id.popup_title);
        tV.setText(name);
        ImageView imageView = (ImageView)contentView.findViewById(R.id.pop_window_image);
        imageView.setImageBitmap(bitmap);
        TextView mLi = (TextView)contentView.findViewById(R.id.pop_material);
        mLi.setText(mList.toString().replaceAll("[\\[\\]]",""));


        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //popupWindow居中显示
        popupWindow.showAtLocation(view,Gravity.CENTER,0,0);
        popupWindow.showAsDropDown(view);


    }


    private List<Map<String, Object>> getData(String JSON) {
        //处理返回的JSON串
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        try{
            JSONArray jsonObjs = new JSONObject(JSON).getJSONArray("results");
            String s = "";
            resultNum = jsonObjs.length();
            for(int i=0;i<jsonObjs.length();i++){
                JSONObject jsonObj = ((JSONObject)jsonObjs.opt(i));
                int id = jsonObj.getInt("idNumber");
                String name = jsonObj.getString("name");
                JSONArray material = jsonObj.getJSONArray("material");
                String imageUrl = jsonObj.getString("imgUrl");

                //s += id+name+material+imageUrl+"...\n";

                Map<String, Object> map = new HashMap<String, Object>();

                //图片下载
                bitmap = getHttpBitmap(imageUrl);
                //imageView = R.drawable.type0pic0;

                map.put("imgUrl",bitmap);//?????????????????????????

                map.put("name",name);

                ArrayList<String> materialList = new ArrayList<String>();
                for(int k =0;k<material.length();k++)
                    materialList.add(material.getString(k));

                map.put("material",materialList.toString().replaceAll("[\\[\\]]",""));
                map.put("materialList",materialList);
                map.put("idNumber",id);

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
        //从网络获取图片
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
