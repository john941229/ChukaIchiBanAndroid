package com.chuka.chuka;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Delayed;


public class MainPage extends AppCompatActivity {

    public final static String EXTRA_TYPE_NUM = "com.chuka.chuka.TYPE_NUM";
    //    public final static String EXTRA_SEARCH_1="com.chuka.chuka.SEARCH1";
//    public final static String EXTRA_SEARCH_2="com.chuka.chuka.SEARCH2";
//    public final static String EXTRA_SEARCH_3="com.chuka.chuka.SEARCH3";
    public final static int ROUTE_TYPE_TAG = 0;
    public final static int ROUTE_TYPE_ALL = 1;
    public final static int ROUTE_TYPE_SEARCH = 2;
    public final static int ROUTE_TYPE_COLLCETION = 3;

    int type = 0;
    public final static String EXTRA_TYPE = "com.chuka.chuka.TYPE";
    String Type = "";
    ImageView spView = null;
    int spNum = 0;
    int[] Spid = {101, 102, 103};
    int[] spDrawableId = new int[10];
    String[] spName = new String[10];
    private Context mContext = null;
    private Data app;

    DrawerLayout drawerLayout;
    NavigationView popNavi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        spHandler.postDelayed(runGetSp, 1000);
        mContext = this;

        drawerLayout = (DrawerLayout) findViewById(R.id.main_page);
        popNavi =(NavigationView)findViewById(R.id.pop_navi);

        final LineEditText searchBar = (LineEditText) findViewById(R.id.searchBar);

        searchBar.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchBar.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        searchBar.setOnEditorActionListener(new LineEditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            String search = searchBar.getText().toString();
                            Log.d("search--", search);
                            Intent intent = new Intent(MainPage.this, ResultPage.class);

                            intent.putExtra("route_type", MainPage.ROUTE_TYPE_SEARCH);
                            intent.putExtra("searchName", search);
                            startActivity(intent);

                            return true;
                        default:
                            return true;
                    }
                }
                return false;
            }
        });
        ImageButton callNavi = (ImageButton)findViewById(R.id.mainPageMenu);
        callNavi.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        ImageButton callCollecttion = (ImageButton)findViewById(R.id.collection);
        callCollecttion.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v){
                app = (Data) getApplication();
                boolean isLogined = app.isLogined();
                if(isLogined){
                    Intent intent = new Intent(MainPage.this, ResultPage.class);
                    intent.putExtra("route_type",MainPage.ROUTE_TYPE_COLLCETION);
                    intent.putExtra("searchName","收藏");
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(), "您未登录,请先登录!", (Toast.LENGTH_SHORT)).show();
                    Intent intent = new Intent(MainPage.this, LoginPage.class);
                    MainPage.this.startActivityForResult(intent, 1);
                }


            }
        });
        popNavi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                if(item.getItemId() == R.id.item_one){
                    app = (Data) getApplication();
                    boolean isLogined = app.isLogined();
                    if(isLogined){
                        Intent intent = new Intent(MainPage.this, ResultPage.class);
                        intent.putExtra("route_type",MainPage.ROUTE_TYPE_COLLCETION);
                        intent.putExtra("searchName","收藏");
                        startActivity(intent);

                    }else{
                        Toast.makeText(getApplicationContext(), "您未登录,请先登录!", (Toast.LENGTH_SHORT)).show();
                        Intent intent = new Intent(MainPage.this, LoginPage.class);
                        MainPage.this.startActivityForResult(intent, 1);
                    }
                }
                return true;
            }
        });



        //  getSP();
    }

    Handler spHandler = new Handler();

    Runnable runGetSp = new Runnable() {
        @Override
        public void run() {

            getSP();
        }
    };

    public void callAll(View view) {
        Intent intent = new Intent(this, ResultPage.class);

        intent.putExtra("route_type", MainPage.ROUTE_TYPE_ALL);
        intent.putExtra("searchName", "全部菜谱");
        startActivity(intent);
    }


    public void searchType0(View view) {
        Intent intent = new Intent(this, TypePages.class);
        //type = 0;

        Button typeButton0 = (Button) findViewById(R.id.mainPageTypeButton_0);
        Type = typeButton0.getText().toString();

        intent.putExtra(EXTRA_TYPE, Type);
        intent.putExtra(EXTRA_TYPE_NUM, 0);
        startActivity(intent);
    }

    public void searchType1(View view) {
        Intent intent = new Intent(this, TypePages.class);
//        type = 1;

        Button typeButton1 = (Button) findViewById(R.id.mainPageTypeButton_1);
        Type = typeButton1.getText().toString();

        intent.putExtra(EXTRA_TYPE, Type);

        intent.putExtra(EXTRA_TYPE_NUM, 1);
        startActivity(intent);
    }

    public void searchType2(View view) {
        Intent intent = new Intent(this, TypePages.class);

        Button typeButton2 = (Button) findViewById(R.id.mainPageTypeButton_2);
        Type = typeButton2.getText().toString();

        intent.putExtra(EXTRA_TYPE, Type);
        intent.putExtra(EXTRA_TYPE_NUM, 2);
        startActivity(intent);
    }

    public void searchType3(View view) {
        Intent intent = new Intent(this, TypePages.class);

        Button typeButton3 = (Button) findViewById(R.id.mainPageTypeButton_3);
        Type = typeButton3.getText().toString();

        intent.putExtra(EXTRA_TYPE, Type);
        intent.putExtra(EXTRA_TYPE_NUM, 3);
        startActivity(intent);
    }

    public void getSP() {
        spNum = 3;
        LinearLayout spViewLayout = (LinearLayout) findViewById(R.id.spview);
        {
            spDrawableId[0] = R.drawable.sp0;
            spDrawableId[1] = R.drawable.sp1;
            spDrawableId[2] = R.drawable.sp2;

            spName[0] = "冬日暖心菜";
            spName[1] = "苦涩酸食物";
            spName[2] = "微波炉菜谱";

        }


        for (int i = 0; i < spNum; i++) {
            spView = new ImageView(this);
            spView.setImageDrawable(getResources().getDrawable(spDrawableId[i]));
            spView.setTag(R.id.tag_sp_sid, Spid[i]);//传入sid
            spView.setTag(R.id.tag_sp_draw, spDrawableId[i]);//传入图片id
            spView.setTag(R.id.tag_sp_name, i);
            spView.setOnClickListener(spListener);

            spViewLayout.addView(spView);
        }
    }

    ImageView.OnClickListener spListener = new ImageView.OnClickListener() {
        public void onClick(View v) {

            Intent intent = new Intent(MainPage.this, SpPage.class);
            intent.putExtra("SpPic", (Integer) v.getTag(R.id.tag_sp_draw));
            intent.putExtra("sid", (Integer) v.getTag(R.id.tag_sp_sid));
            intent.putExtra("sname", spName[(Integer) v.getTag(R.id.tag_sp_name)]);

            System.out.println(v.getTag(R.id.tag_sp_sid));
            System.out.println(v.getTag(R.id.tag_sp_draw));
            MainPage.this.onPause();
            startActivity(intent);

        }
    };


    public void login(View v) {
        final Data app = (Data) getApplication();
        if (!app.isLogined()) {
            //未登录动作
            Intent intent = new Intent(MainPage.this, LoginPage.class);
            MainPage.this.startActivityForResult(intent, 1);
        } else {
            new AlertDialog.Builder(MainPage.this).setTitle("退出登录")
                    .setMessage("是否退出登录?")
                    .setPositiveButton("确认",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog,int which){
                            app.setLogout();
                            View headerView = popNavi.getHeaderView(0);
                            final TextView naviUserName = (TextView) headerView.findViewById(R.id.navi_username);
                            naviUserName.setText("未登录,点击登录");
                        }
                    }).setNegativeButton("取消",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog,int which) {
                            Log.i("canceled", "");
                        }
                }).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                super.onActivityResult(requestCode, resultCode, data);
                final Data app = (Data) getApplication();

                Bundle bundle = data.getExtras();

                View headerView = popNavi.getHeaderView(0);


                final TextView naviUserName = (TextView) headerView.findViewById(R.id.navi_username);
                naviUserName.setText(app.getUserName()+",点击退出");

                break;
            case RESULT_CANCELED:
                Toast.makeText(getApplicationContext(), "登录失败", (Toast.LENGTH_SHORT)).show();
                break;
            default:
                break;
        }
    }


}

