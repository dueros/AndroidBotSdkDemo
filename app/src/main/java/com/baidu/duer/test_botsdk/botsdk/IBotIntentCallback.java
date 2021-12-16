package com.baidu.duer.test_botsdk.botsdk;

import com.baidu.duer.botsdk.BotIntent;

import java.util.HashMap;

/**
 * 用于从BotSDK接受意图
 */
public interface IBotIntentCallback {

    /**
     * 意图下发。开发者在<a herf="">DBP平台</a>上面开发的意图，在匹配到对应用户query之后,会封装对应意图成为Intent下发下来。
     * APP通过{@link BotIntent#name}来确定意图名称，之后开发对应的逻辑。同时APP还可以通过{@link BotIntent#slots}解析参数
     *
     * @param intent     意图内容，参考{@link BotIntent} 包含意图名称和槽位参数
     * @param customData 自定义数据，结构化数据。参考 <a herf="http://icode.baidu.com/repos/baidu/duer/open-platform-api-doc/blob/master:dueros-conversational-service/device-interface/bot-app-sdk-private.md">BotSDK协议文档</a>
     */
    void handleIntent(BotIntent intent, String customData);

    /**
     * ClickLink事件下发,ClickLink是一种Directive,用户新增自定义交互之后，云端会解析用户定义的交互，下发对应的指令
     * 例如APP新增自定义交互样例{@code <"试一试"，url>},用户query"试一试"
     * 之后DuerOS会通过此接口下发 上面定义的url
     * 如果用户引用系统内建自定义类型，用户query中可以包含参数，例如"输入北京"，这个query中北京可以被解析成参数，放到后面paramMap中下发
     * 系统内建类型参考：<a herf="https://github.com/dueros/AndroidBotSdkDemo">DCS type文档</a>
     *
     * @param url      自定义交互描述中的url
     * @param paramMap 对于系统内建类型，参数列表。参数就是从query中通过分词取得的关键词。
     */

    void onClickLink(String url, HashMap<String, String> paramMap);

    /**
     * 处理屏幕导航事件
     *
     * @param event 屏幕导航事件值
     */
    void onHandleScreenNavigatorEvent(int event);

}
