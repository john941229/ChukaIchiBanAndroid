package com.chuka.chuka;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

    public final static String EXTRA_SEARCHING="com.chuka.chuka.SEARCHING";
    private final Button[] typeButton = new Button[30];
    private int buttonNum = 0;
    private int buttonLine = 0;
    private int buttonExp = 0;

    public String typeName= "";
    String TAG = "--";

    private Drawable [][] typePics = new Drawable[4][20];

    private int [] typeMenuNum = {8,4,4,4};

    private String[][] buttonText = new String[4][20];

    public void setTypebuttons() {
        typePics[0][0]=getResources().getDrawable(R.drawable.type0pic0);
        typePics[0][1]=getResources().getDrawable(R.drawable.type0pic1);
        typePics[0][2]=getResources().getDrawable(R.drawable.type0pic2);
        typePics[0][3]=getResources().getDrawable(R.drawable.type0pic3);
        typePics[0][4]=getResources().getDrawable(R.drawable.type0pic4);
        typePics[0][5]=getResources().getDrawable(R.drawable.type0pic5);
        typePics[0][6]=getResources().getDrawable(R.drawable.type0pic6);
        typePics[0][7]=getResources().getDrawable(R.drawable.type0pic7);
        typePics[1][0]=getResources().getDrawable(R.drawable.type1pic0);
        typePics[1][1]=getResources().getDrawable(R.drawable.type1pic1);
        typePics[1][2]=getResources().getDrawable(R.drawable.type1pic2);
        typePics[1][3]=getResources().getDrawable(R.drawable.type1pic3);
        typePics[2][0]=getResources().getDrawable(R.drawable.type2pic0);
        typePics[2][1]=getResources().getDrawable(R.drawable.type2pic1);
        typePics[2][2]=getResources().getDrawable(R.drawable.type2pic2);
        typePics[2][3]=getResources().getDrawable(R.drawable.type2pic3);
        typePics[3][0]=getResources().getDrawable(R.drawable.type3pic0);
        typePics[3][1]=getResources().getDrawable(R.drawable.type3pic1);


        {
            buttonText[0][0]="素食";
            buttonText[0][1]="肉类";
            buttonText[0][2]="蔬菜";
            buttonText[0][3]="下饭菜";
            buttonText[0][4]="鱼虾水产";
            buttonText[0][5]="蛋豆腌咸";
            buttonText[0][6]="快手菜";
            buttonText[0][7]="便当";
            buttonText[1][0]="米饭";
            buttonText[1][1]="面条";
            buttonText[1][2]="面点";
            buttonText[1][3]="包子饺子";
            buttonText[2][0]="港式甜品";
            buttonText[2][1]="饮料";
            buttonText[2][2]="烘焙";
            buttonText[2][3]="乳制品";
            buttonText[3][0]="美味肉汤";
            buttonText[3][1]="瘦身蔬菜汤";
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_page);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        typeName  = intent.getStringExtra(MainPage.EXTRA_TYPE);
        final int typeNum = intent.getIntExtra(MainPage.EXTRA_TYPE_NUM,0);

        TextView layout = (TextView) findViewById(R.id.title_type_name);
        layout.setText(typeName);
    //  layout.setTextSize(40);


        TableLayout layout2 = (TableLayout)findViewById(R.id.types);
        //layout2.setOrientation(LinearLayout.HORIZONTAL);
//        setContentView(layout2);
        buttonNum = typeMenuNum[typeNum];//获取按钮数

        setTypebuttons();

        buttonLine = (buttonNum-1) / 4 +1;
        buttonExp = buttonNum % 4;
        int num = 0;
        int line4 = 4;
        for(int i=0;i<buttonLine;i++)
        {
            if(i == buttonLine -1 && buttonExp != 0)
                line4 = buttonExp;

            TableRow tableRow = new TableRow(this);

            for(int j=0;j<line4 ;j++)
            {
                typeButton[num] = new Button(this);
                typeButton[num].setTag(num);
                typeButton[num].setText(buttonText[typeNum][num]);
                typeButton[num].setBackgroundColor(Color.parseColor("#00000000"));

                Drawable top = typePics[typeNum][num];
                typeButton[num].setCompoundDrawablesWithIntrinsicBounds(null,top,null,null);

                typeButton[num].setOnClickListener(typeButtonListener);

                tableRow.addView(typeButton[num]);
                num = num+1;
            }
            layout2.addView(tableRow);
        }

    }

    Button.OnClickListener typeButtonListener = new Button.OnClickListener(){
        public void onClick(View v){
            Intent intent = new Intent(TypePages.this,ResultPage.class);
            int buttonTag = (Integer) v.getTag();
            String buttonText = typeButton[buttonTag].getText().toString();
            intent.putExtra(EXTRA_SEARCHING,buttonText);
            intent.putExtra("ParentClassName","TypePages");

            TypePages.this.onPause();
            startActivity(intent);


        }
    };

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"----onpause---");
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"----onresume---");

        TextView layout = (TextView) findViewById(R.id.title_type_name);
        layout.setText(typeName);
        Log.d(TAG,typeName);
    }

}
