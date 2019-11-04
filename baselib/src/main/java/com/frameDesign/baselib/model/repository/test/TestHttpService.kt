package com.frameDesign.baselib.model.repository.test

import com.frameDesign.baselib.model.bean.actions.ActionsBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

/**
 * 测试retrofit service服务接口
 * @author liyong
 * @date 2019-11-02
 */
interface TestHttpService {

    companion object {


    }

    @Headers("Cache-Control: max-age=30")
    @GET
    fun getObtainUrls(@Url url: String): Call<ActionsBean>


}