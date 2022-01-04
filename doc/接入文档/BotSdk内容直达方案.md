# BotSdk内容直达方案介绍

内容直达：用户通过特定语音指令/点击行为 打开App的指定页面，并加载对应资源的行为。
第三方apk应用在度秘设备的homefeed、qsb上推广，如果需要点击后跳转到应用内的某一个特定场景和页面，可以通过内容直达实现。

## 1.内容直达场景举例
* 内容消费场景：用户可以点击homeFeed卡片或者语音指令 打开快手/荔枝FM/B站（此处只为举例，不代表具体实现） 等App的资源页并加载对应资源开始播放
* 购物场景：用户通过指定购物类型语音指令（比如我要买音箱）打开商城类应用的商品列表页或者详情页，并加载用户搜索商品内容
* 教育场景: 用户通过语音指令（比如我要上三年级课程，我要学外教课），打开教育类应用课堂页面，加载对应课程内容
* .......
## 2.BotSdk内容直达实现方案
### 2.1度秘端配置【度秘语音云端关注】
运营同学在配置打开应用的linkclick地址中，添加特定的标记字段。dueros打开应用后，将特定的标记字段传递到第三方应用内，第三方应用根据此标记字段打开特定的场景和页面。

**linkclick地址格式（度秘运营同学配置)**
```
dueros://{{botId}}/path?openbot=true&request={\"query\":{\"type\":\"TEXT\",\"original\":\"打开技能\"},\"intents\":[{\"name\":\"OpenBot\",\"slots\":{\"url\":{\"name\":\"url\",\"value\":\"{{url}}\"},\"action\":{\"name\":\"action\",\"value\":\"{{action}}\"}}}]}
```
参数说明
- {{botId}}：技能id
- {{action}}：要下发的页面
- {{url}}：要下发的参数

### 2.2botsdk拉起流程【BotSdk关注】
- BotSdk目前支持的页面拉起方式,通过 packageName + action（默认匹配启动页）匹配符合条件的activity，如果匹配结果不为空，则拉起对应页面。
- BotSdk会在拉起Activity的Intent中传入参数:url[Nullable] 可用于指定资源内容. 
- **url格式和action就是俩String，格式无固定要求，App开发者和度秘后端对齐即可**
### 2.3App启动和资源加载【App开发者关注】
- Activity被拉起后，可以通过getIntent().getStringExtra("url") 的方式，获取url，此url来源于度秘运营配置，经过Botsdk透传至App
- 拿到url之后，App可以根据自己的业务逻辑，加载对应资源
### 2.4流程图
![内容直达流程图](https://github.com/dueros/AndroidBotSdkDemo/blob/master/doc/resources/%E5%86%85%E5%AE%B9%E7%9B%B4%E8%BE%BE%E6%96%B9%E6%A1%88.png)

示例如下(百度侧关注步骤1、2， App开发者关注步骤3，4)：
1. 小度设备展示一个系统弹窗（或者QSB图标/HomeFeed卡片），点击之后上报一个LinkClicked事件给到度秘云端。事件的url内容为(核心值为加粗的action 和 url，都可为空。参数为空表示拉起应用主页，不带参数):

    dueros://{{botId}}/path?openbot=true&request={\"query\":{\"type\":\"TEXT\",\"original\":\"打开技能\"},\"intents\":[{\"name\":\"OpenBot\",\"slots\":{\"url\":{\"name\":\"url\",\"value\":\"**test://test_for_open_bot?query1=1&query2=2**\"},\"action\":{\"name\":\"action\",\"value\":\"**com.android.action.TEST_FOR_OPEN**\"}}}]}
    
    ```java
            // 模拟上报事件的一段java代码（会拉起快手app，并播放一个指定视频）
            LinkClickedEventPayload payload = new LinkClickedEventPayload();
            payload.url = "dueros://4cc38e70-73e7-2111-b2d2-f84ffebeb5d2/path?openbot=true"
                    + "&request={\\\"query\\\": {\\\"type\\\": \\\"TEXT\\\", \\\"original\\\": "
                    + "\\\"打开技能\\\"}, \\\"intents\\\": [{\\\"slots\\\": {\\\"url\\\": "
                    + "{\\\"name\\\": \\\"url\\\", \\\"value\\\": "
                    + "\\\"kwai://work/3xuwky9q3t8rspu?openFrom=null\\\"}, \\\"action\\\": "
                    + "{\\\"name\\\": \\\"action\\\", \\\"value\\\": \\\"kwai.action\\\"}}, "
                    + "\\\"name\\\": \\\"OpenBot\\\"}]}";
            BotSdk.getInstance().uploadLinkClickedEvent(payload);
    ```

2. 度秘云端收到LinkClicked事件，下发一个Open指令打开应用[协议地址（百度内网可访问）](http://icode.baidu.com/repos/baidu/duer/open-platform-api-doc/blob/master:dueros-conversational-service/device-interface/bot-app-sdk-private.md),指令中会带上packageName和（1）中加粗的action(**com.android.action.TEST_FOR_OEPN**) + url(**test://test_for_open_bot?query1=1&query2=2**) 两个参数.
3. 小度设备收到并处理Open指令，拉起应用。拉起应用的intent获取逻辑如下：

Activity索引规则：<b>如果action字段为空，会搜索apk内：action = andriod.intent.action.MAIN, cagetory=android.intent.category.LAUNCHER 的activity，也就是技能启动页。
如果action字段不为空，会搜索apk内 action = 业务方预设的action， category=android.intent.category.DEFAULT 的activity</b>。所以，请千万注意看AndroidManifest.xml中activity的intent-filter配置，是否为目标Activity指定了正确的action和category.

Activity索引代码如下：
```java
        PackageManager packageManager = context.getPackageManager();
        Intent intent;
        if (!TextUtils.isEmpty(action)) {
            intent = new Intent(action);
            intent.setPackage(packageName);
            if (packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
                    == null) {
               return null;
            }
        } else {
            intent = packageManager.getLaunchIntentForPackage(packageName);
            intent = new Intent(Intent.ACTION_MAIN)
                    .addCategory(Intent.CATEGORY_LAUNCHER)
                    .setComponent(intent.getComponent())
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        }
        // 仅为举例
        intent.putExtra("url", "test://test_for_open_bot?query1=1&query2=2")
        return intent;

```
4. 应用拉起后，可以在Activity中通过如下方式获取参数，并做业务逻辑处理
```java
getIntent().getStringExtra("url")
```
## 3.测试验证
- 本地测试：App开发者可以通过adb shell am start -a $action -e url $url 的方式，本地调试，打开对应页面，加载指定资源。**因为BotSdk会kill命令行拉起的进程[参考FAQ](https://github.com/dueros/AndroidBotSdkDemo/blob/master/FAQ.md)，这种测试方法需要先注释BotSdk的init和regist两部分方法调用**
- 联调测试：度秘云端RD配置运营图标，App开发者通过绑定线下环境，看到对应测试图标。可以通过点击图标，测试拉起指定页面并加载对应资源。联调环境请找接口PM协调。



