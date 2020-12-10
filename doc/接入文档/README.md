# BotSDK 接入文档

这个目录是Android APP接入BotSDK的全部文档，正式开发之前请通读文档。
**DBP升级，支持直接创建Android类型技能，文档中所有需要提交技能信息给到百度接口人的流程，现在可以直接忽略了**

## 基础概念

## APP Demo 快速入门

这一部分可以协助APP开发者快速跑起来一个demo，验证环境是否可用。
 - [快速接入](https://github.com/dueros/AndroidBotSdkDemo/wiki/1.%E5%BF%AB%E9%80%9F%E5%BC%80%E5%A7%8B---APK%E6%8E%A5%E5%85%A5%E5%B0%8F%E5%BA%A6%E8%AE%BE%E5%A4%87)

基础概念部分包含系统架构介绍，平台使用指南。文档中会介绍一些基础概念，这些基础概念可以帮助理解整个系统工作原理。
 - [DuerOS接入快速指南-App接入.pdf](DuerOS%E6%8E%A5%E5%85%A5%E5%BF%AB%E9%80%9F%E6%8C%87%E5%8D%97-App%E6%8E%A5%E5%85%A5.pdf)

## 支付
用户可以在App内部发起会员权益或者虚拟物品的购买行为。DuerOS可以提供收银台，支付结果通知和发货指令的能力。调用DuerOS发起支付流程可以参考文档：[支付接入方案](https://github.com/dueros/AndroidBotSdkDemo/wiki/%E9%99%843.-%E6%8A%80%E8%83%BD%E5%86%85%E4%BB%98%E8%B4%B9)

## 手机号一键授权功能接入
“手机号一键授权”指的是，三方技能请求度秘云端，小度设备可以弹出一个授权弹窗提示用户:"某某技能正在尝试获取设备登录的百度账户绑定的手机号......",用户确认授权之后，三方技能就可以拿到用户手机号。如果有获取用户手机号的需求，可以参考如下文档
 - [手机号一键授权接入文档](https://demo.codimd.org/s/B1DnQ4RuH)

## 技能外售卖功能接入
“技能外售卖”指的不用打开app，用户就可以在小度的其他销售渠道售卖app内的商品，例如小度app、小度在家设备端、双十一等运营活动。用户购买后打开商品对应的app后，app识别设备百度帐号下的售卖信息，并完成发货，用户即可享受商品权益。
如果有接入技能外售卖的需求，可以参考如下文档
 - [技能外售卖接入文档](https://demo.codimd.org/s/rJChB3AOH)

## H5应用接入DuerOS
H5应用接入DuerOS可以参考[H5-BotSDK](https://github.com/dueros/h5-bot-sdk)

## DuerOS开放能力
应用可以根据自己特殊场景需求，通过DuerOS开放能力接口改变设备状态，比如：关闭唤醒，启用唤醒，打断tts，启用手势识别，获取当前设备登录账户的用户信息等等.所有开放能力可以通过BotSdk接口``` BotSdk.getInstance().triggerDuerOSCapacity```对外输出，可以在 [BotSdk Java Doc](https://github.com/dueros/AndroidBotSdkDemo/blob/master/doc/%E6%8E%A5%E5%85%A5%E6%96%87%E6%A1%A3/BotSdkDoc.zip)看到详细接口文档.

开放能力需要特殊权限，有需要请联系百度接口人.
