package com.lishuaihua.game.widget.bar

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.graphics.drawable.toBitmap
import com.lishuaihua.game.R

/**
 * 障碍物基类
 */
abstract class Bar(context: Context) {

    protected open val bmp = context.getDrawable(R.mipmap.bar)!!.toBitmap()

    protected abstract val srcRect: Rect

    private lateinit var dstRect: Rect

    private val paint = Paint()

    var h = 0F
        set(value) {
            field = value
            dstRect = Rect(0, 0, w.toInt(), h.toInt())
        }

    var w = 0F
        set(value) {
            field = value
            dstRect = Rect(0, 0, w.toInt(), h.toInt())
        }

    var x = 0F
        set(value) {
            view.x = value
            field = value
        }

    val y
        get() = view.y

    internal val view by lazy {
        BarView(context) {
            it?.apply {
                drawBitmap(
                    bmp,
                    srcRect,
                    dstRect,
                    paint
                )
            }
        }

    }

}



