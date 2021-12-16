package com.baidu.duer.test_botsdk.botsdk;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.baidu.duer.test_botsdk.utils.BotConstants;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * BotSDK相关工具方法
 */
public class BotSDKUtils {

    /**
     * 获取输入info的MD5值
     *
     * @param s 输入字符串
     * @return
     */
    @Nullable
    public static String getMd5(@NonNull final String s) {
        String result = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(s.getBytes("UTF-8"));
            byte[] md5Array = md5.digest();
            result = byteToHex(md5Array);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * byte to Hex String
     *
     * @param md5Array md5Array
     * @return hex String
     */
    @NonNull
    public static String byteToHex(@NonNull byte[] md5Array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < md5Array.length; i++) {
            String hexString = Integer.toHexString(0xFF & md5Array[i]);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            stringBuilder.append(hexString);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取appkey
     *
     * @return appKey
     */
    public static String getAppKey() {
        // TODO 替换自己的appkey
        return BotConstants.SIGNATURE_KEY;
    }

    /**
     * 获取随机数的签名值
     * 签名算法md5（rand + appKey)
     *
     * @param random 随机值
     * @return 签名值
     */
    public static String sign(@NonNull final String random) {
        return getMd5(random + getAppKey());
    }
}
