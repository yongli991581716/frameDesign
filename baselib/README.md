#基础功能组件库
*baselib库需知
>1、本库存放所有组件基础功能组件，如baseActivity、baseFragment、baseApplication等，此库作用域位于独立组件和功能库间
>2、存放mvc架构相关模块基础文件
>3、okhttp 添加缓存策略，
  >*默认全局不缓存
  >**若某个接口需要缓存，则添加请求头（Cache-Control", "public,max-age=xxx"）或者CacheControl（new CacheControl.Builder().maxAge(xx, TimeUnit.SECONDS).build()）
  >*** 若有网络时，无缓存，则请求网络；有缓存时，判断缓存时间是否过期，若过期则加载网络，否则返回缓存
  >**** 若无网络时，直接请求缓存，若有缓存则返回，若无，则返回504错误 