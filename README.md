# 工程目录
## 一、相关文件说明
1. **pkgaar.gradle**  
> 为组件模块打包为aar公共gradle脚本，打包AAR并复制到壳工程app/libs目录>>     下(暂无法使用此文件，故采取对应build.gradle文件添加该脚本)  
*** 

2. **打包信息**   
> MYAPP_RELEASE_STORE_PASSWORD=android  
> MYAPP_RELEASE_KEY_ALIAS=androiddebugkey  
> MYAPP_RELEASE_KEY_PASSWORD=android  
***
3. **发包注意**.    
> 修改批处理里面的版本号  
***
4. **批处理文件(上传至蒲公英v2版本，支持渠道上传)**  
> package_and_publish_apk_test.sh 	执行打包，并上传到 蒲公英 test 路径  
> package_and_publish_apk_uat.sh 		执行打包，并上传到 蒲公英 uat 路径  
> package_and_publish_apk.sh 		执行打包，并上传 所有包 到 蒲公英  
> package_apk.sh 				只执行打包  
> publish_apk_test.sh 			上传 apk 到 蒲公英 test 路径  
> publish_apk_uat.sh 				上传 apk 到 蒲公英 uat 路径  
> publish_apk.sh 				上传 所有包 到 蒲公英le 为组件模块打包为aar公共gradle脚本，打包AAR并复制到壳工程app/libs目录下  
***