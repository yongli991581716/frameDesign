package com.frameDesign.baselib.controller.life

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.FragmentManager
import com.frameDesign.baselib.controller.BaseCommActivity
import com.frameDesign.baselib.controller.BaseCommFragment
import com.frameDesign.commonlib.expand.checkUIThread
import io.reactivex.Observable
import java.io.Serializable

/**
 * @desc  Rxjava activity结果类
 * ..
 * @author liyong
 * @date 2018/10/17
 */
class RxActivityResult {

    companion object {

        const val TAG = "RxActivityResult_Fragment"
        const val KEY = "RxActivityResult_key"

        /**
         * 绑定数据并返回
         * @param act Activity
         * @param intent Intent
         */
        fun exitWithValue(act: Activity, intent: Intent) {
            act.setResult(Activity.RESULT_OK, intent)
            act.finish()
        }

        /**
         * 绑定数据并返回
         * @param act Activity
         * @param intent Intent
         */
        fun exitWithValue(act: Activity, extras: Bundle) {
            val intent = Intent()
            intent.putExtras(extras)

            exitWithValue(act, intent)
        }

        /**
         * 添加单Int数据回调
         * @param act Activity
         * @param value Int
         */
        fun exitWithValue(act: Activity, value: Int) {
            val intent = Intent()
            intent.putExtra(KEY, value)

            exitWithValue(act, intent)
        }

        /**
         * 添加单String数据回调
         * @param act Activity
         * @param value Int
         */
        fun exitWithValue(act: Activity, value: String) {
            val intent = Intent()
            intent.putExtra(KEY, value)

            exitWithValue(act, intent)
        }

        /**
         * 添加单Long数据回调
         * @param act Activity
         * @param value Int
         */
        fun exitWithValue(act: Activity, value: Long) {
            val intent = Intent()
            intent.putExtra(KEY, value)

            exitWithValue(act, intent)
        }

        /**
         * 添加单Double数据回调
         * @param act Activity
         * @param value Int
         */
        fun exitWithValue(act: Activity, value: Double) {
            val intent = Intent()
            intent.putExtra(KEY, value)

            exitWithValue(act, intent)
        }

        /**
         * 添加单Float数据回调
         * @param act Activity
         * @param value Int
         */
        fun exitWithValue(act: Activity, value: Float) {
            val intent = Intent()
            intent.putExtra(KEY, value)

            exitWithValue(act, intent)
        }

        /**
         * 添加单Serializable类型数据回调
         * @param act Activity
         * @param value Int
         */
        fun <T : Serializable> exitWithSerializable(act: Activity, value: T) {
            val intent = Intent()
            intent.putExtra(KEY, value)

            exitWithValue(act, intent)
        }

        /**
         * 添加单Parcelable类型数据数据回调
         * @param act Activity
         * @param value Int
         */
        fun <T : Parcelable> exitWithParcelable(act: Activity, value: T) {
            val intent = Intent()
            intent.putExtra(KEY, value)

            exitWithValue(act, intent)
        }

    }

    private val rxFragment: RxResultFragment

    constructor(act: BaseCommActivity) {
        rxFragment = getLazyFragment(act.supportFragmentManager)
    }

    constructor(frag: BaseCommFragment) {
        rxFragment = getLazyFragment(frag.childFragmentManager)
    }

    /**
     * 获取界面保存的fragment, 如没有添加新的并返回
     * @param fm FragmentManager
     * @return RxResultFragment
     */
    private fun getLazyFragment(fm: FragmentManager): RxResultFragment {
        if (!checkUIThread()) {
            throw ActivityResultException("create on worker thread!")
        }
        return fm.findFragmentByTag(TAG) as? RxResultFragment
            ?: RxResultFragment().apply {
                fm.beginTransaction()
                    .add(this, TAG)
                    .commitNow()
            }
    }

    /**
     * 获取结果返回事件流
     * @return Observable<ActivityResult>
     */
    fun getResultObservable(): Observable<ActivityResult> =
        rxFragment.mResultSubject

    /**
     * 执行[RxResultFragment.startActivityForResult]
     * @param intent Intent
     * @param requestCode Int
     * @return Observable<ActivityResult>
     */
    fun startForResult(intent: Intent, requestCode: Int) {
        if (!checkUIThread()) {
            throw ActivityResultException("create on worker thread!")
        }
        rxFragment.request(intent, requestCode)
    }

    /**
     * 执行[RxResultFragment.startActivityForResult]
     * @param intent Intent
     * @param requestCode Int
     * @return Observable<ActivityResult>
     */
    fun startOnceForResult(intent: Intent, requestCode: Int): Observable<ActivityResult> {
        if (!checkUIThread()) {
            throw ActivityResultException("create on worker thread!")
        }
        return rxFragment.request(intent, requestCode).take(1)
    }

}

/**
 * 转换到Int数据类型
 * @receiver Observable<ActivityResult>
 * @param defaultValue Int
 * @return Observable<Int>
 */
fun Observable<ActivityResult>.transformInt(defaultValue: Int): Observable<Int> {
    return this.map {
        return@map if (it.isSuccess()) {
            it.data?.getIntExtra(RxActivityResult.KEY, defaultValue)
                ?: throw ActivityResultException("onActivityResult data is null")
        } else {
            defaultValue
        }
    }
}

/**
 * 转换到Float数据类型
 * @receiver Observable<ActivityResult>
 * @param defaultValue Int
 * @return Observable<Int>
 */
fun Observable<ActivityResult>.transformFloat(defaultValue: Float): Observable<Float> {
    return this.map {
        return@map if (it.isSuccess()) {
            it.data?.getFloatExtra(RxActivityResult.KEY, defaultValue)
                ?: throw ActivityResultException("onActivityResult data is null")
        } else {
            defaultValue
        }
    }
}

/**
 * 转换到Float数据类型
 * @receiver Observable<ActivityResult>
 * @param defaultValue Int
 * @return Observable<Int>
 */
fun Observable<ActivityResult>.transformDouble(defaultValue: Double): Observable<Double> {
    return this.map {
        return@map if (it.isSuccess()) {
            it.data?.getDoubleExtra(RxActivityResult.KEY, defaultValue)
                ?: throw ActivityResultException("onActivityResult data is null")
        } else {
            defaultValue
        }
    }
}

/**
 * 转换到Long数据类型
 * @receiver Observable<ActivityResult>
 * @param defaultValue Long
 * @return Observable<Long>
 */
fun Observable<ActivityResult>.transformLong(defaultValue: Long): Observable<Long> {
    return this.map {
        return@map if (it.isSuccess()) {
            it.data?.getLongExtra(RxActivityResult.KEY, defaultValue)
                ?: throw ActivityResultException("onActivityResult data is null")
        } else {
            defaultValue
        }
    }
}

/**
 * 转换到String数据类型
 * @receiver Observable<ActivityResult>
 * @param defaultValue String
 * @return Observable<String>
 */
fun Observable<ActivityResult>.transformString(defaultValue: String): Observable<String> {
    return this.map {
        return@map if (it.isSuccess()) {
            val data = it.data

            if (data == null) {
                throw ActivityResultException("onActivityResult data is null")
            } else {
                data.getStringExtra(RxActivityResult.KEY) ?: defaultValue
            }
        } else {
            defaultValue
        }
    }
}

/**
 * 转换到[T] ([Serializable]) 类型的数据
 * @receiver Observable<ActivityResult>
 * @return Observable<NullableResult<T>>
 */
fun <T : Serializable> Observable<ActivityResult>.transformSerializable(): Observable<NullableResult<T>> {
    return this.map {
        return@map if (it.isSuccess()) {
            val data = it.data

            if (data == null) {
                throw ActivityResultException("onActivityResult data is null")
            } else {
                val extra = data.getSerializableExtra(RxActivityResult.KEY)
                val result = extra as? T ?: throw NullPointerException("not found T Type result")

                NullableResult.create(result)
            }
        } else {
            NullableResult.empty()
        }
    }
}

/**
 * 转换到[T] ([Serializable]) 类型的数据
 * @receiver Observable<ActivityResult>
 * @return Observable<NullableResult<T>>
 */
fun <T : Parcelable> Observable<ActivityResult>.transformParcelable(): Observable<NullableResult<T>> {
    return this.map {
        return@map if (it.isSuccess()) {
            val data = it.data

            if (data == null) {
                throw ActivityResultException("onActivityResult data is null")
            } else {
                val extra = data.getParcelableExtra<T>(RxActivityResult.KEY)
                val result = extra ?: throw NullPointerException("not found T Type result")

                NullableResult.create(result)
            }
        } else {
            NullableResult.empty()
        }
    }
}