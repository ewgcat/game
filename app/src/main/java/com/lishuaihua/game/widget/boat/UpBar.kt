package com.lishuaihua.game.widget.boat

import android.content.Context
import android.graphics.Rect
import android.view.ViewGroup
import com.lishuaihua.game.widget.bar.Bar

/**
 * 屏幕上方障碍物
 */
class UpBar(context: Context, container: ViewGroup) : Bar(context) {

    private val _srcRect by lazy(LazyThreadSafetyMode.NONE) {
        Rect(0, (bmp.height * (1 - (h / container.height))).toInt(), bmp.width, bmp.height)
    }
    override val srcRect: Rect
        get() = _srcRect

}