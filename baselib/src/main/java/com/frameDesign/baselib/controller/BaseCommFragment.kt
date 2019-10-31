package com.frameDesign.baselib.controller

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.frameDesign.baselib.controller.life.RxActivityResult
import com.frameDesign.commonlib.views.internal.ILifeCycle
import com.zqkh.commlibrary.internal.LoadAction
import com.zqkh.commlibrary.internal.ViewAction
import io.reactivex.subjects.PublishSubject
import org.greenrobot.eventbus.EventBus


/**
 * @desc  基础Fragment基类, 只定义业务框架
 * ..
 * @author liyong
 * @date 2018/10/17
 */
abstract class BaseCommFragment : Fragment(), ILifeCycle, ViewAction, LoadAction {

    private var hasViewCreated = false
    override var mCurrentCycle: Int = ILifeCycle.CREATE
        set(value) {
            field = value

            if (value == ILifeCycle.VIEW_CREATED) {
                hasViewCreated = true
            }

            mLifecycle.onNext(value)

            if (value == ILifeCycle.DESTROY) {
                mLifecycle.onComplete()
            }
        }

    override val mLifecycle: PublishSubject<Int> = PublishSubject.create()
    protected val mARouter by lazy(LazyThreadSafetyMode.NONE) {
        ARouter.getInstance()
    }

    private var isInitEvenBus = false
    protected val mEventBus by lazy(LazyThreadSafetyMode.NONE) {
        EventBus.getDefault().also { isInitEvenBus = true }
    }

    val rxResult by lazy {
        RxActivityResult(this)
    }

    protected val mActParent: BaseCommActivity
        get() = activity!! as BaseCommActivity

    @Volatile
    override var firstLoad: Boolean = true

    private var mLazyLock = true     // 是否开启懒加载
    private var mFragStarted = false // 记录Fragment是否进入onStart()
    private var mFragVisible = false // 记录Fragment的显示状态
    private var mCallVisible = false // 记录是否调用setUserVisibleHint

    override fun onAttach(context: Context) {
        super.onAttach(context)

        initFragmentState()

        mCurrentCycle = ILifeCycle.ATTACH
    }


    override fun onDetach() {
        super.onDetach()

        initFragmentState()

        firstLoad = true
        mCallVisible = false

        mCurrentCycle = ILifeCycle.DETACH
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCurrentCycle = ILifeCycle.CREATE
    }

    /**
     * 初始化Fragment状态,
     */
    private fun initFragmentState() {
        firstLoad = true
        mFragStarted = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRef = getLayoutView()

        return when (viewRef) {
            is View -> viewRef
            is Int -> inflater.inflate(viewRef, null)
            else -> throw IllegalArgumentException(
                "getLayoutView() can only return an Int (layout res) or a View"
            )
        }
    }

    /**
     * 获取界面View, 只能是layout id或View
     * @return Int
     */
    abstract fun getLayoutView(): Any

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDataBeforeView(view, savedInstanceState)

        mCurrentCycle = ILifeCycle.VIEW_CREATED

        internalInitView(view, savedInstanceState)

        initView(view, savedInstanceState)
    }

    /**
     * 用于在系统内初始化控件, 从而不必重新[initView]
     * @param view View
     * @param savedInstanceState Bundle?
     */
    open fun internalInitView(view: View, savedInstanceState: Bundle?) {

    }

    /**
     * 在[initView]之前初始化数据
     * @param view View
     * @param savedInstanceState Bundle?
     */
    @CallSuper
    protected open fun initDataBeforeView(view: View, savedInstanceState: Bundle?) {

    }

    /**
     * 初始化View
     * @param view View
     * @param savedInstanceState Bundle?
     */
    abstract fun initView(view: View, savedInstanceState: Bundle?)

    override fun onStart() {
        super.onStart()

        mCurrentCycle = ILifeCycle.START

        if (!mFragStarted) {
            mFragStarted = true

            switchFragmentState()
        }
    }

    @CallSuper
    override fun setUserVisibleHint(show: Boolean) {
        /* 只会由ViewPager的Adapter调用 */
        super.setUserVisibleHint(show)

        mCallVisible = true
        mFragVisible = show

        switchFragmentState()
    }

    override fun onPause() {
        super.onPause()

        mCurrentCycle = ILifeCycle.PAUSE
    }

    override fun onStop() {
        super.onStop()

        mCurrentCycle = ILifeCycle.STOP
    }

    override fun onResume() {
        super.onResume()

        mCurrentCycle = ILifeCycle.RESUME
    }

    /**
     * 切换Fragment显示状态
     *
     * 注: 默认实现懒加载, 可以通过[setLazyEnable]实现是否开启懒加载
     */
    private fun switchFragmentState() {
        if (mCallVisible) {
            /* 此处判断ViewPager加载的Fragment */
            if (mFragVisible && mFragStarted) lazyLoad()
        } else {
            /* 此处判断正常加载的Fragment */
            if (mFragStarted) lazyLoad()
        }
    }

    override fun loadData() {

    }

    override fun lazyLoad() {
        if (mLazyLock && firstLoad) {
            firstLoad = false

            loadData()
        }
    }

    override fun isActiveCycle(): Boolean = hasViewCreated
            && mCurrentCycle != ILifeCycle.DESTROY
            && mCurrentCycle != ILifeCycle.DETACH

    /**
     * 设置是否开启懒加载, 为匹配原项目, 默认不开启
     *
     * 注: 方法必需在[onViewCreated]之前调用才会生效
     */
    fun setLazyEnable(enabled: Boolean) {
        mLazyLock = enabled
    }

    override fun onDestroy() {
        super.onDestroy()

        mCurrentCycle = ILifeCycle.DESTROY

        //注销event bus
        if (isInitEvenBus && mEventBus.isRegistered(this)) {
            mEventBus.unregister(this)
        }
    }
}