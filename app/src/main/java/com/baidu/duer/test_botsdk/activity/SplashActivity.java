package com.baidu.duer.test_botsdk.activity;

import com.baidu.duer.test_botsdk.R;
import com.baidu.duer.test_botsdk.utils.BotConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BotConstants.ACTION_REGISTER_SUCCESS);
        LocalBroadcastManager.getInstance(SplashActivity.this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(SplashActivity.this, "注册成功!", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        }, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
