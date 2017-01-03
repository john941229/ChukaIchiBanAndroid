package com.chuka.chuka;
import android.app.Application;
/**
 * Created by cheng on 2017/1/2.
 */

public class Data extends Application{
    private String userName;
    private String userId;
    private boolean logined;

    public String getUserName(){
        return this.userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserId(){
        return  this.userId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }

    public boolean isLogined(){
        return logined;
    }
    public void setLogined(boolean flag){
        this.logined = flag;
    }
    @Override
    public void onCreate(){
        userId = "";
        userName = "";
        logined = false;
        super.onCreate();
    }
}
