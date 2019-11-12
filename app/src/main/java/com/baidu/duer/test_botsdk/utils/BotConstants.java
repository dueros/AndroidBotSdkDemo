package com.baidu.duer.test_botsdk.utils;


public class BotConstants {

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

    // 连接状态广播
    public static final String ACTION_REGISTER_SUCCESS = "com.baidu.duer.test_botsdk.register_success";

    // 自定义快进意图名称
    public static final String FAST_FORWARD_INTENT = "fast_forward_intent";
    // 系统预定义[数字] 槽位名称
    public static final String SYS_NUMBER = "sys.number";
    // 用户自定义词典[时间单位] 槽位名称
    public static final String TIME_UNIT = "time.unit";
    // 用户自定义词典值
    public static final String TIME_UNIT_SECOND = "second";
    public static final String TIME_UNIT_MINUTE = "minute";
}
