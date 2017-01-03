package com.chuka.chuka;
import android.app.Application;
/**
 * Created by cheng on 2017/1/2.
 */

public class Data extends Application{
    private String userName;
    private String userId;

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
    @Override
    public void onCreate(){
        userId = "";
        userName = "";
        super.onCreate();
    }
}
