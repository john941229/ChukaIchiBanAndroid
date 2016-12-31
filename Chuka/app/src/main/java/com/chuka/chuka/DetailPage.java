package com.chuka.chuka;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
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
import static java.lang.Math.min;


/**
 * Created by cheng on 2016/12/21.
 */

public class DetailPage extends AppCompatActivity {

    ArrayList<String> materialList;
    List<List<String>> listMaterial;
    List<String> groupMaterialTitle;
    List<Map<String, Object>> mData;
    public String strResult = "";
    OkHttpClient client = new OkHttpClient();
    static Integer timerButtonStop = 0;
    static Integer timerButtonPause = 2;
    static Integer timerButtonStart = 1;


    Handler handler = new Handler();

    private ExpandableListView expandableListView;
    private Context mContext = null;
    public int id;

    private final List<Step> stepList = new ArrayList<DetailPage.Step>();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);
        mContext = this;


        Toolbar myToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        TextView spTitle = (TextView) findViewById(R.id.title_detail);
        spTitle.setText(intent.getStringExtra("name"));

        ImageView detailImage = (ImageView) findViewById(R.id.detail_image);
        detailImage.setImageBitmap(getHttpBitmap(intent.getStringExtra("imgUrl")));
        detailImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        detailImage.setAdjustViewBounds(true);

        TextView description = (TextView) findViewById(R.id.description);
        description.setText(intent.getStringExtra("description"));

        id = intent.getIntExtra("id", 0);

        ListView listView = (ListView) findViewById(R.id.material_list);

        mData = getData(intent.getStringArrayListExtra("materialList"));

        final SimpleAdapter adapter = new SimpleAdapter(this, mData, R.layout.material_checkbox,
                new String[]{"material_name"},
                new int[]{R.id.material_text});
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
        listView.setFocusable(false);
        mListView = (ListView)findViewById(R.id.main_listview);
        getSteps(id);


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

    private List<Map<String, Object>> getData(ArrayList<String> materialLists) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < materialLists.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("material_name", materialLists.get(i).toString());
            list.add(map);

        }
        return list;
    }


    public void getSteps(int id) {
        final Request request = new Request.Builder()
                .url("http://115.159.71.53:3000/id?id=" + id)
                .build();

        client.newCall(request).enqueue(new Callback() {
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
                                                        setListViewHeightBasedOnChildren(mListView);
                                                        mListView.setFocusable(false);
                                                    }
                                                });

                                            }
                                        }
        );
    }

    public void solveData() {
        try {
            JSONArray jsonObjs = new JSONObject(strResult).getJSONArray("steps");
            String s = "";

            int resultNum = min(jsonObjs.length(), 40);


            for (int i = 0; i < resultNum; i++) {

                JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
                int time = jsonObj.getInt("time");
                String img = jsonObj.getString("imgUrl");
                String text = jsonObj.getString("text");
                String name = jsonObj.getString("name");
                Bitmap bitmap = getHttpBitmap(img);

//                TextView nameView = (TextView)contentView.findViewById(R.id.steps_name);
//                nameView.setText(name);
//
//                TextView textView = (TextView)contentView.findViewById(R.id.steps_text);
//                textView.setText(text);
//
//                ImageView imgView = (ImageView)contentView.findViewById(R.id.steps_img);
//                imgView.setImageBitmap(bitmap);
//
//
//                pageStepsSteps.addView(contentView);
                stepList.add(new Step(name,text,time,img,false));
            }
            mListView.setAdapter(new ListStepItemAdapter(mContext,stepList));

        } catch (JSONException ex) {
            System.out.println("JSONS error\n");
            ex.printStackTrace();
        }

    }

    private class ListStepItemAdapter extends BaseAdapter{
        private final List<Step> dataSource = new ArrayList<DetailPage.Step>();
        private LayoutInflater mLayoutInflater = null;
        private int dimensionPixelSize = 0;

        public ListStepItemAdapter(Context cxt,List<Step> dataSource){
            this.dataSource.addAll(dataSource);
            mLayoutInflater = LayoutInflater.from(cxt);
            dimensionPixelSize = cxt.getResources().getDimensionPixelSize(R.dimen.bottom_item_height);
        }
        @Override
        public int getCount() {
            return dataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return dataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            StepViewHolder stepViewHolder = null;

            if(convertView == null
                    || !StepViewHolder.class.isInstance(convertView.getTag())) {
                convertView = mLayoutInflater.inflate(R.layout.steps_item,
                        null);
                stepViewHolder = new StepViewHolder(convertView);
                convertView.setTag(stepViewHolder);
            } else {
                stepViewHolder = (StepViewHolder) convertView.getTag();
            }
            Step step = dataSource.get(position);
            LayoutParams lp = (LayoutParams)stepViewHolder.targetPanel.getLayoutParams();
            if(step.isOpen) {
                //ViewHelper.setRotation(stepViewHolder.switcherIv,180f);
                lp.bottomMargin = 0;
                lp.height = dimensionPixelSize;
            }else {
                //ViewHelper.setRotation(songViewHolder.switcherIv, 0f);
                lp.bottomMargin = - dimensionPixelSize;
                //高度是0,lp.bottomMargin值无效，这里之所以设置高度为0，因为为了防止布局耗时导致绘制不及时，从而出现错误显示
                lp.height = 0;
            }
            stepViewHolder.targetPanel.setLayoutParams(lp);

            if(step.time > 0) {
                stepViewHolder.label.setOnClickListener(labelClickListener);
                stepViewHolder.stepsTimer.initTime(60*step.time);
                stepViewHolder.stepsTimer.setOnTimeCompleteListener(new Anticlockwise.OnTimeCompleteListener(){
                    @Override
                    public void onTimeComplete(){
                        Toast.makeText(getApplicationContext(),"complete!",(Toast.LENGTH_SHORT)).show();
                    }

                });
                stepViewHolder.stepsItemBt1.setTag(R.id.flag_timebtn,timerButtonStop);
                stepViewHolder.stepsItemBt1.setText("开始");
                stepViewHolder.stepsItemBt1.setBackgroundColor(getResources().getColor(R.color.buttonStart) );
                stepViewHolder.stepsItemBt1.setOnClickListener(startBtnListener);
                stepViewHolder.stepsItemBt2.setOnClickListener(resetBtnListener);
                stepViewHolder.stepsItemBt2.setTag(R.id.flag_timebtn,step.time*60);

            }
            stepViewHolder.label.setTag(position);
            stepViewHolder.stepsName.setText(step.name);
            stepViewHolder.stepsText.setText(step.text);
            Bitmap bitmap = getHttpBitmap(step.img);
            stepViewHolder.stepsImg.setImageBitmap(bitmap);
            Log.d("position",position+"");
            Log.d("ChildCount",parent.getChildCount()+"");
            Log.d("stepsName",step.text);

            return convertView;

        }
    }

    private class Step implements Serializable{
        private String name;
        private String text;
        private int time;
        private String img;
        private boolean isOpen;

        public Step(String name, String text, int time, String img,boolean isOpen){
            super();
            this.name = name;
            this.text = text;
            this.time = time;
            this.img = img;
            this.isOpen = isOpen;
        }

        public String getName() {
            return name;
        }
        public String getText() {
            return text;
        }
        public int getTime() {
            return time;
        }
        public String getImg() {
            return img;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setOpen(boolean isOpen) {
            this.isOpen = isOpen;
        }

        public boolean isOpen() {
            return isOpen;
        }
        @Override
        public String toString() {
            return "Step [name=" + name + ", text=" + text +", time=" + time +", isOpen=" + isOpen + "]";
        }

    }

    private class StepViewHolder implements Serializable {
        private View contentView;
        private TextView stepsName;
        private Anticlockwise stepsTimer;
        private TextView stepsText;
        private ImageView stepsImg;
        private View label;
        private View targetPanel;
        private Button stepsItemBt1;
        private Button stepsItemBt2;

        public StepViewHolder(View contentView){
            this.contentView = contentView;
            stepsName = (TextView)contentView.findViewById(R.id.steps_name);
            stepsText = (TextView)contentView.findViewById(R.id.steps_text);
            stepsImg = (ImageView)contentView.findViewById(R.id.steps_img);
            stepsTimer = (Anticlockwise)contentView.findViewById(R.id.steps_timer);
            label = contentView.findViewById(R.id.layout_onclick);
            targetPanel = contentView.findViewById(R.id.llshow);
            stepsItemBt1 = (Button) contentView.findViewById(R.id.steps_item_bt1);
            stepsItemBt2 = (Button) contentView.findViewById(R.id.steps_item_bt2);

        }
    }

    private View.OnClickListener labelClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            int position = (Integer) v.getTag();

            final BaseAdapter adapter = (BaseAdapter) mListView.getAdapter();
            Step item = (Step) adapter.getItem(position);
            item.setOpen(!item.isOpen());

            ViewGroup itemView = (ViewGroup) v.getParent().getParent().getParent();
            StepViewHolder stepViewHolder = (StepViewHolder) itemView.getTag();

            //Toast.makeText(DetailPage.this,"position="+position+",title="+item.getName(),Toast.LENGTH_SHORT).show();
            int defaultHeight = getResources().getDimensionPixelSize(R.dimen.bottom_item_height);
            itemView.startAnimation(new ExpandAnimation(stepViewHolder.targetPanel, item.isOpen(),defaultHeight));

        }
    };
    private View.OnClickListener startBtnListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            Integer btnFlag = (Integer) v.getTag(R.id.flag_timebtn);

            if(btnFlag == timerButtonStop){
                ViewGroup itemView = (ViewGroup) v.getParent().getParent().getParent();
                StepViewHolder stepViewHolder = (StepViewHolder) itemView.getTag();

                v.setTag(R.id.flag_timebtn,timerButtonStart);
                stepViewHolder.stepsItemBt1.setText("暂停");
                stepViewHolder.stepsItemBt1.setBackgroundColor(getResources().getColor(R.color.buttonPause) );
                stepViewHolder.stepsTimer.reStart();
            }else if(btnFlag == timerButtonStart){
                ViewGroup itemView = (ViewGroup) v.getParent().getParent().getParent();
                StepViewHolder stepViewHolder = (StepViewHolder) itemView.getTag();

                v.setTag(R.id.flag_timebtn,timerButtonPause);
                stepViewHolder.stepsItemBt1.setText("继续");
                stepViewHolder.stepsTimer.onPause();
            }else if(btnFlag == timerButtonPause){
                ViewGroup itemView = (ViewGroup) v.getParent().getParent().getParent();
                StepViewHolder stepViewHolder = (StepViewHolder) itemView.getTag();

                v.setTag(R.id.flag_timebtn,timerButtonStart);
                stepViewHolder.stepsItemBt1.setText("暂停");
                stepViewHolder.stepsTimer.onResume();
            }
        }
    };
    private View.OnClickListener resetBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Integer btnFlag = (Integer) v.getTag(R.id.flag_timebtn);

            ViewGroup itemView = (ViewGroup) v.getParent().getParent().getParent();
            StepViewHolder stepViewHolder = (StepViewHolder) itemView.getTag();

            stepViewHolder.stepsTimer.stop();
            stepViewHolder.stepsItemBt1.setText("开始");
            stepViewHolder.stepsItemBt1.setBackgroundColor(getResources().getColor(R.color.buttonStart) );
            stepViewHolder.stepsItemBt1.setTag(R.id.flag_timebtn,timerButtonStop);
            stepViewHolder.stepsTimer.initTime(btnFlag);
        }
    };

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