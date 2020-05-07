package com.lishuaihua.game.util

import android.content.Context
import android.widget.Toast

object  CommonUtils {

     fun dip2px(context: Context, dp: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dp * scale * 0.5f
    }

    fun Context.toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}