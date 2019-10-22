/**
 *
 */

package com.frameDesign.commonlib.uitls;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import static com.frameDesign.commonlib.CommHelper.mCtx;

/**
 * 屏幕尺寸转换
 *
 * @author liyong
 */
public class DensityUtils {

    private static int mScreenW = 0;
    private static int mScreenH = 0;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = mCtx.getResources()
                .getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = mCtx.getResources()
                .getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp to px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = mCtx
                .getResources().getDisplayMetrics().scaledDensity;

        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */

    public static int px2sp(float pxValue) {
        final float fontScale = mCtx
                .getResources().getDisplayMetrics().scaledDensity;

        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        if (mScreenW <= 0) {
            WindowManager wm = (WindowManager) mCtx
                    .getSystemService(Context.WINDOW_SERVICE);

            DisplayMetrics metric = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metric);
            mScreenW = metric.widthPixels;
        }

        return mScreenW;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight() {
        if (mScreenH <= 0) {
            WindowManager wm = (WindowManager) mCtx
                    .getSystemService(Context.WINDOW_SERVICE);

            DisplayMetrics metric = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metric);
            mScreenH = metric.heightPixels;
        }

        return mScreenH;
    }
}
