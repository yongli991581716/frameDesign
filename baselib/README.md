#基础功能组件库
*baselib库需知
>1、本库存放所有组件基础功能组件，如baseActivity、baseFragment、baseApplication等，此库作用域位于独立组件和功能库间
>2、存放mvc架构相关模块基础文件
>3、okhttp 添加缓存策略，
  >*默认全局默认缓存，过期时间为0
  >**若某个接口需要缓存，则添加请求头（Cache-Control", "public,max-age=xxx"）或者CacheControl（new CacheControl.Builder().maxAge(xx, TimeUnit.SECONDS).build()）
  >*** 若有网络时，无缓存，则请求网络；有缓存时，判断缓存时间是否过期，若过期则加载网络，否则返回缓存
  >**** 若无网络时，直接请求缓存，若有缓存则返回，若无，则返回504错误 
  >请求指令集（在请求报文中的取值）：
  
      no-cache: 不要缓存数据，直接从源服务器获取数据；
      no-store: 不缓存请求或响应的任何内容；
      max-age: 表示可接受过期过久的缓存数据，同指定了参数的max-stale；
      max-stale: 表示接收过期的缓存，如后面未指定参数，则表示永远接收缓存数据。如max-stale: 3600, 表示可接受过期1小时内的数据；
      min-fresh: 表示指定时间内的缓存数据仍有效，与缓存是否过期无关。如min-fresh: 60, 表示60s内的缓存数据都有效，60s之后的缓存数据将无效。
      only-if-cache: 表示直接获取缓存数据，若没有数据返回，则返回504（Gateway Timeout）
  
  >应答指令集（在应答报文中的取值）：
  
      public: 可向任一方提供缓存数据；
      private: 只向指定用户提供缓存数据；
      no-cache: 缓存前需确认其有效性；
      no-store: 不缓存请求或响应的任何内容；
      max-age: 表示缓存的最大时间，在此时间范围内，访问该资源时，直接返回缓存数据。不需要对资源的有效性进行确认；
      must-revalidate: 访问缓存数据时，需要先向源服务器确认缓存数据是否有效，如无法验证其有效性，则需返回504。需要注意的是：如果使用此值，则max-stale将无效。
