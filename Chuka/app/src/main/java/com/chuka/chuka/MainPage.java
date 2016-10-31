package com.chuka.chuka;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainPage extends AppCompatActivity {

    public final static String EXTRA_SEARCH="com.chuka.chuka.SEARCH";
    public final static String EXTRA_SEARCH_1="com.chuka.chuka.SEARCH1";
    public final static String EXTRA_SEARCH_2="com.chuka.chuka.SEARCH2";
    public final static String EXTRA_SEARCH_3="com.chuka.chuka.SEARCH3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

//        Button typeButton0 = (Button) findViewById(R.id.mainPageTypeButton_0) ;
//        Button typeButton1 = (Button) findViewById(R.id.mainPageTypeButton_1) ;
//        Button typeButton2 = (Button) findViewById(R.id.mainPageTypeButton_2) ;
//        Button typeButton3 = (Button) findViewById(R.id.mainPageTypeButton_3) ;

    }
    public void searchType0(View view){
        Intent intent = new Intent(this,TypePages.class);
        int type = 0;
        intent.putExtra(EXTRA_SEARCH,type);
        startActivity(intent);
    }
    public void searchType1(View view){
        Intent intent = new Intent(this,TypePages.class);
        int type = 1;
        intent.putExtra(EXTRA_SEARCH,type);
        startActivity(intent);
    }
    public void searchType2(View view){
        Intent intent = new Intent(this,TypePages.class);
        int type = 2;
        intent.putExtra(EXTRA_SEARCH,type);
        startActivity(intent);
    }
    public void searchType3(View view){
        Intent intent = new Intent(this,TypePages.class);
        int type = 3;
        intent.putExtra(EXTRA_SEARCH,type);
        startActivity(intent);
    }
    public void callNavigation(View view){

    }
}

