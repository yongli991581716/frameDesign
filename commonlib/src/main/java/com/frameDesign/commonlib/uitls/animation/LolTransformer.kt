package com.frameDesign.commonlib.uitls.animation

import android.view.View
import androidx.viewpager.widget.ViewPager

/**
 * Created by liyong on 2019/3/27.
 *  viewpager 页面移动监听
 */
class LolTransformer : ViewPager.PageTransformer {

    var MIN_ALPHA = 0.5f
    var MIN_SCALE = 0.6f
    /**
     * viewpager移动的时候每个页面position都会变化，只对当前页面和两边page做处理，故且遵循以下变化规则（其他positon<-1或者positon>1的位置page 不做处理）
     *      position                    position
    -1 ----------------> 0       0 ---------------> 1

    x1                           x2
    minAlpha ----------------> 1       1 ---------------> minAlpha
    是不是很熟悉？这个x是什么？
    ——这TM不就是初中数学等比运算嘛。这个x不就是我们要求的变化过程中的透明度值嘛。
    好的！开始解题：
    解：设所求变量“透明度”为x。
    由题可得：
    -1 - position      position - 0        0 - position       position - 1
    --------------- = ----------------  , ---------------- =  ----------------
    minAlpha - x1          x1 - 1              1 - x2          x2 - minAlpha
    解得：
    x1 = 1 + position - minAlpha*position, x2 = 1 + minAlpha*position - position


    平移
    原本position的变化：0 -> 1
    原本translationX的变化：0 -> pageWidth
    既然我们要给他一个想反方向的X轴平移，那么我们需要的translationX的变化是否就是：0 -> - pageWidth?
    position
    0 ---------------> 1                            0 - position        position - 1
    ===> ---------------- = ------------------
    x                                       0 - x              x - (-pageWith)
    0 ---------------> -pageWith

    ==> x = - pageWidth * position

     */
    override fun transformPage(view: View, position: Float) {

        if ((position < -1) or (position > 1)) {
            //透明度
            view.alpha = MIN_ALPHA

            //缩放
            view.scaleX = MIN_SCALE
            view.scaleY = MIN_SCALE
        } else {
            if (position >= -1 && position < 0) {
                //透明度
                view.alpha = 1 + position - MIN_ALPHA * position

                //缩放
                view.scaleX = 1 + position - MIN_SCALE * position
                view.scaleY = 1 + position - MIN_SCALE * position

                //view.translationX = 0f
            } else if (position > 0 && position <= 1) {
                //透明度
                view.alpha = 1 + MIN_ALPHA * position - position

                //缩放
                view.scaleX = 1 + MIN_SCALE * position - position
                view.scaleY = 1 + MIN_SCALE * position - position


                //平移
                //view.translationX = -view.width * position
            } else {
                //透明度 设置透明度为1f,在内存中为alpha255，根据像素颜色合成规则，某一点像素alpha值为255时，
                // 此像素RGB都为255,故会导致使用此动画的view(canvas+PorterDuffXfermode(Mode.SRC_IN))在绘制时，
                // canvas+PorterDuffXfermode像素混合的模式失效，直接显示完整的绘制图片
                //view.alpha = 1f

                //缩放
                view.scaleX = 1f
                view.scaleY = 1f
            }
        }
    }
}