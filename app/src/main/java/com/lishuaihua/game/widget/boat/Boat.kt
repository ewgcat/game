package com.lishuaihua.game.widget.boat

import android.content.Context


/**
 * 潜艇类
 */
class Boat(context: Context) {

    internal val view by lazy { BoatView(context) }

    val h
        get() = view.height.toFloat()

    val w
        get() = view.width.toFloat()

    val x
        get() = view.x

    val y
        get() = view.y

    /**
     * 移动到指定坐标
     */
    fun moveTo(x: Int, y: Int) {
        view.smoothMoveTo(x, y)
    }

}


