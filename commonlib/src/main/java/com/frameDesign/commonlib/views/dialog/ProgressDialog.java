package com.frameDesign.commonlib.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.frameDesign.commonlib.R;

/**
 * 对话框工具
 *
 * @author liyong
 * @date  2018/3/30.
 */
public class ProgressDialog extends Dialog {

    private Context mContext;

//    private TextView mMessageTv;//消息提示文本
//    private String mMessage;//从外界设置的消息文本

    /**
     * 是否能设置外部触摸
     */
    private boolean mTouchOutSideFlag = false;

    /**
     * 设置幕布
     */
    private float mDimAount = 0.8f;

    public ProgressDialog(Context context) {
        super(context, R.style.dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);

        //初始化界面控件
        initView();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {

//        mMessageTv = (TextView) findViewById(R.id.message);
//
//
//        if (TextUtils.isEmpty(mMessage)) {
//            mMessageTv.setVisibility(View.GONE);
//        } else {
//            mMessageTv.setText(mMessage);
//        }

        //按空白处不能取消动画
        setCanceledOnTouchOutside(mTouchOutSideFlag);

        //设置幕布
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 设置透明度为0.8
        lp.dimAmount = mDimAount;
        window.setAttributes(lp);
    }

//    /**
//     * 从外界Activity为Dialog设置dialog的message
//     *
//     * @param message
//     */
//    public void setMessage(String message) {
//        mMessage = message;
//    }
//
//    /**
//     * 从外界Activity为Dialog设置dialog的message
//     *
//     * @param messageId
//     */
//    public void setMessage(int messageId) {
//        mMessage = mContext.getString(messageId);
//    }
//
//
//    public TextView getMessage() {
//        return mMessageTv;
//    }
}

