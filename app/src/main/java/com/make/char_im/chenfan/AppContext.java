package com.make.char_im.chenfan;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import com.make.char_im.chenfan.greenDao.DaoMaster;
import com.make.char_im.chenfan.greenDao.DaoSession;
import com.make.char_im.chenfan.utils.FileUtil;
import com.make.char_im.chenfan.utils.SPStrUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.util.ArrayList;

/**
 * App
 */
public class AppContext extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {


    private static AppContext appContext;
    //创建一个集合管理activity
    private ArrayList<Activity> activities = new ArrayList<>();
    //创建一个线程管理集合
    public ArrayList<Thread> threads = new ArrayList<>();
    public SharedPreferences editorInfo ;

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        this.registerActivityLifecycleCallbacks(this);

        editorInfo= getSharedPreferences(SPStrUtil.SETTING_INFO, Context.MODE_PRIVATE);
        setDatabase();

        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=58f6fadd");

    }

    public static AppContext getContextInstance() {
        return appContext;
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为greenDAO 已经帮你做了。
        // 注意：默认的DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this,"notes-db", null);
        db =mHelper.getWritableDatabase();
        // 注意：该数据库连接属于DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
    }


    /**
     * 添加activity到activities集合中，便于管理
     * @param activity
     */
    public void addActivity(Activity activity){
        activities.add(activity);
    }

    /**
     * 关闭集合中的所有activity
     */
    public void finishAllActivity(){
        for (Activity activity:activities){
            if (null != activity){
                activity.finish();
            }
        }
    }

    /**
     * 关闭集合中的所有Thread
     */
    public void finishThread(){
        for (Thread thread:threads){
            if (null != thread){
//                thread.stop();
            }
        }
    }

    /**
     * 关闭集合中的所有不可见的activity
     */
    public void finishAllGoneActivity(){
        for (Activity activity:activities){
            if (null != activity){
                if (activity != activities.get(activities.size()-1))
                    activity.finish();
            }
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {}
    @Override
    public void onActivityStarted(Activity activity) {}
    @Override
    public void onActivityResumed(Activity activity) {}
    @Override
    public void onActivityPaused(Activity activity) {}
    @Override
    public void onActivityStopped(Activity activity) {}
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}
    @Override
    public void onActivityDestroyed(Activity activity) {}

    public void exit() {
        exit(null);
    }

    public void exit(Activity context) {
//        finishAllActivitys();
        onTerminate();
    }

    /**
     * /**
     * 完全退出app，应用销毁执行(不能保证一定)
     */
    @Override
    public void onTerminate() {
//        new HttpTools(this).cancelAllRequest();
        FileUtil.clearLocalCache(this);
        super.onTerminate();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
