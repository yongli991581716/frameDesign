package com.frameDesign.baselib.controller

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.CallSuper
import com.alibaba.android.arouter.launcher.ARouter
import com.frameDesign.baselib.R
import com.frameDesign.baselib.controller.life.RxActivityResult
import com.frameDesign.commonlib.expand.runUIThread
import com.frameDesign.commonlib.uitls.FitHelper
import com.frameDesign.commonlib.views.internal.ILifeCycle
import com.tbruyelle.rxpermissions2.RxPermissions
import com.umeng.analytics.MobclickAgent
import com.zqkh.commlibrary.internal.LoadAction
import com.zqkh.commlibrary.internal.ViewAction
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.imid.swipebacklayout.lib.app.SwipeBackActivity
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.AnkoException
import org.jetbrains.anko.contentView
import java.io.Serializable
import java.util.concurrent.TimeUnit

/**
 * @desc  activity基础公共类
 * ..
 * @author liyong
 * @date 2018/10/17
 */
open abstract class BaseCommActivity : SwipeBackActivity(), ILifeCycle, ViewAction, LoadAction {

    companion object {
        val FINISH_RESULT_ACTIVITY = 100//关闭界面
    }

    @Volatile
    override var firstLoad: Boolean = true
    override val mLifecycle: PublishSubject<Int> = PublishSubject.create()

    override var mCurrentCycle = ILifeCycle.CREATE
        set(value) {
            field = value

            if (value == ILifeCycle.VIEW_CREATED) {
                hasViewCreated = true
            }

            // 发布生命周期事件
            mLifecycle.onNext(value)

            if (value == ILifeCycle.DESTROY) {
                mLifecycle.onComplete()
            }
        }

    private var isInitEvenBus: Boolean = false
    private var hasViewCreated: Boolean = false

    protected val mEventBus by lazy(LazyThreadSafetyMode.NONE) {
        EventBus.getDefault().also { isInitEvenBus = true }
    }
    protected val mARouter by lazy(LazyThreadSafetyMode.NONE) {
        ARouter.getInstance()
    }

    /**
     * rxJava 封装界面[onActivityResult]数据回调
     */
    val rxResult by lazy(LazyThreadSafetyMode.NONE) {
        RxActivityResult(this)
    }

    /**
     * rxJava 封装权限管理
     */
    val rxPermissions by lazy(LazyThreadSafetyMode.NONE) {
        RxPermissions(this)
    }

    protected val mActivity by lazy(LazyThreadSafetyMode.NONE) { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCurrentCycle = ILifeCycle.CREATE // 发布创建事件

        initDataBeforeView()

        val view = getLayoutView()

        when (view) {
            is Int -> setContentView(view)
            is View -> setContentView(view)
            else -> throw IllegalArgumentException(
                "getLayoutView() can only return an Int (layout res) or a View"
            )
        }

        mCurrentCycle = ILifeCycle.VIEW_CREATED

        internalInitView(savedInstanceState)

        initView(savedInstanceState)

        if (isFitTiltBar()) {
            fitTitleBar()
        }

        //StatusBarUtil.setLightMode(this)
    }

    protected open fun isFitTiltBar(): Boolean = true

    private fun fitTitleBar() {
        FitHelper.fitStatusBar(
            contentView,
            findViewById(R.id.title_layout)
        )

    }


    /**
     * 在View前面执行, 初始化数据
     *
     * 注: 此时还没有添加布局
     */
    protected open fun initDataBeforeView() {

    }

    /**
     * 获取界面View, 只能是layout id或View
     * @return Int
     */
    abstract fun getLayoutView(): Any

    /**
     * 用于在系统内初始化控件, 从而不必重新[initView]
     * @param state Bundle?
     */
    @CallSuper
    protected open fun internalInitView(state: Bundle?) {

    }

    /**
     * 初始化View
     * @param state Bundle?
     */
    abstract fun initView(state: Bundle?)


    fun finishResultOK(v: View?) {
        finishResultOKWithIntent(null)
    }

    fun finishResultOKWithIntent(vararg params: Pair<String, Any?>?) {
        val intent = Intent()
        params?.forEach {
            val key = it?.first
            val value = it?.second
            when (value) {
                null -> intent.putExtra(key, null as Serializable?)
                is Int -> intent.putExtra(key, value)
                is Long -> intent.putExtra(key, value)
                is CharSequence -> intent.putExtra(key, value)
                is String -> intent.putExtra(key, value)
                is Float -> intent.putExtra(key, value)
                is Double -> intent.putExtra(key, value)
                is Char -> intent.putExtra(key, value)
                is Short -> intent.putExtra(key, value)
                is Boolean -> intent.putExtra(key, value)
                is Serializable -> intent.putExtra(key, value)
                is Bundle -> intent.putExtra(key, value)
                is Parcelable -> intent.putExtra(key, value)
                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> intent.putExtra(key, value)
                    value.isArrayOf<String>() -> intent.putExtra(key, value)
                    value.isArrayOf<Parcelable>() -> intent.putExtra(key, value)
                    else -> throw AnkoException("Intent extra ${key} has wrong type ${value.javaClass.name}")
                }
                is IntArray -> intent.putExtra(key, value)
                is LongArray -> intent.putExtra(key, value)
                is FloatArray -> intent.putExtra(key, value)
                is DoubleArray -> intent.putExtra(key, value)
                is CharArray -> intent.putExtra(key, value)
                is ShortArray -> intent.putExtra(key, value)
                is BooleanArray -> intent.putExtra(key, value)
            }
        }
        finishResultOKWithIntentIntent(intent)
    }

    fun finishResultOKWithIntentIntent(intent: Intent?) {
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

        mCurrentCycle = ILifeCycle.START
    }

    override fun onResume() {
        super.onResume()

        lazyLoad()

        MobclickAgent.onResume(this)

        mCurrentCycle = ILifeCycle.RESUME
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)

        mCurrentCycle = ILifeCycle.PAUSE
    }

    override fun onStop() {
        super.onStop()

        mCurrentCycle = ILifeCycle.STOP
    }

    override fun onDestroy() {
        super.onDestroy()

        mCurrentCycle = ILifeCycle.DESTROY

        if (isInitEvenBus && mEventBus.isRegistered(this)) {
            mEventBus.unregister(this)
        }
    }

    override fun loadData() {

    }

    override fun lazyLoad() {
        if (firstLoad) {
            firstLoad = false

            loadData()
        }
    }

    override fun isActiveCycle(): Boolean = hasViewCreated
            && mCurrentCycle != ILifeCycle.DESTROY
            && mCurrentCycle != ILifeCycle.DETACH

    /**
     * 线程安全的眼神操作, 会绑定[BaseCommActivity.onDestroy]并自动销毁
     * @receiver Int
     * @param runTimer () -> Unit
     * @Deprecated 替换为postDelay()
     */
//    @Deprecated("使用面狭窄")
    protected infix fun Int.timer(runTimer: () -> Unit) {
        Observable.timer(this.toLong(), TimeUnit.MILLISECONDS)
            .bindDestroy()
            .runUIThread()
            .subscribe {
                kotlin.run(runTimer)
            }
    }
}