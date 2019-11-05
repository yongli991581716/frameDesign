#功能组件库
*commonlib库需知:

>1、本库记录常用工具、控件、第三方sdk
>2、使用本库前需在宿主app工程或独立组件初始化CommContext文件中ctx
>3、使用下载工具，需要初始化DownloadHelper.initDownload(ctx) {
>4、权限库使用此处通过简单工厂设计模式，在基础功能组件入口（BaseApplication）传入AndPermission库实例，使之整个工程权限申请对象为AndPermission；
    换言之，若需要使用其他第三方权限库，只需在基础功能组件入口实例化对应权限库对象
>5、fresco设置缓存，若强制加载网络图片，则需要先删除内存以及磁盘缓存，故才可通过网络加载