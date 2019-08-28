package com.baidu.duer.test_botsdk.application;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.baidu.duer.botsdk.BotSdk;
import com.baidu.duer.test_botsdk.botsdk.BotMessageListener;
import com.baidu.duer.test_botsdk.botsdk.BotSDKUtils;
import com.baidu.duer.test_botsdk.utils.BotConstants;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * 建议在Application中执行BotSDK初始化动作
 */
public class BotsdkApplication extends Application {

    // 管理多页面Bot的全部Activity,在退出意图收到的时候，finish掉所有的activity
    private static List<WeakReference<Activity>> activities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化BotSDK
        BotSdk.getInstance().init(this);
        // 打开BotSDK的Log开关，开发阶段建议打开Log开关，便于排查问题
        BotSdk.enableLog(true);
        // TODO 替换自己的注册信息
        String random1 = BotConstants.RANDOM1_PREFIX + Math.random();
        String random2 = BotConstants.RANDOM2_PREFIX + Math.random();
        BotSdk.getInstance().register(BotMessageListener.getInstance(), BotConstants.BOTID, random1,
                BotSDKUtils.sign(random1), random2, BotSDKUtils.sign(random2));
        registerActivityLifecycleCallbacks(new ActivityContollor());
    }

    public static void exitApp() {
        for (WeakReference<Activity> ref : activities) {
            Activity activity = ref.get();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    private class ActivityContollor implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            activities.add(new WeakReference<Activity>(activity));
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

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            for (WeakReference ref : activities) {
                if (ref.get() == activity) {
                    activities.remove(ref);
                    return;
                }
            }
        }
    }
}
