package com.baidu.duer.test_botsdk.botsdk;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.baidu.duer.botsdk.BotIdentity;
import com.baidu.duer.botsdk.BotIntent;
import com.baidu.duer.botsdk.IBotMessageListener;
import com.baidu.duer.test_botsdk.application.BotsdkApplication;
import com.baidu.duer.test_botsdk.utils.ContextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * BotSDK全局Listener的参考实现。
 * 在应用Application中初始化BotSDK并注册此Listener，
 * 并在这个类中维护各个callback引用，以及回调callback相应方法。
 * 接入方需要实现callBack具体逻辑，接受BotSDK下发的意图和事件
 */
public class BotMessageListener implements IBotMessageListener {

    private final List<IBotIntentCallback> callbacks = new ArrayList<>();

    private BotMessageListener() {
    }

    /**
     * 添加意图监听
     *
     * @param botIntentCallback 被添加意图监听
     */
    public void addCallback(@NonNull final IBotIntentCallback botIntentCallback) {
        callbacks.add(botIntentCallback);
    }

    /**
     * 移除意图监听
     *
     * @param botIntentCallback 被移除的意图监听
     */
    public void removeCallback(@NonNull final IBotIntentCallback botIntentCallback) {
        callbacks.remove(botIntentCallback);
    }

    /**
     * 移除所有监听意图
     */
    public void clearCallback() {
        callbacks.clear();
    }

    public static BotMessageListener getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final BotMessageListener INSTANCE = new BotMessageListener();
    }

    /**
     * 意图下发。开发者在<a herf="">DBP平台</a>上面开发的意图，在匹配到对应用户query之后,会封装对应意图成为Intent下发下来。
     * APP通过{@link BotIntent#name}来确定意图名称，之后开发对应的逻辑。同时APP还可以通过{@link BotIntent#slots}解析参数
     *
     * @param token      指令的token,token是一个场景的标识符，在打开的时候，token会塞到intent中，key为"bot:accessToken"
     *                   可以通过token来区别这个指令是否是当前场景需要处理的指令
     * @param identity   BotID参考,包含packageName和accessToken
     * @param intent     意图内容，参考{@link BotIntent} 包含意图名称和槽位参数
     * @param customData 自定义数据，结构化数据。参考 <a herf="http://icode.baidu.com/repos/baidu/duer/open-platform-api-doc/blob/master:dueros-conversational-service/device-interface/bot-app-sdk-private.md">BotSDK协议文档</a>
     */
    @Override
    public void onHandleIntent(String token, BotIdentity identity, BotIntent intent, String customData) {
        for (IBotIntentCallback callback : callbacks) {
            callback.handleIntent(intent, customData);
        }
    }

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

    @Override
    public void onClickLink(String url, HashMap<String, String> paramMap) {
        for (IBotIntentCallback callback : callbacks) {
            callback.onClickLink(url, paramMap);
        }

    }

    /**
     * 处理屏幕导航事件
     *
     * @param event 屏幕导航事件值
     */

    @Override
    public void onHandleScreenNavigatorEvent(int event) {
        for (IBotIntentCallback callback : callbacks) {
            callback.onHandleScreenNavigatorEvent(event);
        }

    }

    @Override
    public void onCloseRequested() {
        BotsdkApplication.exitApp();
    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onRegisterFailed(int i) {
        Toast.makeText(ContextUtil.getContext(), "注册失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRegisterSucceed() {
        Log.i("BotSdk", "register success");
    }
}
