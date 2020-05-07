package com.lishuaihua.game.widget.bar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import android.view.ViewGroup


/**
 * 屏幕下方障碍物
 */
class DnBar(context: Context, container: ViewGroup) : Bar(context) {

    override val bmp = super.bmp.let {
        Bitmap.createBitmap(
            it, 0, 0, it.width, it.height,
            Matrix().apply { postRotate(-180F) }, true
        )
    }

    private val _srcRect by lazy(LazyThreadSafetyMode.NONE) {
        Rect(0, 0, bmp.width, (bmp.height * (h / container.height)).toInt())
    }

    override val srcRect: Rect
        get() = _srcRect
}
