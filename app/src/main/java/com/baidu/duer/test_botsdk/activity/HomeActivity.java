package com.baidu.duer.test_botsdk.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.duer.test_botsdk.R;
import com.baidu.duer.test_botsdk.activity.audio.AudioDemoActivity;
import com.baidu.duer.test_botsdk.activity.camera.CameraActivity;

/**
 * 背景：因为小度设备软硬件跟Android原生设备有差异（例如只有一个前置Camera，只开放MUSIC的audio通路给开发者）
 * 导致部分在Android原生设备可以工作的代码，迁移到小度设备后，无法正常工作
 * 所以我们提供如下demo，希望可以帮助开发者在小度设备上快速跑通业务流程
 * demo列表如下
 * 1，调起Camera进行拍照，并获取对应的照片内容。
 * 2，调起Camera进行录像，并获取对应的视频文件（包括录像和录音）
 * 3，播放音频文件并调节音量大小。
 * <b>如下是小度设备不支持的能力</b>
 * 4，小度设备不支持在通知栏展示通知，
 * 5，小度设备音频播放只支持STREAM_MUSIC，对于其他类似STREAM_VOICE_CALL,STREAM_RING,STREAM_ALARM等均不支持
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DuerOSDemo-HomeActivity";

    private TextView mDemoDesc;
    private Button mCameraDemo;
    private Button mAudioDemo;
    private Button mBotSdkDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDemoDesc = findViewById(R.id.demo_desc);
        mDemoDesc.setText(Html.fromHtml("因为小度设备软硬件跟Android原生设备有差异（例如只有一个前置Camera，只开放<b>MUSIC的audio通路</b>给开发者）,所以提供如下demo，帮助开发者快速调通Camera和Audio调用，专注业务开发，提升效率。"));
        mCameraDemo = findViewById(R.id.dueros_camera_demo);
        mCameraDemo.setOnClickListener(this);
        mAudioDemo = findViewById(R.id.dueros_audio_demo);
        mAudioDemo.setOnClickListener(this);
        mBotSdkDemo = findViewById(R.id.dueros_botsdk_demo);
        mBotSdkDemo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dueros_audio_demo:
                Log.i(TAG, "trigger audio demo");
                Intent intentForAudio = new Intent(this, AudioDemoActivity.class);
                startActivity(intentForAudio);
                break;
            case R.id.dueros_camera_demo:
                Intent intentForCamera = new Intent(this, CameraActivity.class);
                startActivity(intentForCamera);
                Log.i(TAG, "trigger camera demo");
                break;
            case R.id.dueros_botsdk_demo:
                Log.i(TAG, "trigger botsdk demo and start local register");
                // 走离线校验
                Intent intentForBotSdk = new Intent(this, SplashActivity.class);
                startActivity(intentForBotSdk);
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}