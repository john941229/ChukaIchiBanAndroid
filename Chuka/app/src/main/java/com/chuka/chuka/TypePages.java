package com.chuka.chuka;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by cheng on 2016/10/31.
 */

public class TypePages extends AppCompatActivity{

    private final Button[] typeButton = new Button[20];
    private int buttonNum = 0;
    private int buttonLine = 0;
    private int buttonExp = 0;

    @Override


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_page);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String typeName  = intent.getStringExtra(MainPage.EXTRA_TYPE);

        TextView layout = (TextView) findViewById(R.id.title_type_name);
        layout.setText(typeName);
    //  layout.setTextSize(40);


        TableLayout layout2 = (TableLayout)findViewById(R.id.types);
        //layout2.setOrientation(LinearLayout.HORIZONTAL);
//        setContentView(layout2);

        /////////////////////////

        buttonNum = 5;//获取按钮数

        /////////////////////////

        buttonLine = (buttonNum-1) / 4 +1;
        buttonExp = buttonNum % 4;
        int num = buttonNum;
        int line4 = 4;
        for(int i=0;i<buttonLine;i++)
        {
            if(i == buttonLine -1)
                line4 = buttonExp;

            TableRow tableRow = new TableRow(this);

            for(int j=0;j<line4 ;j++)
            {
                typeButton[num] = new Button(this);
                typeButton[num].setText("123123");
                typeButton[num].setBackgroundColor(Color.parseColor("#00000000"));
                Drawable top = getResources().getDrawable(R.drawable.oval);
                typeButton[num].setCompoundDrawablesWithIntrinsicBounds(null,top,null,null);

                tableRow.addView(typeButton[num]);
                num = num+1;
            }
            layout2.addView(tableRow);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
