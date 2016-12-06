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

import static android.R.attr.button;

public class MainPage extends AppCompatActivity {

    public final static String EXTRA_TYPE_NUM="com.chuka.chuka.TYPE_NUM";
//    public final static String EXTRA_SEARCH_1="com.chuka.chuka.SEARCH1";
//    public final static String EXTRA_SEARCH_2="com.chuka.chuka.SEARCH2";
//    public final static String EXTRA_SEARCH_3="com.chuka.chuka.SEARCH3";
    int type = 0;
    public final static String EXTRA_TYPE="com.chuka.chuka.TYPE";
    String Type = "";
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

}

