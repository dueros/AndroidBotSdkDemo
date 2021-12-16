package com.baidu.duer.test_botsdk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.baidu.duer.botsdk.BotIntent;
import com.baidu.duer.test_botsdk.R;
import com.baidu.duer.test_botsdk.botsdk.BotMessageListener;
import com.baidu.duer.test_botsdk.botsdk.IBotIntentCallback;
import com.baidu.duer.test_botsdk.utils.BotConstants;
import com.baidu.duer.test_botsdk.utils.IntentDecodeUtil;

import java.util.HashMap;

/**
 * 意图处理Demo页面，包括如下demo
 * 1，处理自定义意图
 * （1）引用自定义词典，解析槽位数据
 * （2）引用系统预定义词典，解析槽位信息
 * 2，处理系统预定义意图（确认，取消等）
 */
public class HandleIntentFragment extends Fragment implements IBotIntentCallback {


    private TextView mResultIntentTv;

    private static final String TAG = "HandleIntentFragment";

    public HandleIntentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_handle_intent, container, false);
        mResultIntentTv = view.findViewById(R.id.result_intent_detail);
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
        if (BotConstants.FAST_FORWARD_INTENT.equals(intent.name)) {
            int seekNumber = IntentDecodeUtil.decodeSeekIntentSlot(intent.slots);
            intentResult = intentResult + "\n拖动时长：" + seekNumber;
        }
        mResultIntentTv.setText(String.format(intentResult, intent.name, intent.slots));
    }

    @Override
    public void onClickLink(String url, HashMap<String, String> paramMap) {

    }

    @Override
    public void onHandleScreenNavigatorEvent(int event) {

    }
}
