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

    public final static String EXTRA_SEARCH_0="com.chuka.chuka.SEARCH0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button typeButton0 = (Button) findViewById(R.id.mainPageTypeButton_0) ;


    }
    public void searchType0(View view){

    }
}

