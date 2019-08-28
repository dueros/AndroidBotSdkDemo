package com.baidu.duer.test_botsdk.activity;

import java.util.HashMap;

import com.baidu.duer.bot.directive.payload.AmountInfo;
import com.baidu.duer.botsdk.BotIntent;
import com.baidu.duer.botsdk.BotSdk;
import com.baidu.duer.botsdk.IAccountChargeMsgListener;
import com.baidu.duer.test_botsdk.R;
import com.baidu.duer.test_botsdk.botsdk.BotMessageListener;
import com.baidu.duer.test_botsdk.botsdk.IBotIntentCallback;
import com.baidu.duer.test_botsdk.utils.MockUtil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements IBotIntentCallback, IAccountChargeMsgListener {

    private static final String TAG = "testMainActivity";
    private Button mChargeTestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        BotMessageListener.getInstance().addCallback(this);
        BotSdk.getInstance().setAccountAndChargeListener(this);
    }

    private void initView() {
        mChargeTestBtn = findViewById(R.id.charge_test);
        mChargeTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "发起支付请求", Toast.LENGTH_LONG).show();
                BotSdk.getInstance().requireCharge(MockUtil.mockAmountInfo(), MockUtil.mockSellerOlrderStructure(),
                        "订单备注信息");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BotMessageListener.getInstance().removeCallback(this);
    }

    @Override
    public void handleIntent(BotIntent intent, String customData) {
        Log.d(TAG, "handleIntent intent name:" + intent.name + " custom data:" + customData);
    }

    @Override
    public void onClickLink(String url, HashMap<String, String> paramMap) {
        Log.d(TAG, "onclickLink and url is:" + url + " paramMap is:" + paramMap);
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
        Toast.makeText(MainActivity.this, "支付结果回调, result:" + s, Toast.LENGTH_LONG).show();
    }
}
