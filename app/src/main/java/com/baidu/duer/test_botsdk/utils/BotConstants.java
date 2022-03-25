package com.baidu.duer.test_botsdk.utils;


import android.media.MediaRecorder;
import androidx.annotation.Nullable;

public class BotConstants {

    public static final String LOG_TAG_AUDIO = "AudioRecorder";
    public static final String LOG_TAG_CAMERA = "CameraDemo";
    public static final String LOG_TAG_BOTSDK = "BotSDKDemo";

    /**
     * BotID和签名key ,可以在自己的技能控制台看到相关信息：https://dueros.baidu.com/dbp/main/console
     */
    public static final String BOTID = "756a51a0-f74a-e324-66f1-5b49a48932cb";
    public static final String SIGNATURE_KEY = "fwjioefjwef121iowe";

    // 两个随机值的前缀，这里对格式不作要求
    public static final String RANDOM1_PREFIX = "random1";
    public static final String RANDOM2_PREFIX = "random2";

    // UIControl用到的路由常量
    public static final String CLICK_LOGIN_URL = "sdkdemo://clicklogin";
    public static final String CLICK_REGISTER_URL = "sdkdemo://clickregister";
    public static final String SELETC_TEST_URL = "sdkdemo://selecttest/";
    public static final String INPUT_TEST_URL = "sdkdemo://inputtest/";
    // 课程表 or 日程表页面
    public static final String OPEN_CANLENDAR_URL = "dueros://6402fc3a-7825-3147-b52e-c25911b6d03c/entity"
            + "/timerShow/allUsedList?startDay=MONTH_START_DAY&endDay=MONTH_END_DAY&selectedDay=TODAY&requestType"
            + "=SHOW&habitChannel=duyayaapk";

    // 自定义快进意图名称
    public static final String FAST_FORWARD_INTENT = "fast_forward_intent";
    // 系统预定义[数字] 槽位名称
    public static final String SYS_NUMBER = "sys.number";
    // 用户自定义词典[时间单位] 槽位名称
    public static final String TIME_UNIT = "time.unit";
    // 用户自定义词典值
    public static final String TIME_UNIT_SECOND = "second";
    public static final String TIME_UNIT_MINUTE = "minute";


    public static class Frequency {
        public static final int F16K = 16000;
        public static final int F22K = 22050;
        public static final int F11K = 11025;
        public static final int F64K = 64000;
    }

    public static class UiControlType {
        public static final String LINK = "link";
        public static final String SELECT = "select";
        public static final String INPUT = "input";
        public static final String BUTTON = "button";
    }

    /**
     * 音频源
     */
    public static class AudioSource {
        /**
         * 默认的音频信号。通过默认音频源获取的采集信号
         */
        public static final int DEFAULT = MediaRecorder.AudioSource.VOICE_COMMUNICATION;

        /**
         * 原始音频信号。小度正式向开发者开放原始音频录制能力。
         * 通过此音频源可获取麦克风采集的原始音频数据。
         * 开发者可自行选择算法处理信号以满足需求。
         */
        public static final int ORIGINAL = 1007;
    }

    /** 打开App的token字段 */
    @Nullable
    public static String token;

}
