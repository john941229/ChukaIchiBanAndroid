package com.chuka.chuka;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Math.min;


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
    int[] imageCollection = new int[]{
            R.drawable.collection_false,
            R.drawable.collection_true
    };
    LoadingDialog dialog;

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
        searchName = intent.getStringExtra("searchName");

        String route = "";
        int route_type = intent.getIntExtra("route_type",MainPage.ROUTE_TYPE_ALL);
        final Data app = (Data) getApplication();
        String userId = app.getUserId();

        switch (route_type){
            case MainPage.ROUTE_TYPE_TAG:
                route = "tag?tag="+searchName;
                break;
            case MainPage.ROUTE_TYPE_SEARCH:
                route = "search?name="+searchName;
                break;
            case MainPage.ROUTE_TYPE_COLLCETION:
                route = "collection?userId="+userId;
                break;
            default:
                route = "all";
                searchName = "全部菜谱";
                break;
        }

        //标题
        TextView layout = (TextView) findViewById(R.id.title_search);
        layout.setText(searchName);
        dialog=new LoadingDialog(mContext,"玩命加载中...");
//显示Dialog
        dialog.show();

        getRequest(route);//请求数据


    }
    Handler handler = new Handler();

    public  void solveData() {

        ListView listview = (ListView)findViewById(R.id.search_resultspage);

        mData = getData(strResult);
        if(mData == null) {
            Dialog alertDialog = new AlertDialog.Builder(this).
                    setTitle("提示").
                    setMessage("没有有关\""+searchName+"\"的内容").
                    create();
            alertDialog.show();
            dialog.close();

            return;
        }

        //设置adapter数据格式内容
        final SimpleAdapter adapter = new SimpleAdapter(this,mData,R.layout.search_result_list,
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

        listview.setAdapter(adapter);
        //list item单击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long arg3) {

                Intent intent = new Intent(ResultPage.this,DetailPage.class);
                intent.putStringArrayListExtra("materialList",(ArrayList<String>) mData.get(position).get("materialList"));
                intent.putExtra("description",mData.get(position).get("description").toString());
                intent.putExtra("id",(int)mData.get(position).get("idNumber"));
                intent.putExtra("name",mData.get(position).get("name").toString());
                intent.putExtra("imgUrl",mData.get(position).get("imgUrl").toString());
                startActivity(intent);

            }
        });

        //item 长按事件
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //popupWindow调用
                showPopupWindow(view,(int)mData.get(position).get("idNumber"),
                        mData.get(position).get("name").toString(),
                        (Bitmap)mData.get(position).get("bitmap"),
                        (List)mData.get(position).get("materialList"),mData.get(position));
                return true;
            }
        });
    }


    private void showPopupWindow(View view,int idNumber,String name, final Bitmap bitmap, List mList, Map<String, Object> metaData){
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.pop_window,null);

        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        final Map<String, Object> mData = metaData;
        final int id = idNumber;

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

        //是否已收藏

        final boolean isCollection;
        boolean flag = false;
        final Data app = (Data)getApplication();
        if(app.isLogined()){
            try {
                flag = getCollectioned(id);
            }
            catch (IOException e){
            }
        }
        Log.d("flag",flag?"true":"false");
        isCollection=flag;

        final ImageButton buttonCollection = (ImageButton) contentView.findViewById(R.id.button_collection);
        if(isCollection){
            buttonCollection.setImageResource(imageCollection[1]);
        }else{
            buttonCollection.setImageResource(imageCollection[0]);
        }
        buttonCollection.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean flag = false;
                final Data app = (Data) getApplication();
                if (app.isLogined()){
                        buttonCollection.setImageResource(imageCollection[(isCollection) ? 0 : 1]);
                    try {
                        setCollection(id);
                    }
                    catch (IOException e) {
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "您未登录,请先登录!", (Toast.LENGTH_SHORT)).show();
                }
            }
        });



        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //popupWindow居中显示
        popupWindow.showAtLocation(view,Gravity.CENTER,0,0);
        popupWindow.showAsDropDown(view);

        Button goDetailButton = (Button)contentView.findViewById(R.id.go_detail);
        goDetailButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ResultPage.this,DetailPage.class);
                intent.putStringArrayListExtra("materialList",(ArrayList<String>) mData.get("materialList"));
                intent.putExtra("description",mData.get("description").toString());
                intent.putExtra("id",(int)mData.get("idNumber"));
                intent.putExtra("name",mData.get("name").toString());
                intent.putExtra("imgUrl",mData.get("imgUrl").toString());

                startActivity(intent);
            }
        });


    }


    private List<Map<String, Object>> getData(String JSON) {
        //处理返回的JSON串
        List<Map<String, Object>> list = null;

        try{
            JSONArray jsonObjs = new JSONObject(JSON).getJSONArray("results");
            String s = "";
            resultNum = min(jsonObjs.length(),40);
            if (resultNum == 0)
                return list;
            else
                list = new ArrayList<Map<String, Object>>();

            for(int i=0;i<min(jsonObjs.length(),20);i++){
                JSONObject jsonObj = ((JSONObject)jsonObjs.opt(i));
                int id = jsonObj.getInt("idNumber");
                String name = jsonObj.getString("name");
                JSONArray material = jsonObj.getJSONArray("material");
                String imageUrl = jsonObj.getString("imgUrl");
                String description = jsonObj.getString("description");

                //s += id+name+material+imageUrl+"...\n";

                Map<String, Object> map = new HashMap<String, Object>();

                //图片下载
                bitmap = getHttpBitmap(imageUrl);
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

            Log.d("1",""+jsonObjs.length());
        }catch (JSONException ex){
            System.out.println("JSONS error\n");
            ex.printStackTrace();
        }
        dialog.close();
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


    private void getRequest(String route) {
        final Request request = new Request.Builder()
                .url("http://115.159.71.53:3000/"+route)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                strResult =  response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        solveData();
                    }
                });
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

    public boolean getCollectioned(int id) throws IOException{
        final Data app = (Data) getApplication();
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        String URL = "http://115.159.71.53:3000/users/hasStared?id="+id+"&userId="+app.getUserId();
        final Request request = new Request.Builder()
                .url(URL)
                .build();
        Call call = client.newCall(request);
        Response execute = call.execute();
        String output = execute.body().string();
        Log.d("getCollection",output+""+output.length());
        return (output.length() == 4);

    }
    public void setCollection(int id) throws IOException{
        final Data app = (Data) getApplication();
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        String URL = "http://115.159.71.53:3000/cusine/star?id="+id+"&userId="+app.getUserId();

        final Request request = new Request.Builder()
                .url(URL)
                .build();
        Call call = client.newCall(request);
        Response execute = call.execute();
        Log.d("setCollection",execute.body().string());

    }

}
