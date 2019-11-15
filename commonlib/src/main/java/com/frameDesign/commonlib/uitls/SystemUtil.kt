package com.frameDesign.commonlib.uitls

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.content.Intent.ACTION_SENDTO
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.frameDesign.commonlib.CommHelper
import com.frameDesign.commonlib.R
import com.frameDesign.commonlib.expand.fdToast
import com.frameDesign.commonlib.expand.px2dp


/**
 * 系统级工具
 * @author liyong
 * @date 2019-10-22.
 */
object SystemUtil {

    private var ctx = CommHelper.mCtx

    /**
     * 判断是不是小米
     *
     * @return
     */
    val isXiaomi: Boolean
        get() {
            val mf = android.os.Build.MANUFACTURER
            return mf != null && (mf.toLowerCase().contains("xiaomi") || mf.toLowerCase().contains("miui"))

        }


    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    fun copy(content: String) {
        // 得到剪贴板管理器
        val cmb = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        //创建ClipData对象
        var clipData = ClipData.newPlainText("label", content)
        cmb.setPrimaryClip(clipData)
    }

    /**
     * @param @param mCtx @param @return @return String @throws
     * @Title: getAppVersionName
     * @Description: Get the app versionName.
     */
    fun getAppVersionName(): String? {
        var vname: String? = null
        try {
            val pi = ctx.packageManager
                .getPackageInfo(ctx.packageName, 0)
            vname = pi.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return vname
    }

    fun getAppVersionCode(): Int {
        var vcode = 0
        try {
            val pi = ctx.packageManager
                .getPackageInfo(ctx.packageName, 0)
            vcode = pi.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return vcode
    }

//    /**
//     * Get the IMEI of the phone
//     *
//     * @return
//     */
//    fun getDeviceIMEI(mCtx: Context): String? {
//        var strIMEI: String? = null
//
//        try {
//            val tm = mCtx.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            if (ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                return null
//            }
//            strIMEI = tm.deviceId
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        if (TextUtils.isEmpty(strIMEI)) {
//            strIMEI = java.lang.Long.toString(System.nanoTime())
//        }
//
//        return strIMEI
//    }

    /**
     * 获得独一无二的Psuedo ID
     * @return String
     */
    fun getUniquePsuedoID(): String {
        var serial: String? = null

        val deviceId = Build.BRAND + "_" + Build.BRAND + "_" +

                Build.CPU_ABI + "_" + Build.DEVICE + "_" +

                Build.DISPLAY + "_" + Build.HOST + "_" +

                Build.ID + "_" + Build.MANUFACTURER + "_" +

                Build.MODEL + "_" + Build.PRODUCT + "_" +

                Build.TAGS + "_" + Build.TYPE + "_" +

                Build.USER

        try {
            serial = android.os.Build::class.java.getField("SERIAL").get(null).toString()
            //API>=9 使用serial号
//            return deviceId + serial
        } catch (e: Exception) {
            //serial需要一个初始化
            e.printStackTrace()
            serial = "serial" // 随便一个初始化
        }

        //使用硬件信息拼凑出来的15位号码
        val id = "${deviceId}_$serial"
        return id
    }

    fun getAndroidId(): String {
        val androidID = Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ANDROID_ID)
        val id = "${androidID}_${Build.SERIAL}"
        return id
    }

    /**
     * 得到 一个组合的唯一Id
     * @return String
     */
    @SuppressLint("HardwareIds")
    fun getUnitId(): String {
        val id = Md5Utils.md5(getUniquePsuedoID() + getAndroidId())
        LogUtils.d("getAndroidId id=$id")
        return id
    }

    fun getLoginAndroidId(): String {
        val id = "${getUnitId()}#${System.currentTimeMillis()}"
        LogUtils.d("getLoginAndroidId id=$id")
        return id
    }

    /**
     * Get the application information of the application.
     *
     * @param filePath
     * @return
     */
    fun getAPKInfo(filePath: String): PackageInfo {
        val pkgManger = ctx.packageManager
        return pkgManger.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES)
    }

    /**
     * Whether the application is available.
     *
     * @param intent
     * @return
     */
    fun isIntentAvailable(intent: Intent): Boolean {
        val packageManager = ctx.packageManager
        val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        return list != null && list.size > 0
    }

    /**
     * Whether the application is installed.
     *
     * @param pkg
     * @return
     */
    fun isAppInstalled(pkg: String): Boolean {
        try {
            val packageInfo =
                ctx.packageManager.getPackageInfo(pkg, PackageManager.GET_UNINSTALLED_PACKAGES)
            if (packageInfo != null) {
                return true
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return false
    }


    fun gotoDial(activity: Activity, phone: String?) {
        if (!StringUtils.isEmpty(phone)) {
            //跳转到拨号界面，同时传递电话号码
            val intent = Intent(ACTION_DIAL, Uri.parse("tel:$phone"))
            activity.startActivity(intent)
        }
    }

    fun sendSmsWithPhone(phone: String?, body: String?) {
        if (!StringUtils.isEmpty(phone) && !StringUtils.isEmpty(body)) {
            val intent = Intent(ACTION_SENDTO, Uri.parse("smsto:$phone"))
            intent.putExtra("sms_body", body)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.startActivity(intent)
        }
    }

    /**
     * 重启
     */
    fun restart() {
        val intent = ctx.packageManager.getLaunchIntentForPackage(ctx.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ctx.startActivity(intent)
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    fun getStatusHeight(): Int {
        /**
         * 获取状态栏高度——方法1
         */
//        var statusBarHeight1 = -1
        //获取status_bar_height资源的ID
        val resourceId = ctx.resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight1 = if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            ctx.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
        LogUtils.d("statusBarHeight1 =$statusBarHeight1, dp=${statusBarHeight1.px2dp()}")
        return statusBarHeight1
    }

    /**
     * 得到屏幕宽高
     *
     * @param context
     * @return
     */
    fun getScreenSize(): IntArray {
        val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return intArrayOf(outMetrics.widthPixels, outMetrics.heightPixels)
    }

    /**
     * 浏览器打开链接
     *
     * @param ctx
     * @param url
     */
    fun loadUrlForBrowser(url: String) {
        if (!TextUtils.isEmpty(url)) {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val uri = Uri.parse(url)
            intent.data = uri
            ctx.startActivity(intent)
        }
    }

    /**
     * 打开视频
     *
     * @param ctx
     * @param url
     */
    fun openVideo(url: String) {
        if (!TextUtils.isEmpty(url)) {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            val uri = Uri.parse(url)
            intent.setDataAndType(uri, "video/*")
            ctx.startActivity(intent)
        }
    }


    /**
     * 是不是安装了微信
     * @param ctx
     * @return
     */
//    fun isWxAppInstalled(wxId: String?): Boolean {
//        val wxApi = WXAPIFactory.createWXAPI(mCtx, null)
//        wxApi.registerApp(wxId)
//
//        return wxApi.isWXAppInstalled
//    }

//    /**
//     * 微信支不支持支付
//     * @param mCtx
//     * @return
//     */
//    fun isWxAppSupported(wxId: String?): Boolean {
//        val wxApi = WXAPIFactory.createWXAPI(mCtx, null)
//        wxApi.registerApp(wxId)
//
//        return wxApi.isWXAppSupportAPI
//    }

    /**
     * 判断有没有安装支付宝
     * @param context
     * @return
     */
    fun checkAliPayInstalled(): Boolean {

        val uri = Uri.parse("alipays://platformapi/startApp")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val componentName = intent.resolveActivity(ctx.packageManager)
        return componentName != null
    }

    /**
     *设置状态栏颜色
     * @param activity
     * @param isGray
     * @return
     *
     */
    fun setStatusTextColor(activity: Activity, isGray: Boolean) {
        if (isGray) {
            // 灰色
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        } else {
            // 白色
            activity.window.decorView.systemUiVisibility = 0
        }
    }

    //隐藏虚拟键盘
    fun hideKeyboard(v: View) {
        val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(v.applicationWindowToken, 0)

        }
    }

    //显示虚拟键盘
    fun showKeyboard() {
        val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED)
        //(这个方法可以实现输入法在窗口上切换显示，如果输入法在窗口上已经显示，则隐藏，如果隐藏，则显示输入法到窗口上)
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }


    /**
     *
     * Get the application name.
     *
     * @Title: getAppName @Description: TODO @param @param ctx @param @param
     * pID @param @return @return String @throws
     */
    fun getAppName(pID: Int): String? {
        val am = CommHelper.mCtx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val itor = am.runningAppProcesses.iterator()
        // PackageManager pm = getCtx().getPackageManager();
        while (itor.hasNext()) {
            val info = itor.next() as ActivityManager.RunningAppProcessInfo
            try {
                if (info.pid == pID) {
                    // CharSequence c =
                    // pm.getApplicationLabel(pm.getApplicationInfo(info.processName,
                    // PackageManager.GET_META_DATA));
                    return info.processName
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        return null
    }


    /**
     * 进入权限设置页面
     */
    fun gotoPermissionSettings(activity: Activity?) {
        fdToast(activity?.getString(R.string.permission_refuse_hint))
        activity?.startActivity(Intent(Settings.ACTION_SETTINGS))
    }

    /**
     * 以下代码可以跳转到应用详情，可以通过应用详情跳转到权限界面
     */
    fun getAppDetailSettingIntent(context: Context) {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            localIntent.data = Uri.fromParts("package", context.packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.action = Intent.ACTION_VIEW
            localIntent.setClassName(
                "com.android.settings",
                "com.android.settings.InstalledAppDetails"
            )
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.packageName)
        }
        context.startActivity(localIntent)
    }

}
