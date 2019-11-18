package com.baidu.duer.test_botsdk.fragment;

import java.util.HashMap;

import com.baidu.duer.bot.directive.payload.AmountInfo;
import com.baidu.duer.bot.event.payload.LinkClickedEventPayload;
import com.baidu.duer.botsdk.BotIntent;
import com.baidu.duer.botsdk.BotSdk;
import com.baidu.duer.botsdk.IAccountChargeMsgListener;
import com.baidu.duer.test_botsdk.R;
import com.baidu.duer.test_botsdk.botsdk.BotMessageListener;
import com.baidu.duer.test_botsdk.botsdk.IBotIntentCallback;
import com.baidu.duer.test_botsdk.utils.MockUtil;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 支付和手机号授权Demo页面，包括如下demo
 * 1，一键手机号授权
 * 2，发起支付行为
 * 3, 技能外售卖介绍
 */
public class PaymentAndAccountFragment extends Fragment implements IBotIntentCallback, View.OnClickListener,
        IAccountChargeMsgListener {


    private TextView mResultIntentTv;
    private Button mGrantPhoneNumber;
    private Button mTriggerPayment;

    private static final String TAG = "HandleIntentFragment";
    public PaymentAndAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_account, container, false);
        mResultIntentTv = view.findViewById(R.id.result_intent_detail);
        mGrantPhoneNumber = view.findViewById(R.id.phone_number_grant);
        mGrantPhoneNumber.setOnClickListener(this);
        mTriggerPayment = view.findViewById(R.id.trigger_payment);
        mTriggerPayment.setOnClickListener(this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "on fragment attach");
        super.onAttach(context);
        BotMessageListener.getInstance().addCallback(this);
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "on fragment detach");
        super.onDetach();
        BotMessageListener.getInstance().removeCallback(this);
    }

    @Override
    public void handleIntent(BotIntent intent, String customData) {

        String intentResult = getString(R.string.result_intent) + "\n指令名称:%s\n槽位信息：%s";
        mResultIntentTv.setText(String.format(intentResult, intent.name, intent.slots));
    }

    @Override
    public void onClickLink(String url, HashMap<String, String> paramMap) {

    }

    @Override
    public void onHandleScreenNavigatorEvent(int event) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trigger_payment:
                BotSdk.getInstance().requireCharge(MockUtil.mockAmountInfo(), MockUtil.mockSellerOlrderStructure(),
                        "测试订单");
                break;
            case R.id.phone_number_grant:
                LinkClickedEventPayload linkClickedEventPayload = new LinkClickedEventPayload();
                linkClickedEventPayload.url = MockUtil.mockGrantPhoneNumberUrl();
                BotSdk.getInstance().uploadLinkClickedEvent(linkClickedEventPayload);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLinkAccountSucceed(String s, String s1) {

    }

    @Override
    public void onChargeStatusUpdated(String s, AmountInfo amountInfo, AmountInfo amountInfo1, long l, String s1,
                                      String s2, String s3) {

    }
}
