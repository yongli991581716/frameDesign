package com.frameDesign.baselib.view.delegate

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.frameDesign.baselib.R
import com.frameDesign.commonlib.views.internal.BaseActivityDelegate
import com.frameDesign.commonlib.views.internal.ILifeCycle
import com.frameDesign.commonlib.expand.dp2px
import com.frameDesign.commonlib.expand.zqColor
import com.frameDesign.commonlib.expand.zqString
import com.frameDesign.commonlib.uitls.SystemUtil
import com.jaeger.library.StatusBarUtil
import com.frameDesign.commonlib.expand.doOnClick
import com.frameDesign.commonlib.expand.inVisible
import com.frameDesign.commonlib.expand.isVisible
import com.frameDesign.commonlib.expand.updateLayoutSize
import org.jetbrains.anko.textColor


/**
 * @desc  titlebar View代理
 * ..
 * @author liyong
 * @date 2018/10/17
 */
class TitleDelegate(
    act: Activity,
    cycle: ILifeCycle,
    val doDefaultLeft: (v: View) -> Unit
) : BaseActivityDelegate(act, cycle) {

    companion object {

        fun fitImmersiveStatusHeight(positionView: View) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                var height = SystemUtil.getStatusHeight()

                height = height.takeIf { height > 0 } ?: 25.dp2px()

                positionView.updateLayoutSize(height = height)
            } else {
                positionView.updateLayoutSize(height = 0)
            }
        }

        fun fitTitleBarHeight(titleLayout: View) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                var height = SystemUtil.getStatusHeight()

                height = height.takeIf { height > 0 } ?: 25.dp2px()

                val params = titleLayout.layoutParams

                if (params is ViewGroup.MarginLayoutParams) {
                    params.topMargin = height
                }

                titleLayout.requestLayout()
            }
        }

        fun fitStatusBarColor(act: Activity, @ColorInt color: Int) {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> try {
                    val decorView = act.window.decorView     //获取DecorView实例
                    val field = decorView::class.java.getDeclaredField(
                        "mSemiTransparentStatusBarColor"
                    )  //获取特定的成员变量
                    field.isAccessible = true                    //设置对此属性的可访问性
                    field.setInt(decorView, Color.TRANSPARENT)  //修改属性值
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                    act.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)   //去除半透明状态栏
                    act.window.decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //全屏显示
                    act.window.statusBarColor = Color.TRANSPARENT
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                    //由于设置fitSystemWindow无效,设置一个类似fitSystemWindow的效果,否则content会从最顶部开始
                    act.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    act.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

                    StatusBarUtil.setColorNoTranslucent(act, color)
                }
            }
        }
    }

    private var mTitleLayout: View? = null
    private var mTitleLayoutL: ViewGroup? = null
    private var mTitleLayoutR: ViewGroup? = null
    private var mTitleContent: ViewGroup? = null

    private var mTextLeft: TextView? = null
    private var mTextRight: TextView? = null
    private var mTextCenter: TextView? = null

    private var mIconLeft: ImageView? = null
    private var mIconRight: ImageView? = null

    private var mLineView: View? = null
    private var mTitleArrowView: View? = null

    private var isVisibleLine: Boolean = true
    private var isVisibleTitleArrow: Boolean = false

    private var mLeftTextResource: Any? = null
    private var mRightTextResource: Any? = null
    private var mContentTextResource: Any? = null

    private var mLeftTextColor = zqColor(R.color.colorWhite)
    private var mRightTextColor = zqColor(R.color.colorWhite)
    private var mContentTextColor = zqColor(R.color.colorWhite)
    private var mBackgroundColor = zqColor(R.color.colorPrimary)

    private var mTitleClick: View.OnClickListener? = null
    private var mLiftClick: View.OnClickListener? = null
    private var mRightClick: View.OnClickListener? = null

    override fun onViewCreated() {
        mTitleLayout = find(R.id.ll_title_root)

        mTitleContent = mTitleLayout.fv(R.id.rl_title_content)
        mTitleLayoutL = mTitleLayout.fv(R.id.layout_title_left)
        mTitleLayoutR = mTitleLayout.fv(R.id.layout_title_right)

        mTextLeft = mTitleLayout.fv(R.id.tv_title_left)
        mTextRight = mTitleLayout.fv(R.id.tv_title_right)
        mTextCenter = mTitleLayout.fv(R.id.tv_title_content)

        mIconLeft = mTitleLayout.fv(R.id.iv_title_left)
        mIconRight = mTitleLayout.fv(R.id.iv_title_right)

        mLineView = mTitleLayout.fv(R.id.iv_line)
        mTitleArrowView = mTitleLayout.fv(R.id.tv_title_arrow)

        mTitleLayoutL?.doOnClick {
            doDefaultLeft(it)
        }

        updateViewData()
    }

    private fun updateViewData() {
        mLineView?.isVisible = isVisibleLine
        mTitleArrowView?.isVisible = isVisibleTitleArrow

        initViewData(mLeftTextResource, mTitleLayoutL, mTextLeft, mIconLeft, mLiftClick)
        initViewData(mRightTextResource, mTitleLayoutR, mTextRight, mIconRight, mRightClick)
        initViewData(mContentTextResource, mTitleContent, mTextCenter, null, mTitleClick)

        mTextLeft?.setTextColor(mLeftTextColor)
        mTextRight?.setTextColor(mRightTextColor)
        mTextCenter?.setTextColor(mContentTextColor)

        mTitleLayout?.setBackgroundColor(mBackgroundColor)

//        if (mTitleLayout != null) {
//            fitTitleBarHeight(mTitleLayout!!)
//        }

//        val activity = mActivityRef.get()
//
//        if (activity != null) {
//            fitStatusBarColor(activity, mBackgroundColor)
//        }
    }

    private fun initViewData(
        data: Any?,
        root: ViewGroup?,
        text: TextView?,
        image: ImageView?,
        l: View.OnClickListener?
    ) {
        if (data != null) {
            var isInit = false
            if (data is Int) {
                isInit = true

                image?.setImageResource(data)
            } else if (data is CharSequence && data.isNotEmpty()) {
                isInit = true

                text?.text = data
            }

            if (isInit) {
                root?.inVisible = false
                if (l != null) {
                    root?.setOnClickListener(l)
                }
                return
            }
        }

        root?.inVisible = true
    }

    fun switchTransTheme() {
        setTitleBackground(R.color.colorTransparent)
        setTitleLeftIcon(R.mipmap.commom_ic_back)

        setTitleLeftColor(R.color.colorBlack)
        setTitleRightColor(R.color.colorBlack)
        setTitleContentColor(R.color.colorBlack)
    }

    fun switchWhiteTheme() {
        setTitleBackground(R.color.colorWhite)
        setTitleLeftIcon(R.mipmap.commom_ic_back)

        setTitleLeftColor(R.color.colorBlack)
        setTitleRightColor(R.color.colorBlack)
        setTitleContentColor(R.color.colorBlack)
    }

    fun switchBlueTheme() {
        setTitleLeftIcon(R.mipmap.common_ic_back_white)
        setTitleBackground(R.color.colorPrimary)

        setTitleLeftColor(R.color.colorWhite)
        setTitleRightColor(R.color.colorWhite)
        setTitleContentColor(R.color.colorWhite)
    }

    fun setLineVisible(show: Boolean) {
        isVisibleLine = show

        mLineView?.isVisible = show
    }

    fun setArrowVisible(show: Boolean) {
        isVisibleTitleArrow = show

        mTitleArrowView?.isVisible = show
    }

    /**
     * 设置左边标题
     */
    fun setTitleLeftText(str: CharSequence?) {
        mLeftTextResource = str

        mTextLeft?.text = str
        mIconLeft?.setImageDrawable(null)

        mTitleLayoutL?.inVisible =
            str.isNullOrEmpty()
    }

    /**
     * 设置左边图片
     * @param resId Int
     */
    fun setTitleLeftIcon(@DrawableRes resId: Int) {
        mLeftTextResource = resId

        mIconLeft?.setImageResource(resId)
        mTitleLayoutL?.visibility = View.VISIBLE
    }

    /**
     * 设置右边标题
     */
    fun setTitleRight(str: CharSequence?) {
        mRightTextResource = str

        mTextRight?.text = str
        mTitleLayoutR?.inVisible =
            str.isNullOrEmpty()
    }

    /**
     * 设置右边图片
     */
    fun setTitleRightIcon(@DrawableRes resId: Int) {
        mRightTextResource = resId

        mIconRight?.setImageResource(resId)
        mTitleLayoutR?.visibility = View.VISIBLE
    }

    /**
     * 设置右边标题
     */
    fun setTitleLeftColor(@ColorRes resId: Int) {
        mLeftTextColor = zqColor(resId)

        mTextLeft?.textColor = mLeftTextColor
    }

    /**
     * 设置右边标题
     */
    fun setTitleRightColor(@ColorRes resId: Int) {
        mRightTextColor = zqColor(resId)

        mTextRight?.textColor = mRightTextColor
    }

    /**
     * 设置中间标题
     */
    fun setTitleContent(str: CharSequence?) {
        mContentTextResource = str

        mTextCenter?.text = str
        mTitleContent?.inVisible =
            str.isNullOrEmpty()
    }

    /**
     * 设置中间标题
     */
    fun setTitleContent(@StringRes resId: Int) {
        mContentTextResource = resId

        mTextCenter?.text = zqString(resId)
        mTitleContent?.visibility = View.VISIBLE
    }

    /**
     * 设置中间标题颜色
     */
    fun setTitleContentColor(@ColorRes resId: Int) {
        mContentTextColor = zqColor(resId)

        mTextCenter?.textColor = mContentTextColor
    }

    fun setTitleBackground(@ColorRes resId: Int) {
        mBackgroundColor = zqColor(resId)

        mTitleLayout?.setBackgroundColor(mBackgroundColor)
    }

    /**
     * 设置左边点击事件
     * @param onClick View.OnClickListener?
     */
    fun doTitleClick(onClick: View.OnClickListener?) {
        mTitleClick = onClick

        if (onClick != null) {
            mTitleLayout?.doOnClick(onClick)
        } else {
            mTitleLayout?.setOnClickListener(null)
        }
    }

    /**
     * 设置左边点击事件
     * @param onClick View.OnClickListener?
     */
    fun doLeftClick(onClick: View.OnClickListener?) {
        mLiftClick = onClick

        if (onClick != null) {
            mTitleLayoutL?.doOnClick(onClick)
        } else {
            mTitleLayoutL?.setOnClickListener(null)
        }
    }

    /**
     * 设置右边的点击事件
     * @param onClick View.OnClickListener
     */
    fun doRightClick(onClick: View.OnClickListener?) {
        mRightClick = onClick

        if (onClick != null) {
            mTitleLayoutR?.doOnClick(onClick)
        } else {
            mTitleLayoutR?.setOnClickListener(null)
        }
    }

    /**
     * 设置左边点击事件
     * @param onClick View.OnClickListener?
     */
    fun doLeftClick(onClick: (v: View) -> Unit) =
        doLeftClick(View.OnClickListener {
            onClick(it)
        })

    /**
     * 设置右边的点击事件
     * @param onClick View.OnClickListener
     */
    fun doRightClick(onClick: (v: View) -> Unit) =
        doRightClick(View.OnClickListener {
            onClick(it)
        })
}