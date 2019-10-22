#资源组件库
* commonreslib库需知

>1、本库存放所有公用资源，包括不限于图片、shape、theme、color、dimen、依赖包、组件路由名封装等
>2、7.0系统手机下载安装适配
  （1）添加文件xml/file_paths.xml,
  （2）AndroidManifest.xml 添加
>                                    <provider
>                                            android:name="androidx.core.content.FileProvider"
>                                            android:authorities="${applicationId}.fileprovider"
>                                            android:exported="false"
>                                            android:grantUriPermissions="true">
>
>                                        <!-- 元数据 -->
>                                        <meta-data
>                                                android:name="android.support.FILE_PROVIDER_PATHS"
>                                                android:resource="@xml/file_paths"/>
>                                    </provider>
>3、9.0要求不能明码传输，解决方案
  （1）APP改用https请求
  （2）targetSdkVersion 降到27以下
  （3）更改网络安全配置
        > 添加文件xml/network_security_config.xml,
        > 在AndroidManifest.xml文件下的application标签增加以下属性：
        >                            <application
        >                           ...
        >                             android:networkSecurityConfig="@xml/network_security_config"
        >                            ...
        >                                />