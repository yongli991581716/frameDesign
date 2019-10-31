package com.frameDesign.baselib.model.bean.miss

/**
 * @desc  自定义异常基类, 规范RxJava事件处理流程
 * ..
 * @author liyong
 * @date 2018/10/17
 */
open class BaseMiss(val code: Int, val msg: String) : RuntimeException(msg)

/**
 * 默认异常, 用于包装后台返回的无法确定错误
 * @constructor
 */
class DefMiss(code: Int, msg: String) : BaseMiss(code, msg)

/**
 * 网络连接异常
 */
class NetMiss : BaseMiss(1199, "无法获取网络连接, 请求后重试")

/**
 * 空数据异常
 */
class EmptyDataMiss : BaseMiss(1200, "暂无数据, 请稍后再试!")

/**
 * 响应数据解析异常
 */
class ResponseParseMiss : BaseMiss(1201, "数据解析异常, 请稍后再试!")

/**
 * 在接口列表中未找到对应api的url
 */
class NotFoundUrlMiss : BaseMiss(1202, "请求路径出错, 请稍后再试!")

/**
 * 登录异常
 */
class LoginMiss : BaseMiss(1203, "登录异常, 请重新登录")