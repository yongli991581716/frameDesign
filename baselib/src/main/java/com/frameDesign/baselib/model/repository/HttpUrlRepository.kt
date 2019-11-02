package com.frameDesign.baselib.model.repository

import com.frameDesign.baselib.const.HttpConfig
import com.frameDesign.baselib.model.bean.actions.ActionsBean
import com.frameDesign.baselib.model.bean.actions.ActionsH5Bean
import com.frameDesign.baselib.model.bean.miss.NotFoundUrlMiss
import com.frameDesign.baselib.model.bean.test.ADTest
import com.frameDesign.baselib.model.bean.test.TestUserBean
import com.frameDesign.baselib.model.bean.test.UserTokenBean
import com.frameDesign.baselib.utils.ActionsHelp
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * 全局url接口仓库
 *
 * @author JustBlue
 * @time 2018/10/15
 */
object HttpUrlRepository : BaseRepository<HttpUrlService>() {

    val globalObtainUrls: Observable<ActionsBean> by lazy {
        obtainUrls()
            .doOnNext { ActionsHelp.saveData(it) }
            .replay(1)
            .refCount(16, TimeUnit.SECONDS)
    }

    val globalObtainH5Urls: Observable<ActionsH5Bean> by lazy {
        obtainH5Urls()
            .doOnNext { ActionsHelp.saveH5Actions(it) }
            .replay(1)
            .refCount(16, TimeUnit.SECONDS)
    }

    /**
     * 创建全局获取URL接口, 当前接口支持多个注册
     * @return Observable<ActionsBean>
     */
    private fun obtainUrls(): Observable<ActionsBean> {
        return service.obtainUrls(HttpConfig.globalApi.global_url)
    }

    /**
     * 获取系统H5路径
     * @return Observable<ActionsH5Bean>
     */
    private fun obtainH5Urls(): Observable<ActionsH5Bean> {
        return service.obtainH5Urls(HttpConfig.globalApi.global_h5_url)
            .asyncWorker()
    }

    /**
     * 获取对应别名的路径
     * @param alias String
     * @return Observable<String>
     */
    fun obtainH5Action(alias: String): Observable<String> {
        val item = ActionsHelp.getH5ApiByAlias(alias)

        return if (item == null || item.invalidAction()) {
            obtainH5Urls()
                .map {
                    val beanItem = ActionsHelp.getH5ApiByAlias(alias)
                    if (beanItem == null || beanItem.invalidAction()) {
                        throw NotFoundUrlMiss()
                    }
                    return@map beanItem
                }
        } else {
            Observable.just(item)
        }
            .map { it.href }

    }

    fun obtainAd(): Observable<List<ADTest>> {
        return service.obtainADList("GENE_0001")
            .asyncWorker()
            .rebase()
    }

    fun obtainSy(): Observable<TestUserBean> {
        val body = jsonParams(
            "phone" to "15025496981",
            "captcha" to "0817"
        )
        return service.obtainSYList(body)
            .asyncWorker()
            .rebase()
    }

    fun requestLogin(): Observable<UserTokenBean> {

        return service.requestLogin(
            jsonParams(
                "captcha" to "0817",
                "phone" to "18202894517"
            )
        ).unitConnect().rebase()

    }
}