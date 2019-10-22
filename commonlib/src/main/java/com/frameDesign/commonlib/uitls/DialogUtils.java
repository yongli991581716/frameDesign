package com.frameDesign.commonlib.uitls;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import com.frameDesign.commonlib.R;
import com.frameDesign.commonlib.views.dialog.ProgressDialog;
import com.frameDesign.commonlib.views.dialog.ZqkhDialog;

/**
 * 对话框工具
 *
 * @author liyong
 * @date  2018/1/22.
 */
public class DialogUtils {

    public static ProgressDialog createProgressDialog(Activity activity, String message) {
        ProgressDialog pgsDialog = new ProgressDialog(activity);
//        pgsDialog.setMessage(message);
        pgsDialog.setCancelable(false);
        pgsDialog.setCanceledOnTouchOutside(false);

        return pgsDialog;
    }

    /**
     * 等待对话框
     */
    public static ProgressDialog createProgressDialog(Activity activity) {
        return createProgressDialog(activity, activity.getString(R.string.pls_waiting));
    }

    public static void dismissDialog(Dialog pgsDialog) {
        if (pgsDialog != null && pgsDialog.isShowing()) {
            pgsDialog.dismiss();
        }
    }

    public static Boolean dialogShowing(Dialog dialog)

    {
        return dialog != null && dialog.isShowing();
    }

    public static ZqkhDialog createNormalDialog(Activity activity, String msg)

    {
        ZqkhDialog dialog = new ZqkhDialog(activity);
        dialog.setMessage(msg);
        dialog.setNegativeButton(R.string.known, null);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    /**
     * 普通对话框 有title
     */
    public static ZqkhDialog createAlertDialog(Activity activity, int titleId, int messageId,
                                               int leftButton, DialogInterface.OnClickListener onLeftClickListener,
                                               int rightButton, DialogInterface.OnClickListener onRightClickListener)

    {
        ZqkhDialog dialog = createAlertDialog(activity, messageId, leftButton, onLeftClickListener, rightButton, onRightClickListener);
        dialog.setTitle(activity.getResources().getString(titleId));
        return dialog;
    }

    /**
     * 普通对话框 有title
     */
    public static ZqkhDialog createAlertDialog(Activity activity, String title, String message,
                                               String leftButton, DialogInterface.OnClickListener onLeftClickListener,
                                               String rightButton, DialogInterface.OnClickListener onRightClickListener)

    {
        ZqkhDialog dialog = createDialog(activity, message, leftButton, onLeftClickListener, rightButton, onRightClickListener);
        dialog.setTitle(title);
        return dialog;
    }

    /**
     * 普通对话框 有title
     */
    public static ZqkhDialog createLeftDialog(Activity activity, String title, String message,
                                              String leftButton, DialogInterface.OnClickListener onLeftClickListener,
                                              String rightButton, DialogInterface.OnClickListener onRightClickListener)

    {
        ZqkhDialog dialog = createDialog(activity, message, leftButton, onLeftClickListener, rightButton, onRightClickListener);
        dialog.setTitle(title);
        dialog.setTxtGravity(Gravity.LEFT);
        return dialog;
    }

    /**
     * 普通对话框 有title
     */
    public static ZqkhDialog createAlertDialog(Activity activity, int titleId, String message,
                                               String leftButton, DialogInterface.OnClickListener onLeftClickListener,
                                               String rightButton, DialogInterface.OnClickListener onRightClickListener)

    {
        ZqkhDialog dialog = createDialog(activity, message, leftButton, onLeftClickListener, rightButton, onRightClickListener);
        dialog.setTitle(activity.getResources().getString(titleId));
        return dialog;
    }

    /**
     * 普通对话框 无title
     */
    public static ZqkhDialog createAlertDialog(Activity activity, String message,
                                               String leftButton, DialogInterface.OnClickListener onLeftClickListener,
                                               String rightButton, DialogInterface.OnClickListener onRightClickListener)

    {
        ZqkhDialog dialog = createDialog(activity, message, leftButton, onLeftClickListener, rightButton, onRightClickListener);
        return dialog;
    }

    /**
     * 创建基础对话框
     *
     * @param activity
     * @param message
     * @param leftButton
     * @param onLeftClickListener
     * @param rightButton
     * @param onRightClickListener
     * @return
     */
    private static ZqkhDialog createDialog(Activity activity, String message, String leftButton, DialogInterface.OnClickListener onLeftClickListener, String rightButton, DialogInterface.OnClickListener onRightClickListener) {
        ZqkhDialog dialog = new ZqkhDialog(activity);
        dialog.setMessage(message);
        if (!TextUtils.isEmpty(leftButton)) {
            dialog.setNegativeButton(leftButton, onLeftClickListener);
        }

        if (!TextUtils.isEmpty(rightButton)) {
            dialog.setPositiveButton(rightButton, onRightClickListener);
        }

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    /**
     * 普通对话框 无title
     */
    public static ZqkhDialog createAlertDialog(Activity activity, int message,
                                               int leftButton, DialogInterface.OnClickListener onLeftClickListener,
                                               int rightButton, DialogInterface.OnClickListener onRightClickListener)

    {
        ZqkhDialog dialog = new ZqkhDialog(activity);
        dialog.setMessage(message);
        if (leftButton > 0) {
            dialog.setNegativeButton(leftButton, onLeftClickListener);
        }

        if (rightButton > 0) {
            dialog.setPositiveButton(rightButton, onRightClickListener);
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }


    /**
     * 普通对话框 icon
     */
    public static ZqkhDialog createIconDialog(Activity activity, int icon, int message,
                                              int leftButton, DialogInterface.OnClickListener onLeftClickListener,
                                              int rightButton, DialogInterface.OnClickListener onRightClickListener)

    {
        ZqkhDialog dialog = new ZqkhDialog(activity);
        dialog.setTitleIcon(icon);
        dialog.setMessage(message);
        if (leftButton > 0) {
            dialog.setNegativeButton(leftButton, onLeftClickListener);
        }

        if (rightButton > 0) {
            dialog.setPositiveButton(rightButton, onRightClickListener);
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    /**
     * 普通AlertDialog对话框
     */
    public static AlertDialog.Builder createAlertDialogBuilder(Activity activity, int title, int message,
                                                               int leftButton, DialogInterface.OnClickListener onLeftClickListener,
                                                               int rightButton, DialogInterface.OnClickListener onRightClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        if (leftButton > 0) {
            builder.setNegativeButton(leftButton, onLeftClickListener);
        }
        if (rightButton > 0) {
            builder.setPositiveButton(rightButton, onRightClickListener);
        }

        return builder;
    }

    /**
     * 普通AlertDialog对话框
     */
    public static AlertDialog.Builder createAlertDialogBuilder(Activity activity, String title, String message,
                                                               String leftButton, DialogInterface.OnClickListener onLeftClickListener,
                                                               String rightButton, DialogInterface.OnClickListener onRightClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        if (!TextUtils.isEmpty(leftButton)) {
            builder.setNegativeButton(leftButton, onLeftClickListener);
        }
        if (!TextUtils.isEmpty(rightButton)) {
            builder.setPositiveButton(rightButton, onRightClickListener);
        }

        return builder;
    }


}
