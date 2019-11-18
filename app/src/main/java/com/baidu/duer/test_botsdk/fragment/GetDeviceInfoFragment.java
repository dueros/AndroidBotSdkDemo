package com.baidu.duer.test_botsdk.fragment;

import java.util.Arrays;
import java.util.HashMap;

import com.baidu.duer.botsdk.BotIntent;
import com.baidu.duer.botsdk.BotSdk;
import com.baidu.duer.botsdk.UiContextPayload;
import com.baidu.duer.test_botsdk.R;
import com.baidu.duer.test_botsdk.botsdk.BotMessageListener;
import com.baidu.duer.test_botsdk.botsdk.IBotIntentCallback;
import com.baidu.duer.test_botsdk.utils.BotConstants;
import com.baidu.duer.test_botsdk.utils.DeviceInfoUtil;
import com.baidu.duer.test_botsdk.utils.IntentDecodeUtil;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 自定义交互组件Demo页面，包括如下demo
 * 1，处理自定义交互组件
 * 2，处理系统预定义交互组件，比如选择第几个，输入XX等
 */
public class GetDeviceInfoFragment extends Fragment implements View.OnClickListener {


    private TextView mResultIntentTv;
    private Button mReadPassInfo;
    private Button mReadClientId;
    private Button mReadBduss;
    private Button mReadUserId;
    private Button mReadDeviceSerialNumber;

    private static final String TAG = "HandleIntentFragment";
    public GetDeviceInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_info, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mResultIntentTv = view.findViewById(R.id.result_intent_detail);
        mReadPassInfo = view.findViewById(R.id.get_pass_info);
        mReadPassInfo.setOnClickListener(this);
        mReadClientId = view.findViewById(R.id.get_clientid);
        mReadClientId.setOnClickListener(this);
        mReadBduss = view.findViewById(R.id.get_bduss);
        mReadBduss.setOnClickListener(this);
        mReadUserId = view.findViewById(R.id.get_userid);
        mReadUserId.setOnClickListener(this);
        mReadDeviceSerialNumber = view.findViewById(R.id.read_device_serial_number);
        mReadDeviceSerialNumber.setOnClickListener(this);
    }


    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "on fragment attach");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "on fragment detach");
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read_device_serial_number:
                mResultIntentTv.setText(DeviceInfoUtil.getSerialNumber());
                break;
            case R.id.get_userid:
                mResultIntentTv.setText(DeviceInfoUtil.getUserInfo(DeviceInfoUtil.PATH_USER_ID));
                break;
            case R.id.get_clientid:
                mResultIntentTv.setText(DeviceInfoUtil.getUserInfo(DeviceInfoUtil.PATH_CLIENT_ID));
                break;
            case R.id.get_bduss:
                mResultIntentTv.setText(DeviceInfoUtil.getUserInfo(DeviceInfoUtil.PATH_BDUSS));
                break;
            case R.id.get_pass_info:
                mResultIntentTv.setText(DeviceInfoUtil.getUserInfo(DeviceInfoUtil.PATH_PASS_INFO));
                break;
        }
    }
}
