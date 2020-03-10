# 更新说明

## BotSdk 1.35.5版本发版
- [依赖方式](https://github.com/dueros/AndroidBotSdkDemo#%E6%96%B0%E7%9A%84%E5%AE%89%E5%8D%93%E9%A1%B9%E7%9B%AE%E9%9B%86%E6%88%90) botsdk版本改成1.35.5即可
- 变更内容
  * 增加打断tts接口演示
  * 增加未识别query，直接返回query原文能力
  * 增加唤醒状态/TTS播报状态回调
- 注意事项
  * 此次新增能力依赖小度设备Launcher版本号大于等于1.36, 可以通过```adb shell pm dump com.baidu.launcher|grep -i version```命令看到Launcher版本号，VersionName前两位大于等于1.36就行
  * 修改内容可以参考commitId:f79fef0ccf977a68544f0a799c61eab5a901342a

