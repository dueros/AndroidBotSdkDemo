package com.baidu.duer.test_botsdk.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.duer.botsdk.BotIntent;

import java.util.List;

/**
 * 解析意图名称和槽位信息的工具类
 */
public class IntentDecodeUtil {

    private static final String TAG = "IntentDecodeUtil";

    /**
     * 解析拖动进度意图的槽位信息，返回拖动的秒数
     *
     * @param slots 槽位信息
     * @return 快进总秒数
     */
    public static int decodeSeekIntentSlot(@Nullable final List<BotIntent.Slot> slots) {
        int result = 0;
        if (slots == null || slots.isEmpty()) {
            Log.w(TAG, "decode seek intent slot fail, slots is empty");
            return result;
        }
        String timeSlotValueName;
        for (BotIntent.Slot slot : slots) {
            timeSlotValueName = null;
            switch (slot.value) {
                case BotConstants.TIME_UNIT_SECOND:
                    timeSlotValueName =
                            BotConstants.SYS_NUMBER + slot.name.substring(BotConstants.TIME_UNIT.length());
                    result += Integer.valueOf(getSlotNumberValueBySlotName(slots, timeSlotValueName));
                    break;
                case BotConstants.TIME_UNIT_MINUTE:
                    timeSlotValueName =
                            BotConstants.SYS_NUMBER + slot.name.substring(BotConstants.TIME_UNIT.length());
                    result += Integer.valueOf(getSlotNumberValueBySlotName(slots, timeSlotValueName)) * 60;
                    break;
                default:
                    Log.w(TAG, "unknown slot value");
                    break;
            }
            Log.i(TAG, "slot name:" + timeSlotValueName + " result:" + result);
        }
        return result;
    }

    private static String getSlotNumberValueBySlotName(@NonNull final List<BotIntent.Slot> slots,
                                                       @NonNull final String slotName) {
        for (BotIntent.Slot slot : slots) {
            if (slot.name.equals(slotName)) {
                return slot.value;
            }
        }
        return "0";
    }
}
