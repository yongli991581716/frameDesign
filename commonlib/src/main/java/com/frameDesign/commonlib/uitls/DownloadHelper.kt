package com.frameDesign.commonlib.uitls

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build

import android.view.LayoutInflater
import androidx.core.content.FileProvider
import com.frameDesign.commonlib.R
import com.frameDesign.commonlib.expand.toast
import com.frameDesign.commonlib.views.dialog.ZqkhDialog
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.DownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.model.FileDownloadStatus
import com.xiaochen.progressroundbutton.AnimDownloadProgressButton
import com.xiaochen.progressroundbutton.AnimDownloadProgressButton.DOWNLOADING
import java.io.File

/**
 * 下载安装具
 *
 * @author liyong
 * @date 2018-10-22.
 */
object DownloadHelper {

    /**
     * 常规文件大小单位
     */
    val SIZE_1K = 1024
    val SIZE_1M = SIZE_1K * SIZE_1K//1M
    val fileName = "update.apk"

    private var downloader = FileDownloader.getImpl()
    private var curDownTask: DownloadTask? = null

    fun initDownload(ctx: Application) {
        FileDownloader.setupOnApplicationOnCreate(ctx)
        FileDownloader.setGlobalPost2UIInterval(1000)
    }

    /**
     * downUrl: apk地址
     * forceUpdate 强制升级
     */
    fun startDownloadDialog(ctx: Context, applicationId: String, downUrl: String, forceUpdate: Boolean) {
        val apkRealPath = ctx.externalCacheDir?.path + File.separator + fileName //下载文件地址
        val apkTempPath = "$apkRealPath.temp"//下载文件临时地址

        //判断是不是下载好了，是就直接安装
        if (FileUtils.fileIsExists(apkRealPath)
            && SystemUtil.getAPKInfo(apkRealPath).versionCode > SystemUtil.getAppVersionCode()
        ) {
            installApk(ctx, applicationId, apkRealPath)
            return
        }

        //强制升级没有按钮
        val view = LayoutInflater.from(ctx).inflate(R.layout.download_layout, null)
        var dialogBuilder: ZqkhDialog? = null
        dialogBuilder = if (forceUpdate) {
            DialogUtils.createAlertDialog(ctx as Activity, "升级进度", "",
                "", { dialog, which -> },
                "", { dialog, which -> })
        } else {
            DialogUtils.createAlertDialog(ctx as Activity, "升级进度", "",
                "取消下载", { dialog, which ->
                    downloader.pauseAll()
                    downloader.clearAllTaskData()
                },
                "关闭窗口", { dialog, which -> })
        }

        dialogBuilder?.custView = view

        val dialog = dialogBuilder

        val downBtn = view.findViewById<AnimDownloadProgressButton>(R.id.download_btn)

        downBtn.state = DOWNLOADING
        downBtn.textSize = ctx.resources.getDimensionPixelOffset(R.dimen.dt_10).toFloat()

//        val downUri = Uri.parse("http://yixin.dl.126.net/update/installer/yixin.apk")

        val fileLis = object : FileDownloadListener() {

            override fun warn(task: BaseDownloadTask?) {

            }

            override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {

            }

            override fun error(task: BaseDownloadTask?, e: Throwable?) {
                ctx.runOnUiThread {
                    e?.message?.let { ctx.toast(it) }
                }
            }

            override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                if (totalBytes != 0) {
                    val progress = (soFarBytes * 100f) / totalBytes
                    LogUtils.d("progress=$progress, $soFarBytes, $totalBytes")
                    if (DialogUtils.dialogShowing(dialog)) {
                        downBtn.setProgressText("下载中 ${soFarBytes / SIZE_1K}K/${totalBytes / SIZE_1K}K，", progress)
                    }

                }

            }

            override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
            }

            override fun completed(task: BaseDownloadTask?) {
                LogUtils.d("BaseDownloadTask=${task?.path}")
                //下载成功，把temp地址重命名成直接的地址
                File(apkRealPath).delete()
                File(task!!.path).renameTo(File(apkRealPath))
                if (DialogUtils.dialogShowing(dialog)) {
                    dialog.dismiss()
                    installApk(ctx, applicationId, apkRealPath)
                }
            }

        }

        val status = downloader.getStatus(downUrl, apkTempPath)
        if (curDownTask != null
            && (status == FileDownloadStatus.retry
                    || status == FileDownloadStatus.progress)
        ) {//正在下载中
            curDownTask?.listener = fileLis

        } else {
            File(apkRealPath).delete()
            downloader.pauseAll()
            downloader.clearAllTaskData()
            curDownTask = downloader.create(downUrl) as DownloadTask?
            curDownTask?.setPath(apkTempPath)?.setListener(fileLis)?.start()
        }

        dialog.show()
    }

    fun installApk(ctx: Context, applicationId: String, filePath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            val apkUri = FileProvider.getUriForFile(ctx, "$applicationId.fileprovider", File(filePath))
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(File(filePath)), "application/vnd.android.package-archive")
        }

        ctx.startActivity(intent)
    }

}