package com.lishuaihua.game

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.lishuaihua.game.util.PermissionUtils.PERMISSION_REQUEST_CODE
import com.lishuaihua.game.util.PermissionUtils.PERMISSION_SETTING_CODE
import com.lishuaihua.game.util.PermissionUtils.REQUEST_PERMISSION
import com.lishuaihua.game.controller.GameController
import com.lishuaihua.game.controller.GameState
import com.lishuaihua.game.util.PermissionUtils
import com.lishuaihua.game.widget.AutoFitTextureView
import kotlinx.android.synthetic.main.activity_fullscreen.*


class GameActivity : AppCompatActivity() {

    private val gameController by lazy {
        AutoFitTextureView(this).let {
            root.addView(
                it,
                0,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            GameController(
                this,
                it,
                background,
                foreground
            )
        }
    }

    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        root.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

    }

    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val mDelayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_fullscreen)

        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.
        root.setOnClickListener { toggle() }




        mHidePart2Runnable.run()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions[0] == REQUEST_PERMISSION && requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGame()
            } else {
                PermissionUtils.showPermissionSettingDialog(this)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PERMISSION_SETTING_CODE -> {
                startGame()
            }
        }
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
        startGame()
    }

    @SuppressLint("SetTextI18n")
    private fun startGame() {
        PermissionUtils.checkPermission(this, Runnable {
            gameController.start()
            gameController.gameState.observe(this, Observer {
                when (it) {
                    is GameState.Start ->
                        score.text = "DANGER\nAHEAD"
                    is GameState.Score ->
                        score.text = "${it.score / 10f} m"
                    is GameState.Over ->
                        AlertDialog.Builder(this)
                            .setMessage("游戏结束！成功推进 ${it.score / 10f} 米! ")
                            .setNegativeButton("结束游戏") { _: DialogInterface, _: Int ->
                                finish()
                            }.setCancelable(false)
                            .setPositiveButton("再来一把") { _: DialogInterface, _: Int ->
                                gameController.start()
                            }.show()
                }
            })
        })
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        mVisible = false

        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        root.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
    }

}
