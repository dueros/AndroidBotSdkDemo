package com.baidu.duer.test_botsdk.fragment;

import java.util.Arrays;
import java.util.HashMap;

import com.baidu.duer.bot.event.payload.LinkClickedEventPayload;
import com.baidu.duer.botsdk.BotIntent;
import com.baidu.duer.botsdk.BotSdk;
import com.baidu.duer.botsdk.UiContextPayload;
import com.baidu.duer.test_botsdk.R;
import com.baidu.duer.test_botsdk.botsdk.BotMessageListener;
import com.baidu.duer.test_botsdk.botsdk.IBotIntentCallback;
import com.baidu.duer.test_botsdk.utils.BotConstants;
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
public class UiControlFragment extends Fragment implements IBotIntentCallback, View.OnClickListener {


    private TextView mResultIntentTv;
    private Button mLoginBtn;
    private Button mRegisterBtn;
    private Button mSelectOne;
    private Button mSelectTwo;
    private EditText mInputText;
    private Button mOpenCalendar;

    private static final String TAG = "HandleIntentFragment";
    public UiControlFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ui_control, container, false);
        initView(view);
        initClientContext();
        return view;
    }

    private void initView(View view) {
        mResultIntentTv = view.findViewById(R.id.result_intent_detail);
        mLoginBtn = view.findViewById(R.id.login_btn);
        mLoginBtn.setOnClickListener(this);
        mRegisterBtn = view.findViewById(R.id.sign_up_btn);
        mRegisterBtn.setOnClickListener(this);
        mSelectOne = view.findViewById(R.id.select_one);
        mSelectOne.setOnClickListener(this);
        mSelectTwo = view.findViewById(R.id.select_two);
        mSelectTwo.setOnClickListener(this);
        mInputText = view.findViewById(R.id.input_edit_view);
        mOpenCalendar = view.findViewById(R.id.open_calendar);
        mOpenCalendar.setOnClickListener(this);
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
        String intentResult = getString(R.string.result_intent) + "\n指令匹配url:%s\n参数信息:%s";
        mResultIntentTv.setText(String.format(intentResult, url, paramMap));

        if (BotConstants.CLICK_LOGIN_URL.equals(url)) {
            mLoginBtn.performClick();
        } else if(BotConstants.CLICK_REGISTER_URL.equals(url)) {
            mRegisterBtn.performClick();
        } else if (url.startsWith(BotConstants.SELETC_TEST_URL)) {
            String index = url.substring(BotConstants.SELETC_TEST_URL.length());
            if ("1".equals(index)) {
                mSelectOne.performClick();
            } else if ("2".equals(index)) {
                mSelectOne.performClick();
            }
        } else if (BotConstants.INPUT_TEST_URL.equals(url)) {
            mInputText.setText(paramMap.get("content"));
        } else if (BotConstants.OPEN_CANLENDAR_URL.equals(url)) {
            mOpenCalendar.performClick();
            LinkClickedEventPayload payload = new LinkClickedEventPayload();
            payload.url = BotConstants.OPEN_CANLENDAR_URL;
            BotSdk.getInstance().uploadLinkClickedEvent(payload);
        }
    }

    @Override
    public void onHandleScreenNavigatorEvent(int event) {

    }

    /**
     * 注册UIControl， UIControl就是根据界面元素自定义语音指令
     */
    private void initClientContext() {
        UiContextPayload payload = new UiContextPayload();
        // 可以定义支持的话术, link固定写法
        String[] loginWords = {"登录", "点击登录"};
        payload.addHyperUtterance(BotConstants.CLICK_LOGIN_URL, Arrays.asList(loginWords), "link", null);
        String[] registerWords = {"注册", "点击注册"};
        payload.addHyperUtterance(BotConstants.CLICK_REGISTER_URL, Arrays.asList(registerWords), "link", null);


        // 也可以引用系统预定义的话术，select就是系统预定义的选择意图，不需要自己定义话术就可以支持诸如： “选择”，“打开”，“点击”等话术
        // 如下注册就可以支持打开第一个，点击第一个，选择第一个等等话术,第二个，第三个同理
        HashMap<String, String> params = null;
        for (int i = 1 ; i < 3; i++) {
            params = new HashMap<>();
            params.put("index", String.valueOf(i));
            payload.addHyperUtterance(BotConstants.SELETC_TEST_URL + i, null, "select", params);
        }
        params = new HashMap<>();
        params.put("name", "地址");
        params.put("type", "city");
        payload.addHyperUtterance(BotConstants.INPUT_TEST_URL, null, "input", params);

        params = new HashMap<>();
        params.put("name", "日程表");
        payload.addHyperUtterance(BotConstants.OPEN_CANLENDAR_URL, null, "button", params);

        params = new HashMap<>();
        params.put("name", "课程表");
        payload.addHyperUtterance(BotConstants.OPEN_CANLENDAR_URL, null, "button", params);

        BotSdk.getInstance().updateUiContext(payload);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                BotSdk.getInstance().speakRequest("点击了登录");
                break;
            case R.id.sign_up_btn:
                BotSdk.getInstance().speakRequest("点击了注册");
                break;
            case R.id.select_one:
                BotSdk.getInstance().speakRequest("点击了第一个");
                break;
            case R.id.select_two:
                BotSdk.getInstance().speakRequest("点击了第二个");
                break;
            case R.id.open_calendar:
                BotSdk.getInstance().speakRequest("点击了课程表");
                break;
        }
    }
}
