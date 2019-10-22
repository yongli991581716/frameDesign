package com.frameDesign.commonlib.uitls;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


/**
 * toast工具
 * @author liyong
 * @date 2019-10-22.
 */
public class ToastUtils {

    public static void show(Activity activity, String message) {
        toastInstace(activity, message).show();
    }

    public static void show(Activity activity, int resId) {
        toastInstace(activity, activity.getResources().getString(resId)).show();
    }

    /**
     * 通过{@link Context}生成Toast
     *
     * @param context
     * @param msg
     */
    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showCenter(Activity activity, String message) {
        Toast toast = toastInstace(activity, message);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public static void showCenter(Activity activity, int resId) {
        Toast toast = toastInstace(activity, activity.getResources().getString(resId));
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * toast实例
     *
     * @param activity
     * @param message
     * @return
     */
    private static Toast toastInstace(Activity activity, String message) {
        return Toast.makeText(activity, message, Toast.LENGTH_SHORT);
    }
}
