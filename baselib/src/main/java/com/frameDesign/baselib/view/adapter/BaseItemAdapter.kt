package com.frameDesign.baselib.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.frameDesign.baselib.R
import com.frameDesign.baselib.controller.BaseCommActivity
import com.frameDesign.commonlib.expand.currentTime
import com.frameDesign.commonlib.expand.nullOrEmpty
import com.orhanobut.logger.Logger
import com.frameDesign.commonlib.expand.doOnClick
import com.frameDesign.commonlib.expand.doOnLayout
import kotlin.math.absoluteValue

/**
 * 自定义RecyclerView.Adapter基类
 *
 * @author liyong
 * @time 2018/10/23
 */
abstract class BaseItemAdapter<T, VH : BaseViewHolder>(@LayoutRes layoutId: Int, data: List<T>) :
    BaseQuickAdapter<T, VH>(layoutId, data) {

    private var mEmptyCallback: (() -> Unit)? = null
    private var mChangeCallback: (() -> Unit)? = null
    private var mEmptyViewConvert: ((View) -> Unit)? = null

    private var mDataObserver: RecyclerView.AdapterDataObserver? = null

    constructor(data: List<T>) : this(0, data)

    constructor(lRes: Int) : this(lRes, mutableListOf<T>())

    constructor() : this(0, mutableListOf<T>())

    init {
        setLoadMoreView(FDLoadMoreView {
            loadMoreComplete()
        })
    }

    override fun setEmptyView(layoutResId: Int, viewGroup: ViewGroup?) {
        viewGroup?.doOnLayout {
            val view = LayoutInflater.from(viewGroup.context)
                .inflate(layoutResId, viewGroup, false)

            super.setEmptyView(view)


            val emptyRoot = emptyView

            val params = emptyRoot.layoutParams

            params.width = FrameLayout.LayoutParams.MATCH_PARENT
            params.height = viewGroup.height

            emptyRoot.requestLayout()
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        super.onBindViewHolder(holder, position)

        val type = holder.itemViewType

        if (type == EMPTY_VIEW) {
            mEmptyViewConvert?.invoke(holder.itemView)
        }
    }

    /**
     * 设置控件面的布局活动
     * @param convert (View) -> Unit
     */
    fun setEmptyViewConvert(convert: (View) -> Unit) {
        mEmptyViewConvert = convert
    }

    /**
     * 获取[BaseViewHolder]在实际数据列表中的映射下标
     * (因为adapter父类中可能存在header View)
     */
    val BaseViewHolder.actualDataPosition: Int
        get() = layoutPosition - headerLayoutCount

    /**
     * 是否为实际数据中的最后一项
     * (可能不是实际显示的最后一项, 因为父类中可能存在footerView)
     */
    val BaseViewHolder.isLastItem: Boolean
        get() = layoutPosition == headerLayoutCount + mData.size - 1

    /**
     * 移除[start]以后的所有数据
     * @param start Int
     * @param ins 是否包含开始项
     */
    fun removeLast(start: Int, ins: Boolean) {
        val list = mData ?: return

        if (ins && start == 0) {
            mData?.clear()
            notifyDataSetChanged()
            return
        }

        val sta = if (ins) start - 1 else start

        val actualCount = list.size - 1 - sta
        val actualStart = headerLayoutCount + sta + 1

        if (actualCount <= 0) return

        val tempList = list.take(sta + 1)
        mData.clear()
        mData.addAll(tempList)

        notifyItemRangeRemoved(
            actualStart,
            actualCount
        )

        if (dataSize == 0) notifyDataSetChanged()
    }

    /**
     * 获取实际的下标(只可在[BaseItemAdapter]中使用)
     * @receiver Int
     * @return Int
     */
    protected inline fun Int.dataPosition(): Int =
        this - headerLayoutCount

    /**
     * 通过[Int.this]inflate View
     * @receiver Int
     * @param parent ViewGroup?
     * @return View
     */
    protected inline fun <V : View> Int.inflate(parent: ViewGroup?): View {
        return mLayoutInflater.inflate(this, parent, false) as V
    }

    /**
     * 按条件刷新 第一条满足条件的item
     * @param mapFilter (T) -> Boolean
     */
    fun updateBy(mapFilter: (T) -> Boolean) {
        if (!mData.nullOrEmpty()) {
            for ((i, d) in mData.withIndex()) {
                if (mapFilter(d)) {
                    notifyItemChanged(i)
                    return
                }
            }
        }
    }

    /**
     * 按条件删除指定item
     * @param predicate () -> Boolean
     */
    fun removeBy(predicate: (T) -> Boolean) {
        if (!mData.nullOrEmpty()) {
            for ((i, d) in mData.withIndex()) {
                if (predicate(d)) {
                    remove(i)
                    return
                }
            }
        }
    }

    /**
     * 按添加删除所有满足条件的item
     * @param predicate (T) -> Boolean
     */
    fun removeAllBy(predicate: (T) -> Boolean) {
        if (!mData.nullOrEmpty()) {
            mData.removeAll(predicate)
            notifyDataSetChanged()
        }
    }

    /**
     * 移除数据直到为空时的回调
     * @param doEmpty () -> Unit
     */
    fun doRemoveToEmpty(doEmpty: () -> Unit) {
        initDataObserver()

        mEmptyCallback = doEmpty
    }

    /**
     * 当数据发送变化是回调
     * @param doChange () -> Unit
     */
    fun doDataChange(doChange: () -> Unit) {
        initDataObserver()

        mChangeCallback = doChange
    }

    private var mEmptyLockTime = 0L
    private val mEmptyLock: Boolean
        get() {
            val time = currentTime()
            val dt = time - mEmptyLockTime

            return if (dt.absoluteValue > 150) {
                mEmptyLockTime = time
                true
            } else false
        }

    private fun initDataObserver() {
        if (mDataObserver == null) {
            mDataObserver = object : RecyclerView.AdapterDataObserver() {

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    if (isEmptyData() && mEmptyLock) {
                        mEmptyCallback?.invoke()
                        Logger.e("callback--empty")
                    }

                    mChangeCallback?.invoke()
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    if (isEmptyData() && mEmptyLock) {
                        mEmptyCallback?.invoke()
                        Logger.e("callback--empty")
                    }

                    mChangeCallback?.invoke()
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    mChangeCallback?.invoke()
                }

            }

            registerAdapterDataObserver(mDataObserver!!)

            val activity = mContext ?: return

            if (activity is BaseCommActivity) {
                activity.doOnDestroy {
                    if (mDataObserver != null) {
                        unregisterAdapterDataObserver(mDataObserver!!)
                    }
                }
            }
        }
    }

}

/**
 * 获取实际数据的count
 */
inline val BaseQuickAdapter<*, *>.dataSize
    get() = if (isEmptyData()) 0 else data.size

/**
 * 获取最后一条数据所对应的layout position
 * (因为可能存在headerView, position会有偏差)
 */
inline val BaseQuickAdapter<*, *>.lastDataLayoutPosition
    get() = headerLayoutCount + dataSize - 1

/**
 * 判断adapter是否存在数据
 * @receiver BaseQuickAdapter<*, *>
 * @return Boolean
 */
inline fun BaseQuickAdapter<*, *>.isEmptyData(): Boolean =
    data == null || data.isEmpty()

/**
 * 重组方法[BaseQuickAdapter]的方法, 简化调用逻辑
 * @receiver BaseQuickAdapter<T, out BaseViewHolder>
 * @param data List<out T>
 * @param append Boolean
 */
inline fun <T> BaseQuickAdapter<T, out BaseViewHolder>.concatData(
    data: List<out T>,
    append: Boolean
) =
    if (append) addData(data) else setNewData(data)

/**
 * 添加View DataBinding的Adapter
 *
 * @author liyong
 * @time 2018/10/23
 */
abstract class BaseDataBindingAdapter<T, DB : ViewDataBinding>(@LayoutRes layoutId: Int) :
    BaseItemAdapter<T, DataBindingVH<DB>>(layoutId) {

    override fun createBaseViewHolder(parent: ViewGroup?, layoutResId: Int): DataBindingVH<DB> {
        val itemBind = DataBindingUtil.inflate<DB>(
            mLayoutInflater,
            layoutResId, parent, false
        )

        return DataBindingVH(itemBind)
    }

}

/**
 * 添加View DataBinding适配的ViewHolder
 *
 * @author liyong
 * @time 2018/10/23
 */
open class DataBindingVH<DB : ViewDataBinding>(val itemBind: DB) : BaseViewHolder(itemBind.root)

class FDLoadMoreView(private val doRetryClick: (v: View) -> Unit) : LoadMoreView() {

    override fun convert(holder: BaseViewHolder) {
        super.convert(holder)

        if (loadMoreStatus == STATUS_FAIL) {
            holder.itemView.doOnClick(doRetryClick)
        } else {
            holder.itemView.setOnClickListener(null)
        }
    }

    override fun getLayoutId(): Int = R.layout.layout_adapter_load_more

    override fun getLoadEndViewId(): Int = R.id.ll_adapter_no_more

    override fun getLoadingViewId(): Int = R.id.ll_adapter_loading

    override fun getLoadFailViewId(): Int = R.id.ll_adapter_failure

}
