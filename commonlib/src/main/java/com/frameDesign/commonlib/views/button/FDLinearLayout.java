package com.frameDesign.commonlib.views.button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 自定义button
 *
 * @author liyong
 * @date 2017/5/9 13:57
 */
@SuppressLint("AppCompatCustomView")
public class FDLinearLayout extends LinearLayout {
    /**
     * 最近一次down事件时间
     */
    private long lastTime;
    /**
     * 间隔时间
     */
    private long TIME_GAP = 500;

    public FDLinearLayout(Context context) {
        super(context);
    }

    public FDLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FDLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                long currentTime = System.currentTimeMillis();
                long intervalTime = currentTime - lastTime;
                Log.d(this.getClass().getName(), lastTime + "");
                if (intervalTime < TIME_GAP) {
                    //防止抖动两次事件
                    lastTime = currentTime;
                    Log.d(this.getClass().getName(), "stop");
                    return true;
                } else {
                    lastTime = currentTime;
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
