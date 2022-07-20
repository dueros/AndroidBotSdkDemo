package com.baidu.duer.test_botsdk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.baidu.duer.bot.BotMessageProtocol;
import com.baidu.duer.bot.directive.payload.JsonUtil;
import com.baidu.duer.bot.event.payload.LinkClickedEventPayload;
import com.baidu.duer.bot.util.Constants;
import com.baidu.duer.botsdk.BotIntent;
import com.baidu.duer.botsdk.BotSdk;
import com.baidu.duer.botsdk.util.RequestBotSdkUtil;
import com.baidu.duer.botsdk.util.ResourceProgressElapsedEventPayload;
import com.baidu.duer.test_botsdk.R;
import com.baidu.duer.test_botsdk.botsdk.BotMessageListener;
import com.baidu.duer.test_botsdk.botsdk.IBotIntentCallback;
import com.baidu.duer.test_botsdk.utils.BotConstants;
import com.baidu.duer.test_botsdk.utils.DeviceInfoUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 自定义交互组件Demo页面，包括如下demo
 * 1，处理自定义交互组件
 * 2，处理系统预定义交互组件，比如选择第几个，输入XX等
 */
public class GetDeviceInfoFragment extends Fragment implements View.OnClickListener, IBotIntentCallback {


    private TextView mResultIntentTv;
    private Button mReadPassInfo;
    private Button mReadClientId;
    private Button mReadBduss;
    private Button mReadUserId;
    private Button mReadDeviceUniqueId;
    private Button mReadDeviceResettableUniqueId;
    private Button mReportClickLinkEvent;
    private Button mReportResourceProgressEvent;
    private Button mModifyWakeUpStatus;
    private Button mTriggerFaceUnlock;
    private Button mRequestCameraInfo;
    private Button mRegisteGestureList;

    private Button clearLogBtn;

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
        mReadDeviceUniqueId = view.findViewById(R.id.read_device_unique_id);
        mReadDeviceUniqueId.setOnClickListener(this);
        mReadDeviceResettableUniqueId = view.findViewById(R.id.read_device_resettable_unique_id);
        mReadDeviceResettableUniqueId.setOnClickListener(this);
        mReportClickLinkEvent = view.findViewById(R.id.report_link_clicked_event);
        mReportClickLinkEvent.setOnClickListener(this);
        mReportResourceProgressEvent = view.findViewById(R.id.report_resource_progress_event);
        mReportResourceProgressEvent.setOnClickListener(this);
        mModifyWakeUpStatus = view.findViewById(R.id.update_modify_wakeup_state);
        mModifyWakeUpStatus.setOnClickListener(this);
        mTriggerFaceUnlock = view.findViewById(R.id.request_face_unlock);
        mTriggerFaceUnlock.setOnClickListener(this);
        mRequestCameraInfo = view.findViewById(R.id.request_camera_info);
        mRequestCameraInfo.setOnClickListener(this);
        mRegisteGestureList = view.findViewById(R.id.register_gesture_list);
        mRegisteGestureList.setOnClickListener(this);

        clearLogBtn = view.findViewById(R.id.clear_log_btn);
        clearLogBtn.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read_device_unique_id:
                /** 获取设备唯一不变id，要求系统版本 1.59 及以上。此id不会随着设备重置改变，仅开放给白名单应用读取。
                 * 开发者可以联系小度接口人，申请加入白名单 */
                BotSdk.getInstance().triggerDuerOSCapacity(
                        BotMessageProtocol.DuerOSCapacity.AI_DUER_SHOW_REQUEST_DEVICE_UNIQUE_ID,
                        null);
                break;
            case R.id.read_device_resettable_unique_id:
                /** 获取设备唯一id(设备重置后id会变，可以保证唯一性），要求系统版本 1.59 及以上。此id会随着设备重置改变,不设读取权限控制 */
                BotSdk.getInstance().triggerDuerOSCapacity(
                        BotMessageProtocol.DuerOSCapacity.AI_DUER_SHOW_REQUEST_DEVICE_RESETTABLE_UNIQUE_ID,
                        null);
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
            case R.id.report_link_clicked_event:
                LinkClickedEventPayload linkClickedEventPayload = new LinkClickedEventPayload();
                linkClickedEventPayload.url =
                        "dueros://6402fc3a-7825-3147-b52e-c25911b6d03c/entity/swan/add?recommendId=recommend_habit_operative_xiashuoyingwen&habitDetail=%7b%22repeatDays%22%3a%5b%22MON%22%2c%22TUE%22%2c%22WED%22%2c%22THU%22%2c%22FRI%22%2c%22SAT%22%2c%22SUN%22%5d%2c%22startTime%22%3a%2208%3a18%22%2c%22duration%22%3a0%2c%22enableTriggerMode%22%3atrue%2c%22triggerMode%22%3a2%2c%22enableAutoClose%22%3atrue%2c%22autoCloseOptionSelected%22%3a%7b%22text%22%3a%2290%e5%88%86%e9%92%9f%22%2c%22id%22%3a0%2c%22activated%22%3atrue%7d%7d&callback=callback__o_0__d934a2128b5944648639e4e06c3ad737cb&habitChannel=THIRD_PARTY_DUYAYA_TEST";
                BotSdk.getInstance().uploadLinkClickedEvent(linkClickedEventPayload);
                break;
            case R.id.report_resource_progress_event:
                /**
                 * 上报资源消费事件
                 * 开发者可根据应用内部资源情况，按需填入ResourceProgressElapsedEventPayload结构体相应字段中
                 * 各个字段描述详见Github文档
                 */
                ResourceProgressElapsedEventPayload payload = new ResourceProgressElapsedEventPayload();
                /** 必选字段 */
                payload.packageName = getContext().getPackageName();
                /** 必选字段 */
                payload.botId = BotConstants.BOTID;
                /** <b>以下均为可选字段 </b> */
                /** 资源总时长，单位 ms , 例如视频资源时长 3分钟，值为 3 * 60 * 1000*/
                payload.durationInMilliseconds = 30000L;
                /** 资源当前已播放时长，单位 ms, 例如视频播放到1分半，上报 1.5 * 60 * 1000
                 * <b>为了保证进度上报有效性（避免因为进程退出等场景，导致进度上报失败）建议在资源消费期间，采用30s间隔定时上报逻辑。
                 * 这样就可以保证，资源上报信息，最多误差也就30s。不建议降低上报间隔，避免频繁上报对链路产生过大压力 </b> */
                payload.offsetInMilliseconds = 100L;
                /** 剧集，例如第一集，上报 1*/
                payload.episode = 1L;
                /** 资源唯一id */
                payload.resourceId = "1000";
                /** 资源地址 */
                payload.resourceUrl = "https://dueros.baidu.com/";
                /** 资源描述信息 */
                payload.resourceDesc = "测试资源描述";
                payload.token = BotConstants.token;
                /** 开始播放（包含start，resume）*/
                // payload.progressType = ResourceProgressElapsedEventPayload.ProgressType.PlaybackStarted.toString();
                /** 播放进度上报, SDK中会做频控，上报间隔>=30s  */
                payload.progressType = ResourceProgressElapsedEventPayload.ProgressType.ProgressElapsed.toString();
                /** 停止播放（包含 stop, pause, <b>不包含FINISH</b>）*/
                // payload.progressType = ResourceProgressElapsedEventPayload.ProgressType.PlaybackStop.toString();
                /** 完播, 仅仅代表FINISH */
                // payload.progressType = ResourceProgressElapsedEventPayload.ProgressType.PlaybackFinished.toString();
                payload.statisticData = BotConstants.statisticData;
                BotSdk.getInstance().reportResourceProgressEvent(payload);

                break;
            case R.id.update_modify_wakeup_state:
                BotSdk.getInstance().triggerDuerOSCapacity(BotMessageProtocol.DuerOSCapacity.AI_DUER_SHOW_DISABLE_WAKEUP, null);
                break;
            case R.id.request_face_unlock:
                BotMessageProtocol.BaseDuerCapacityParamInfo paramInfo = new BotMessageProtocol.BaseDuerCapacityParamInfo();
                paramInfo.requestId = RequestBotSdkUtil.generateRequestId();
                BotSdk.getInstance().triggerDuerOSCapacity(BotMessageProtocol.DuerOSCapacity.AI_DUER_SHOW_OPEN_FACE_UNLOCK, JsonUtil.toJson(paramInfo));
                break;
            case R.id.request_camera_info:
                /**
                 * 通过下述方法调用获取camera状态
                 * camera状态返回结果通过HandleIntent指令返回，可以参考{@link handleIntent}方法处理返回意图
                 * 意图名称：com.baidu.duer.cameraStateChanged
                 * curstomData: ENABLED
                 * <b>后续只要应用在前台，如果camera状态发生变化，都会通过此意图通知App</b>
                 */
                BotSdk.getInstance().triggerDuerOSCapacity(BotMessageProtocol.DuerOSCapacity.AI_DUER_SHOW_GET_CAMERA_STATE, null);
                break;
            case R.id.register_gesture_list:
                // 注册手势列表到系统，如果手势列表传空，表示当前不再支持手势模式
                // （因为支持手势，系统侧需要运行视觉相关服务，切到不支持手势的页面，请清空手势支持）
                BotMessageProtocol.GestureRegisterParams gestureRegisterParams =
                        new BotMessageProtocol.GestureRegisterParams();
                gestureRegisterParams.enabledGestures = new ArrayList<>();
                gestureRegisterParams.enabledGestures.add(BotMessageProtocol.AiDuerGesture.GESTURE_OK);
                gestureRegisterParams.enabledGestures.add(BotMessageProtocol.AiDuerGesture.GESTURE_PALM);
                gestureRegisterParams.enabledGestures.add(BotMessageProtocol.AiDuerGesture.GESTURE_LEFT);
                gestureRegisterParams.enabledGestures.add(BotMessageProtocol.AiDuerGesture.GESTURE_RIGHT);
                BotSdk.getInstance().triggerDuerOSCapacity(BotMessageProtocol.DuerOSCapacity.AI_DUER_SHOW_GESTURE_REGISTER,
                        JsonUtil.toJson(gestureRegisterParams));
                break;
            case R.id.clear_log_btn:
                mResultIntentTv.setText(R.string.result_intent);
                break;
        }
    }

    @Override
    public void handleIntent(BotIntent intent, String customData) {
        String intentResult = getString(R.string.result_intent) + "\n指令名称:%s\n槽位信息：%s\n自定义信息：%s";
        mResultIntentTv.setText(String.format(intentResult, intent.name, intent.slots, customData));

    }

    @Override
    public void onClickLink(String url, HashMap<String, String> paramMap) {

    }

    @Override
    public void onHandleScreenNavigatorEvent(int event) {

    }
}
