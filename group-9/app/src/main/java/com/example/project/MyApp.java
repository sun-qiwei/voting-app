/** author: HangXu B00780054
 * date: 2019.10.30
 * desription: this is inhrtited from all activities.
 * It will hold the application instances
 * and we can get the context to the view
 */
package com.example.project;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.example.project.utils.SPTool;


/**
 */

public class MyApp extends MultiDexApplication {

    private Context mContext;

    private static MyApp mMyApp;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * application 全局单列
     *
     * @return
     */
    public static MyApp getInstance() {
        return mMyApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMyApp = this;
        mContext = getApplicationContext();
        SPTool.getInstanse().init(mContext);

    }

    public Context getContext() {
        return mContext;
    }
}
