package com.test.yin.anoprocess;

import android.app.Application;

import com.test.yin.runtime.ActivityBuilder;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/6/13.
 */
public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ActivityBuilder.INSTANCE.init(this);
    }
}
