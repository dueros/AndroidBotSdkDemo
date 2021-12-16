package com.baidu.duer.test_botsdk.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.baidu.duer.botsdk.BotSdk;
import com.baidu.duer.botsdk.util.HeartBeatReporter;
import com.baidu.duer.test_botsdk.BuildConfig;
import com.baidu.duer.test_botsdk.botsdk.BotMessageListener;
import com.baidu.duer.test_botsdk.botsdk.BotSDKUtils;
import com.baidu.duer.test_botsdk.utils.BotConstants;
import com.baidu.duer.test_botsdk.utils.ContextUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 建议在Application中执行BotSDK初始化动作
 */
public class BotsdkApplication extends Application {

    // 管理多页面Bot的全部Activity,在退出意图收到的时候，finish掉所有的activity
    private static final List<WeakReference<Activity>> activities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtil.setContext(this);
        /** 接入益智乐园的App,需要删掉下面这一行。保证益智乐园的付费等逻辑正常执行，不知道益智乐园是啥的，不要删掉这一行 */
        HeartBeatReporter.getInstance().setShouldUploadHeartBeatByApp(false);
        // 初始化BotSDK
        BotSdk.getInstance().init(this);
        // 打开BotSDK的Log开关，开发阶段建议打开Log开关，便于排查问题
        BotSdk.enableLog(BuildConfig.DEBUG);

        /*
         * 旧版本在线注册接口.在线校验支持所有系统版本。缺点：速度稍慢，依赖网络
         * TODO 替换自己的注册信息
         */
        String random1 = BotConstants.RANDOM1_PREFIX + Math.random();
        String random2 = BotConstants.RANDOM2_PREFIX + Math.random();
        // 在线校验示例
        BotSdk.getInstance().register(BotMessageListener.getInstance(), BotConstants.BOTID,
                random1, BotSDKUtils.sign(random1), random2, BotSDKUtils.sign(random2));

        /*
         * 1.46.0版本开始，支持离线校验。依赖系统版本大于等于1.46
         * 离线校验不依赖网络，速度更快。支持无网情况进行验证。但是需要依赖系统版本大于等于Sp46
         * 离线校验，需要在技能平台，打开离线校验开关，下载License文件，打包到assets/dueros/目录内
         * sdk会读取license文件，进行离线校验。
         * NOTE：离线校验支持版本较少（>=46),如非必要，不要走离线校验！！！
         */
        // 离线校验示例
        // BotSdk.getInstance().register(BotMessageListener.getInstance(), BotConstants.BOTID, BotSDKUtils.getAppKey());

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

    private static class ActivityContollor implements ActivityLifecycleCallbacks {

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
