package com.baidu.duer.test_botsdk.utils;

import com.baidu.duer.bot.directive.payload.AmountInfo;
import com.baidu.duer.bot.event.payload.SellerOrderStructure;

public class MockUtil {

    /**
     * 产生一个固定的金额信息
     * @return amountinfo
     */
    public static AmountInfo mockAmountInfo() {
        AmountInfo amountInfo = new AmountInfo();
        amountInfo.amount = "0.99";
        amountInfo.currencyCode = "CNY";
        return amountInfo;
    }

    public static SellerOrderStructure mockSellerOlrderStructure() {
        SellerOrderStructure sellerOrderStructure = new SellerOrderStructure();
        sellerOrderStructure.productId = "111";
        sellerOrderStructure.description = "测试商品介绍";
        sellerOrderStructure.sellerOrderId = "222";
        sellerOrderStructure.productName = "测试商品名称";
        sellerOrderStructure.sellerNote = "测试商品备注";
        return sellerOrderStructure;
    }

}
