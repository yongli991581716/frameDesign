package com.frameDesign.commonlib.uitls;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import java.io.*;
import java.text.DecimalFormat;
import java.util.UUID;

/**
 * 文件工具
 *
 * @author liyong
 * @date 2019-10-22.
 */
public class FileUtils {
    private static final int BUFFER = 8192;

    private static String MAIN_PATH = "main";
    private static String TEMP_PATH = "temp";

    private static Context ctx;

    /**
     * 初始化
     *
     * @param ctx
     */
    public static void init(Context ctx) {
        FileUtils.ctx = ctx;
    }

    /**
     * 可见的路径
     *
     * @return
     */
    public static String getVisiblePath() {
        File dir = new File(Environment.getExternalStorageDirectory(), "health_manager");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    /**
     * 获取缓存主路径(main)路径
     *
     * @return
     */
    public static String getMainPath() {
        File dir = new File(getCacheFile(), MAIN_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    /**
     * 获取缓存下临时（temp）文件路径
     *
     * @return
     */
    public static String getTempPath() {
        File dir = new File(getCacheFile(), TEMP_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    /**
     * 获取缓存下图片文件路径
     *
     * @return
     */
    public static String getImagePath() {
        File dir = new File(getCacheFile(), "images");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    /**
     * 获取图片随机文件
     *
     * @return
     */
    public static File getRandomImageFile() {
        File file = new File(getImagePath(), UUID.randomUUID() + ".png");
        return file;
    }

    /**
     * 获取缩略图片文件路径
     *
     * @return
     */
    public static String getThumbnailPath() {
        File dir = new File(getCacheFile(), "thumbnail");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    /**
     * 获取缩略图片随机文件
     *
     * @return
     */
    public static File getRandomThumbnailFile() {
        File file = new File(getThumbnailPath(), UUID.randomUUID() + ".png");
        return file;
    }

    /**
     * 获取录音文件路径
     *
     * @return
     */
    public static String getVoicePath() {
        File dir = new File(getCacheFile(), "voices");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    /**
     * 获取录音随机文件路径
     *
     * @param ex 后缀名
     * @return
     */
    public static File getRandomVoiceFile(String ex) {
        File file = new File(getVoicePath(), UUID.randomUUID() + ex);
        return file;
    }

    /**
     * 缓存文件地址
     *
     * @return
     */
    public static File getCacheFile() {

        final File cacheFile = ctx.getExternalCacheDir() != null
                ? ctx.getExternalCacheDir() : ctx.getCacheDir();

//		final File cacheFile = EBSApplication.getInstance().getCacheDir();
        return cacheFile;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     */
    public static void deleteFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 删除文件
     *
     * @param file 文件
     */
    public static void deleteFile(File file) {
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    /**
     * 删除文件夹
     *
     * @param path 文件夹路径
     */
    public static void deleteDir(String path) {
        File dir = new File(path);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(path); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     */
    public static boolean fileIsExists(String path) {
        try {
            if (!TextUtils.isEmpty(path)) {
                File f = new File(path);
                if (!f.exists()) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     */
    public static boolean fileIsExists(File file) {
        boolean exit = file != null && file.exists() && file.length() > 0;
        return exit;
    }

    /**
     * 获取可读的文件大小xxGBxxMBxxKB
     *
     * @param size 总字节
     */
    public static String getReadableFileSize(int size) {
        final int BYTES_IN_KILOBYTES = 1024;
        final DecimalFormat dec = new DecimalFormat("###.#");
        final String KILOBYTES = " KB";
        final String MEGABYTES = " MB";
        final String GIGABYTES = " GB";
        float fileSize = 0;
        String suffix = KILOBYTES;
        if (size > BYTES_IN_KILOBYTES) {
            fileSize = size / BYTES_IN_KILOBYTES;
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES;
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES;
                    suffix = GIGABYTES;
                } else {
                    suffix = MEGABYTES;
                }
            }
        }
        return String.valueOf(dec.format(fileSize) + suffix);
    }

    /**
     * 获取文件的文件名(不包括扩展名)
     *
     * @param path 文件路径
     */
    public static String getFileNameWithoutExtension(String path) {
        if (path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(File.separator);
        if (separatorIndex < 0) {
            separatorIndex = 0;
        }
        int dotIndex = path.lastIndexOf(".");
        if (dotIndex < 0) {
            dotIndex = path.length();
        } else if (dotIndex < separatorIndex) {
            dotIndex = path.length();
        }
        return path.substring(separatorIndex + 1, dotIndex);
    }

    /**
     * 获取文件名(包括扩展名)
     *
     * @param path 文件路径
     */
    public static String getFileName(String path) {
        if (path == null) {
            return null;
        }
        int separatorIndex = path.lastIndexOf(File.separator);
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }


    /**
     * 获取文件扩展名)
     *
     * @param path 文件路径
     */
    public static String getExtension(String path) {
        if (path == null) {
            return null;
        }
        int dot = path.lastIndexOf(".");
        if (dot >= 0) {
            return path.substring(dot);
        } else {
            return "";
        }
    }

    /**
     * 获取uri文件对象
     *
     * @param context 上下文
     * @param uri     uri
     */
    public static File getUriFile(Context context, Uri uri) {
        String path = getUriPath(context, uri);
        if (path == null) {
            return null;
        }
        return new File(path);
    }


    /**
     * 获取uri文件
     *
     * @param context 上下文
     * @param uri     uri
     */
    public static String getUriPath(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    /**
     * 读取文件内容
     *
     * @param file 文件
     */
    public static String readTextFile(File file) {
        String text = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            text = readTextInputStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return text;
    }

    /**
     * 从流中读取文件内容
     *
     * @param is 文件
     */
    public static String readTextInputStream(InputStream is) throws IOException {
        StringBuffer strbuffer = new StringBuffer();
        String line;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                strbuffer.append(line).append("\r\n");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return strbuffer.toString();
    }

    //

    /**
     * 将文本内容写入文件
     *
     * @param file 文件
     * @param str  文本内容
     */
    public static void writeTextFile(File file, String str) throws IOException {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(file));
            out.write(str.getBytes());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 将asset文件下内容写入savepath文件
     *
     * @param context
     * @param assetpath asset路径
     * @param savePath  savePath下保存路径
     */
    public static void AssetToSD(Context context, String assetpath, String savePath) {

        AssetManager asset = context.getAssets();
        FileOutputStream out = null;
        InputStream in = null;
        try {

            File SDFlie = new File(savePath);
            if (!SDFlie.exists()) {
                SDFlie.createNewFile();
            }
            //将内容写入到文件中
            in = asset.open(assetpath);
            out = new FileOutputStream(SDFlie);
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteCount);
            }
            out.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
                asset.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }


    /**
     * 将base64字符转换成临时音乐文件
     *
     * @param base64Str
     * @param ex        后续名
     * @return
     */
    public static File base64ToFile(String base64Str, String ex) {
        FileOutputStream outputStream = null;

        try {
            File tempFile = new File(getVoicePath(), System.currentTimeMillis() + ex);
            byte[] audioByte = Base64.decode(base64Str, Base64.DEFAULT);
            outputStream = new FileOutputStream(tempFile);
            outputStream.write(audioByte, 0, audioByte.length);
            outputStream.flush();
            outputStream.close();
            return tempFile;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 保存位图
     *
     * @param bitmap
     * @param file
     * @return
     */
    public static boolean saveBitmap(Bitmap bitmap, File file) {
        if (bitmap == null)
            return false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 保存位图
     *
     * @param bitmap
     * @param absPath 文件路径
     * @return
     */
    public static boolean saveBitmap(Bitmap bitmap, String absPath) {
        return saveBitmap(bitmap, new File(absPath));
    }
}
