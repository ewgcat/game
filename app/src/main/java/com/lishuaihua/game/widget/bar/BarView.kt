package com.lishuaihua.game.widget.bar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.View

@SuppressLint("ViewConstructor")
class BarView(context: Context?, private val block: (Canvas?) -> Unit) :
    View(context) {

    override fun onDraw(canvas: Canvas?) {
        block((canvas))
    }
}