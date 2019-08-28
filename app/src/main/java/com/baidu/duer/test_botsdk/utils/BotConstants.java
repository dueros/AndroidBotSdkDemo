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
    public static final String CLICK_TEST_URL = "sdkdemo://clicktest";
    public static final String SELETC_TEST_URL = "sdkdemo://selecttest/";

    // 连接状态广播
    public static final String ACTION_REGISTER_SUCCESS = "com.baidu.duer.test_botsdk.register_success";
}
