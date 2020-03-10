package com.baidu.duer.test_botsdk.fragment;

import java.util.Arrays;
import java.util.HashMap;

import com.baidu.duer.bot.BotMessageProtocol;
import com.baidu.duer.bot.event.payload.LinkClickedEventPayload;
import com.baidu.duer.botsdk.BotIntent;
import com.baidu.duer.botsdk.BotSdk;
import com.baidu.duer.botsdk.IDialogStateListener;
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
public class UiControlFragment extends Fragment implements IBotIntentCallback, View.OnClickListener,
        IDialogStateListener {


    private TextView mResultIntentTv;
    private Button mLoginBtn;
    private Button mRegisterBtn;
    private Button mSelectOne;
    private Button mSelectTwo;
    // 打断tts播报
    private Button mInterruptTts;
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
        mInterruptTts = view.findViewById(R.id.interrupt_tts);
        mInterruptTts.setOnClickListener(this);
    }


    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "on fragment attach");
        super.onAttach(context);
        BotMessageListener.getInstance().addCallback(this);
        BotSdk.getInstance().setDialogStateListener(this);
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "on fragment detach");
        super.onDetach();
        BotMessageListener.getInstance().removeCallback(this);
        BotSdk.getInstance().setDialogStateListener(null);
    }

    @Override
    public void handleIntent(BotIntent intent, String customData) {

        String intentResult = getString(R.string.result_intent) + "\n指令名称:%s\n槽位信息：%s";
        if (BotConstants.FAST_FORWARD_INTENT.equals(intent.name)) {
            int seekNumber = IntentDecodeUtil.decodeSeekIntentSlot(intent.slots);
            intentResult = intentResult + "\n拖动时长：" + seekNumber;
        }
        mResultIntentTv.append(String.format(intentResult, intent.name, intent.slots));
        mResultIntentTv.append("\n");
    }

    @Override
    public void onClickLink(String url, HashMap<String, String> paramMap) {
        String intentResult = getString(R.string.result_intent) + "\n指令匹配url:%s\n参数信息:%s";
        mResultIntentTv.append(String.format(intentResult, url, paramMap));
        mResultIntentTv.append("\n");

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
        /**
         * 增加未识别话术兜底透传机制，用户说到了未识别的预定义话术，可以把用户query原文透传到App内，App可以根据用户query结果做逻辑处理
         * <b>建议自己有兜底语义理解需求的客户设置此开关为false，自身没有语义理解服务的客户请不要调用此代码！</b>
         */
        payload.setEnableGeneralUtterances(false);
        String[] loginWords = {"登录", "点击登录"};
        // 可以定义支持的话术, link固定写法
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
            case R.id.interrupt_tts:
                // 打断当前正在播报的tts，参数传空就行
                mResultIntentTv.append("打断了Tts播报\n");
                BotSdk.getInstance().triggerDuerOSCapacity(BotMessageProtocol.DuerOSCapacity.AI_DUER_SHOW_INTERRPT_TTS,
                        null);
                break;
            case R.id.open_calendar:
                BotSdk.getInstance().speakRequest("点击了课程表");
                break;
        }
    }

    /**
     * 当前聆听状态回调，包含
     * {@link IDialogStateListener.DialogState#IDLE} 空闲态
     * {@link IDialogStateListener.DialogState#LISTENING} 聆听中
     * {@link IDialogStateListener.DialogState#SPEAKING} 语音播报中
     * {@link IDialogStateListener.DialogState#THINKING} 语义识别中
     * @param dialogState
     */
    @Override
    public void onDialogStateChanged(DialogState dialogState) {
        mResultIntentTv.append("唤醒窗口状态:" + dialogState.name());
        mResultIntentTv.append("\n");
    }
}
