package com.frameDesign.baselib.controller.life

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @desc  代理界面
 * ..
 * @author liyong
 * @date 2018/10/17
 */

class RxResultFragment : Fragment() {

    internal val mResultSubject by lazy(LazyThreadSafetyMode.NONE) {
        PublishSubject.create<ActivityResult>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.retainInstance = true
    }

    internal fun request(intent: Intent, requestCode: Int): Observable<ActivityResult> {
        startActivityForResult(intent, requestCode)

        return mResultSubject
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = ActivityResult(requestCode, resultCode, data)

        mResultSubject.onNext(result)
    }

    override fun onDestroy() {
        super.onDestroy()

        mResultSubject.onComplete()
    }

}