# 常见问题和解答
### 1，DBP技能创建完毕，应用安装到小度设备上，通过语音指令无法拉起页面？
1. 检查小度音箱登录的百度账号和[DBP](https://dueros.baidu.com/dbp/main/console)上登录百度账号是否为同一个百度账号
2. 检查DBP上创建的技能是否有打开“技能调试开关”,检查方式，在DPB平台查看技能信息，点左边工具栏的 测试验证 -> 真机测试 -> 按照步骤1,2,3操作打开技能调试模式。
3. 语音打开应用之前是否在小度音箱上说了“小度小度，打开调试技能模式”
4. 以上都检查了，还是不行。抓日志找小度接口人解决。抓日志直接使用 adb logcat > log.txt 来抓，千万不要单抓某个进程的日志，也不要做任何过滤，抓全部日志。
### 2，使用adb命令拉起/使用Android Studio(或者其他IDE)直接运行的APP为什么会闪退/无法接收语音指令？

通过BotSkd接入的APP，测试阶段必须使用语音指令调起。所有使用adb拉起或者IDE拉起的应用都会被认为是异常启动的应用，和宿主的通信链路建立会失败，会出现应用被杀，指令分发异常等问题，所以一定要使用语音指令拉起App，一定不可使用adb或者IDE拉起app。

如果测试阶段可以不依赖BotSdk相关功能，可以把BotSdk相关代码先注释掉，之后使用adb或者IDE拉起就不会被宿主侦测到，也就不会被杀进程了。

### 3,因为语音指令识别的文字不对导致应用无法拉起？
1. 使用adb命令发起模拟语音指令，注意这里adb指令是模拟语音指令，不是直接通过ams拉起activity，所以不算做adb拉起引用。
     - 命令：adb shell am broadcast -a com.baidu.duer.query -e q 打开XXX
2. 临时使用比较不容易识别错的调用名称顶替一下，比如:"苹果测试"，“测试苹果”之类的
### 4,如何用adb 访问小度设备？如何安装应用？安装应用之后如何打开？
1. 小度设备就是一个普通的Android设备，可以通过MicroUSB线连接电脑。设备接口位置比较隐蔽，基本都藏在设备底部后者底部背板附近，或者被缓震脚垫覆盖，或者被标签覆盖。可以参考下图寻找接口位置
    ![插口示例图](https://github.com/dueros/AndroidBotSdkDemo/blob/master/doc/resources/%E6%8F%92%E5%8F%A3%E7%A4%BA%E4%BE%8B%E5%9B%BE%E7%89%87.png)
2. 连接插线连接设备之后，就可以通过adb 安装和调试应用了。如果出现电脑不识别设备的情况，请换插孔，换线尝试连接。推荐使用Mac,Ubuntu等linux电脑连接设备，windows电脑可能有驱动问题(驱动问题请自行解决)。
### 5,如何获取设备序列号？
代码库中有获取设备序列号的示例代码，请照抄即可。[DeviceInfoUti.java](https://github.com/dueros/AndroidBotSdkDemo/blob/master/app/src/main/java/com/baidu/duer/test_botsdk/utils/DeviceInfoUtil.java)
```java
    /**
     * 使用反射的方式读取设备序列号
     * @return
     */
    public static String getSerialNumber() {
        try {
            Class<?> clz = DeviceInfoUtil.class.getClassLoader().loadClass("android.os.SystemProperties");
            Method method = clz.getMethod("get", String.class);
            Object value = method.invoke(null, "ro.serialno");
            return value == null ? null : value.toString();
        } catch (Exception e) {
            return null;
        }
    }
```
### 6, 如何调用设备Camera
因为音箱设备特殊性（客厅/卧室等私人空间，设备常开），所以Camera权限非常敏感，正常来说不允许App调用设备Camera能力.系统侧增加了额外的camera权限白名单，非白名单应用也无法调用camera。所以，如非必要，请不要尝试访问小度设备Camera(访问也访问不到)。
如果跟百度PM确认过，App场景确实需要访问camera，可以通过如下方式开发调试。
1. 确认设备版本号，是以D结尾（D结尾的是开发版本可以测试camera，R结尾的是用户版本不可用于测试camera）
    ```bash
    adb shell getprop ro.build.display.id
    输出结果类似如下,需要确认的就是结尾字符是R,还是D
    DuerShow_PUFFER1S_v0.37.0.202003240105.D
    ```
2. 确认系统可以支持调试camera之后，此时就可以正常调用camera了。可以参考这个demo代码库[CameraDemo](https://github.com/TokenChen/CameraDemo)
3. 调试完毕，正式上线之前，一定通知百度侧PM，把应用包名加入到访问Camera白名单中。否则调试环境可以正常运行，线上环境依然无法访问Camera.
### 7，如何判定设备是小度设备
目前系统侧没有配置表明当前是一台小度设备。因为接入小度设备需要集成BotSdk，所以建议针对小度设备单独打渠道包，通过flavor判定是小度设备。
### 8，在小度设备如何管理应用权限？
1. 小度设备对应用都是默认授权的，不会弹出动态申请授权弹窗。需要注意的是，在小度设备访问**麦克风，摄像头**需要单独配置白名单,配置方式联系对接的百度PM。
2. 小度设备不允许应用自更新，所以应用也无法申请**安装应用**的权限
3. WRITE_EXTERNAL_STORAGE，MOUNT_UNMOUNT_FILESYSTEM权限,禁止三方app申请.如果有写本地文件的需求，请写到应用私有目录，如：data/data/$pacakgeName 
### 9，小度设备如何获取地理位置信息
小度设备没有gps模块。建议直接获取网络定位。
### 10，adb连接设备提示未授权(adb devices unauthorized)？
我们对外寄出的设备，默认是user版本，不开放adb权限。
在遇到未授权的case是，请联系百度PM，或者通过issue提交设备号。我们收到之后会推送开发版本升级。升级之后的系统版本号以1.99开头，此时就可以用adb正常访问设备了。
### 11，应用安装后再小度桌面找不到应用图标? 安装应用后应该如何打开？
* 小度桌面不同于传统Android Launcher，不会显示应用图标。
* 小度设备桌面的图标都是一些云端配置的链接，不是真正应用图标，点击链接是发起一次云端交互，下发指令打开对应内容（有可能是浏览器，有可能是三方页面，也有可能是系统设置页......)
* 调试阶段，如果未集成BotSdk,可以用AndroidStudio或者adb 拉起。集成BotSdk之后，应该通过语音指令调起。原因详见**问题（1）**
