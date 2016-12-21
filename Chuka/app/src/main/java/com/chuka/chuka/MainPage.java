package com.chuka.chuka;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.concurrent.Delayed;


public class MainPage extends AppCompatActivity {

    public final static String EXTRA_TYPE_NUM="com.chuka.chuka.TYPE_NUM";
//    public final static String EXTRA_SEARCH_1="com.chuka.chuka.SEARCH1";
//    public final static String EXTRA_SEARCH_2="com.chuka.chuka.SEARCH2";
//    public final static String EXTRA_SEARCH_3="com.chuka.chuka.SEARCH3";
    int type = 0;
    public final static String EXTRA_TYPE="com.chuka.chuka.TYPE";
    String Type = "";
    ImageView spView = null;
    int spNum = 0;
    int [] Spid = {101,202,303};
    int [] spDrawableId = new int[10];
    String [] spName = new String[10];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        spHandler.postDelayed(runGetSp,1000);

        //  getSP();
    }

    Handler spHandler = new Handler();

    Runnable runGetSp = new Runnable() {
        @Override
        public void run() {

            getSP();
        }
    };

    public void searchType0(View view){
        Intent intent = new Intent(this,TypePages.class);
        //type = 0;

        Button typeButton0 = (Button) findViewById(R.id.mainPageTypeButton_0) ;
        Type = typeButton0.getText().toString();

        intent.putExtra(EXTRA_TYPE,Type);
        intent.putExtra(EXTRA_TYPE_NUM,0);
        startActivity(intent);
    }
    public void searchType1(View view){
        Intent intent = new Intent(this,TypePages.class);
//        type = 1;

        Button typeButton1 = (Button) findViewById(R.id.mainPageTypeButton_1) ;
        Type = typeButton1.getText().toString();

        intent.putExtra(EXTRA_TYPE,Type);

        intent.putExtra(EXTRA_TYPE_NUM,1);
        startActivity(intent);
    }
    public void searchType2(View view){
        Intent intent = new Intent(this,TypePages.class);

        Button typeButton2 = (Button) findViewById(R.id.mainPageTypeButton_2) ;
        Type = typeButton2.getText().toString();

        intent.putExtra(EXTRA_TYPE,Type);
        intent.putExtra(EXTRA_TYPE_NUM,2);
        startActivity(intent);
    }
    public void searchType3(View view){
        Intent intent = new Intent(this,TypePages.class);

        Button typeButton3 = (Button) findViewById(R.id.mainPageTypeButton_3) ;
        Type = typeButton3.getText().toString();

        intent.putExtra(EXTRA_TYPE,Type);
        intent.putExtra(EXTRA_TYPE_NUM,3);
        startActivity(intent);
    }

    public void getSP(){
        spNum = 3;
        LinearLayout spViewLayout = (LinearLayout)findViewById(R.id.spview);
        {
            spDrawableId[0] = R.drawable.sp0;
            spDrawableId[1] = R.drawable.sp1;
            spDrawableId[2] = R.drawable.sp2;

            spName[0]="冬日暖心菜";
            spName[1]="苦涩酸食物";
            spName[2]="微波炉菜谱";

        }


        for(int i = 0;i<spNum;i++)
        {
            spView = new ImageView(this);
            spView.setImageDrawable(getResources().getDrawable(spDrawableId[i]));
            spView.setTag(R.id.tag_sp_sid,Spid[i]);//传入sid
            spView.setTag(R.id.tag_sp_draw,spDrawableId[i]);//传入图片id
            spView.setTag(R.id.tag_sp_name,i);
            spView.setOnClickListener(spListener);

            spViewLayout.addView(spView);
        }
    }

    ImageView.OnClickListener spListener = new ImageView.OnClickListener(){
        public void onClick(View v){

            Intent intent = new Intent(MainPage.this,SpPage.class);
            intent.putExtra("SpPic",(Integer)v.getTag(R.id.tag_sp_draw));
            intent.putExtra("sid",(Integer)v.getTag(R.id.tag_sp_sid));
            intent.putExtra("sname",spName[(Integer)v.getTag(R.id.tag_sp_name)]);

            System.out.println(v.getTag(R.id.tag_sp_sid));
            System.out.println(v.getTag(R.id.tag_sp_draw));
            MainPage.this.onPause();
            startActivity(intent);
        }
    };
}

