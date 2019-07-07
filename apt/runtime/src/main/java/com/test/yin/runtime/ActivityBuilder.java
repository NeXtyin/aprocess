package com.test.yin.runtime;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.lang.reflect.InvocationTargetException;

/**
 * @author leilongling@kungeek.com
 * @version 3.2
 * date 2019/7/6.
 */
public class ActivityBuilder {

    public final static String BUILDER = "Builder";

    public final static ActivityBuilder INSTANCE = new ActivityBuilder();

    private Application application;

    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            performInject(activity, savedInstanceState);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            performSaveState(activity, outState);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    private void performInject(Activity activity, Bundle savedInstanceState) {
        try {
            Class.forName(activity.getClass().getName() + BUILDER)
                    .getDeclaredMethod("inject", Activity.class, Bundle.class)
                    .invoke(null, activity, savedInstanceState);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void performSaveState(Activity activity, Bundle outState) {
        try {
            Class.forName(activity.getClass().getName() + BUILDER)
                    .getDeclaredMethod("inject", Activity.class, Bundle.class)
                    .invoke(null, activity, outState);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void init(Context context) {
        if (this.application == null) {
            this.application = (Application) context.getApplicationContext();
            application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        }
    }

    public void startActivity(Context context, Intent intent) {
        if (context instanceof Activity) {
            context.startActivity(intent);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}
