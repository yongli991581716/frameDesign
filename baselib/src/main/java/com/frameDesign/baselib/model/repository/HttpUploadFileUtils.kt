package com.frameDesign.baselib.model.repository

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.frameDesign.baselib.const.HttpConfig
import org.json.JSONArray
import java.io.File
import java.util.HashMap

object HttpUploadFileUtils {


    fun uploadFile(url: String, listFiles: List<File>, listener: UploadListener?) {
        AndroidNetworking.upload(url)
            .addMultipartFileList("file", listFiles)
            .addHeaders(createHeader())
            .setContentType("multipart/form-data")
            .setPriority(Priority.HIGH)
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes ->
                listener?.onProgress(bytesUploaded, totalBytes)
            }.getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    listener?.onResponse(response)
                }

                override fun onError(anError: ANError) {
                    listener?.onError(anError)
                }
            })
    }

    private fun createHeader(): Map<String, String> {
        val map = HashMap<String, String>()
        map["version"] = "android"
        map["osType"] = "Android"
        map["version_code"] = "1"
        map["appName"] = "frameDesign"
        map["osVersion"] = "1"
        map["Accept"] = "application/json"
        map["Content-Type"] = "application/json"
        map["Authorization"] = HttpConfig.getToken()
        return map
    }

    interface UploadListener {
        fun onProgress(bytesUploaded: Long, totalBytes: Long)

        fun onResponse(response: JSONArray)

        fun onError(anError: ANError)
    }
}
