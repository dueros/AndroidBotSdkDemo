package com.baidu.duer.test_botsdk.utils;

import com.baidu.duer.botsdk.BotSdk;
import com.baidu.duer.test_botsdk.R;

import android.view.View;
import android.widget.Toast;

public class BotDemoClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.charge_test:
                Toast.makeText(v.getContext(), "发起支付请求", Toast.LENGTH_LONG).show();
                BotSdk.getInstance().requireCharge(MockUtil.mockAmountInfo(), MockUtil.mockSellerOlrderStructure(),
                        "订单备注信息");
                break;
            case R.id.click_test:
                BotSdk.getInstance().speakRequest("点击了试一试");
                break;
            case R.id.first_num_test:
                BotSdk.getInstance().speakRequest("点击了第一个");
                break;
            case R.id.second_num_test:
                BotSdk.getInstance().speakRequest("点击了第二个");
                break;
            case R.id.third_num_test:
                BotSdk.getInstance().speakRequest("点击了第三个");
                break;

        }
    }
}
