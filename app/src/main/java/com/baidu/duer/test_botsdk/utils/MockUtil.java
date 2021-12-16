package com.baidu.duer.test_botsdk.utils;

import com.baidu.duer.bot.directive.payload.AmountInfo;
import com.baidu.duer.bot.event.payload.SellerOrderStructure;

public class MockUtil {

    /**
     * 产生一个固定的金额信息
     *
     * @return amountinfo
     */
    public static AmountInfo mockAmountInfo() {
        AmountInfo amountInfo = new AmountInfo();
        amountInfo.amount = "0.01";
        amountInfo.currencyCode = "CNY";
        return amountInfo;
    }

    /**
     * 构造卖家订单信息
     *
     * @return
     */
    public static SellerOrderStructure mockSellerOlrderStructure() {
        SellerOrderStructure sellerOrderStructure = new SellerOrderStructure();
        sellerOrderStructure.productId = "111" + System.currentTimeMillis();
        sellerOrderStructure.description = "测试商品介绍";
        sellerOrderStructure.sellerOrderId = "222" + System.currentTimeMillis();
        sellerOrderStructure.productName = "测试商品名称";
        sellerOrderStructure.sellerNote = "测试商品备注";
        return sellerOrderStructure;
    }

    /**
     * 构造手机号一键授权的url
     *
     * @return
     */
    public static String mockGrantPhoneNumberUrl() {
        String url = "http://%s/path?openbot=true&request={\\\"query\\\":{\\\"type\\\":\\\"TEXT\\\","
                + "\\\"original\\\":\\\"手机号授权\\\",\\\"rewritten\\\":\\\"手机号授权\\\"},"
                + "\\\"dialogState\\\":\\\"COMPLETED\\\","
                + "\\\"intents\\\":[{\\\"name\\\":\\\"AskForPermissionsConsentRequired\\\",\\\"score\\\":100,"
                + "\\\"confirmationStatus\\\":\\\"NONE\\\","
                + "\\\"slots\\\":{\\\"permission\\\":{\\\"name\\\":\\\"permission\\\","
                + "\\\"value\\\":\\\"READ::USER:PHONE\\\",\\\"values\\\":[\\\"READ::USER:PHONE\\\"],"
                + "\\\"score\\\":0,\\\"confirmationStatus\\\":\\\"NONE\\\"}}}]}";
        return String.format(url, BotConstants.BOTID);
    }

}
