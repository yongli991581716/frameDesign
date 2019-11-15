package com.frameDesign.baselib.model.repository.test

import com.frameDesign.baselib.model.bean.actions.ActionsBean
import com.frameDesign.baselib.model.bean.actions.ActionsH5Bean
import com.frameDesign.baselib.model.bean.internal.FDData
import com.frameDesign.baselib.model.bean.internal.FDList
import com.frameDesign.baselib.model.bean.test.ADTest
import com.frameDesign.baselib.model.bean.test.TestUserBean
import com.frameDesign.baselib.model.bean.test.UserTokenBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * ..
 *
 * @author JustBlue
 * @time 2018/10/15
 */
interface HttpUrlService {

    companion object {
        const val api_captcha_login = "api_captcha_login"
        const val api_ad_list = "api_ad_list"
        const val api_user_bindOrLoginPhone = "api_user_bindOrLoginPhone"

    }

    /**
     * 获取系统接口数据
     * @param url String
     * @return Observable<ActionsBean>
     */
    @Headers("Cache-Control: max-age=30")
    @GET()
    fun obtainUrls(@Url url: String): Observable<ActionsBean>

    /**
     * 获取系统接口数据
     * @param url String
     * @return Observable<ActionsBean>
     */
    //@Headers("Cache-Control:no-store")//请求响应都不用缓存
    @GET()
    fun obtainH5Urls(@Url url: String): Observable<ActionsH5Bean>

    @GET(api_ad_list)
    fun obtainADList(@Query("code") code: String): Observable<FDList<ADTest>>

    @POST(api_user_bindOrLoginPhone)
    fun obtainSYList(@Body body: RequestBody): Observable<FDData<TestUserBean>>

    /**
     * 请求发生验证码
     * @param body RequestBody
     * @return Observable<ZQData<String>>
     */
    @Headers("Cache-Control: max-age=10")
    @POST(api_captcha_login)
    fun requestLogin(@Body body: RequestBody): Observable<FDData<UserTokenBean>>
}