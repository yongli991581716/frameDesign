#apk 对应的版本号
pkg_version=v1.0.1
apk_path=./app/build/outputs/apk/flavor_uat/release/frameDesign_"$pkg_version"_flavor_uat_release.apk
uKey=4d03b84aa6fe35937c6251a28d59d431
apiKey=660151e47d1c6bfe060c045733ee6b77

echo
echo -----㊎-----㊍-----㊌-----㊋-----㊏----- start uat -----㊀-----㊁-----㊂-----㊃-----㊄-----
#登录 蒲公英 并发布应用
curl -F "file=@"$apk_path \
-F "uKey="$uKey \
-F "_api_key="$apiKey \
-F "buildChannelShortcut=Ziew" \
https://www.pgyer.com/apiv2/app/upload
echo
echo -----㊎-----㊍-----㊌-----㊋-----㊏----- uat url: www.pgyer.com/Ziew -----㊀-----㊁-----㊂-----㊃-----㊄-----
echo -----㊎-----㊍-----㊌-----㊋-----㊏----- uat apk path: $apk_path -----㊀-----㊁-----㊂-----㊃-----㊄-----
echo -----㊎-----㊍-----㊌-----㊋-----㊏----- end -----㊀-----㊁-----㊂-----㊃-----㊄-----
echo
