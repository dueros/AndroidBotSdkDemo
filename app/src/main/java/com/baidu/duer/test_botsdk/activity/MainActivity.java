package com.baidu.duer.test_botsdk.activity;

import com.baidu.duer.bot.directive.payload.AmountInfo;
import com.baidu.duer.botsdk.IAccountChargeMsgListener;
import com.baidu.duer.test_botsdk.R;
import com.baidu.duer.test_botsdk.fragment.GetDeviceInfoFragment;
import com.baidu.duer.test_botsdk.fragment.HandleIntentFragment;
import com.baidu.duer.test_botsdk.fragment.PaymentAndAccountFragment;
import com.baidu.duer.test_botsdk.fragment.UiControlFragment;
import com.baidu.duer.test_botsdk.utils.BotConstants;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 这是一个BotSDK demo， demo包括如下使用案例
 * 1, 意图处理，包括自定义意图和系统预定义意图，槽位处理
 * 1， LinkClick事件上报，三方app可以通过上报事件主动与DuerOS交互
 * 2， UIControl使用,包括自定义交互组件和系统预定义交互组件
 * 3， 用户手机号一键授权，支付功能验证
 * 5， 宿主端信息获取，包括设备bduss，登录用户的user
 */
public class MainActivity extends AppCompatActivity implements IAccountChargeMsgListener {

    private static final String TAG = "testMainActivity";

    private String[] tabs = new String[]{"意图处理", "自定义交互", "支付和手机号", "设备和用户信息"};
    private Class[] mFragmentClasses = new Class[]{HandleIntentFragment.class, UiControlFragment.class,
            PaymentAndAccountFragment.class, GetDeviceInfoFragment.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("BotSdk Demo");
        initView();
        //BotSdk.getInstance().setAccountAndChargeListener(this);
    }

    /**
     * tab的view对象
     * @param index 索引
     * @return view对象
     */
    private View getTabView(int index) {
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        TextView tabTitle = inflate.findViewById(R.id.tab_title_content);
        tabTitle.setText(tabs[index]);
        return inflate;
    }

    private void initView() {
        FragmentTabHost tabHost = findViewById(R.id.tab_container);
        // 初始化tabHost
        tabHost.setup(this, getSupportFragmentManager(), R.id.fragment_container);
        for (int i = 0; i < 4; i++) {
            tabHost.addTab(tabHost.newTabSpec(tabs[i]).setIndicator(getTabView(i)), mFragmentClasses[i], null);
        }
        // 设置默认tab
        tabHost.setCurrentTab(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(BotConstants.MAIN_ACTIVITY_LAUNCH_SUCCESS);
        LocalBroadcastManager.getInstance(this).sendBroadcastSync(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
