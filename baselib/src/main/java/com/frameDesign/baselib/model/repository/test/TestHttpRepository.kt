package com.frameDesign.baselib.model.repository.test

import android.util.Log
import com.frameDesign.baselib.const.HttpConfig
import com.frameDesign.baselib.model.bean.actions.ActionsBean
import com.frameDesign.baselib.model.repository.JsonParserHelper
import com.frameDesign.baselib.model.repository.OkHttpHelp
import com.frameDesign.commonlib.uitls.LogUtils
import com.frameDesign.commonlib.uitls.NetUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * description
 * @author liyong
 * @date 2019-11-02
 */
object TestHttpRepository {


    fun obtainUrl() {

//okhttp 使用
//        doAsync {
//            var request = Request.Builder().url(HttpConfig.globalApi.global_url)
//                .header("Cache-Control", "public,max-age=30")
//                .build()
//            var response = OkHttpHelp.createDefault().newCall(request).execute()
//            //response.body?.close()
//            if (response.isSuccessful) {
//                if (response.networkResponse != null) {
//                    Log.e("network", "${response.body.toString().length}")
//                } else if (response.cacheResponse != null) {
//                    if (NetUtil.isConnected()) {
//                        Log.e("cache", "${response.body.toString().length}")
//                    } else {
//                        Log.e("cache(no network)", "${response.body.toString().length}")
//                    }
//                }
//            }
//        }

//retrofit+okhttp 使用
        var bena = Retrofit.Builder()
            .client(OkHttpHelp.createDefault())  // 添加OkHttp绑定
            .addConverterFactory(
                GsonConverterFactory
                    .create(JsonParserHelper.getCustomGson())
            )
            .baseUrl(HttpConfig.BASE_URL)
            .build()
            .create(TestHttpService::class.java).getObtainUrls(HttpConfig.globalApi.global_url)


        var callback = object : Callback<ActionsBean> {
            override fun onFailure(call: Call<ActionsBean>, t: Throwable) {
                LogUtils.d(t.message)
            }

            override fun onResponse(call: Call<ActionsBean>, response: Response<ActionsBean>) {

                var a = response.body()

                if (response.isSuccessful) {
                    if (response.raw().networkResponse != null) {
                        Log.e("network", "${response.body().toString().length}")
                    } else if (response.raw().cacheResponse != null) {
                        if (NetUtil.isConnected()) {
                            Log.e("cache", "${response.body().toString().length}")
                        } else {
                            Log.e("cache(no network)", "${response.body().toString().length}")
                        }
                    }
                }
            }
        }
        bena.enqueue(callback)

    }


}