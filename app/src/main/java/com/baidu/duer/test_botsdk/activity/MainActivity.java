package com.baidu.duer.test_botsdk.activity;

import java.util.Arrays;
import java.util.HashMap;

import com.baidu.duer.bot.directive.payload.AmountInfo;
import com.baidu.duer.botsdk.BotIntent;
import com.baidu.duer.botsdk.BotSdk;
import com.baidu.duer.botsdk.IAccountChargeMsgListener;
import com.baidu.duer.botsdk.UiContextPayload;
import com.baidu.duer.test_botsdk.R;
import com.baidu.duer.test_botsdk.botsdk.BotMessageListener;
import com.baidu.duer.test_botsdk.botsdk.IBotIntentCallback;
import com.baidu.duer.test_botsdk.utils.BotConstants;
import com.baidu.duer.test_botsdk.utils.BotDemoClickListener;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * 这是一个BotSDK demo， demo包括如下使用案例
 * 1， 支付接口调用
 * 2， UIControl使用
 * 3， 系统预定义UiControl的使用
 */
public class MainActivity extends AppCompatActivity implements IBotIntentCallback, IAccountChargeMsgListener {

    private static final String TAG = "testMainActivity";
    private Button mChargeTestBtn;
    private Button mClickTestBtn;
    private Button mFirstNumTestBtn;
    private Button mSecondNumTestBtn;
    private Button mThirdNumTestBtn;
    private final View.OnClickListener mClickListener = new BotDemoClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initClientContext();
        BotMessageListener.getInstance().addCallback(this);
        BotSdk.getInstance().setAccountAndChargeListener(this);
    }

    private void initView() {
        mChargeTestBtn = findViewById(R.id.charge_test);
        mChargeTestBtn.setOnClickListener(mClickListener);
        mClickTestBtn = findViewById(R.id.click_test);
        mClickTestBtn.setOnClickListener(mClickListener);
        mFirstNumTestBtn = findViewById(R.id.first_num_test);
        mFirstNumTestBtn.setOnClickListener(mClickListener);
        mSecondNumTestBtn = findViewById(R.id.second_num_test);
        mSecondNumTestBtn.setOnClickListener(mClickListener);
        mThirdNumTestBtn = findViewById(R.id.third_num_test);
        mThirdNumTestBtn.setOnClickListener(mClickListener);
    }

    /**
     * 注册UIControl， UIControl就是根据界面元素自定义语音指令
     */
    private void initClientContext() {
        UiContextPayload payload = new UiContextPayload();
        // 可以定义支持的话术, link固定写法
        String[] words = {"试一试", "点击试一试"};
        payload.addHyperUtterance(BotConstants.CLICK_TEST_URL,
                Arrays.asList(words), "link",
                null);


        // 也可以引用系统预定义的话术，select就是系统预定义的选择意图，不需要自己定义话术就可以支持诸如： “选择”，“打开”，“点击”等话术
        // 如下注册就可以支持打开第一个，点击第一个，选择第一个等等话术,第二个，第三个同理
        for (int i = 1 ; i < 4; i++) {
            HashMap<String, String> params = new HashMap<>();
            params.put("index", String.valueOf(i));
            payload.addHyperUtterance(BotConstants.SELETC_TEST_URL + i, null, "select", params);
        }

        BotSdk.getInstance().updateUiContext(payload);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BotMessageListener.getInstance().removeCallback(this);
    }

    @Override
    public void handleIntent(BotIntent intent, String customData) {
        /**
         * 测试技能支持意图“同步管理", 对小度设备说： "小度小度，同步管理" ,可以收到平台定义的同步管理意图
         */
        Log.d(TAG, "handleIntent intent name:" + intent.name + " custom data:" + customData);
        Toast.makeText(MainActivity.this, "handle intent:" + intent.name, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClickLink(String url, HashMap<String, String> paramMap) {
        // 处理UIControl的返回结果,UIControl的返回结果跟自己定义的url相关。通过url确定用户命中了何种意图
        Log.d(TAG, "onclickLink and url is:" + url + " paramMap is:" + paramMap);
        if (BotConstants.CLICK_TEST_URL.equals(url)) {
            mClickTestBtn.performClick();
        } else if (url.startsWith(BotConstants.SELETC_TEST_URL)) {
            String index = url.substring(BotConstants.SELETC_TEST_URL.length());
            if ("1".equals(index)) {
                mFirstNumTestBtn.performClick();
            } else if ("2".equals(index)) {
                mSecondNumTestBtn.performClick();
            } else if ("3".equals(index)) {
                mThirdNumTestBtn.performClick();
            }
        }
    }

    @Override
    public void onHandleScreenNavigatorEvent(int event) {
        Log.d(TAG, "handle screen navigator event:" + event );
    }

    @Override
    public void onLinkAccountSucceed(String s, String s1) {

    }

    @Override
    public void onChargeStatusUpdated(String s, AmountInfo amountInfo, AmountInfo amountInfo1, long l, String s1,
                                      String s2, String s3) {
        // 处理支付结果回调
        Toast.makeText(MainActivity.this, "支付结果回调, result:" + s, Toast.LENGTH_LONG).show();
    }
}
