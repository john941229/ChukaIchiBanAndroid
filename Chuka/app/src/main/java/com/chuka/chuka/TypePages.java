package com.chuka.chuka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by cheng on 2016/10/31.
 */

public class TypePages extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_page);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String typeName = "";
        int type = intent.getIntExtra(MainPage.EXTRA_SEARCH,0);

        if(type == 0)
                typeName = "菜肴";
        if(type == 1)
                typeName = "主食";
        if(type == 2)
                typeName = "甜点";
        if(type == 3)
                typeName = "汤类";

        TextView layout = (TextView) findViewById(R.id.display_message);
        layout.setText(typeName);
        layout.setTextSize(40);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
