# 更新说明

## 依赖方式和版本确认
- 参考[**依赖文档**](https://github.com/dueros/AndroidBotSdkDemo#%E6%96%B0%E7%9A%84%E5%AE%89%E5%8D%93%E9%A1%B9%E7%9B%AE%E9%9B%86%E6%88%90) ,替换sdkbotsdk版本改成到对应数字即可即可
- 依赖版本确认：可以通过如下命令看到Launcher版本号
   ```
    adb shell pm dump com.baidu.launcher|grep -i version
   ``` 

## 发布历史

### 1.45.0
- 变更内容
  * 移除OnePixelActivity，提升启动速度。**要求系统版本>=Sp43**

### 1.44.0
- 变更内容
  * 修复云应用事件上报包名异常问题。
  
### 1.40.0
- 变更内容
  * 增加手势能力支持。[手势文档](https://github.com/dueros/AndroidBotSdkDemo/wiki/2.-DuerShow%E7%89%B9%E8%89%B2%E8%83%BD%E5%8A%9B-%E6%89%8B%E5%8A%BF%E8%AF%86%E5%88%AB)
- Launcher版本依赖，Launcher版本大于等于1.40.0.0
- Demo提交：7f13525b6ce64652cd53f6912e1948506cdeff8f

### 1.35.5
- 变更内容
  * 增加打断tts接口演示
  * 增加未识别query，直接返回query原文能力
  * 增加唤醒状态/TTS播报状态回调
- Launcher版本依赖：大于1.36.0.0
- Demo提交：commitId:*f79fef0ccf977a68544f0a799c61eab5a901342a*
### 1.35.2
- 变更内容
  * 增加Camera 启用/禁用 状态获取和通知接口
- Launcher版本依赖:大于1.37.0.0
- Demo提交：commitId:*6dda6bc3b2497029212a57d9bb3cc7c05a5183cc*
