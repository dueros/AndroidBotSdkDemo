package com.baidu.duer.test_botsdk.activity.audio;

import android.util.Log;

public class AppLog {
        private static final String APP_TAG = "AudioRecorder";
        
        public static int logString(String message){
                return Log.i(APP_TAG,message);
        }
}
