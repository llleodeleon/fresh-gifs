package com.leodeleon.freshgifs.utils

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.animation.Animation
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Any.logd(msg:String, tag: String = this.javaClass.simpleName){
    Log.d(tag,msg)
}

private val animStub: (Animation) -> Unit = {}
fun Animation.listen(onEnd: (Animation) -> Unit = animStub) {
    setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {}

        override fun onAnimationRepeat(animation: Animation) {}

        override fun onAnimationEnd(animation: Animation) {
            onEnd(animation)
        }
    })
}

fun TabLayout.listen(onSelected: (TabLayout.Tab) -> Unit = {}){
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab) { }

        override fun onTabUnselected(tab: TabLayout.Tab) { }

        override fun onTabSelected(tab: TabLayout.Tab) {
            onSelected(tab)
        }
    })
}