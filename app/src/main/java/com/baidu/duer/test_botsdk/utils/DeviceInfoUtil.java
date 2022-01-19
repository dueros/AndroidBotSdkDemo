package com.baidu.duer.test_botsdk.utils;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 获取用户和设备信息的工具类
 */
public class DeviceInfoUtil {

    private static final String DeviceInfoAuthorize = "content://com.baidu.baiduaccount.provider.userinfoprovider/";
    public static final String PATH_BDUSS = "bduss";
    public static final String PATH_DEVICE_BDUSS = "device_bduss";
    public static final String PATH_PASS_INFO = "accountinfo";
    public static final String PATH_USER_ID = "userid";
    public static final String PATH_CLIENT_ID = "device_client_id";
    public static final String FAMILY_NUMBER = "family_number";
    public static final String DUMI_USER_DEVICE_ID = "dumi_user_device_id";
    public static final String DUMI_CUID = "dumi_cuid";

    /**
     * 读取UserInfoProvider
     *
     * @param infoPath provider path
     * @return value
     */
    public static String getUserInfo(@NonNull final String infoPath) {
        String result = null;
        try {
            final Cursor cursor = ContextUtil.getContext().getContentResolver()
                    .query(Uri.parse(DeviceInfoAuthorize + infoPath), null, null, null, null);

            if (cursor != null && cursor.getColumnNames() != null && cursor.moveToFirst()) {
                String[] columns = cursor.getColumnNames();
                for (int i = 0; i < columns.length; i++) {
                    if (Objects.equals(columns[i], infoPath)) {
                        result = cursor.getString(i);
                        break;
                    }
                }
                if (TextUtils.isEmpty(result)) {
                    result = "can not find column for infoPath:" + infoPath;
                }
            } else {
                Log.e("DeviceInfoUtil", "get " + infoPath + "fail and curosr is: " + cursor);
                result = "colums is null or cursor is empty";
            }
        } catch (Exception e) {
            // 用于处理remote exception, permission denial exception
            Log.e("DeviceInfoUtil", "encounter exception when add bduss", e);
            result = e.getMessage();
        }
        return result;
    }

    /**
     * 获取当前设备的麦克风数量
     * @return 麦克风数量
     */
    public static String getMicNumber() {
        try {
            Class<?> classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", String.class);
            return (String) getMethod.invoke(classType, new Object[]{"persist.audio.mic_num"});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "0";
    }
}
