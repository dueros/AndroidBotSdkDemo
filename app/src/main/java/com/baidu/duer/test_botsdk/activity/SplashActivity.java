package com.baidu.duer.test_botsdk.activity;

import com.baidu.duer.botsdk.BotSdk;
import com.baidu.duer.test_botsdk.R;
import com.baidu.duer.test_botsdk.botsdk.BotMessageListener;
import com.baidu.duer.test_botsdk.botsdk.BotSDKUtils;
import com.baidu.duer.test_botsdk.utils.BotConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BotSdk.getInstance().register(BotMessageListener.getInstance(), BotConstants.BOTID, BotSDKUtils.getAppKey());

        setContentView(R.layout.splash_layout);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BotConstants.ACTION_REGISTER_SUCCESS);
        intentFilter.addAction(BotConstants.MAIN_ACTIVITY_LAUNCH_SUCCESS);
        LocalBroadcastManager.getInstance(SplashActivity.this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction() != null) {
                    switch (intent.getAction()) {
                        case BotConstants.ACTION_REGISTER_SUCCESS:
                            Toast.makeText(SplashActivity.this, "注册成功!", Toast.LENGTH_LONG).show();
                            startMainActivity();
                            break;
                        case BotConstants.MAIN_ACTIVITY_LAUNCH_SUCCESS:
                            Log.i(TAG, "main activity launch success and finish splash activity:");
                            finish();
                            break;
                        default:
                            Log.i(TAG, "unknown action:" + intent.getAction());
                            break;
                    }
                }
            }
        }, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void startMainActivity() {
        Intent intent1 = new Intent(SplashActivity.this, BotSdkDemoActivity.class);
        startActivity(intent1);
    }
}
