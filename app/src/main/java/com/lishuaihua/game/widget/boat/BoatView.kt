package com.lishuaihua.game.widget.boat

import android.animation.ObjectAnimator
import android.content.Context
import android.widget.OverScroller
import androidx.appcompat.widget.AppCompatImageView
import com.lishuaihua.game.R
import kotlin.math.atan

class BoatView(context: Context?) : AppCompatImageView(context) {

    private val _scroller by lazy { OverScroller(context) }

    private val _res = arrayOf(
        R.mipmap.boat0,
        R.mipmap.boat1
    )

    private var _rotationAnimator: ObjectAnimator? = null

    private var _cnt = 0
        set(value) {
            field = if (value > 1) 0 else value
        }

    init {
        scaleType = ScaleType.FIT_CENTER
        _startFlashing()
    }

    private fun _startFlashing() {
        postDelayed({
            setImageResource(_res[_cnt++])
            _startFlashing()
        }, 500)
    }

    override fun computeScroll() {
        super.computeScroll()

        if (_scroller.computeScrollOffset()) {

            x = _scroller.currX.toFloat()
            y = _scroller.currY.toFloat()

            // Keep on drawing until the animation has finished.
            postInvalidateOnAnimation()
        }

    }

    /**
     * 移动更加顺换
     */
    internal fun smoothMoveTo(x: Int, y: Int) {
        if (!_scroller.isFinished) _scroller.abortAnimation()
        _rotationAnimator?.let { if (it.isRunning) it.cancel() }

        val curX = this.x.toInt()
        val curY = this.y.toInt()

        val dx = (x - curX)
        val dy = (y - curY)
        _scroller.startScroll(curX, curY, dx, dy, 250)

        _rotationAnimator = ObjectAnimator.ofFloat(
            this,
            "rotation",
            rotation,
            Math.toDegrees(atan((dy / 100.toDouble()))).toFloat()
        ).apply {
            duration = 100
            start()
        }

        postInvalidateOnAnimation()
    }
}