package com.frameDesign.baselib.view.animation

import android.animation.Animator
import android.os.Build
import android.transition.Transition
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.annotation.RequiresApi
import com.frameDesign.baselib.view.internal.AnimAction
import com.frameDesign.baselib.view.internal.AnimListener


internal const val MAX_DURATION = 600L
internal const val MIN_DURATION = 300L

internal fun createAnimationAction(view: View, anim: Animation): AnimAction {
    view.animation = anim

    return AnimationInternal(anim)
}

internal fun createAnimatorAction(anim: Animator): AnimAction =
        AnimatorInternal(anim)

@RequiresApi(Build.VERSION_CODES.KITKAT)
internal fun createTransitionAction(root: ViewGroup, target: View, anim: Transition, isEnter: Boolean): AnimAction =
        TransitionInternal(root, target, anim, isEnter)

internal  fun AnimAction.doOnEnd(block: () -> Unit) {
    this.setListener(object : AnimListener {
        override fun onStart() {}

        override fun onComplete() {
            block()
        }
    })
}