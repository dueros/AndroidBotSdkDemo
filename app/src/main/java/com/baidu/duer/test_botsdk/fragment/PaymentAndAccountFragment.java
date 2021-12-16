package com.baidu.duer.test_botsdk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.baidu.duer.bot.directive.payload.AmountInfo;
import com.baidu.duer.bot.event.payload.LinkClickedEventPayload;
import com.baidu.duer.botsdk.BotIntent;
import com.baidu.duer.botsdk.BotSdk;
import com.baidu.duer.botsdk.IAccountChargeMsgListener;
import com.baidu.duer.botsdk.util.RequestBotSdkUtil;
import com.baidu.duer.test_botsdk.R;
import com.baidu.duer.test_botsdk.botsdk.BotMessageListener;
import com.baidu.duer.test_botsdk.botsdk.IBotIntentCallback;
import com.baidu.duer.test_botsdk.utils.MockUtil;

import java.util.HashMap;

/**
 * 支付和手机号授权Demo页面，包括如下demo
 * 1  一键手机号授权
 * 2  发起支付行为
 * 3  技能外售卖介绍
 */
public class PaymentAndAccountFragment extends Fragment implements IBotIntentCallback, View.OnClickListener,
        IAccountChargeMsgListener {


    private TextView mResultIntentTv;
    private Button mGrantPhoneNumber;
    private Button mTriggerPayment;
    private Button mTriggerBuy;

    private Button clearLogBtn;

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
        mTriggerBuy = view.findViewById(R.id.trigger_buy);
        mTriggerBuy.setOnClickListener(this);
        clearLogBtn = view.findViewById(R.id.clear_log_btn);
        clearLogBtn.setOnClickListener(this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "on fragment attach");
        super.onAttach(context);
        BotMessageListener.getInstance().addCallback(this);
        BotSdk.getInstance().setAccountAndChargeListener(this);
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "on fragment detach");
        super.onDetach();
        BotMessageListener.getInstance().removeCallback(this);
        BotSdk.getInstance().setAccountAndChargeListener(null);
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
            case R.id.trigger_buy:
                RequestBotSdkUtil.requestBuyProduct("123123123123", "191025095313376709");
                break;
            case R.id.clear_log_btn:
                mResultIntentTv.setText(R.string.result_intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLinkAccountSucceed(String s, String s1) {
    }

    /**
     * 支付状态改变的通知
     *
     * @param purchaseResult        支付结果 SUCCESS 支付成功 - ERROR 支付发生错误
     * @param authorizationAmount   应收金额信息
     * @param capturedAmount        实际扣款信息
     * @param creationTimestamp     订单创建时间戳
     * @param baiduOrderReferenceId 此次交易百度生成的订单Id
     * @param sellerOrderId         对应支付的订单ID
     * @param msg                   订单信息
     */
    @Override
    public void onChargeStatusUpdated(String purchaseResult, AmountInfo authorizationAmount,
                                      AmountInfo capturedAmount, long creationTimestamp,
                                      String baiduOrderReferenceId, String sellerOrderId, String msg) {
        String intentResult = getString(R.string.result_intent) + "\n支付状态更新:%s\n订单金额信息：%s\n"
                + "实收金额信息：%s\n订单时间戳：%d\n"
                + "百度侧订单号：%s\n卖方生成的订单号：%s\n订单备注信息：%s";
        mResultIntentTv.setText(String.format(intentResult, purchaseResult, authorizationAmount, capturedAmount,
                creationTimestamp, baiduOrderReferenceId, sellerOrderId, msg));
    }

    /**
     * 购买结果更新
     *
     * @param purchaseResult 支付结果，SUCCESS 支付成功 -ERROR 支付发生错误
     * @param productId      商品id
     * @param baiduOrderId   百度侧订单id
     * @param sellerOrderId  卖家订单id
     * @param msg            订单备注信息
     */
    @Override
    public void onBuyStatusUpdated(@NonNull final String purchaseResult,
                                   @NonNull final String productId,
                                   @NonNull final String baiduOrderId,
                                   @NonNull final String sellerOrderId, @Nullable final String msg, String token) {
        String intentResult = getString(R.string.result_intent) + "\n支付状态更新:%s\n商品id：%s\n"
                + "百度侧订单号：%s\n卖方生成的订单号：%s\n订单备注信息：%s";
        mResultIntentTv.setText(String.format(intentResult, purchaseResult, productId, baiduOrderId,
                sellerOrderId, msg));
    }
}
