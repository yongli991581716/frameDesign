package com.frameDesign.commonlib.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.frameDesign.commonlib.R;
import com.frameDesign.commonlib.uitls.DensityUtils;
import com.frameDesign.commonlib.views.button.ZqkhTextButton;

/**
 * 自定义对话框
 *
 * @author liyong
 * @date  2018/2/9.
 */
public class ZqkhDialog extends Dialog {

    private Context mContext;

    // private DialogViewBinding mBinding;
    private ZqkhTextButton mPositiveBtn;//确定按钮
    private ZqkhTextButton mNegativeBtn;//取消按钮
    private TextView mTitleTv;//消息标题文本
    private ImageView mTitleIv;//消息
    private View mMessageView;//消息布局
    private TextView mMessageTv;//消息提示文本
    private int mTitleColor;//消息颜色
    private int mMessageColor;//消息颜色
    private View iconIv;//提示icon
    private ViewGroup custContainer;//自定义view容器
    private View custView;//自定义view
    private int leftColor;//左边按钮的颜色
    private int rightColor;//右边按钮的颜色
    private int txtGravity = -1;//对齐方式

    private OnClickListener onNegativeClickListener;

    private OnClickListener onPositiveClickListener;
    private String mTitle;//从外界设置的title文本
    private int titleIcon;//titleicon
    private String mMessage;//从外界设置的消息文本
    //确定文本和取消文本的显示内容
    private String mNegative, mPositive;
    private int iconVisibility = View.GONE;


    /**
     * 是否能设置外部触摸
     */
    private boolean mTouchOutSideFlag = false;

    /**
     * 设置幕布
     */
    private float mDimAount = 0.8f;

    public ZqkhDialog(Context context) {
        super(context, R.style.dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_view);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        mPositiveBtn = (ZqkhTextButton) findViewById(R.id.positive);
        mNegativeBtn = (ZqkhTextButton) findViewById(R.id.negative);
        mTitleTv = (TextView) findViewById(R.id.title);
        mTitleIv = (ImageView) findViewById(R.id.titleIcon);
        mMessageTv = (TextView) findViewById(R.id.message);
        iconIv = findViewById(R.id.icon);
        custContainer = findViewById(R.id.cust_container);
        //设置消息控件全局监听
        mMessageView = findViewById(R.id.mes_container);
        mMessageView.getViewTreeObserver().addOnGlobalLayoutListener(new OnViewGlobalLayoutListener(mMessageView));

        if (titleIcon > 0) {
            mTitleIv.setVisibility(View.VISIBLE);
            mTitleTv.setVisibility(View.GONE);
            mTitleIv.setImageResource(titleIcon);
        } else {
            if (TextUtils.isEmpty(mTitle)) {
                mTitleTv.setVisibility(View.GONE);
            } else {
                mTitleTv.setText(mTitle);
                if (mTitleColor > 0) {
                    mTitleTv.setTextColor(getContext().getResources().getColor(mTitleColor));
                }
            }
        }


        if (TextUtils.isEmpty(mMessage)) {
            mMessageTv.setVisibility(View.GONE);
            mMessageView.setVisibility(View.GONE);
        } else {
            mMessageTv.setText(mMessage);
            if (mMessageColor > 0) {
                mMessageTv.setTextColor(getContext().getResources().getColor(mMessageColor));
            }
        }

        if (txtGravity >= 0) {
            mMessageTv.setGravity(txtGravity);
        }

        if (custView != null) {
            custContainer.addView(custView);
        }

        if (TextUtils.isEmpty(mNegative)) {
            mNegativeBtn.setVisibility(View.GONE);
        } else {
            mNegativeBtn.setText(mNegative);
            if (leftColor > 0) {
                mNegativeBtn.setTextColor(getContext().getResources().getColor(leftColor));
            }
        }
        if (TextUtils.isEmpty(mPositive)) {
            mPositiveBtn.setVisibility(View.GONE);
        } else {
            mPositiveBtn.setText(mPositive);
            if (rightColor > 0) {
                mPositiveBtn.setTextColor(getContext().getResources().getColor(rightColor));
            }
        }

        if (TextUtils.isEmpty(mNegative) && TextUtils.isEmpty(mPositive)) {
            LinearLayout linearLayout = findViewById(R.id.linearLayout_listener);
            linearLayout.setVisibility(View.GONE);
        }

        //设置图标参数值
        if (iconVisibility == View.VISIBLE) {
            LinearLayout linearLayout = findViewById(R.id.linearLayout);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
            params.setMargins(0, DensityUtils.dip2px(mContext, 70), 0, 0);
            linearLayout.setLayoutParams(params);

            LinearLayout linearLayoutContent = findViewById(R.id.linearLayout_content);
            linearLayoutContent.setPadding(DensityUtils.dip2px(mContext, 20),
                    DensityUtils.dip2px(mContext, 50),
                    DensityUtils.dip2px(mContext, 20),
                    DensityUtils.dip2px(mContext, 20));
        }
        iconIv.setVisibility(iconVisibility);

        //按空白处不能取消动画
        setCanceledOnTouchOutside(mTouchOutSideFlag);

        //设置幕布
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        // 设置透明度为0.8
        lp.dimAmount = mDimAount;
        window.setAttributes(lp);
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        mNegativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZqkhDialog.this.dismiss();
                if (onNegativeClickListener != null) {
                    onNegativeClickListener.onClick(ZqkhDialog.this, R.id.negative);
                }
            }
        });

        mPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZqkhDialog.this.dismiss();
                if (onPositiveClickListener != null) {
                    onPositiveClickListener.onClick(ZqkhDialog.this, R.id.positive);
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {

    }

    /**
     * message 最大高度
     */
    public class OnViewGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private int maxHeight;//300dp
        private View view;

        public OnViewGlobalLayoutListener(View view) {
            this.view = view;
            this.maxHeight = DensityUtils.dip2px(mContext, 300);
        }

        @Override
        public void onGlobalLayout() {
            if (view.getHeight() > maxHeight)
                view.getLayoutParams().height = maxHeight;
        }
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        mTitle = title;
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param titleId
     */
    public void setTitle(int titleId) {
        mTitle = mContext.getString(titleId);
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        mMessage = message;
    }

    /**
     * 文字对方方式
     *
     * @param gravity
     */
    public void setTxtGravity(int gravity) {
        this.txtGravity = gravity;
    }

    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param messageId
     */
    public void setMessage(int messageId) {
        mMessage = mContext.getString(messageId);
    }

    /**
     * @param content
     * @param onClickListener
     */
    public void setNegativeButton(String content, OnClickListener onClickListener) {
        mNegative = content;
        onNegativeClickListener = onClickListener;
    }

    public void setIconVisible(int visibility) {
        iconVisibility = visibility;
    }

    /**
     * @param contentId
     * @param onClickListener
     */
    public void setNegativeButton(int contentId, OnClickListener onClickListener) {
        mNegative = mContext.getString(contentId);
        onNegativeClickListener = onClickListener;
    }

    /**
     * @param content
     * @param onClickListener
     */
    public void setPositiveButton(String content, OnClickListener onClickListener) {
        mPositive = content;
        onPositiveClickListener = onClickListener;
    }

    /**
     * @param contentId
     * @param onClickListener
     */
    public void setPositiveButton(int contentId, OnClickListener onClickListener) {
        mPositive = mContext.getString(contentId);
        onPositiveClickListener = onClickListener;
    }

    public ZqkhTextButton getPositive() {
        return mPositiveBtn;
    }

    public ZqkhTextButton getNegative() {
        return mNegativeBtn;
    }

    public TextView getTitle() {
        return mTitleTv;
    }

    public TextView getMessage() {
        return mMessageTv;
    }

    public View getIcon() {
        return iconIv;
    }

    /**
     * 自定义布局
     *
     * @return
     */
    public View getCustView() {
        return custView;
    }

    public void setCustView(View custView) {
        this.custView = custView;
    }

    public void setMessageColor(int mMessageColor) {
        this.mMessageColor = mMessageColor;
    }

    public void setTitleColor(int mTitleColor) {
        this.mTitleColor = mTitleColor;
    }

    public void setRightColor(int rightColor) {
        this.rightColor = rightColor;
    }

    public void setLeftColor(int leftColor) {
        this.leftColor = leftColor;
    }

    public void setTitleIcon(int titleIcon) {
        this.titleIcon = titleIcon;
    }
}
