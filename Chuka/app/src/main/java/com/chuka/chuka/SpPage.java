package com.chuka.chuka;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
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

import static com.chuka.chuka.ResultPage.getHttpBitmap;


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

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);}
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

                ListView spList = (ListView)spItemView.findViewById(R.id.sp_list);
                JSONArray spItem = jsonObj.getJSONArray("list");

                final List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
                for(int j=0;j<spItem.length();j++) {


                    JSONObject spObj = ((JSONObject)spItem.opt(j));
                    int id = spObj.getInt("idNumber");
                    String name = spObj.getString("name");
                    JSONArray material = spObj.getJSONArray("material");
                    String imageUrl = spObj.getString("imgUrl");
                    String description = spObj.getString("description");

                    //s += id+name+material+imageUrl+"...\n";

                    Map<String, Object> map = new HashMap<String, Object>();

                    //图片下载
                    Bitmap bitmap = getHttpBitmap(imageUrl);
                    //imageView = R.drawable.type0pic0;

                    map.put("bitmap",bitmap);
                    map.put("imgUrl",imageUrl);

                    map.put("name",name);
                    map.put("description",description);

                    ArrayList<String> materialList = new ArrayList<String>();
                    for(int k =0;k<material.length();k++)
                        materialList.add(material.getString(k));

                    map.put("material",materialList.toString().replaceAll("[\\[\\]]",""));
                    map.put("materialList",materialList);
                    map.put("idNumber",id);

                    list.add(map);
                }

                final SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.search_result_list,
                        new String[]{"bitmap","name","material","idNumber"},
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

                spList.setAdapter(adapter);
                spList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position,
                                            long arg3) {

                        Intent intent = new Intent(SpPage.this,DetailPage.class);
                        intent.putStringArrayListExtra("materialList",(ArrayList<String>) list.get(position).get("materialList"));
                        intent.putExtra("description",list.get(position).get("description").toString());
                        intent.putExtra("id",(int)list.get(position).get("idNumber"));
                        intent.putExtra("name",list.get(position).get("name").toString());
                        intent.putExtra("imgUrl",list.get(position).get("imgUrl").toString());
                        startActivity(intent);
                    }
                });


                setListViewHeightBasedOnChildren(spList);

                spLayout.addView(spItemView);
            }
        }catch (JSONException ex){
            System.out.println("JSONS error\n");
            ex.printStackTrace();
        }

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
// 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
// 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));
// listView.getDividerHeight()获取子项间分隔符占用的高度
// params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    private void getRequest(int spid){
        final Request request = new Request.Builder()
                .url("http://115.159.71.53:3000/sp?sid="+spid)
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
