package com.baidu.duer.test_botsdk.activity.audio;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.duer.test_botsdk.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AudioDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AudioRecordDemoTag";
    private final int PERMISSION_REQUEST_CODE = 0X1008;
    private final int DURATION_UPDATE_INTERVAL = 50;
    private static int frequency = 16000;
    private static final int RECORDER_BPP = 16;
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";

    private Button mStartRecordDemo;
    private TextView mRecordDuration;
    private Button mPlayRecord;
    private RadioGroup mVoiceSelectorContainer, mFrequencySelectorContainer, mBitWidthSelectorContainer;
    private RadioButton mRadioDoubleChannel, mRadioSingleChannel, mFrequency16K, mFrequency22K, mFrequency11K, mBitWidth16, mBitWidth8;

    private final Handler mMainHandler = new Handler();

    private static int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private static int EncodingBitRate = AudioFormat.ENCODING_PCM_16BIT;
    // 逻辑控制变量
    private boolean isRecording = false;
    private long mStartRecordTimestamp = 0;
    private boolean isRecordPlaying = false;
    private String filePath;

    private AudioRecord audioRecord = null;
    private AudioTrack audioTrack = null;
    private int recBufSize = 0;
    private int playBufSize = 0;
    private Thread recordingThread = null;
    private boolean m_keep_running;

    protected PCMAudioTrack m_player;
    SeekBar skbVolume;// 调节音量
    private final Runnable mUpdateDurationTask = new Runnable() {
        @Override
        public void run() {
            mRecordDuration.setText(getString(R.string.audio_record_duration) + getRecordDuration());
            mMainHandler.postDelayed(this, DURATION_UPDATE_INTERVAL);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        setContentView(R.layout.activity_audio_demo);
        mStartRecordDemo = findViewById(R.id.dueros_record_demo);
        mStartRecordDemo.setOnClickListener(this);
        mRecordDuration = findViewById(R.id.demo_record_duration);
        mPlayRecord = findViewById(R.id.dueros_play_record_demo);
        mPlayRecord.setOnClickListener(this);
        setButtonHandlers();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            /** 检查相关权限 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "try to request permission");
                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i] == Manifest.permission.RECORD_AUDIO && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "录音权限获取失败", Toast.LENGTH_SHORT).show();
                    return;
                } else if (permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "访问外置存储权限获取失败", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dueros_record_demo:
                AppLog.logString("click record btn and current isRecording:" + isRecording);
                if (isRecording) {
                    // 如果当前正在录音，停止录音，修改录音Btn文案，停止duration更新task
                    mMainHandler.removeCallbacks(mUpdateDurationTask);
                    mStartRecordDemo.setText(R.string.audio_start_record);
                    isRecording = false;
                    AppLog.logString("Stop Recording");
                    stopRecording();
                    Toast.makeText(this, "文件已保存到目录："+filePath, Toast.LENGTH_LONG).show();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                            && checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                    } else {
                        mStartRecordTimestamp = System.currentTimeMillis();
                        AppLog.logString("Start Recording");
                        startRecording();
                        mStartRecordDemo.setText(R.string.audio_recording);
                        mMainHandler.postDelayed(mUpdateDurationTask, DURATION_UPDATE_INTERVAL);
                        isRecording = true;
                    }
                }
                break;
            case R.id.dueros_play_record_demo:
                if (isRecordPlaying) {
                    mPlayRecord.setText(R.string.audio_start_play);
                    AppLog.logString("Stop Tracking");
                    m_player.free();
                    m_player = null;
                    mStartRecordDemo.setEnabled(true);
                } else {
                    mPlayRecord.setText(R.string.audio_playing);
                    AppLog.logString("Start Tracking");
                    m_player = new PCMAudioTrack();
                    m_player.init();
                    m_player.start();
                    mStartRecordDemo.setEnabled(false);
                }
                isRecordPlaying = !isRecordPlaying;
                break;

        }
    }

    private long getRecordDuration() {
        return System.currentTimeMillis() - mStartRecordTimestamp;
    }



    private void setButtonHandlers() {
        mVoiceSelectorContainer = (RadioGroup) findViewById(R.id.voice_channel_selector_container);
        mFrequencySelectorContainer = (RadioGroup) findViewById(R.id.frequency_selector_container);
        mBitWidthSelectorContainer = (RadioGroup) findViewById(R.id.bit_width_selector_container);
        mRadioDoubleChannel = (RadioButton) findViewById(R.id.radio_double_channel);
        mRadioSingleChannel = (RadioButton) findViewById(R.id.radio_single_channel);
        mFrequency16K = (RadioButton) findViewById(R.id.frequency_16k);
        mFrequency22K = (RadioButton) findViewById(R.id.frequency_22k);
        mFrequency11K = (RadioButton) findViewById(R.id.frequency_11k);
        mBitWidth16 = (RadioButton) findViewById(R.id.bit_width_16);
        mBitWidth8 = (RadioButton) findViewById(R.id.bit_width_8);

        mVoiceSelectorContainer.setOnCheckedChangeListener(mChangeRadio1);
        mFrequencySelectorContainer.setOnCheckedChangeListener(mChangeRadio2);
        mBitWidthSelectorContainer.setOnCheckedChangeListener(mChangeRadio3);
    }



    private void startRecording() {

        createAudioRecord();

        startSaveAudioFile();
        mFrequencySelectorContainer.setEnabled(false);
        mBitWidthSelectorContainer.setEnabled(false);
        mVoiceSelectorContainer.setEnabled(false);
        mPlayRecord.setEnabled(false);
    }

    private void startSaveAudioFile() {
        audioRecord.startRecording();

        isRecording = true;
        recordingThread = new Thread(new Runnable() {

            @Override
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");

        recordingThread.start();
    }

    private void writeAudioDataToFile() {
        byte data[] = new byte[recBufSize];
        filePath = VoiceFileManager.getInstance().startOpenFile();
        int read = 0;

        while (isRecording) {
            read = audioRecord.read(data, 0, recBufSize);

            if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                VoiceFileManager.getInstance().saveToFile(data);
            }
        }
        VoiceFileManager.getInstance().closeFile();
    }

    private void stopRecording() {
        mPlayRecord.setEnabled(true);
        if (null != audioRecord) {
            isRecording = false;
            /*
             * try { am.voiceCtrlSwitchMode(11); } catch (TvCommonException e) {
             * e.printStackTrace(); }
             */
            audioRecord.stop();
            audioRecord.release();

            audioRecord = null;
            recordingThread = null;
        }
    }


    public void createAudioRecord() {
        /*
         * Log.d(TAG, "----->ydd voice ctrl switch mode 3"); try { am.voiceCtrlSwitchMode(3); }
         * catch (TvCommonException e) { e.printStackTrace(); }
         */
        recBufSize = AudioRecord.getMinBufferSize(frequency,
                channelConfiguration, EncodingBitRate);

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, frequency,
                channelConfiguration, EncodingBitRate, recBufSize);
    }

    public void createAudioTrack() {
        playBufSize = AudioTrack.getMinBufferSize(frequency,
                channelConfiguration, EncodingBitRate);

        /**
         * 小度设备仅支持通过 {@link AudioManager#STREAM_MUSIC} 播放音频，并且调节音量也仅对MUSIC通路生效
         * 使用其他的stream type会导致音量调整不生效
         */
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
                channelConfiguration, EncodingBitRate,
                playBufSize, AudioTrack.MODE_STREAM);
    }

    class PCMAudioTrack extends Thread {

        protected byte[] m_out_bytes;

        File file;
        FileInputStream in;

        public void init() {
            try {
                file = new File(VoiceFileManager.getInstance().getCurrentFilePath());
                file.createNewFile();
                in = new FileInputStream(file);

                // in.read(temp, 0, length);

                m_keep_running = true;

                createAudioTrack();

                m_out_bytes = new byte[playBufSize];

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void free() {
            m_keep_running = false;
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                Log.d("sleep exceptions...\n", "");
            }
        }

        public void run() {
            byte[] bytes_pkg = null;
            audioTrack.play();
            while (m_keep_running) {
                try {
                    in.read(m_out_bytes);
                    bytes_pkg = m_out_bytes.clone();
                    audioTrack.write(bytes_pkg, 0, bytes_pkg.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            audioTrack.stop();
            audioTrack = null;
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    RadioGroup.OnCheckedChangeListener mChangeRadio1 =
            new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    // TODO Auto-generated method stub
                    if (checkedId == mRadioDoubleChannel.getId())
                    {

                        channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;

                    }
                    else if (checkedId == mRadioSingleChannel.getId())
                    {

                        channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
                    }
                }
            };

    RadioGroup.OnCheckedChangeListener mChangeRadio2 =
            new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    // TODO Auto-generated method stub
                    if (checkedId == mFrequency16K.getId())
                    {

                        frequency = 16000;
                    }
                    else if (checkedId == mFrequency22K.getId())
                    {

                        frequency = 22050;
                    }
                    else if (checkedId == mFrequency11K.getId())
                    {

                        frequency = 11025;
                    }
                }
            };

    RadioGroup.OnCheckedChangeListener mChangeRadio3 =
            new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId)
                {
                    // TODO Auto-generated method stub
                    if (checkedId == mBitWidth16.getId())
                    {

                        EncodingBitRate = AudioFormat.ENCODING_PCM_16BIT;
                    }
                    else if (checkedId == mBitWidth8.getId())
                    {

                        EncodingBitRate = AudioFormat.ENCODING_PCM_8BIT;
                    }
                }
            };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}