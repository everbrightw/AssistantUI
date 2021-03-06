package com.everbright.wangyusen.myapplication;

/**
 * Created by wangyusen on 7/27/17.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context mContext;
    private Activity mCurrentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }

    public static Context getContext() {
        return mContext;
    }

}