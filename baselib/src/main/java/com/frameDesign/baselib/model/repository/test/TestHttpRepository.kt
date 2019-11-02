package com.frameDesign.baselib.model.repository.test

import com.frameDesign.baselib.const.HttpConfig
import com.frameDesign.baselib.model.bean.actions.ActionsBean
import com.frameDesign.commonlib.uitls.LogUtils
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

        var bena = Retrofit.Builder().baseUrl(HttpConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TestHttpService::class.java).getObtainUrls(HttpConfig.globalApi.global_url)

        bena.enqueue(object : Callback<ActionsBean> {
            override fun onFailure(call: Call<ActionsBean>, t: Throwable) {
                LogUtils.d(t.message)
            }

            override fun onResponse(call: Call<ActionsBean>, response: Response<ActionsBean>) {

                var a = response.body()
            }
        })

    }



}