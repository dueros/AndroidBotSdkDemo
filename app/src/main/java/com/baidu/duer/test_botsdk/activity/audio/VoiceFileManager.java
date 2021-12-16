package com.baidu.duer.test_botsdk.activity.audio;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class VoiceFileManager {
    private static final int RECORDER_BPP = 16;
    private static final String TAG = "VoiceFileManager";
    public static VoiceFileManager mVoiceFileManager = null;
    private String currentFilePath;
    private RandomAccessFile randomAccessFile = null;

    public String getCurrentFilePath() {
        return currentFilePath;
    }

    public void setCurrentFilePath(String currentFilePath) {
        this.currentFilePath = currentFilePath;
    }

    public static VoiceFileManager getInstance() {
        if (mVoiceFileManager == null) {
            mVoiceFileManager = new VoiceFileManager();
        }
        return mVoiceFileManager;
    }

    public String startOpenFile() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {

            File sdcardDir = Environment.getExternalStorageDirectory();

            String path = sdcardDir.getPath() + "/AudioRecorder";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-HH-mm");
            String timestamp = sdf.format(date);
            Log.d("m", "saveToFile" + timestamp);
            String filePath = file.getAbsolutePath() + "/" + "B-" + timestamp + ".wav";

            File f = new File(filePath);
            try {
                randomAccessFile = new RandomAccessFile(f, "rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                WriteWaveFileHeader(randomAccessFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            setCurrentFilePath(filePath);
            return filePath;
        }
        return null;
    }

    public void closeFile() {
        try {
            randomAccessFile.seek(4);
            randomAccessFile.writeInt(Integer.reverseBytes((int) (randomAccessFile.length() - 8)));

            randomAccessFile.seek(40); // data chunk size
            randomAccessFile.writeInt(Integer.reverseBytes((int) (randomAccessFile.length() - 44)));

            Log.d(TAG, "wav size: " + randomAccessFile.length());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        randomAccessFile = null;
    }

    public void saveToFile(byte[] data) {
        try {
            randomAccessFile.write(data);
            Log.d(TAG, "fwrite: " + data.length);
        } catch (Exception e) {
            Log.e(TAG, "file write error");
        }
    }

    private void WriteWaveFileHeader(RandomAccessFile file) throws IOException {
        /* RIFF header */
        file.writeBytes("RIFF"); // riff id
        file.writeInt(0); // riff chunk size *PLACEHOLDER*
        file.writeBytes("WAVE"); // wave type

        /* fmt chunk */
        file.writeBytes("fmt "); // fmt id
        file.writeInt(Integer.reverseBytes(16)); // fmt chunk size
        file.writeShort(Short.reverseBytes((short) 1)); // format: 1(PCM)
        file.writeShort(Short.reverseBytes((short) 1)); // channels: 1
        file.writeInt(Integer.reverseBytes(16000)); // samples per second
        file.writeInt(Integer.reverseBytes((int) (1 * 16000 * 16 / 8))); // BPSecond
        file.writeShort(Short.reverseBytes((short) (1 * 16 / 8))); // BPSample
        file.writeShort(Short.reverseBytes((short) (1 * 16))); // bPSample

        /* data chunk */
        file.writeBytes("data"); // data id
        file.writeInt(0); // data chunk size *PLACEHOLDER*
    }

}
